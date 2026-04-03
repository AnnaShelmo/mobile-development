package com.mirea.shelmichas.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class MyProgressDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Загрузка...");
        builder.setMessage("Пожалуйста, подождите");

        AlertDialog dialog = builder.create();

        new Handler().postDelayed(dialog::dismiss, 3000);

        return dialog;
    }
}
