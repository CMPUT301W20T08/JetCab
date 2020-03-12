package com.example.jetcab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

//This is driver's main menu
public class MainMenuD extends AppCompatActivity {

//    Button signoutD;
//    TextView profileD;
    Button search_req_button, current_reqs_button, past_reqs_button, profile_button, signoutD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_d);

        //set the tile "Driver's Main Menu"
        this.setTitle("Driver's Main Menu");

        search_req_button = findViewById(R.id.search_req);
        current_reqs_button = findViewById(R.id.current_reqD);
        past_reqs_button = findViewById(R.id.past_reqD);
        profile_button = findViewById(R.id.profileD);
        signoutD = findViewById(R.id.signout_buttonD);
        profile_button = findViewById(R.id.profileD);

        //Sign out if the user click on signout button
        signoutD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuD.this, CurrentRequest.class));
            }
        });

        //view drivers previously completed trips
        past_reqs_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainMenuD.this, PastRequests.class)); //TODO need to implement pastrequests.java
            }
        });

        //go to the user's profile if the user clicks on "My Profile"
        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuD.this,Profile.class));
            }
        });

        //driver should be able to see all the current requests.
        search_req_button.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                startActivity ( new Intent ( MainMenuD.this,Driver_Search_Request.class ) );
            }
        } );

        //Sign out if the user click on signout button
        signoutD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainMenuD.this,MainActivity.class));
            }
        });

    }
}
