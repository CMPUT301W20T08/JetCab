package com.example.jetcab;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DriverCurrentRequest extends AppCompatActivity {
    private FirebaseAuth myFirebaseAuth;
    private FirebaseFirestore myFF;
    private String driverID;
    String TAG = "DriverCurrentRequest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DriverCurrentRequest.this.setTitle("Current Request");

        myFirebaseAuth = FirebaseAuth.getInstance();
        driverID = myFirebaseAuth.getCurrentUser().getUid();
        myFF = FirebaseFirestore.getInstance();

        Query query = myFF.collection("Accepted Requests")
                .whereEqualTo("Driver User Id", driverID);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listener Failed", e);
                }

                if (!queryDocumentSnapshots.isEmpty()) {
                    setContentView(R.layout.driver_current_request_waiting);
                } else {
                    setContentView(R.layout.driver_current_request_none);
                }
            }
        });
    }
}

