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
    String [] strAnswers;

    private SoundPool soundPool;
    private int click_sound;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        Intent receivedIntent = getIntent();
        strAnswers = receivedIntent.getStringArrayExtra("answers");


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

    }

    /* Called when the user taps the Start button */
    public void tryAgain(View view) {
        Intent intent = new Intent(EndActivity.this, MainActivity.class);
        startActivity(intent);
    }


    /* Called when the user taps the Save button */
    public void save(View v) {
        soundPool.play(click_sound, 0.75f, 0.75f, 0, 0, 1);

        Intent newIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        newIntent.addCategory(Intent.CATEGORY_OPENABLE);
        newIntent.setType("text/plain");
        newIntent.putExtra(Intent.EXTRA_TITLE, FILE_NAME);

        startActivityForResult(newIntent, 1);
    }


    /* Called when the user taps the Quit button */
    public void quitGame() {
        finishAffinity();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri uri = data.getData();
                    OutputStream outputStream =getContentResolver().openOutputStream(uri);
                    for(String s: strAnswers) {
                        outputStream.write(s.getBytes());
                    }
                    outputStream.close();
                    Toast.makeText(this, "Answers saved successfully", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Answers have not been saved", Toast.LENGTH_LONG).show();
            }
        }

    }
}