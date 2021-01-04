/*
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are not permitted without written permission form the copyright holders.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.i.medimatch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

/**
 * Represents a dialog opened when the user would like to add a new medication to the game.
 * @author Agata Polejowska
 */
public class AddNewDialog extends AppCompatDialogFragment {

    /** The name of the new medication from the user input. */
    private EditText editMedName;
    /** The function of the new medication from the user input. */
    private EditText editMedFunction;
    /** The image associated with the function of the new medication from the user input (url). */
    private EditText editMedUrl;
    /** An interface for applying values of the new medication added by the user. */
    private AddNewMedDialogListener listener;

    /**
     * Initializing the dialog.
     * @param saveInstanceState a reference to a Bundle object
     * @return dialog with user inputs
     */
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

    /**
     * Associates the dialog with the activity.
     * @param context global information about an application environment
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddNewMedDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddNewMedDialogListener");
        }
    }

    /**
     * The listener interface
     */
    public interface AddNewMedDialogListener {
        void applyValues(String med_name, String med_function, String med_url);
    }

}
