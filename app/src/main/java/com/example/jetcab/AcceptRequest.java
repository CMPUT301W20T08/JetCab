package com.example.jetcab;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * @author chirag
 * This activity show the full details of a request and enables Driver to Accept Request
 */
public class AcceptRequest extends AppCompatActivity {

    TextView username, email, phone, pickup, dropoff, fare;
    Button Accept, Back;
    ImageButton mapIcon;
    private FirebaseAuth myFirebaseAuth;
    private FirebaseFirestore myFF;

    /**
     * Set the layout attributes by retrieving data from firebase
     * Uses Geocoder to set the pickup and dropoff address.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_request);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        pickup = findViewById(R.id.pickup);
        dropoff = findViewById(R.id.drop);
        fare = findViewById(R.id.fare);
        Accept = findViewById(R.id.Accept);
        mapIcon = findViewById(R.id.map_icon_driver);
        Back = findViewById(R.id.Back);

        myFirebaseAuth = FirebaseAuth.getInstance();
        myFF = FirebaseFirestore.getInstance();

        this.setTitle("Accept Request");

        DocumentReference dF = myFF.collection("users").document(getIntent().getStringExtra("UserId"));
        dF.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override

            public void onEvent ( @Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e ) {
                if(e!=null)
                {
                    Log.d("","Error:"+e.getMessage());
                }
                else{
                username.setText ((String)documentSnapshot.get ( "username" )  );
                email.setText ((String)documentSnapshot.get ( "email" )  );
                phone.setText ((String)documentSnapshot.get ( "phone" )  );
            }}
        } );
           


        Geocoder geocoder;
        geocoder = new Geocoder(this, Locale.getDefault());
        String coordinates[] = getIntent().getStringExtra("pickup").split(",");
        String coordinates1[] = getIntent().getStringExtra("Drop").split(",");

        String address;
        String address1;
        List<Address> addresses;
        List<Address> addresses1;
        try {
            addresses = geocoder.getFromLocation(Double.valueOf(coordinates[0]), Double.valueOf(coordinates[1]), 3);
            if (addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0);
                int count = 0;
                while (count < addresses.size()) {
                    address = addresses.get(count).getAddressLine(0);
                    count++;
                }
                pickup.setText(address);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            addresses1 = geocoder.getFromLocation(Double.valueOf(coordinates1[0]), Double.valueOf(coordinates1[1]), 3);
            if (addresses1.size() > 0) {
                address1 = addresses1.get(0).getAddressLine(0);
                int count = 0;
                while (count < addresses1.size()) {
                    address = addresses1.get(count).getAddressLine(0);
                    count++;
                }
                dropoff.setText(address1);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        fare.setText(getIntent().getStringExtra("fare"));

        //click the map icon to show the start and end location on the map
        final String start_lat = coordinates[0];
        final String start_lng = coordinates[1];
        final String end_lat = coordinates1[0];
        final String end_lng = coordinates1[1];
        mapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Activity_DriverMapDisplay.class);
                intent.putExtra("Start Lat", start_lat);
                intent.putExtra("Start Lng", start_lng);
                intent.putExtra("End Lat", end_lat);
                intent.putExtra("End Lng", end_lng);
                startActivity(intent);
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * Accept On ClickListner accepts the request and changes it status in the firebase.
         */

        Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference dF = myFF.collection("Requests").document(getIntent().getStringExtra("UserId"));
                Activity_Request.AcceptedRequest(getIntent().getStringExtra("DriverID"), getIntent().getStringExtra("UserId"), dF);
                finish();
            }
        });

    }
}
