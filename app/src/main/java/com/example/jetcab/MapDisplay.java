package com.example.jetcab;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapDisplay extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    String start_location;
    TextView test;
    Double start_lat, start_lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_display);
        MapDisplay.this.setTitle("Map View");

        test = findViewById(R.id.test);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker of start location
        Intent intent = getIntent();
        start_location = intent.getStringExtra("START LOCATION");

        GeocoderHandler geocoderHandler = new GeocoderHandler();
        getLatLng(start_location, getApplicationContext(), geocoderHandler);
        //!!!!!get error here, cannot get lat and lng from handler!!!!!
        if (start_lat == null && start_lng == null) {
            LatLng from = new LatLng(Double.valueOf(test.getText().toString()),-29.3);
            mMap.addMarker(new MarkerOptions().position(from).title(start_location));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(from));
        }
    }

    public static void getLatLng(final String location, final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                Address address;
                Double lat = null;
                Double lng = null;
                try {
                    List<Address> addresses = geocoder.getFromLocationName(location, 5);
                    if (addresses != null && addresses.size() > 0) {
                        address = addresses.get(0);
                        lat = address.getLatitude();
                        lng = address.getLongitude();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (lat != null && lng != null) {
                        Bundle bundle = new Bundle();
                        bundle.putDouble("LATITUDE", lat);
                        bundle.putDouble("LONGITUDE", lng);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }

    private class GeocoderHandler extends Handler {
        Double lat, lng;
        @Override
        public void handleMessage(Message message) {
            Bundle bundle = message.getData();
            lat = bundle.getDouble("LATITUDE");
            lng = bundle.getDouble("LONGITUDE");
            test.setText(lat.toString());
        }
    }

}
