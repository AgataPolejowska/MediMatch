package com.i.medimatch;


import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;


public class InfoDialog extends AppCompatDialogFragment  {

    private TextView textView;
    private StringBuilder text = new StringBuilder();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_info, null);

        textView = view.findViewById(R.id.info);

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(getContext().getAssets().open("info.txt")));

            // Read file until the end of file
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                text.append(mLine);
                text.append('\n');
            }

        } catch (IOException e) {

            Toast.makeText(getContext().getApplicationContext(),"Error reading file!",Toast.LENGTH_LONG).show();
            e.printStackTrace();

        } finally {

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
            
        }

        textView.setText((CharSequence) text);

        builder.setView(view)
                .setTitle("Information")
                .setCancelable(true)
                .setMessage("Instructions and guidelines")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

            return builder.create();

    }

}