package com.example.jetcab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class Driver_Search_Request extends AppCompatActivity {

    ListView openRequests;


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_driver__search__request );
    }
}
