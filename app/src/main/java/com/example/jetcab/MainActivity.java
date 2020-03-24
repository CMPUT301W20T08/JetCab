package com.example.jetcab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Paint;
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
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;


//This is login activity
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText loginEmail, loginPassword;
    Button loginButton;
    TextView signup;
    FirebaseAuth myFirebaseAuth;
    FirebaseFirestore myFF;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Login");  //set the tile "Login"

        checkPermission();

        //need FirebaseAuth and FirebaseFirestore to login, and check collections and documents
        myFirebaseAuth = FirebaseAuth.getInstance();
        myFF = FirebaseFirestore.getInstance();
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signup = findViewById(R.id.signup);
        signup.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        //check if a user has already logged
        //if there is no current user then call ifexits
        if (myFirebaseAuth.getCurrentUser() != null) {
            ifexits();
        }

        //if the user clicks on the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailIn = loginEmail.getText().toString();
                String passwordIn = loginPassword.getText().toString();

                //check whether email is empty or not, set error if it is empty
                if (emailIn.isEmpty()) {
                    loginEmail.setError("Please enter email address");
                    loginEmail.requestFocus();
                }
                //check whether password is empty or not, set error if it is empty
                else if (passwordIn.isEmpty()) {
                    loginPassword.setError("Please enter password");
                    loginPassword.requestFocus();
                } else {
                    //sign in use email and password
                    myFirebaseAuth.signInWithEmailAndPassword(emailIn, passwordIn).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Login Unsuccessful, Please Try Again" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                //call ifexits to check the user's role
                                ifexits();
                            }
                        }
                    });
                }
            }
        });
        //if the user clicks on "SIGN UP HERE" textview, go to Signup activity
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Signup.class));
            }
        });
    }

    public void ifexits() {
        //get the current user's user ID
        userID = myFirebaseAuth.getCurrentUser().getUid();
        //check if the user is in "rider" collection
        DocumentReference dr = myFF.collection("rider").document(userID);
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    //if the user is the "rider" collection
                    //the user's role is a rider
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!");
                        //go to the MainMenuR activity (Rider's Main Menu)
                        startActivity(new Intent(MainActivity.this, MainMenuR.class));
                        //if the user is not in the "rider" collection (which means it is in "driver" collection)
                        //the user's role is a driver
                    } else {
                        Log.d(TAG, "Document does not exist!");
                        //go to the MainMenuD activity (Driver's Main Menu)
                        startActivity(new Intent(MainActivity.this, MainMenuD.class));
                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });
    }

    //asks for permissions
    public void checkPermission() {
        PermissionListener pl = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
            }
        };

        TedPermission.with(MainActivity.this)
                .setPermissionListener(pl)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET)
                .check();
    }
}