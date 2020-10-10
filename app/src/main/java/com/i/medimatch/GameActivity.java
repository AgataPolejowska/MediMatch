package com.i.medimatch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class GameActivity extends AppCompatActivity {

    public GameActivity() {
        // Empty constructor
    }

    /* private static final String ARG_PARAM1 = "param1";
     private static final String ARG_PARAM2 = "param2";

     private String mParam1;
     private String mParam2;

     public static GameActivity newInstance(String param1, String param2) {
         GameActivity fragment = new GameActivity();
         Bundle args = new Bundle();
         args.putString(ARG_PARAM1, param1);
         args.putString(ARG_PARAM2, param2);
         fragment.setArguments(args);
         return fragment;
     }
 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  /*      if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        } */
        setContentView(R.layout.fragment_game_activity);
    }
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_activity, container, false);
    }
*/


}
