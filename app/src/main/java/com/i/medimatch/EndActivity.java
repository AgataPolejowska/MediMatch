package com.i.medimatch;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;


public class EndActivity extends AppCompatActivity {

    private static final String FILE_NAME = "answers.txt";
    private static final String FILE_NAME_RESULTS = "results.txt";
    private String [] strAnswers;
    private String [] strResults;

    int[] buttonMemory = new int[] {0, 0};

    private SoundPool soundPool;
    private int clickSound;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        Intent receivedIntent = getIntent();
        strAnswers = receivedIntent.getStringArrayExtra("answers");
        strResults = receivedIntent.getStringArrayExtra("results");

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(audioAttributes)
                .build();
        clickSound = soundPool.load(this, R.raw.click, 1);
    }

    /* Called when the user taps the Start button */
    public void tryAgain(View view) {
        MedicationCard.setNumberNewCard(0);
        Intent intent = new Intent(EndActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /* Called when the user taps the Save Answers button */
    public void save(View v) {
        soundPool.play(clickSound, 0.75f, 0.75f, 0, 0, 1);

        buttonMemory[0] = 1;
        buttonMemory[1] = 0;

        Intent newIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        newIntent.addCategory(Intent.CATEGORY_OPENABLE);
        newIntent.setType("text/plain");
        newIntent.putExtra(Intent.EXTRA_TITLE, FILE_NAME);

        startActivityForResult(newIntent, 1);
    }

    /* Called when the user taps the Save button */
    public void saveResults(View v) {
        soundPool.play(clickSound, 0.75f, 0.75f, 0, 0, 1);

        buttonMemory[0] = 0;
        buttonMemory[1] = 1;

        Intent newIntent2 = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        newIntent2.addCategory(Intent.CATEGORY_OPENABLE);
        newIntent2.setType("text/plain");
        newIntent2.putExtra(Intent.EXTRA_TITLE, FILE_NAME_RESULTS);

        startActivityForResult(newIntent2, 1);
    }

    /* Called when the user taps the Quit button */
    public void quitGame(View v) {
        finishAffinity();
        System.exit(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    assert data != null;
                    Uri uri = data.getData();
                    assert uri != null;
                    OutputStream outputStream = getContentResolver().openOutputStream(uri);
                    if(buttonMemory[0] == 1 && buttonMemory[1] == 0) {
                        for(String s: strAnswers) {
                            assert outputStream != null;
                            outputStream.write(s.getBytes());
                        }
                    }
                    else if(buttonMemory[0] == 0 && buttonMemory[1] == 1) {
                        for(String s: strResults) {
                            assert outputStream != null;
                            outputStream.write(s.getBytes());
                        }
                    }
                    assert outputStream != null;
                    outputStream.close();
                    Toast.makeText(this, "Saved successfully", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "The file has not been saved", Toast.LENGTH_LONG).show();
            }
        }
    }
}