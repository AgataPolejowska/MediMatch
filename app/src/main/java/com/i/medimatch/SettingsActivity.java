package com.i.medimatch;


import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;


public class SettingsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);  // Set layout
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.med1:
                if (checked) {
                    // Save
                }
                else {
                    //
                }
                break;
            case R.id.med2:
                if (checked) {
                    // Save
                }
                else {
                    //
                }
                break;
        }

    }


}
