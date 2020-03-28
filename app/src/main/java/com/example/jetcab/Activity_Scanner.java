package com.example.jetcab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Yingxin
 * this is where the driver scan QR code
 */
public class Activity_Scanner extends AppCompatActivity {
    Button scan,done_btn;
    public static TextView result;
    LinearLayout all;

    /**
     * asks the driver to scan QR code
     * the amount of payment the driver received will be shown
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__scanner);

        this.setTitle("QR Scanner");

        scan = findViewById(R.id.sc_btn);
        result = findViewById(R.id.result);
        done_btn = findViewById(R.id.done_btn);
        all = findViewById(R.id.all);

        //clicks on scan qr code button
        //go to Activity_scanCode
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Activity_scanCode.class));
                Handler handler = new Handler();
                //the result will display in 3s
                handler.postDelayed(new Runnable() {
                    public void run() {
                        all.setVisibility(View.VISIBLE);
                    }
                }, 3000);

            }
        });

        //go back to previous page
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
