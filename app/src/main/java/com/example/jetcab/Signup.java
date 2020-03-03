package com.example.jetcab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText signupEmail,signupPassword;
    EditText signupName,signupPhone;
    Button signupButton;
    TextView login;
    RadioGroup signupGroup;
    RadioButton signupRider,signupDriver;
    FirebaseAuth myFirebaseAuth;
    FirebaseFirestore myFF;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        this.setTitle("Sign up");

        myFirebaseAuth = FirebaseAuth.getInstance();
        myFF = FirebaseFirestore.getInstance();
        signupEmail = findViewById(R.id.signup_email);
        signupName = findViewById(R.id.signup_username);
        signupPhone = findViewById(R.id.signup_phone);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        login = findViewById(R.id.login);
        signupGroup = findViewById(R.id.signup_roles);
        signupRider=findViewById(R.id.signup_rider);
        signupDriver=findViewById(R.id.signup_driver);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailUp = signupEmail.getText().toString();
                String passwordUp = signupPassword.getText().toString();
                int selectUp = signupGroup.getCheckedRadioButtonId();
                final String name = signupName.getText().toString();
                final String phone = signupPhone.getText().toString();

                if (emailUp.isEmpty()){
                    signupEmail.setError("Please enter email address");
                    signupEmail.requestFocus();
                }
                else if(passwordUp.isEmpty()){
                    signupPassword.setError("Please enter password");
                    signupPassword.requestFocus();
                }
                else if(passwordUp.isEmpty()){
                    signupPassword.setError("Please enter password");
                    signupPassword.requestFocus();
                }
                else if (passwordUp.length() < 6){
                    signupPassword.setError("Password must be >= 6 characters");
                    signupPassword.requestFocus();
                }
                else if(name.isEmpty()){
                    signupName.setError("Please enter username");
                    signupName.requestFocus();
                }
                else if(phone.isEmpty()){
                    signupPhone.setError("Please enter phone number");
                    signupPhone.requestFocus();
                }
                else if(selectUp == -1){
                    Toast.makeText(Signup.this,"Please choose a role",Toast.LENGTH_SHORT).show();
                }
                else{
                    myFirebaseAuth.createUserWithEmailAndPassword(emailUp,passwordUp).addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(Signup.this,"signup unsuccessfully, please try again. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                DocumentReference docu = myFF.collection("users_list").document(name);
                                docu.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {

                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.d(TAG, "Document exists!");
                                                signupName.setError("Username is used");
                                                signupName.requestFocus();
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                user.delete()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    Log.d(TAG, "User account deleted");
                                                                }
                                                                else {
                                                                    Log.d(TAG, "Failed with: ", task.getException());
                                                                }
                                                            }
                                                        });
                                            } else {
                                                Log.d(TAG, "Document does not exist!");
                                                userID = myFirebaseAuth.getCurrentUser().getUid();
                                                DocumentReference docu = myFF.collection("users_list").document(name);
                                                Map<String,Object> users = new HashMap<>();
                                                users.put("username",name);
                                                users.put("role","rider");
                                                docu.set(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "onSuccess: user profile is create for" + name);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d(TAG, "onFailure: " + e.toString());
                                                    }
                                                });

                                                DocumentReference dF = myFF.collection("users").document(userID);
                                                Map<String,Object> u = new HashMap<>();
                                                u.put("username",name);
                                                u.put("email",emailUp);
                                                u.put("phone",phone);
                                                u.put("role","rider");
                                                dF.set(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "onSuccess: user profile is create for" + name);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d(TAG, "onFailure: " + e.toString());
                                                    }
                                                });

                                                if(signupRider.isChecked()){
                                                    DocumentReference dr = myFF.collection("rider").document(userID);
                                                    Map<String,Object> user = new HashMap<>();
                                                    user.put("username",name);
                                                    dr.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d(TAG, "onSuccess: user profile is create for" + userID);
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d(TAG, "onFailure: " + e.toString());
                                                        }
                                                    });
                                                    startActivity(new Intent(Signup.this,MainMenuR.class));
                                                }
                                                else{
                                                    DocumentReference dr = myFF.collection("driver").document(userID);
                                                    Map<String,Object> user = new HashMap<>();
                                                    user.put("username",name);
                                                    dr.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d(TAG, "onSuccess: user profile is create for" + userID);
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d(TAG, "onFailure: " + e.toString());
                                                        }
                                                    });
                                                    startActivity(new Intent(Signup.this,MainMenuD.class));
                                                }
                                            }
                                        } else {
                                            Log.d(TAG, "Failed with: ", task.getException());
                                        }
                                    }
                                });
                            }
                        }
                    });
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }

}
