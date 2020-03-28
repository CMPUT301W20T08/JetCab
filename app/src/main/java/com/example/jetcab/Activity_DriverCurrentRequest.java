package com.example.jetcab;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Joyce
 * show the status and details of current request
 */
public class Activity_DriverCurrentRequest extends AppCompatActivity implements OnMapReadyCallback{
    private FirebaseAuth myFirebaseAuth;
    private FirebaseFirestore myFF;
    private String driverID;
    String TAG = "DriverCurrentRequest";
    private GoogleMap mMap;
    public Double startLat, startLng, endLat, endLng;
    Geocoder geocoder;
    String status;
    public static String from_address, to_address;
    public static Double from_lat, from_lng, to_lat, to_lng;

    /**
     * check the status of the request from firebase, and show the status of current request
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                    getCoordinate(queryDocumentSnapshots);
                    status = queryDocumentSnapshots.getDocuments().get(0).get("status").toString();

                    //if the status of the request is "Accepted"
                    if (status.matches("Accepted")) {
                        waitingStatus();
                    } else if (status.matches("Confirmed") || status.matches("Pickup")
                            || status.matches("On The Way") || status.matches("Arrived")) { //the in the progress status
                        progressStatus(queryDocumentSnapshots);
                    }

                //if the driver haven't accepted request
                } else {
                    setContentView(R.layout.driver_current_request_none);
                }
            }
        });
    }

    /**
     * get the coordinates from firebase
     * specify the start and end location on map
     * @param queryDocumentSnapshots
     */
    public void getCoordinate(QuerySnapshot queryDocumentSnapshots) {
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

        //set the static variables
        from_address = getAddress(startLat, startLng);
        to_address = getAddress(endLat, endLng);
        from_lat = startLat;
        from_lng = startLng;
        to_lat = endLat;
        to_lng = endLng;
    }

    /**
     * when the driver accepts the request (but not be confirmed by rider)
     */
    public void waitingStatus() {
        setContentView(R.layout.driver_current_request_waiting);
        TextView status_text = findViewById(R.id.status_w_text);
        status_text.setPaintFlags(status_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_w);
        mapFragment.getMapAsync(this);
    }

    /**
     * driver can select the current status of request
     * enable payment
     * @param queryDocumentSnapshots
     */
    public void progressStatus(QuerySnapshot queryDocumentSnapshots) {
        setContentView(R.layout.driver_current_request_progress);
        TextView pickup_text = findViewById(R.id.pickup_text_view);
        TextView on_the_way_text = findViewById(R.id.on_the_way_text_view);
        TextView arrived_text = findViewById(R.id.arrived_text_view);
        TextView status_text = findViewById(R.id.status_p_text);
        status_text.setText(status);
        ImageButton scan_button = findViewById(R.id.scan_image_button);
        status_text.setPaintFlags(status_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //notify when the user loses the internet connection
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) {
            Toast.makeText(getApplicationContext(), "Please check the Internet Connection", Toast.LENGTH_LONG).show();
        }

        final DocumentReference documentReference = myFF.collection("Accepted Requests").
                document(queryDocumentSnapshots.getDocuments().get(0).getId());

        if (status.matches("Pickup")) {
            pickup_text.setTextColor(getResources().getColor(android.R.color.black));
            pickup_text.setBackground(getBottomBorder(10));
        } else if (status.matches("On The Way")) {
            pickup_text.setTextColor(getResources().getColor(android.R.color.black));
            on_the_way_text.setTextColor(getResources().getColor(android.R.color.black));
            on_the_way_text.setBackground(getBottomBorder(10));
        } else if (status.matches("Arrived")) {
            pickup_text.setTextColor(getResources().getColor(android.R.color.black));
            on_the_way_text.setTextColor(getResources().getColor(android.R.color.black));
            arrived_text.setTextColor(getResources().getColor(android.R.color.black));
            arrived_text.setBackground(getBottomBorder(10));

            //set the scan button visible
            scan_button.setVisibility(Button.VISIBLE);
            //click the scan button to pay
            //...
        }

        pickup_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> update = new HashMap<>();
                update.put("status", "Pickup");
                documentReference.update(update);
            }
        });

        on_the_way_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update the status of the request in firebase
                Map<String, Object> update = new HashMap<>();
                update.put("status", "On The Way");
                documentReference.update(update);
            }
        });

        arrived_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update the status of the request in firebase
                Map<String, Object> update = new HashMap<>();
                update.put("status", "Arrived");
                documentReference.update(update);
            }
        });

        if(getSupportFragmentManager().findFragmentById(R.id.map_p) != null) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.remove(getSupportFragmentManager().findFragmentById(R.id.map_p));
            ft.commit();
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.map_p, new MapFragment());
        ft.commit();
    }

    /**
     * add the bottom border to the textView
     * @param bottom
     * @return
     */
    public LayerDrawable getBottomBorder(int bottom) {
        ColorDrawable borderColorDrawable = new ColorDrawable(getResources().getColor(android.R.color.black));
        ColorDrawable backgroundColorDrawable = new ColorDrawable(getResources().getColor(R.color.status_bg_color));

        Drawable[] drawables = new Drawable[]{
                borderColorDrawable,
                backgroundColorDrawable
        };
        LayerDrawable layerDrawable = new LayerDrawable(drawables);
        layerDrawable.setLayerInset(1,0,0,0, bottom);

        return layerDrawable;
    }

    /**
     * add the start and end markers to the map (include address) -> waiting status
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
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.40);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        googleMap.moveCamera(cameraUpdate);
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

