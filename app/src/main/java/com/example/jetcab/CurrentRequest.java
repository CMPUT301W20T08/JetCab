package com.example.jetcab;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class CurrentRequest extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView status, wait;
    private Button cancel_button;
    private LatLng pickup, dropoff;
    private static FirebaseAuth myFirebaseAuth;
    private static FirebaseFirestore myFF;
    private static String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_request);

        Intent intent = getIntent();
        Bundle coords_bun = intent.getParcelableExtra("COORDS");
        pickup = coords_bun.getParcelable("PICKUP");
        dropoff = coords_bun.getParcelable("DROPOFF");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        wait = findViewById(R.id.wait);
        status = findViewById(R.id.status_text);
        cancel_button = findViewById(R.id.cancel_button);

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
}