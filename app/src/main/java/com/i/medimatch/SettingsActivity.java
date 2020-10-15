package com.i.medimatch;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class SettingsActivity extends AppCompatActivity {

    ArrayList<MedicationCard> medNames = new ArrayList<MedicationCard>();
    ArrayList<MedicationCard> medCheckedNames = new ArrayList<MedicationCard>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        CheckBox med1 = (CheckBox) findViewById(R.id.med1);
        CheckBox med2 = (CheckBox) findViewById(R.id.med2);
        CheckBox med3 = (CheckBox) findViewById(R.id.med3);
        CheckBox med4 = (CheckBox) findViewById(R.id.med4);
        CheckBox med5 = (CheckBox) findViewById(R.id.med5);

        medNames.add(new MedicationCard("AXTIL", true));
        medNames.add(new MedicationCard("MED2", false));
        medNames.add(new MedicationCard("MED3", false));
        medNames.add(new MedicationCard("MED4", false));
        medNames.add(new MedicationCard("MED5", false));

        med1.setText((medNames.get(0)).getName());
        med2.setText((medNames.get(1)).getName());
        med3.setText((medNames.get(2)).getName());
        med4.setText((medNames.get(3)).getName());
        med5.setText((medNames.get(4)).getName());

        med1.setChecked((medNames.get(0)).isChecked());
        med2.setChecked(medNames.get(1).isChecked());
        med3.setChecked(medNames.get(2).isChecked());
        med4.setChecked(medNames.get(3).isChecked());
        med5.setChecked(medNames.get(4).isChecked());
    }


    /* Called when the user taps the Save and Start button */
    public void setStartGameActivity(View view) {

        CheckBox med1 = (CheckBox) findViewById(R.id.med1);
        CheckBox med2 = (CheckBox) findViewById(R.id.med2);
        CheckBox med3 = (CheckBox) findViewById(R.id.med3);
        CheckBox med4 = (CheckBox) findViewById(R.id.med4);
        CheckBox med5 = (CheckBox) findViewById(R.id.med5);

        if (med1.isChecked()) { medCheckedNames.add(new MedicationCard ("AXTIL", true)); }
        if (med2.isChecked()) { medCheckedNames.add(new MedicationCard ("MED2", true)); }
        if (med3.isChecked()) { medCheckedNames.add(new MedicationCard ("MED3", true)); }
        if (med4.isChecked()) { medCheckedNames.add(new MedicationCard ("MED4", true)); }
        if (med5.isChecked()) { medCheckedNames.add(new MedicationCard ("MED5", true)); }

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
        intent.putExtra("Medications", medCheckedNames);
        startActivity(intent);

    }


}
