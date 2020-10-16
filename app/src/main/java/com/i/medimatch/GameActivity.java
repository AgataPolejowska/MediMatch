package com.i.medimatch;


import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class GameActivity extends AppCompatActivity {

    private TextView ScoreLabel = null;
    private int score = 0;

    TextView TimerLabel;
    long startTime = 0;

    // Timer
    private Timer timer = new Timer();
    private Handler handler = new Handler(Looper.getMainLooper());

    Handler timerHandler = new Handler(Looper.getMainLooper());

    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis/1000);
            int minutes = seconds/60;
            seconds = seconds % 60;

            TimerLabel.setText(String.format("Timer: %d:%02d", minutes, seconds));
            timerHandler.postDelayed(this, 500);
        }
    };

    // Array for function cards
    ArrayList<FunctionCard> cardsFun = new ArrayList<FunctionCard>();

    CardView cardFun, cardFunImg, cardName;
    ImageView cardImg;

    TextView medNameText;

    private float card_fun_x, card_fun_y, card_fun_img_x, card_fun_img_y;
    private float card_fun_speed, card_fun_img_speed;

    // Size
    private int screen_width;
    private int screen_height;
    private int frame_height;


    /* ON CREATE */

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        ArrayList<MedicationCard> MedCards = (ArrayList<MedicationCard>) getIntent().getSerializableExtra("Medications");

        // Testing
        StringBuilder builder = new StringBuilder();
        builder.append("You have chosen: ");
        for(MedicationCard i : MedCards)
        {
            builder.append(i.getName() + " ");
        }
        Toast.makeText(this, builder, Toast.LENGTH_LONG).show();



        ScoreLabel = (TextView) findViewById(R.id.scoreLabel);

        TimerLabel = (TextView) findViewById(R.id.timerLabel);
        Button timerbutton = (Button) findViewById(R.id.timerButton);

        // Start timer
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);

        timerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button timerbutton = (Button) v;
                if (timerbutton.getText().equals("stop")) {
                    timerHandler.removeCallbacks(timerRunnable);
                    timerbutton.setText("start");
                } else {
                    timerHandler.postDelayed(timerRunnable, 0);
                    timerbutton.setText("stop");
                }
            }

        });

        

        cardFun = (CardView) findViewById(R.id.card_fun);
        cardFunImg = (CardView) findViewById(R.id.card_fun_img);
        cardImg = (ImageView) findViewById(R.id.image_card);
        cardName = (CardView) findViewById(R.id.card_name);

        // Set the name of checked medication
        medNameText = (TextView) findViewById(R.id.icon_name);
        medNameText.setText((MedCards.get(0)).getName());


      //  cardFun.setOnLongClickListene(longClickListener);
      //  cardFunImg.setOnLongClickListener(longClickListener);


        cardFun.setOnTouchListener(mOnTouchListener);
        cardFunImg.setOnTouchListener(mOnTouchListener);


        cardName.setOnDragListener(dragListener);

        // Set image in a card
        cardImg.setImageResource(R.drawable.heart);

        // Get screen size
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screen_width = size.x;
        screen_height = size.y;

        card_fun_speed = Math.round(screen_width/95.0f);
        card_fun_img_speed = Math.round(screen_width/100.0f);
        cardFun.setX(-80.0f);
        cardFunImg.setX(-80.0f);

        FrameLayout frameLayout = findViewById(R.id.frame);
        frame_height = frameLayout.getHeight();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        changePosition();
                    }
                });
            }
        }, 0, 20);

    }

    /* END OF ON CREATE */



    // TODO: IN OBJECT-ORIENTED WAY
    public void changePosition() {

        card_fun_x -= card_fun_speed;
        if (card_fun_x < 0) {
            card_fun_x = screen_width;
            card_fun_y = (float)Math.floor(Math.random() * (screen_height - cardFun.getHeight()));
        }
        cardFun.setX(card_fun_x);
        cardFun.setY(card_fun_y);

        card_fun_img_x -= card_fun_img_speed;
        if (card_fun_img_x < 0) {
            card_fun_img_x = screen_width;
            card_fun_img_y = (float)Math.floor(Math.random() * (screen_height - cardImg.getHeight()));
        }
        cardFunImg.setX(card_fun_img_x);
        cardFunImg.setY(card_fun_img_y);

    }



    /*//
    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, myShadowBuilder, v, 0);
            return false;
        }
    };
    */


    View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
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
                    if (view.getId() == R.id.card_fun || view.getId() == R.id.card_fun_img) {
                        cardName.setCardBackgroundColor(Color.LTGRAY);
                    }
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    if (view.getId() == R.id.card_fun || view.getId() == R.id.card_fun_img) {
                        cardFun.setVisibility(View.VISIBLE); // Make the card appear
                        cardName.setCardBackgroundColor(Color.WHITE);
                    }
                    break;
                case DragEvent.ACTION_DROP:
                    if (view.getId() == R.id.card_fun || view.getId() == R.id.card_fun_img) {
                        cardName.setCardBackgroundColor(Color.WHITE);
                        if (view.getId() == R.id.card_fun) {
                            cardFun.setVisibility(View.GONE); // Make the card disappear
                            ScoreLabel.setText("Score: " + --score);
                            checkVisibility();
                        }
                        else if (view.getId() == R.id.card_fun_img) {
                            cardFunImg.setVisibility(View.GONE);
                            ScoreLabel.setText("Score: " + ++score);
                            checkVisibility();
                        }

                    }

                    break;
            }

            return true;

        }
    };


    // TODO: CHECK IF ALL CARDS INVISIBLE, CHECK SCORE
        public void checkVisibility() {
        if (cardFunImg.getVisibility()  != View.VISIBLE) {
            Intent intent = new Intent(GameActivity.this, EndActivity.class);
            startActivity(intent);
            }
        }

}




