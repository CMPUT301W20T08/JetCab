package com.example.jetcab;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

/**
 * @author Joyce
 * show the start and end location on the map for accepting request
 */
public class Activity_DriverMapDisplay extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    Geocoder geocoder;
    Double startLat, startLng, endLat, endLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_request_map_display);
        Activity_DriverMapDisplay.this.setTitle("Map View");

        geocoder = new Geocoder(this, Locale.getDefault());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_accept);
        mapFragment.getMapAsync(this);

        Intent intent = this.getIntent();
        startLat = Double.parseDouble(intent.getStringExtra("Start Lat"));
        startLng = Double.parseDouble(intent.getStringExtra("Start Lng"));
        endLat = Double.parseDouble(intent.getStringExtra("End Lat"));
        endLng = Double.parseDouble(intent.getStringExtra("End Lng"));
    }

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
