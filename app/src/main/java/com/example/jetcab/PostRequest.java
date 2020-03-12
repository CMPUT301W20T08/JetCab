package com.example.jetcab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

public class PostRequest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_request);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //click the map icon to from location on map
        TextInputLayout textInputLayout_from = findViewById(R.id.from_textField);
        textInputLayout_from.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(v.getContext(), MapDisplay.class);
                startActivityForResult(mapIntent, 0);
            }
        });

        //post and save the information of ride, end the activity
        Button post_button = findViewById(R.id.post_button);
        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //...
                finish();
            }
        });
    }
}
