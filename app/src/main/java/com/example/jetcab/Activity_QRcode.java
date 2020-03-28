package com.example.jetcab;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * @author Yingxin
 * this is where the rider generate QR code
 */
public class Activity_QRcode extends AppCompatActivity {
    Button qr_button,button_done;
    ImageView qr_image;
    LinearLayout done,choices;
    RadioButton r1,r2,r3,r4;
    FirebaseAuth myFirebaseAuth;
    FirebaseFirestore myFF;
    String userID;
    Double fare,tip;

    /**
     * adds tip
     * generates qr code
     * shows the qr code
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__qrcode);

        this.setTitle("QR Code");

        qr_button = findViewById(R.id.qr_button);
        qr_image = findViewById(R.id.qr_image);
        done = findViewById(R.id.done);
        button_done = findViewById(R.id.button_done);
        choices = findViewById(R.id.choices);
        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        r3 = findViewById(R.id.r3);
        r4 = findViewById(R.id.r4);

        myFirebaseAuth = FirebaseAuth.getInstance();
        myFF = FirebaseFirestore.getInstance();

        //get fare amount from firebase
        userID = myFirebaseAuth.getCurrentUser().getUid();
        DocumentReference dr = myFF.collection("Accepted Requests").document(userID);
        dr.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                fare = documentSnapshot.getDouble("fare");
            }
        });

        //add tip amount based on rider's selection
        qr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    if (r1.isChecked()){
                        tip=0d;
                    }
                    else if (r2.isChecked()){
                        tip=1d;
                    }
                    else if (r3.isChecked()){
                        tip=2d;
                    }
                    else{
                        tip=3d;
                    }
                    //calculates total payment
                    Double m = fare + tip;
                    String payment = "$ " + m;
                    //generates the qr code
                    BitMatrix bitMatrix = multiFormatWriter.encode(payment, BarcodeFormat.QR_CODE,300,300);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    qr_image.setImageBitmap(bitmap);
                }catch (WriterException e){
                    e.printStackTrace();
                }
                //visible and invisible views
                choices.setVisibility(View.INVISIBLE);
                done.setVisibility(View.VISIBLE);
            }
        });

        //clicks on done button to go back to previous page
        button_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
