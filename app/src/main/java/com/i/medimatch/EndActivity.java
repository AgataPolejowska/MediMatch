package com.i.medimatch;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class EndActivity extends AppCompatActivity {

    private static final String FILE_NAME = "answers.txt";
    String [] strAnswers;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        Intent receivedIntent = getIntent();
        strAnswers = receivedIntent.getStringArrayExtra("answers");
    }

    /* Called when the user taps the Start button */
    public void tryAgain(View view) {
        Intent intent = new Intent(EndActivity.this, MainActivity.class);
        startActivity(intent);
    }


    /* Called when the user taps the Save button */
    public void save(View v) {
        Intent newIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        newIntent.addCategory(Intent.CATEGORY_OPENABLE);
        newIntent.setType("text/plain");
        newIntent.putExtra(Intent.EXTRA_TITLE, FILE_NAME);

        startActivityForResult(newIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri uri = data.getData();
                    OutputStream outputStream =getContentResolver().openOutputStream(uri);
                    outputStream.write(strAnswers[0].getBytes());
                    outputStream.write(strAnswers[1].getBytes());
                    outputStream.close();
                    Toast.makeText(this, "Answers saved successfully", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "File has not been saved", Toast.LENGTH_LONG).show();
            }
        }

    }
}