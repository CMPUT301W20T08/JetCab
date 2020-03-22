package com.example.jetcab;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Joyce
 * show the status and details of current request
 */
public class Activity_DriverCurrentRequest extends AppCompatActivity implements OnMapReadyCallback {
    private FirebaseAuth myFirebaseAuth;
    private FirebaseFirestore myFF;
    private String driverID;
    String TAG = "DriverCurrentRequest";
    private GoogleMap mMap;
    Double startLat, startLng, endLat, endLng;
    Geocoder geocoder;

    /**
     * check the status of the request from firebase, and show the status of current request
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity_DriverCurrentRequest.this.setTitle("Current Request");

        geocoder = new Geocoder(this, Locale.getDefault());

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
                //if the driver have already accepted request
                if (!queryDocumentSnapshots.isEmpty()) {
                    setContentView(R.layout.driver_current_request_waiting);
                    waitingStatus(queryDocumentSnapshots);
                    Log.d(TAG, queryDocumentSnapshots.toString());

                    // Obtain the SupportMapFragment and get notified when the map is ready to be used
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map_w);
                    mapFragment.getMapAsync(Activity_DriverCurrentRequest.this);

                //if the driver haven't accepted request
                } else {
                    setContentView(R.layout.driver_current_request_none);
                }
            }
        });
    }

    /**
     * add the start and end markers to the map (include address)
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        //add the marker to start location
        LatLng from = new LatLng(startLat, startLng);
        mMap.addMarker(new MarkerOptions()
                .position(from).title("From: " + getAddress(startLat, startLng))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        builder.include(from);

        //add the marker to the end location
        LatLng to = new LatLng(endLat, endLng);
        mMap.addMarker(new MarkerOptions()
                .position(to).title("To: " + getAddress(endLat, endLng))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        builder.include(to);

        //move the camera to fit all the markers
        LatLngBounds bounds = builder.build();
        int padding = 0;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.moveCamera(cameraUpdate);
    }

    /**
     * when the driver accepts the request (but not be confirmed by rider)
     * specify the start and end location on map
     * @param queryDocumentSnapshots
     */
    public void waitingStatus(QuerySnapshot queryDocumentSnapshots) {
        String pickupCoordinate, dropOffCoordinate;
        if (queryDocumentSnapshots.size() > 1) {
            Log.w(TAG, "driver accepted " + queryDocumentSnapshots.size() + " uncompleted requests");
        }
        //driver should have only one uncompleted request
        pickupCoordinate = queryDocumentSnapshots.getDocuments().get(0).get("Pickup Coordinates").toString();
        dropOffCoordinate = queryDocumentSnapshots.getDocuments().get(0).get("DropOff Coordinates").toString();

        String[] startLatLng = pickupCoordinate.split(",");
        startLat = Double.parseDouble(startLatLng[0]);
        startLng = Double.parseDouble(startLatLng[1]);

        String[] endLatLng = dropOffCoordinate.split(",");
        endLat = Double.parseDouble(endLatLng[0]);
        endLng = Double.parseDouble(endLatLng[1]);
    }

    /**
     * get address from given latitude and longitude
     * @param lat
     * @param lng
     * @return the address
     */
    public String getAddress(Double lat, Double lng) {
        //get the address of current location
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            return addresses.get(0).getAddressLine(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

