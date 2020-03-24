package com.example.jetcab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
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

//This is Sign up activity
public class Signup extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText signupEmail, signupPassword;
    EditText signupName, signupPhone;
    Button signupButton;
    TextView login;
    RadioGroup signupGroup;
    RadioButton signupRider, signupDriver;
    FirebaseAuth myFirebaseAuth;
    FirebaseFirestore myFF;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //set the tile "Sign up"
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
        signupRider = findViewById(R.id.signup_rider);
        signupDriver = findViewById(R.id.signup_driver);
        login.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        //if the user clicks on sign up button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailUp = signupEmail.getText().toString();
                String passwordUp = signupPassword.getText().toString();
                int selectUp = signupGroup.getCheckedRadioButtonId();
                final String name = signupName.getText().toString();
                final String phone = signupPhone.getText().toString();

                //check whether email is empty or not, set error if it is empty
                if (emailUp.isEmpty()) {
                    signupEmail.setError("Please enter email address");
                    signupEmail.requestFocus();
                }
                //check whether password is empty or not, set error if it is empty
                else if (passwordUp.isEmpty()) {
                    signupPassword.setError("Please enter password");
                    signupPassword.requestFocus();
                }
                //check whether email is less than 6 characters or not, set error if it is less than 6 characters
                else if (passwordUp.length() < 6) {
                    signupPassword.setError("Password must be >= 6 characters");
                    signupPassword.requestFocus();
                }
                //check whether name is empty or not, set error if it is empty
                else if (name.isEmpty()) {
                    signupName.setError("Please enter username");
                    signupName.requestFocus();
                }
                //check whether phone is empty or not, set error if it is empty
                else if (phone.isEmpty()) {
                    signupPhone.setError("Please enter phone number");
                    signupPhone.requestFocus();
                }
                //check whether the user choose a role or not
                else if (selectUp == -1) {
                    Toast.makeText(Signup.this, "Please choose a role", Toast.LENGTH_SHORT).show();
                } else {
                    myFirebaseAuth.createUserWithEmailAndPassword(emailUp, passwordUp).addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Signup.this, "signup unsuccessfully, please try again. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                //check if the username is used
                                DocumentReference docu = myFF.collection("users_list").document(name);
                                docu.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {

                                            DocumentSnapshot document = task.getResult();
                                            // If the username is used, set error
                                            if (document.exists()) {
                                                Log.d(TAG, "Document exists!");
                                                signupName.setError("Username is used");
                                                signupName.requestFocus();
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                //delete the user
                                                user.delete()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d(TAG, "User account deleted");
                                                                } else {
                                                                    Log.d(TAG, "Failed with: ", task.getException());
                                                                }
                                                            }
                                                        });
                                            } else {
                                                //if the username is not created
                                                Log.d(TAG, "Document does not exist!");
                                                //get the user's user ID
                                                userID = myFirebaseAuth.getCurrentUser().getUid();
                                                //create "users_list" collection which the document is the user's name
                                                DocumentReference docu = myFF.collection("users_list").document(name);
                                                Map<String, Object> users = new HashMap<>();
                                                //add name and role value into the user's field
                                                users.put("username", name);
                                                if (signupRider.isChecked()) {
                                                    users.put("role", "rider");
                                                } else {
                                                    users.put("role", "driver");
                                                }
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

                                                //create "users" collection which the document is the user's user ID
                                                DocumentReference dF = myFF.collection("users").document(userID);
                                                Map<String, Object> u = new HashMap<>();
                                                //add name, email, phone and role value into the user's field
                                                u.put("username", name);
                                                u.put("email", emailUp);
                                                u.put("phone", phone);
                                                if (signupRider.isChecked()) {
                                                    u.put("role", "rider");
                                                } else {
                                                    u.put("role", "driver");
                                                }
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

                                                //if the user sign up as a rider
                                                //create "rider" collection and add the user's name as a value
                                                //go to MainMenuR activity
                                                if (signupRider.isChecked()) {
                                                    DocumentReference dr = myFF.collection("rider").document(userID);
                                                    Map<String, Object> user = new HashMap<>();
                                                    user.put("username", name);
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
                                                    startActivity(new Intent(Signup.this, MainMenuR.class));
                                                } else {
                                                    //if the user sign up as a driver
                                                    //create "driver" collection and add the user's name as a value
                                                    //go to MainMenuD activity
                                                    DocumentReference dr = myFF.collection("driver").document(userID);
                                                    Map<String, Object> user = new HashMap<>();
                                                    user.put("username", name);
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
                                                    startActivity(new Intent(Signup.this, MainMenuD.class));
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
        //if the user clicks on "LOGIN IN HERE" textview, go to Login activity
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}