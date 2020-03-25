package com.example.jetcab;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * @author Yingxin
 * this a splash screen activity
 */
public class Activity_SplashScreen extends AppCompatActivity {
    /**
     * stay on splash screen for 2s before go to the main activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Activity_SplashScreen.this, MainActivity.class));
            }
        },2000);
    }

}
