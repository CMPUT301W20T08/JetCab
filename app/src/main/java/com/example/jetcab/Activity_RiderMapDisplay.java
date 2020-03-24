package com.example.jetcab;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

/**
 * @author Joyce
 * specify the start and end location on map
 */
public class Activity_RiderMapDisplay extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    String start_location, end_location;
    ImageButton back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_request_map_display);
        Activity_RiderMapDisplay.this.setTitle("Map View");

        back_button = findViewById(R.id.back_image_button);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * connect to the map, check the marker type (from/to), and add marker
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Intent intent = this.getIntent();
        if (intent != null) {
            String type = intent.getStringExtra("TYPE");

            //add the marker of the start location
            if (type.equals("from")) {
                start_location = intent.getStringExtra("START LOCATION");
                getLatLng(start_location, getApplicationContext(), new StartGeocoderHandler());

                //add the marker of the end location
            } else if (type.equals("to")) {
                end_location = intent.getStringExtra("END LOCATION");
                getLatLng(end_location, getApplicationContext(), new EndGeocoderHandler());
            }
        }
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * bundle the latitude and longitude from location
     * start a new thread
     * @param location
     * @param context
     * @param handler
     */
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

    /**
     * get the latitude and longitude of start location, and add marker
     */
    private class StartGeocoderHandler extends Handler {
        public Double start_lat, start_lng;
        @Override
        public void handleMessage(Message message) {
            Bundle bundle = message.getData();
            start_lat = bundle.getDouble("LATITUDE");
            start_lng = bundle.getDouble("LONGITUDE");

            //add start marker
            if (start_lat != null && start_lng != null) {
                LatLng from = new LatLng(start_lat, start_lng);
                mMap.addMarker(new MarkerOptions().
                        position(from).title("Start Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                //move camera to the marker and zoom in the map
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(from, 15));
            }
        }
    }

    /**
     * get the latitude and longitude of end location, and add marker
     */
    private class EndGeocoderHandler extends Handler {
        public Double end_lat, end_lng;
        @Override
        public void handleMessage(Message message) {
            Bundle bundle = message.getData();
            end_lat = bundle.getDouble("LATITUDE");
            end_lng = bundle.getDouble("LONGITUDE");

            //add end marker
            if (end_lat != null && end_lng != null) {
                LatLng to = new LatLng(end_lat, end_lng);
                mMap.addMarker(new MarkerOptions()
                        .position(to).title("End Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                //move camera to the marker and zoom in the map
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(to, 15));
            }
        }
    }
}
