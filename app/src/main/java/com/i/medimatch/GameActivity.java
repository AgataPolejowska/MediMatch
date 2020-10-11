package com.i.medimatch;

import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


public class GameActivity extends AppCompatActivity {

    private TextView ScoreLabel = null;
    private int score = 0;
    boolean answer = false;

    TextView TimerLabel;
    long startTime = 0;

    Handler timerHandler = new Handler(Looper.getMainLooper());
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis/1000);
            int minutes = seconds/60;
            seconds = seconds % 60;

            TimerLabel.setText(String.format("%d:%02d", minutes, seconds));
            timerHandler.postDelayed(this, 500);
        }
    };



    CardView cardFun, cardName;


    public GameActivity() {
        // Empty constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ScoreLabel = (TextView) findViewById(R.id.scoreLabel);

        TimerLabel = (TextView) findViewById(R.id.timerLabel);
        Button timerbutton = (Button) findViewById(R.id.timerButton);

        timerbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button timerbutton = (Button) v;
                if (timerbutton.getText().equals("stop")) {
                    timerHandler.removeCallbacks(timerRunnable);
                    timerbutton.setText("start");
                } else {
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                    timerbutton.setText("stop");
                }
            }

        });
        ;

        cardFun = (CardView) findViewById(R.id.card_fun);
        cardName = (CardView) findViewById(R.id.card_name);

        cardFun.setOnLongClickListener(longClickListener);
        cardName.setOnDragListener(dragListener);

    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        Button timerbutton = (Button)findViewById(R.id.timerButton);
        timerbutton.setText("start");
    }


    /* Drag and drop */
    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, myShadowBuilder, v, 0);
            return false;
        }
    };


    View.OnDragListener dragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {

            int dragEvent = event.getAction();
            final View view = (View) event.getLocalState();

            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    if (view.getId() == R.id.card_fun) {
                        cardName.setCardBackgroundColor(Color.LTGRAY);
                    }
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    if (view.getId() == R.id.card_fun) {
                        cardFun.setVisibility(View.VISIBLE); // Make the card appear
                    }
                    break;
                case DragEvent.ACTION_DROP:
                    if (view.getId() == R.id.card_fun) {
                        cardName.setCardBackgroundColor(Color.WHITE);
                        cardFun.setVisibility(View.GONE); // Make the card disappear

                        /* If correct (CHECK) match, update the score */
                        if (answer) {
                            ScoreLabel.setText("Score: " + ++score);
                        }
                        else {
                            ScoreLabel.setText("Score: " + --score);
                        }

                    }
                    break;
            }

            return true;
        }
    };


}
