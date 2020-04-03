package com.example.jetcab;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;



/**
 * this is rider's main menu
 */
public class Activity_MainMenuR extends AppCompatActivity {


    Button PostRequest, current_req, past_req, profileR;
    ImageView signoutR;
    private static FirebaseAuth myFirebaseAuth;
    private static FirebaseFirestore myFF;
    private static String userID;
    private  Bundle coords;


    /**
     * asks the rider to choose one task
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_r);


        //set the tile "Rider's Main Menu"
        this.setTitle("Rider Main Menu");
        signoutR = findViewById(R.id.logout_r_image_button);

        profileR = findViewById(R.id.profileR);
        PostRequest = findViewById(R.id.postrequest);
        current_req = findViewById(R.id.current_req);
        past_req = findViewById(R.id.past_req);

        //rider post requests functionality
        PostRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent post_request_intent = new Intent(v.getContext(), Activity_PostRequest.class);
                startActivity(post_request_intent);
            }
        });

        //rider should be able to see their current request
        current_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coords == null){  //checks if there is an active request
                    Toast.makeText(getApplicationContext(), "No Current Active Requests", Toast.LENGTH_LONG).show();
                }
                 else { //shows current request if active
                    getBundle ();
                    Intent current_request_intent = new Intent(Activity_MainMenuR.this, CurrentRequest.class);
                    current_request_intent.putExtra("COORDS", coords);
                    startActivity(current_request_intent);
                }
            }
        });

//        //rider should be able to see thier past requests
//        past_req.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent past_req_intent = new Intent(Activity_MainMenuR.this, Activity_rider_past_requests.class);
//                startActivity(past_req_intent);
//                startActivity(new Intent(Activity_MainMenuR.this, Activity_rider_past_requests.class));
//            }
//        });

        //go to the user's profile if the user clicks on "My Profile"
        profileR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_MainMenuR.this, Activity_Profile.class));
            }
        });

        //Sign out if the user click on signout button
        signoutR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Activity_MainMenuR.this, MainActivity.class));
            }
        });
    }

    public void getBundle()
    {
        myFirebaseAuth = FirebaseAuth.getInstance();
        myFF = FirebaseFirestore.getInstance();
        userID = myFirebaseAuth.getCurrentUser().getUid();
        DocumentReference dF = myFF.collection("Requests").document(userID);
        final DocumentReference dF1 = myFF.collection("Accepted Requests").document(userID);
        dF.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot> () {

            @Override
            public void onComplete ( @NonNull Task<DocumentSnapshot> task ) {
                if (task.isSuccessful ( )) {
                    DocumentSnapshot document = task.getResult ( );
                    if (document!= null) {
                        coords = new Bundle (  );
                        String coordinates[] = document.get ( "Pickup Coordinates" ).toString ( ).split ( "," );
                        String coordinates1[] = document.get ( "DropOff Coordinates" ).toString ( ).split ( "," );
                        coords.putParcelable ( "PICKUP", new LatLng ( Double.parseDouble ( coordinates[ 0 ] ), Double.parseDouble ( coordinates[ 1 ] ) ) );   //https://stackoverflow.com/questions/16134682/how-to-send-a-latlng-instance-to-new-intent
                        coords.putParcelable ( "DROPOFF", new LatLng ( Double.parseDouble ( coordinates1[ 0 ] ), Double.parseDouble ( coordinates1[ 1 ] ) ) );
                    } else {
                        dF1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot> () {

                            @Override
                            public void onComplete ( @NonNull Task<DocumentSnapshot> task ) {
                                if (task.isSuccessful ( )) {
                                    DocumentSnapshot document = task.getResult ( );
                                    if (document!= null) {
                                        coords=new Bundle();
                                        String coordinates[] = document.get ( "Pickup Coordinates" ).toString ( ).split ( "," );
                                        String coordinates1[] = document.get ( "DropOff Coordinates" ).toString ( ).split ( "," );
                                        coords.putParcelable ( "PICKUP", new LatLng ( Double.parseDouble ( coordinates[ 0 ] ), Double.parseDouble ( coordinates[ 1 ] ) ) );   //https://stackoverflow.com/questions/16134682/how-to-send-a-latlng-instance-to-new-intent
                                        coords.putParcelable ( "DROPOFF", new LatLng ( Double.parseDouble ( coordinates1[ 0 ] ), Double.parseDouble ( coordinates1[ 1 ] ) ) );
                                    } else {
                                        Log.d("TAG","no such document");
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
