package com.example.jetcab;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * this is rider's main menu
 */
public class Activity_MainMenuR extends AppCompatActivity {

    Button PostRequest, current_req, past_req, profileR, signoutR;

    /**
     * asks the rider to choose one task
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_r);

        //set the tile "Rider's Main Menu"
        this.setTitle("Rider Main Menu");

        signoutR = findViewById(R.id.signout_buttonR);
        profileR = findViewById(R.id.profileR);
        PostRequest = findViewById(R.id.postrequest);
        current_req = findViewById(R.id.current_req);
        past_req = findViewById(R.id.past_req);

        //rider post requests functionality
        PostRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent post_request_intent = new Intent(v.getContext(), Activity_PostRequest.class);
                startActivity(post_request_intent);
            }
        });

        //rider should be able to see their current request
        current_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle coords = Activity_PostRequest.getValues();
                if (coords == null){  //checks if there is an active request
                    Toast.makeText(getApplicationContext(), "No Current Active Requests", Toast.LENGTH_LONG).show();
                } else { //shows current request if active
                    Intent current_request_intent = new Intent(getApplicationContext(), CurrentRequest.class);
                    current_request_intent.putExtra("COORDS", coords);
                    startActivity(current_request_intent);
                }
            }
        });

        //rider should be able to see thier past requests
        past_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainMenuR.this, PastRequest.class)); //todo need to implement past requests
            }
        });

        //go to the user's profile if the user clicks on "My Profile"
        profileR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_MainMenuR.this, Activity_Profile.class));
            }
        });

        //Sign out if the user click on signout button
        signoutR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Activity_MainMenuR.this, MainActivity.class));
            }
        });
    }
}
