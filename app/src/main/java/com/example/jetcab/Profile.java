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

//This is user's profile
public class Profile extends AppCompatActivity {

    TextView name,email,phone,back;
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

        //set the tile "My Profile"
        this.setTitle("My Profile");

        name = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        back=findViewById(R.id.back);

        myFirebaseAuth = FirebaseAuth.getInstance();
        myFF = FirebaseFirestore.getInstance();

        //get the user's id and read the user's name, email and phone, and then set texts
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

        //create dialog to edit email
        diaE = new AlertDialog.Builder(this).create();
        editE = new EditText(this);
        diaE.setTitle("Edit email address");
        diaE.setView(editE);

        //set positive button "Save"
        //The user clicks on the "Save" button, update email on the screen and in firebase
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

        //set negative button "Cancel"
        //The user clicks on the "Cancel" button, do nothing
        diaE.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                diaE.dismiss();
            }
        });

        //show the dialog
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editE.setText(email.getText());
                diaE.show();
            }
        });

        //create dialog to edit phone
        diaP = new AlertDialog.Builder(this).create();
        editP = new EditText(this);
        diaP.setTitle("Edit phone number");
        diaP.setView(editP);

        //set positive button "Save"
        //The user clicks on the "Save" button, update phone on the screen and in firebase
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

        //set negative button "Cancel"
        //The user clicks on the "Cancel" button, do nothing
        diaP.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                diaP.dismiss();
            }
        });

        //show the dialog
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editP.setText(phone.getText());
                diaP.show();
            }
        });

        //back to previous activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
