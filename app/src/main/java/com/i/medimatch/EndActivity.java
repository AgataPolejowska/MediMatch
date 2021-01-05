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

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Represents the game end activity.
 * @author Agata Polejowska
 */
public class EndActivity extends AppCompatActivity {

    /** The name of a file where answers are stored. */
    private static final String FILE_NAME = "answers.txt";
    /** The name of a file where results are stored. */
    private static final String FILE_NAME_RESULTS = "results.txt";
    /** Stores answers. */
    private String [] strAnswers;
    /** Stores results. */
    private String [] strResults;

    /** Saves button states */
    private int[] buttonMemory = new int[] {0, 0};

    /** An object for managing and playing audio resources. */
    private SoundPool soundPool;
    /** Stores the loaded sound from the APK resource. */
    private int clickSound;

    /**
     * Initializing the end activity.
     * @param savedInstanceState a reference to a Bundle object
     */
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

    /**
     * Called when the user taps the Try again button.
     * @param view the user interface component
     */
    public void tryAgain(View view) {
        soundPool.play(clickSound, 0.75f, 0.75f, 0, 0, 1);

        MedicationCard.setNumberNewCard(0);
        Intent intent = new Intent(EndActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the user taps the Save Answers button
     * @param v the user interface component
     */
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

    /**
     * Called when the user taps the Save Results button
     * @param v the user interface component
     */
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

    /**
     * Called when the user taps the Quit button
     * @param v the user interface component
     */
    public void quitGame(View v) {
        soundPool.play(clickSound, 0.75f, 0.75f, 0, 0, 1);
        
        finishAffinity();
        System.exit(0);
    }

    /**
     * Used to get a result back from the activity when it ends.
     * @param requestCode the requestCode the activity started with
     * @param resultCode the resultCode returned from the activity
     * @param data the additional data from the activity
     */
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