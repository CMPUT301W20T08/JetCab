package com.example.jetcab;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.jetcab.Signup.TAG;

public class Request {
    private static FirebaseAuth myFirebaseAuth;
    private static FirebaseFirestore myFF;
    private static String userID;
    private static String status;
    private static String CurrDateTime;

    /**
     * create a new Request and will save it in the firebase
     * Initially set the request to Open
     */

    public Request(LatLng pickup, LatLng dropoff, float fare) {
        ResponseOpenRequest();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        CurrDateTime = simpleDateFormat.format(new Date());
        myFirebaseAuth = FirebaseAuth.getInstance();
        myFF = FirebaseFirestore.getInstance();
        userID = myFirebaseAuth.getCurrentUser().getUid();
        String pick = pickup.latitude + "," + pickup.longitude;
        String drop = dropoff.latitude + "," + dropoff.longitude;
        DocumentReference dF = myFF.collection("Requests").document(userID);
        Map<String, Object> request = new HashMap<>();
        request.put("Date And Time", CurrDateTime);
        request.put("User ID", userID);
        request.put("Pickup Coordinates", pick);
        request.put("DropOff Coordinates", drop);
        request.put("fare", fare);
        request.put("status", status);
        request.put("Driver User Id", "Not Assigned Yet");
        dF.set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Request Successfully created");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Request Unsuccessfull");
            }
        });
    }

    /**
     * Set the status open, will be used to display all open request to the driver
     */
    public void ResponseOpenRequest() {
        status = "Open";
    }

    /**
     * Set the status  cancelled, if the rider cancelled the ride;
     */
    public static void CancelledRequest(String userID, final DocumentReference dF) {
        status = "Cancelled";
        Map<String, Object> update = new HashMap<>();
        update.put("status", status);
        myFirebaseAuth = FirebaseAuth.getInstance();
        myFF = FirebaseFirestore.getInstance();
        final DocumentReference dF1 = myFF.collection("Cancelled Requests").document(userID);
        dF.update(update).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Request Successfully Updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Request Update Unsuccessfull");
            }
        });
        ShiftData(dF, dF1);
    }

    /**
     * Set the status  completed after the rider has reached his destination;
     */
    public static void CompletedRequest(String userID, final DocumentReference dF) {

        status = "Completed";
        Map<String, Object> update = new HashMap<>();
        update.put("status", status);
        myFirebaseAuth = FirebaseAuth.getInstance();
        myFF = FirebaseFirestore.getInstance();
        final DocumentReference dF1 = myFF.collection("Completed Requests").document(userID);
        dF.update(update).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Request Successfully Updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Request Update Unsuccessful");
            }
        });
        ShiftData(dF, dF1);
    }

    /**
     * Set the status Accepted if the driver accepted the riders request;
     */
    public static void AcceptedRequest(String DriverID, String userID, final DocumentReference dF) {
        status = "Accepted";
        Map<String, Object> update = new HashMap<>();
        update.put("status", status);
        update.put("Driver User Id", DriverID);
        myFirebaseAuth = FirebaseAuth.getInstance();
        myFF = FirebaseFirestore.getInstance();
        dF.update(update).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Request Successfully Updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Request Update Unsuccessful");
            }
        });
        final DocumentReference dF1 = myFF.collection("Accepted Requests").document(userID);
        ShiftData(dF, dF1);
    }

    public static void ShiftData(final DocumentReference dF, final DocumentReference dF1) {
        dF.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        dF1.set(document.getData())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                        dF.delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error deleting document", e);
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}