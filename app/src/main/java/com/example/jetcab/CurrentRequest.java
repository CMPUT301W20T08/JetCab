package com.example.jetcab;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

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
import com.google.android.gms.maps.model.MarkerOptions;


public class CurrentRequest extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView status, wait;
    private Button cancel_button;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_request);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        wait = findViewById(R.id.wait);
        status = findViewById(R.id.status_text);
        cancel_button = findViewById(R.id.cancel_button);

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
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady ( GoogleMap googleMap ) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng (53.518882, -113.453807 );
        mMap.addMarker ( new MarkerOptions ( ).position ( sydney ).title ( "Marker in Sydney" ) );
        mMap.moveCamera ( CameraUpdateFactory.newLatLng ( sydney ) );
        Request c=new Request ( sydney,sydney,300 );
        Request d=new Request ( sydney,sydney,300 );
        Request f=new Request ( sydney,sydney,200 );




    }
}
