package com.example.jetcab;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * @author Charles
 * This is the fragment pop up window that
 * prompts the rider to cancel the request before
 * it gets accepted (Yes or No)
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
                        // remove the request from Requests on FireBase
                        // of the ID of the current user
                        String userID = myFirebaseAuth.getCurrentUser().getUid();
                        Request.CancelledRequest(userID, myFF.collection("Requests").document(userID));
                        // finish activity
                        getActivity().finish();
                    }
                })
                .setNegativeButton("No",null)
                .create();

    }
}
