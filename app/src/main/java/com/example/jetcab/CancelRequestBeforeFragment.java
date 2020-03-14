package com.example.jetcab;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

/**
 * @author Charles
 * This is the fragment pop up window that
 * prompts the rider to cancel the request before
 * it gets accepted (Yes or No)
 *
 * NOTE: the name of this fragment is CancelRequestBefore
 * but it will search for the userID in both Requests and Accepted Requests
 * and delete accordingly, fulfilling both user stories of cancelling a request
 * before acceptance and after. Renaming of the fragment will be done later
 */

public class CancelRequestBeforeFragment extends DialogFragment {

    private FirebaseAuth myFirebaseAuth;
    private FirebaseFirestore myFF;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        myFirebaseAuth = FirebaseAuth.getInstance();
        myFF = FirebaseFirestore.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // pop up window initialization
        return builder
                .setTitle("CANCEL THE REQUEST")
                .setMessage("Are you sure you want to cancel the request?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // of the ID of the current user
                        String userID = myFirebaseAuth.getCurrentUser().getUid();
                        DocumentReference docRef = myFF.collection("Requests").document(userID);
                        // check if the rider's request exists in Requests
                        // if so, delete, otherwise, delete from Accepted Requests
                        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                // reinitialization
                                String userID = myFirebaseAuth.getCurrentUser().getUid();

                                if (documentSnapshot.exists()) {
                                    // Request exists in requests, delete
                                    Request.CancelledRequest(userID, myFF.collection("Requests").document(userID));
                            } else {
                                    // Request doesn't exist in requests, delete from Accepted Requests
                                    Request.CancelledRequest(userID, myFF.collection("Accepted Requests").document(userID));
                                }
                        }
                    });


                        // finish activity
                        getActivity().finish();
                    }
                })
                .setNegativeButton("No",null)
                .create();

    }
}
