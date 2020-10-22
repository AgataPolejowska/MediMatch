package com.i.medimatch;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class SettingsActivity extends AppCompatActivity {

    ArrayList<MedicationCard> medObjects = new ArrayList<>();
    ArrayList<MedicationCard> medCheckedNames = new ArrayList<>();

    RadioGroup radioGroup;
    RadioButton radioButton;
    RadioButton [] medRadioButton;

    MedicationCard medSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        radioGroup = findViewById(R.id.radioGroup);

        RadioButton med1 = findViewById(R.id.med1);
        RadioButton med2 = findViewById(R.id.med2);
        RadioButton med3 = findViewById(R.id.med3);
        RadioButton med4 = findViewById(R.id.med4);

        medObjects.add(new MedicationCard("AXTIL", false));
        medObjects.add(new MedicationCard("RIDLIP", false));
        medObjects.add(new MedicationCard("SYLIMAROL", false));
        medObjects.add(new MedicationCard("ENCEPHABOL", false));

        medRadioButton = new RadioButton[]{med1, med2, med3, med4};

        for (int i = 0; i < medObjects.size(); i++) {
            medRadioButton[i].setText((medObjects.get(i).getName()));
        }
    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }


    /* Called when the user taps the Save and Start button */
    public void setStartGameActivity(View view) {

        medSelected = new MedicationCard(radioButton.getText().toString(), true);
        medSelected.setNameSelected(true);

        // Testing
        StringBuilder builder = new StringBuilder();
        builder.append("You have chosen: " + medSelected.getName());
        Toast.makeText(this, builder, Toast.LENGTH_SHORT).show();

        /* Start game activity */
        Intent intent = new Intent(SettingsActivity.this, GameActivity.class);
        // Pass objects to game activity
        intent.putExtra("Medications", medObjects);
        // Pass selected medication to next the game activity
        intent.putExtra("MedicationSelected", medSelected);
        startActivity(intent);

    }


}
