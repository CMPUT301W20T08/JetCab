package com.example.jetcab;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AcceptRequest extends AppCompatActivity {

    TextView username, email, phone, pickup, dropoff, fare;
    Button Accept;
    private FirebaseAuth myFirebaseAuth;
    private FirebaseFirestore myFF;

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

        myFirebaseAuth = FirebaseAuth.getInstance();
        myFF = FirebaseFirestore.getInstance();

        DocumentReference dF = myFF.collection("users").document(getIntent().getStringExtra("UserId"));
        dF.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                username.setText((String) documentSnapshot.get("username"));
                email.setText((String) documentSnapshot.get("email"));
                phone.setText((String) documentSnapshot.get("phone"));
            }
        });

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

        Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference dF = myFF.collection("Requests").document(getIntent().getStringExtra("UserId"));
                Request.AcceptedRequest(getIntent().getStringExtra("DriverID"), getIntent().getStringExtra("UserId"), dF);
                finish();
            }
        });


    }
}
