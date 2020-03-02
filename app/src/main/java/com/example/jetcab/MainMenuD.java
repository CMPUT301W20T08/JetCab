package com.example.jetcab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainMenuD extends AppCompatActivity {

    Button signoutD;
    TextView profileD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_d);
        this.setTitle("Driver's Main Menu");

        signoutD = findViewById(R.id.signout_buttonD);
        profileD = findViewById(R.id.profileD);

        signoutD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainMenuD.this,MainActivity.class));
            }
        });

        profileD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuD.this,Profile.class));
            }
        });
    }
}
