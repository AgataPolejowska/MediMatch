package com.i.medimatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class EndActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
    }

    /* Called when the user taps the Start button */
    public void tryAgain(View view) {
        Intent intent3 = new Intent(EndActivity.this, MainActivity.class);
        startActivity(intent3);
    }

}