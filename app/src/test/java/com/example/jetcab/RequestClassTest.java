package com.example.jetcab;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;


import org.junit.Test;

import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.*;

public class RequestClassTest {
    private static String userID;
    private static FirebaseAuth myFirebaseAuth;

    private static FirebaseFirestore myFF;
    private Activity_Request mockRequest(){
        Activity_Request request = new Activity_Request(new LatLng(53.521967, -113.511960), new LatLng(53.534191, -113.507541) ,100);
        return request;
    }

    //tests dont work with firestore implementation. todo figure out testing with fb
    @Test
    public void checkDatabase() {
        myFirebaseAuth = FirebaseAuth.getInstance();
        userID = myFirebaseAuth.getCurrentUser().getUid();
        DocumentReference dr = myFF.collection("Requests").document(userID);
        dr.addSnapshotListener((Executor) this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                assertEquals("53.521967, -113.511960", documentSnapshot.getString("Pickup Coordinates"));
                assertEquals("53.534191, -113.507541", documentSnapshot.getString("DropOff Coordinates"));
            }
        });
    }

    //tests dont work with firestore implementation. todo figure out testing with fb
    @Test
    public void checkStatus() {
//        myFirebaseAuth = FirebaseAuth.getInstance();
        myFF = FirebaseFirestore.getInstance();
//        userID = myFirebaseAuth.getCurrentUser().getUid();
        DocumentReference dF = myFF.collection("Requests").document();
        dF.addSnapshotListener((Executor) this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                assertEquals("open", documentSnapshot.getString("status"));
            }
        });
    }
//    @Test
//    public void check() {
//
//    }
//
//    @Test
//    public void check2() {
//
//    }
}