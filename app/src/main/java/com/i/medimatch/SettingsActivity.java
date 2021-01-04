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


import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the setting menu where the medication has to be chosen and new medication can be added.
 * @author Agata Polejowska
 */
public class SettingsActivity extends AppCompatActivity implements AddNewDialog.AddNewMedDialogListener {

    /** Stores medication card objects */
    private ArrayList<MedicationCard> medObjects = new ArrayList<>();
    /** Stores names of drugs */
    private ArrayList<String> medNames = new ArrayList<>();
    /** Stores medication functions */
    private ArrayList<String> medFunctions = new ArrayList<>();
    /** Stores and connects a drug name with its function */
    private HashMap<String, String> medNameFunction = new HashMap<>();

    /** Used to create multiple-exclusion scope for a set of radio buttons */
    private RadioGroup radioGroup;
    /** A two-state button */
    private RadioButton radioButton;
    /** An array of radio buttons representing the particular medication */
    private RadioButton [] medRadioButton;

    /** Stores the name of the selected medication */
    private String medNameSelected;
    /** An object representing the selected medication */
    private MedicationCard MedSelected;
    /** An object representing the medication added by the user */
    private MedicationCard NewMed;

    /** An object for managing and playing audio resources. */
    private SoundPool soundPool;
    /** Stores the loaded sound from the APK resource */
    private int clickSound, startSound;

    /**
     * Initializing the activity.
     * @param savedInstanceState a reference to a Bundle object
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        try {
            InputStream inputStream1 = getApplicationContext().getResources().getAssets()
                    .open("medication_names.txt", Context.MODE_WORLD_READABLE);
            InputStream inputStream2 = getApplicationContext().getResources().getAssets()
                    .open("medication_functions.txt", Context.MODE_WORLD_READABLE);

            BufferedReader inputNames = new BufferedReader(new InputStreamReader(inputStream1));
            BufferedReader inputFunctions = new BufferedReader(new InputStreamReader(inputStream2));

            String line = "";
            while ((line = inputNames.readLine()) != null) {
                medNames.add(line);
            }
            while ((line = inputFunctions.readLine()) != null) {
                medFunctions.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i = 0, j = 0; i < medNames.size() && j < medFunctions.size(); i++,j++) {
            medNameFunction.put(medNames.get(i), medFunctions.get(j));
        }

        radioGroup = findViewById(R.id.radioGroup);

        RadioButton med1 = findViewById(R.id.med1);
        RadioButton med2 = findViewById(R.id.med2);
        RadioButton med3 = findViewById(R.id.med3);
        RadioButton med4 = findViewById(R.id.med4);
        RadioButton med5 = findViewById(R.id.med5);

        for(Map.Entry<String, String> entry : medNameFunction.entrySet()) {
            medObjects.add((new MedicationCard(entry.getKey(), entry.getValue())));
        }

        medRadioButton = new RadioButton[]{med1, med2, med3, med4, med5};

        for (int i = 0; i < medObjects.size(); i++) {
            medRadioButton[i].setText((medObjects.get(i).getName()));
        }

        // Implementing  sounds
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(audioAttributes)
                .build();

        clickSound = soundPool.load(this, R.raw.click, 1);
        startSound = soundPool.load(this, R.raw.start, 1);
    }
    /* END OF ON CREATE */


    /**
     * Checks which button was clicked.
     * @param v the user interface component
     */
    public void checkButton(View v) {
        soundPool.play(startSound, 0.75f, 0.75f, 0, 0, 1);
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        YoYo.with(Techniques.Swing)
                .duration(700)
                .playOn(radioButton);
    }

    /**
     * Called when the user taps the Add New button.
     * @param view the user interface component
     */
    public void addNew(View view) {
        openDialog();
    }

    /**
     * Creates a dialog.
     */
    public void openDialog() {
        AddNewDialog addNewDialog = new AddNewDialog();
        addNewDialog.show(getSupportFragmentManager(), "Add new dialog");
    }

    /**
     * Creates and applies values to new medication card object.
     * @param med_name the name of the medication
     * @param med_function the function of the medication
     * @param med_img the image representing the medication function
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void applyValues(String med_name, String med_function, String med_img) {
        NewMed = new MedicationCard(med_name, med_function);
        NewMed.setImageUrl(med_img);
        NewMed.isNew(true);
        medObjects.add(NewMed);

        if(MedicationCard.getNumberNewCard() <= 1) {
            RadioButton rb = new RadioButton(SettingsActivity.this);
            rb.setText(med_name);
            rb.setTextColor(ContextCompat.getColorStateList(SettingsActivity.this, R.color.colorWhite));
            rb.setTextSize(25);
            rb.setButtonTintList(ContextCompat.getColorStateList(this, R.color.colorWhite));
            rb.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    checkButton(v);
                }
            });
            radioGroup.addView(rb);
        }
    }

    /**
     * Called when the user taps the Save and Start button.
     * @param view the user interface component
     */
    public void setStartGameActivity(View view) {
        soundPool.play(clickSound, 0.75f, 0.75f, 0, 0, 1);

        medNameSelected = radioButton.getText().toString();

        MedSelected = new MedicationCard(medNameSelected, (String) medNameFunction.get(medNameSelected));

        /* Start game activity */
        Intent intent = new Intent(SettingsActivity.this, GameActivity.class);
        // Pass objects to game activity
        intent.putExtra("Medications", medObjects);
        // Pass selected medication to next the game activity
        intent.putExtra("MedicationSelected", MedSelected);
        startActivity(intent);
    }

}
