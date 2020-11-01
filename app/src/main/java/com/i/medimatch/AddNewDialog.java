package com.i.medimatch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

public class AddNewDialog extends AppCompatDialogFragment {

    private EditText editMedName;
    private EditText editMedFunction;
    private EditText editMedUrl;
    private ImageView mImageView;
    private Button loadButton;
    private AddNewMedDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_addnew, null);

        editMedName = view.findViewById(R.id.med_name);
        editMedFunction = view.findViewById(R.id.med_function);
        editMedUrl = view.findViewById(R.id.med_img_url);



        builder.setView(view)
                .setTitle("Add new medication")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String med_name = editMedName.getText().toString();
                        String med_function = editMedFunction.getText().toString();
                        String med_url = editMedUrl.getText().toString();
                        listener.applyValues(med_name, med_function, med_url);
                    }
                });


        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (AddNewMedDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddNewMedDialogListener");
        }
    }

    public interface AddNewMedDialogListener {
        void applyValues(String med_name, String med_function, String med_url);
    }

}
