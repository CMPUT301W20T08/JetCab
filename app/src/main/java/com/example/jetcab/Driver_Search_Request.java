package com.example.jetcab;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

/**
 * This Activity let the driver serach for Open request posted by the riders
 * @author chirag
 */

public class Driver_Search_Request extends AppCompatActivity {
    private ArrayList<String[]> requestdetails;
    private FirebaseAuth myFirebaseAuth;
    private FirebaseFirestore myFF;
    private String DriverID;
    private ArrayAdapter<String[]> openrequestaddaptor;
    private ListView OpenrequestList;

    /**
     * Get all the layout attributes.
     * Fill the layout by retrieving data from firebase using colllection reference
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver__search__request);

        OpenrequestList = findViewById(R.id.openrequest);
        requestdetails = new ArrayList<>();
        myFirebaseAuth = FirebaseAuth.getInstance();
        myFF = FirebaseFirestore.getInstance();
        DriverID = myFirebaseAuth.getCurrentUser().getUid();


        final CollectionReference collectionReference = myFF.collection("Requests");
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
                    String UserId=(String)doc.getData ().get("User ID");
                    String PickUp=(String)doc.getData ().get("Pickup Coordinates");
                    String Dropoff=(String)doc.getData ().get("DropOff Coordinates");
                    String fare=String.valueOf (  doc.getData ().get("fare")); //TODO returns string with too many decimal places. fix later.
                    String details[]= {UserId,PickUp,fare,Dropoff};
                    requestdetails.add(details);
                }
                openrequestaddaptor=new CustomList (Driver_Search_Request.this,requestdetails );
                OpenrequestList.setAdapter ( openrequestaddaptor );
            }}
        } );

        OpenrequestList.setOnItemClickListener ( new AdapterView.OnItemClickListener ( ) {

            /**
             * Starts AcceptRequest Activity whenever a ListItem(request) is clicked
             * @param parent
             * @param view
             * @param position
             * @param id
             */

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Driver_Search_Request.this, AcceptRequest.class);
                intent.putExtra("UserId", requestdetails.get(position)[0]);
                intent.putExtra("DriverID", DriverID);
                intent.putExtra("pickup", requestdetails.get(position)[1]);
                intent.putExtra("Drop", requestdetails.get(position)[3]);
                intent.putExtra("fare", requestdetails.get(position)[2]);
                startActivity(intent);
                finish();
            }
        });
    }
}
