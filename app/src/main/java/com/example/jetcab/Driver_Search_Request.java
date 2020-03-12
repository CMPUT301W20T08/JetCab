package com.example.jetcab;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class Driver_Search_Request extends AppCompatActivity {

    private ListView RequestListview;
    private ArrayAdapter<Request> requestArrayAdapter;
    private ArrayList<Request> openRequestList;
    private String userID;
    private FirebaseAuth myFirebaseAuth;
    private FirebaseFirestore myFF;


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_driver__search__request );

        RequestListview = findViewById(R.id.request_list);

        userID = myFirebaseAuth.getCurrentUser().getUid();
        myFirebaseAuth = FirebaseAuth.getInstance();
        myFF = FirebaseFirestore.getInstance();
        DocumentReference dF = myFF.collection("Requests").document(userID);

        dF.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                openRequestList.clear();
                for (QueryDocumentSnapshot doc : QueryDocumentSnapshots) {

                }
            }
        })
    }
}