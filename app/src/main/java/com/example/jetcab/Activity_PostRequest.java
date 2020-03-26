package com.example.jetcab;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Locale;

/**
 * @author Joyce, ...(who did the fare part!!!)
 * enable posting request for riders
 */
public class Activity_PostRequest extends AppCompatActivity {
    FusedLocationProviderClient fusedLocationProviderClient;
    TextInputLayout textInputLayout_from;
    TextInputEditText editText_from;
    TextInputLayout textInputLayout_to;
    TextInputEditText editText_to;
    Button post_button;
    Geocoder geocoder;
    String start_location, end_location;
    TextView fare;
    Double start_lat, start_lng, end_lat, end_lng;
    private static final int LAT_LNG_REQUEST_CODE = 0;

    /**
     * be able to specify the start and end location on map when click the map icon
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_request);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //get the current location latitude and longitude
        //get the current address
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this, Locale.getDefault());
        //check whether the location permission is open
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        } else {
            Toast.makeText(getApplicationContext(), "Turn on the Location Permission", Toast.LENGTH_LONG).show();
        }

        //click the map icon to specify start location on map
        textInputLayout_from = findViewById(R.id.from_textField);
        editText_from = findViewById(R.id.from_editText);

        //click the map icon to specify start location on map
        textInputLayout_from.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_location = editText_from.getText().toString();
                if (start_location.matches("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter the Start Location", Toast.LENGTH_LONG).show();
                } else {
                    if (isAddressValid(start_location)) {
                        Intent intent = new Intent(v.getContext(), Activity_RiderMapDisplay.class);
                        intent.putExtra("START LOCATION", start_location);
                        intent.putExtra("TYPE", "from");
                        //get the lat and lng from MapDisplay.class
                        startActivity(intent);
                        //update fare fair
                        final TextView fare = findViewById(R.id.fair_fare_text);
                        fare.setText(getFare(start_location, end_location));
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Start Location Address", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        textInputLayout_to = findViewById(R.id.to_textField);
        editText_to = findViewById(R.id.to_editText);

        //click the map icon to specify end location on map
        textInputLayout_to.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end_location = editText_to.getText().toString();
                if (end_location.matches("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter the End Location", Toast.LENGTH_LONG).show();
                } else {
                    if (isAddressValid(end_location)) {
                        Intent intent = new Intent(v.getContext(), Activity_RiderMapDisplay.class);
                        intent.putExtra("END LOCATION", end_location);
                        intent.putExtra("TYPE", "to");
                        startActivity(intent);
                        //update fair fare
                        final TextView fare = findViewById(R.id.fair_fare_text);
                        fare.setText(getFare(start_location, end_location));
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid End Location Address", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        //post and save the information of ride, end the activity
        post_button = findViewById(R.id.post_button);
        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check whether the input address is valid
                if (isAddressValid(editText_from.getText().toString()) && !start_location.matches("")
                        && !end_location.matches("") && isAddressValid(editText_to.getText().toString())) {
                    //...
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Locations Address", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Returns the amount of the fare based on a fair calculation.
     * Fares calculated by base amount + price per KM.
     * @param start string address of starting/pickup location
     * @param end string address of end/dropoff location
     * @return a string containing the fare amount
     */
    @SuppressLint("DefaultLocale")
    private String getFare(String start, String end) {
        float[] distance = new float[1];
        Double startLat = getLat(start);
        Double startLng = getLng(start);
        Double endLat = getLat(end);
        Double endLng = getLng(end);
        double perKM = 0.80;
        double base = 5.0;
        double amount = 0;

        if (!(startLat == null) & !(startLng == null) & !(endLat == null) & !(endLng == null)) {
            Location.distanceBetween(startLat, startLng, endLat, endLng, distance);
            Log.d("distance", "Distance: " + (distance[0]));
            amount = (base + perKM*distance[0]/1000);
        }
        return String.format("$%.2f", amount); //convert to float, divide by 1000 to get km
    }

    /**
     * get the address of current location
     */
    private void getLastLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //get the latitude and longitude of current location
                Location location = task.getResult();
                if (location != null) {
                    String address = null;
                    Double curr_latitude, curr_longitude;
                    curr_latitude = location.getLatitude();
                    curr_longitude = location.getLongitude();

                    //get the address of current location
                    try {
                        List<Address> addresses = geocoder.getFromLocation(curr_latitude, curr_longitude, 1);
                        address = addresses.get(0).getAddressLine(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    editText_from.setText(address);
                } else {
                    Toast.makeText(getApplicationContext(), "Turn on the Location Permission", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * check if the location is valid
     * @param location
     * @return true/false
     */
    public boolean isAddressValid(String location) {
        Geocoder coder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addressList = coder.getFromLocationName(location, 1);
            if (addressList == null && addressList.size() < 1) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * get the latitude of location
     * @param location
     * @return latitude
     */
    public Double getLat(String location) {
        Address address;
        Double lat = null;
        try {
            List<Address> addresses = geocoder.getFromLocationName(location, 5);
            if (addresses != null && addresses.size() > 0) {
                address = addresses.get(0);
                lat = address.getLatitude();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lat;
    }

    /**
     * get the longitude of location
     * @param location
     * @return longitude
     */
    public Double getLng(String location) {
        Address address;
        Double lng = null;
        try {
            List<Address> addresses = geocoder.getFromLocationName(location, 5);
            if (addresses != null && addresses.size() > 0) {
                address = addresses.get(0);
                lng = address.getLongitude();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lng;
    }
}


