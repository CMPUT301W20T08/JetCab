package com.example.jetcab;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;

import android.app.Dialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import com.google.firebase.firestore.QuerySnapshot;


import java.util.HashMap;
import java.util.Map;

/**
 * @author chirag , Mazhar
 * Displays useful data to the rider about his request like the status.
 * Enables payment option.
 */

public class CurrentRequest extends FragmentActivity implements OnMapReadyCallback {


    private String TAG="TAG";
    private GoogleMap mMap;
    private TextView status, wait;
  private TextView cancel_button;
    private LatLng pickup, dropoff;
    private static FirebaseAuth myFirebaseAuth;
    private static FirebaseFirestore myFF;
    private static String userID;

    private Dialog dialog;
    String username, phone, email;

    /**
     * checks if the device is connected to internet.
     * handles different events in the activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_request);

        Intent intent = getIntent();

        dialog = new Dialog(this);

        Bundle coords_bun = intent.getParcelableExtra("COORDS");
        pickup = coords_bun.getParcelable("PICKUP");
        dropoff = coords_bun.getParcelable("DROPOFF");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        status = findViewById(R.id.status);
        status.setText ( "Waiting" );
        cancel_button = findViewById(R.id.cancel);

        myFirebaseAuth = FirebaseAuth.getInstance();
        myFF = FirebaseFirestore.getInstance();
        userID = myFirebaseAuth.getCurrentUser().getUid();

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) {
            Toast.makeText(getApplicationContext(), "Please check the Internet Connection", Toast.LENGTH_LONG).show();
        }

        DocumentReference dF1 = myFF.collection("Accepted Requests").document(userID);
        dF1.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.getString("status") !=null)
                {
                    status.setText(documentSnapshot.getString("status"));
                    if(status.getText ().toString ().equals ( "Accepted" ))
                    {
                        getDriverInfo ( documentSnapshot.getString ( "Driver User Id" ));
                        createDialog ( "Do you want to confirm your ride " ,"Confirmed");
                    }

                    if(status.getText ().toString ().equals ( "Arrived" ))
                    {
                        createDialog ( "Have you arrived " ,"Completed");
                    }

                }
            }
        });



        myFirebaseAuth = FirebaseAuth.getInstance();
        myFF = FirebaseFirestore.getInstance();
        userID = myFirebaseAuth.getCurrentUser().getUid();
        DocumentReference dF = myFF.collection("Requests").document(userID);
        dF.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                status.setText(documentSnapshot.getString("status"));
            }
        });

        /**
         * brings up CancelRequestBeforeFragment as a pop up
         * to confirm if the user wants to cancel or not
         */
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CancelRequestBeforeFragment().show(getSupportFragmentManager(),"Cancel Request Before");
            }
        });


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        MarkerOptions pickup_marker = new MarkerOptions().position(pickup).title("Pickup");
        MarkerOptions dropoff_marker = new MarkerOptions().position(dropoff).title("Dropoff");
        mMap.addMarker(pickup_marker);
        mMap.addMarker(dropoff_marker);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(pickup_marker.getPosition());
        builder.include(dropoff_marker.getPosition());
        LatLngBounds marker_bounds = builder.build();

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(marker_bounds, 100));


    }

    /**
     * change the status of the request in the firebase.
     * also enables payment.
     * @param status
     */
    public void setStatus(String status)
    {
        myFirebaseAuth = FirebaseAuth.getInstance();
        myFF = FirebaseFirestore.getInstance();
        DocumentReference dF = myFF.collection("Accepted Requests").document(userID);
        if(status.equals ( "Confirmed" )) {
            Map<String, Object> update = new HashMap<> ( );
            update.put ( "status", status );
            dF.update ( update ).addOnSuccessListener ( new OnSuccessListener<Void> ( ) {
                @Override
                public void onSuccess ( Void aVoid ) {
                    Log.d ( TAG, "Request Successfully Updated" );
                }
            } ).addOnFailureListener ( new OnFailureListener ( ) {
                @Override
                public void onFailure ( @NonNull Exception e ) {
                    Log.d ( TAG, "Request Update Unsuccessful" );
                }
            } );
        }
        else
        {
            Activity_Request.CompletedRequest ( userID,dF );
            startActivity(new Intent(CurrentRequest.this, Activity_QRcode.class));
            finish ();
        }
    }

    /**
     * creates a dialog box to confirm the status of the request.
     * @param message
     * @param status
     */

    public void createDialog( String message, final String status)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage ( message);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                setStatus ( status );
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.show ();
    }

    /**
     * get drivers profile from the firebase
     * @param DriverId
     */

    public void getDriverInfo(String DriverId) {
        DocumentReference documentReference = myFF.collection("users").document (DriverId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot> () {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        username = document.get("username").toString();
                        phone = document.get("phone").toString();
                        email = document.get("email").toString();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * show the popup window when click the rider's profile button
     * @param view
     */
    public void ShowProfile(View view) {
        dialog.setContentView(R.layout.driver_profile_popup_window);
        ImageButton ok_button = dialog.findViewById(R.id.ok_image);
        TextView username_text = dialog.findViewById(R.id.driver_username_text);
        TextView phone_text = dialog.findViewById(R.id.driver_phone_text);
        TextView email_text = dialog.findViewById(R.id.driver_email_text);

        username_text.setText(username);
        phone_text.setText(phone);
        email_text.setText(email);

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}