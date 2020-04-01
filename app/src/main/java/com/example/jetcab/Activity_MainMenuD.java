package com.example.jetcab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

/**
 * this is driver's main menu
 */
public class Activity_MainMenuD extends AppCompatActivity {

    //    Button signoutD;
    //    TextView profileD;
    Button search_req_button, current_reqs_button, past_reqs_button, profile_button, signoutD;

    /**
     * asks the driver to choose one task
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_d);
        this.setTitle("Driver's Main Menu"); //set the tile "Driver's Main Menu"

        search_req_button = findViewById(R.id.search_req);
        current_reqs_button = findViewById(R.id.current_reqD);
        past_reqs_button = findViewById(R.id.past_reqD);
        profile_button = findViewById(R.id.profileD);
        signoutD = findViewById(R.id.signout_buttonD);
        profile_button = findViewById(R.id.profileD);

        //driver should be able to see all the active requests.
        search_req_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_MainMenuD.this, Driver_Search_Request.class));
            }
        });
        //driver should be able to see their current request.
        current_reqs_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_MainMenuD.this, Activity_DriverCurrentRequest.class));
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
                startActivity(new Intent(Activity_MainMenuD.this, Activity_Profile.class));
            }
        });
        //Sign out if the user click on signout button
        signoutD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Activity_MainMenuD.this, MainActivity.class));
            }
        });
    }
}
