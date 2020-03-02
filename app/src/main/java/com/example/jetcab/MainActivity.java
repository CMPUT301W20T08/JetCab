package com.example.jetcab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText loginEmail,loginPassword;
    Button loginButton;
    TextView signup;
    FirebaseAuth myFirebaseAuth;
    FirebaseFirestore myFF;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("Login");

        myFirebaseAuth = FirebaseAuth.getInstance();
        myFF = FirebaseFirestore.getInstance();
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signup = findViewById(R.id.signup);


        if (myFirebaseAuth.getCurrentUser() != null){
            ifexits();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailIn = loginEmail.getText().toString();
                String passwordIn = loginPassword.getText().toString();

                if (emailIn.isEmpty()){
                    loginEmail.setError("Please enter email address");
                    loginEmail.requestFocus();
                }
                else if(passwordIn.isEmpty()){
                    loginPassword.setError("Please enter password");
                    loginPassword.requestFocus();
                }
                else {
                    myFirebaseAuth.signInWithEmailAndPassword(emailIn,passwordIn).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"login unsuccessfully, please try again. " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            else {
                                ifexits();
                            }
                        }
                    });
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Signup.class));
            }
        });

    }
    void ifexits(){
        userID = myFirebaseAuth.getCurrentUser().getUid();
        DocumentReference dr = myFF.collection("rider").document(userID);
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!");
                        startActivity(new Intent(MainActivity.this,MainMenuR.class));
                    } else {
                        Log.d(TAG, "Document does not exist!");
                        startActivity(new Intent(MainActivity.this,MainMenuD.class));
                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });

    }

}
