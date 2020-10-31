package com.i.medimatch;


import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SettingsActivity extends AppCompatActivity implements AddNewDialog.AddNewMedDialogListener {

    ArrayList<MedicationCard> medObjects = new ArrayList<>();
    HashMap<String, String> medNameFunction = new HashMap<>();
    ArrayList<Integer> imageViews = new ArrayList<>();

    RadioGroup radioGroup;
    RadioButton radioButton;
    RadioButton [] medRadioButton;

    String medNameSelected;
    MedicationCard medSelected;
    MedicationCard NewMed;

    private SoundPool soundPool;
    private int click_sound, start_sound;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        medNameFunction.put("AXTIL", "Facilitating blood pumping");
        medNameFunction.put("RIDLIP", "Reduction in the amount of LDL cholesterol in the blood");
        medNameFunction.put("SYLIMAROL", "Regeneration and reconstruction of damaged liver");
        medNameFunction.put("ENCEPHABOL", "Treatment of brain disorders");


        radioGroup = findViewById(R.id.radioGroup);

        RadioButton med1 = findViewById(R.id.med1);
        RadioButton med2 = findViewById(R.id.med2);
        RadioButton med3 = findViewById(R.id.med3);
        RadioButton med4 = findViewById(R.id.med4);

        for(Map.Entry<String, String> entry : medNameFunction.entrySet()) {
            medObjects.add((new MedicationCard(entry.getKey(), entry.getValue())));
        }

        medRadioButton = new RadioButton[]{med1, med2, med3, med4};

        for (int i = 0; i < medObjects.size(); i++) {
            medRadioButton[i].setText((medObjects.get(i).getName()));
        }


        // Implementing  sounds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }

        click_sound = soundPool.load(this, R.raw.click, 1);
        start_sound = soundPool.load(this, R.raw.start, 1);

    }
    /* END OF ON CREATE */


    public void checkButton(View v) {
        soundPool.play(start_sound, 0.75f, 0.75f, 0, 0, 1);
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }

    /* Called when the user taps the Add New button */
    public void addNew(View view) {
        openDialog();

        MedicationCard medCard;
       // medCard = new MedicationCard(-1, )
        DataBaseHelper dataBaseHelper = new DataBaseHelper(SettingsActivity.this);
    }

    public void openDialog() {
        AddNewDialog addNewDialog = new AddNewDialog();
        addNewDialog.show(getSupportFragmentManager(), "Add new dialog");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void applyValues(String med_name, String med_function) {
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

        NewMed = new MedicationCard(med_name, med_function);
        medObjects.add(NewMed);


        radioGroup.addView(rb);
    }


    /* Called when the user taps the Save and Start button */
    public void setStartGameActivity(View view) {

        soundPool.play(click_sound, 0.75f, 0.75f, 0, 0, 1);

        medNameSelected = radioButton.getText().toString();

        medSelected = new MedicationCard(medNameSelected, (String) medNameFunction.get(medNameSelected));
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
