package com.example.jetcab;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Profile extends AppCompatActivity {

    TextView name,email,phone;
    FirebaseAuth myFirebaseAuth;
    FirebaseFirestore myFF;
    String userID;
    AlertDialog diaE;
    AlertDialog diaP;
    EditText editE,editP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.setTitle("My Profile");

        name = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);

        myFirebaseAuth = FirebaseAuth.getInstance();
        myFF = FirebaseFirestore.getInstance();

        userID = myFirebaseAuth.getCurrentUser().getUid();
        DocumentReference dr = myFF.collection("users").document(userID);
        dr.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                name.setText(documentSnapshot.getString("username"));
                email.setText(documentSnapshot.getString("email"));
                phone.setText(documentSnapshot.getString("phone"));
            }
        });

        diaE = new AlertDialog.Builder(this).create();
        editE = new EditText(this);
        diaE.setTitle("Edit email address");
        diaE.setView(editE);
        diaE.setButton(DialogInterface.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                email.setText(editE.getText());
                DocumentReference dr = myFF.collection("users").document(userID);
                dr.update("email", email.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener< Void >() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Profile.this, "Updated Successfully",
                                        Toast.LENGTH_SHORT).show();
                            }

                        });
            }
        });

        diaE.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                diaE.dismiss();
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editE.setText(email.getText());
                diaE.show();
            }
        });


        diaP = new AlertDialog.Builder(this).create();
        editP = new EditText(this);
        diaP.setTitle("Edit phone number");
        diaP.setView(editP);
        diaP.setButton(DialogInterface.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                phone.setText(editP.getText());
                DocumentReference dr = myFF.collection("users").document(userID);
                dr.update("phone", phone.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener< Void >() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Profile.this, "Updated Successfully",
                                        Toast.LENGTH_SHORT).show();
                            }

                        });

            }
        });
        diaP.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                diaP.dismiss();
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editP.setText(phone.getText());
                diaP.show();
            }
        });

    }
}
