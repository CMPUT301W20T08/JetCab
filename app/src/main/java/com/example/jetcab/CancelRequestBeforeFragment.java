package com.example.jetcab;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * This is the fragment pop up window that
 * prompts the user to cancel the request (Yes or No)
 */

public class CancelRequestBeforeFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setTitle("CANCEL THE REQUEST")
                .setMessage("Are you sure you want to cancel the request?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // finish activity
                        getActivity().finish();
                    }
                })
                .setNegativeButton("No",null)
                .create();

    }
}
