package com.cartop.android.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class StopProgramDialog extends DialogFragment implements DialogInterface.OnClickListener {

    public static final String TAG = StopProgramDialog.class.getSimpleName();

    public static StopProgramDialog newInstance() {
        StopProgramDialog fragment = new StopProgramDialog();
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Stop Program")
                .setMessage("Are you sure you want to stop the program?")
                .setNegativeButton("Cancel", this)
                .setPositiveButton("Stop", this)
                .create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        ((DialogInterface.OnClickListener) getActivity()).onClick(dialog, which);
    }
}
