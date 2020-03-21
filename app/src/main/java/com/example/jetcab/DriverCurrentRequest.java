package com.example.jetcab;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
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
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    setContentView(R.layout.driver_current_request_waiting);
                    
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " -> " + document.getData());
                    }
                } else {
                    setContentView(R.layout.driver_current_request_none);
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
}

