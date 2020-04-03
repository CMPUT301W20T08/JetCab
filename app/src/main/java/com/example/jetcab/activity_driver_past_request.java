package com.example.jetcab;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class activity_driver_past_request extends AppCompatActivity {

    private ArrayList<String[]> requestdetails;
    private FirebaseAuth myFirebaseAuth;
    private FirebaseFirestore myFF;
    private String DriverID;
    private ArrayAdapter<String[]> openrequestaddaptor;
    private ListView pastrequestList;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_driver_past_request );

        pastrequestList = findViewById(R.id.pastrequest);
        requestdetails = new ArrayList<>();
        myFirebaseAuth = FirebaseAuth.getInstance();
        myFF = FirebaseFirestore.getInstance();
        DriverID = myFirebaseAuth.getCurrentUser().getUid();


        final CollectionReference collectionReference = myFF.collection("Completed Requests");
        collectionReference.addSnapshotListener ( new EventListener<QuerySnapshot> ( ) {

            /**
             * Add data to the ArrayList
             * Set the arrayadaptor
             * @param queryDocumentSnapshots
             * @param e
             */
            @Override
            public void onEvent ( @Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e ) {
                if (e!=null){
                    Log.d("","Error:"+e.getMessage());
                }
                else {
                    for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                    {
                        if(doc.getData ().get("Driver User Id").toString().matches ( DriverID))
                        {
                            String Date=(String)doc.getData ().get("Date and Time");
                            String PickUp=(String)doc.getData ().get("Pickup Coordinates");
                            String Dropoff=(String)doc.getData ().get("DropOff Coordinates");
                            String details[]= {Date,PickUp,Dropoff};
                            requestdetails.add(details);
                        }

                    }
                    openrequestaddaptor=new CustomList2 (activity_driver_past_request.this,requestdetails );
                    pastrequestList.setAdapter ( openrequestaddaptor );
                }}
        } );
    }
}
