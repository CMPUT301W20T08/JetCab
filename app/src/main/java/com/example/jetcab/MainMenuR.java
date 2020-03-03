package com.example.jetcab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainMenuR extends AppCompatActivity {

    Button signoutR;
    TextView profileR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_r);
        this.setTitle("Rider's Main Menu");

        signoutR = findViewById(R.id.signout_buttonR);
        profileR = findViewById(R.id.profileR);

        signoutR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainMenuR.this,MainActivity.class));
            }
        });

        profileR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuR.this,Profile.class));
            }
        });
    }
}
