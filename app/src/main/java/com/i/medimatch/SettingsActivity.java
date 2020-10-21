package com.i.medimatch;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class SettingsActivity extends AppCompatActivity {

    ArrayList<MedicationCard> medObjects = new ArrayList<>();
    ArrayList<MedicationCard> medCheckedNames = new ArrayList<>();

    CheckBox [] medCheckbox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        CheckBox med1 = findViewById(R.id.med1);
        CheckBox med2 = findViewById(R.id.med2);
        CheckBox med3 = findViewById(R.id.med3);
        CheckBox med4 = findViewById(R.id.med4);

        medObjects.add(new MedicationCard("AXTIL", true));
        medObjects.add(new MedicationCard("RIDLIP", false));
        medObjects.add(new MedicationCard("SYLIMAROL", false));
        medObjects.add(new MedicationCard("ENCEPHABOL", false));

        medCheckbox = new CheckBox[]{med1, med2, med3, med4};

        for (int i = 0; i < medObjects.size(); i++) {
            medCheckbox[i].setText((medObjects.get(i).getName()));
            medCheckbox[i].setChecked(medObjects.get(i).isChecked());
        }
    }

    /* Called when the user taps the Save and Start button */
    public void setStartGameActivity(View view) {

        // If a medication name is checked, create the object and add to the arraylist
        for (int j = 0; j < medObjects.size(); j++) {
            if (medCheckbox[j].isChecked()) {
                medCheckedNames.add(new MedicationCard((medObjects.get(j)).getName(), true));
            }
        }

        // Testing
        StringBuilder builder = new StringBuilder();
        builder.append("You have chosen: ");
        for(MedicationCard i : medCheckedNames)
        {
            builder.append(i.getName() + " ");
        }
        Toast.makeText(this, builder, Toast.LENGTH_LONG).show();

        /* Start game activity */
        Intent intent = new Intent(SettingsActivity.this, GameActivity.class);
        // Pass objects to game activity
        intent.putExtra("Medications", medObjects);
        // Pass checked medications to next the game activity
        intent.putExtra("MedicationsChecked", medCheckedNames);
      //  intent.putExtra();
        startActivity(intent);

    }


}
