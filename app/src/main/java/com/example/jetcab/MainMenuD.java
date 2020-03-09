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

    Button signoutD;
    TextView profileD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_d);

        //set the tile "Driver's Main Menu"
        this.setTitle("Driver's Main Menu");

        signoutD = findViewById(R.id.signout_buttonD);
        profileD = findViewById(R.id.profileD);

        //Sign out if the user click on signout button
        signoutD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainMenuD.this,MainActivity.class));
            }
        });

        //go to the user's profile if the user clicks on "My Profile"
        profileD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuD.this,Profile.class));
            }
        });
    }
}
