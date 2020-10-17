package com.i.medimatch;


import android.annotation.SuppressLint;
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

    private Timer timer = new Timer();
    private Handler handler = new Handler(Looper.getMainLooper());

    Handler timerHandler = new Handler(Looper.getMainLooper());

    Runnable timerRunnable = new Runnable() {
        @SuppressLint("DefaultLocale")
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


    CardView cardFun1, cardFun2, cardFun3, cardFun4;
    TextView cardFunText1, cardFunText2, cardFunText3, cardFunText4;

    CardView cardFunImg1, cardFunImg2;
    ImageView cardImg1, cardImg2;

    CardView cardName;
    TextView medNameText;

    public float cardFun1_x, cardFun1_y;
    public float cardFun2_x, cardFun2_y;
    public float cardFun3_x, cardFun3_y;
    public float cardFun4_x, cardFun4_y;
    public float cardFunImg1_x, cardFunImg1_y;
    public float cardFunImg2_x, cardFunImg2_y;


    // Size
    public float frame_height;
    public float screen_width, screen_height;



    /* ON CREATE */

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        ArrayList<MedicationCard> MedCards = (ArrayList<MedicationCard>) getIntent().getSerializableExtra("Medications");

        // Testing
        final StringBuilder builder = new StringBuilder();
        builder.append("You have chosen: ");
        for(MedicationCard i : MedCards)
        {
            builder.append(i.getName() + " ");
        }
        Toast.makeText(this, builder, Toast.LENGTH_LONG).show();



        ScoreLabel = findViewById(R.id.scoreLabel);

        TimerLabel = findViewById(R.id.timerLabel);
        Button timerbutton = findViewById(R.id.timerButton);

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


        // Get screen size
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        screen_width = size.x;
        screen_height = size.y;


        cardFun1 = findViewById(R.id.card_fun_1);
        cardFun2 = findViewById(R.id.card_fun_2);
        cardFun3 = findViewById(R.id.card_fun_3);
        cardFun4 = findViewById(R.id.card_fun_4);

        cardFunText1 = findViewById(R.id.card_fun_text_1);
        cardFunText2 = findViewById(R.id.card_fun_text_2);
        cardFunText3 = findViewById(R.id.card_fun_text_3);
        cardFunText4 = findViewById(R.id.card_fun_text_4);

        cardFunImg1 = findViewById(R.id.card_fun_img_1);
        cardFunImg2 = findViewById(R.id.card_fun_img_2);

        cardImg1 = findViewById(R.id.image_card_1);
        cardImg2 = findViewById(R.id.image_card_2);


        cardName = findViewById(R.id.card_name);


        // Set the name of checked medication
        medNameText = findViewById(R.id.med_name);
        medNameText.setText((MedCards.get(0)).getName());


        cardFun1.setOnTouchListener(mOnTouchListener);
        cardFun2.setOnTouchListener(mOnTouchListener);
        cardFun3.setOnTouchListener(mOnTouchListener);
        cardFun4.setOnTouchListener(mOnTouchListener);
        cardFunImg1.setOnTouchListener(mOnTouchListener);
        cardFunImg2.setOnTouchListener(mOnTouchListener);

        cardName.setOnDragListener(dragListener);

        // Set image in a card
        cardImg1.setImageResource(R.drawable.heart);
        cardImg2.setImageResource(R.drawable.heart);


        FrameLayout frameLayout = findViewById(R.id.frame);
        frame_height = frameLayout.getHeight();


        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        changePosition(cardFun1, Math.round(screen_width/100.0f));
                        changePosition(cardFun2, Math.round(screen_width/120.0f));
                        changePosition(cardFunImg1, Math.round(screen_width/90.0f));


                    }
                });
            }
        }, 0, 20);



    }



    /* END OF ON CREATE */



    public void changePosition(CardView view, float speed) {

        switch((view.getId())) {

            case R.id.card_fun_1:

                cardFun1_x -= speed;
                if (cardFun1_x < -700) {
                    cardFun1_x = screen_width;
                    cardFun1_y = (float)Math.floor(Math.random() * (screen_height - view.getHeight()));
                }
                view.setX(cardFun1_x);
                view.setY(cardFun1_y);
                break;

            case R.id.card_fun_2:

                cardFun2_x -= speed;
                if (cardFun2_x < -700) {
                    cardFun2_x = screen_width;
                    cardFun2_y = (float)Math.floor(Math.random() * (screen_height - view.getHeight()));
                }
                view.setX(cardFun2_x);
                view.setY(cardFun2_y);
                break;

            case R.id.card_fun_img_1:

                cardFunImg1_x -= speed;
                if (cardFunImg1_x < -700) {
                    cardFunImg1_x = screen_width;
                    cardFunImg1_y = (float)Math.floor(Math.random() * (screen_height - view.getHeight()));
                }
                view.setX(cardFunImg1_x);
                view.setY(cardFunImg1_y);
                break;

        }

    }





    View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
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
                    cardName.setCardBackgroundColor(Color.LTGRAY);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    cardName.setCardBackgroundColor(Color.WHITE);
                    if (view.getId() == R.id.card_fun_1 || view.getId() == R.id.card_fun_img_1) {
                        cardFun1.setVisibility(View.VISIBLE); // Make the card appear
                    }
                    break;
                case DragEvent.ACTION_DROP:
                    if (view.getId() == R.id.card_fun_1 || view.getId() == R.id.card_fun_img_1) {
                        cardName.setCardBackgroundColor(Color.WHITE);
                        if (view.getId() == R.id.card_fun_1) {
                            cardFun1.setVisibility(View.GONE); // Make the card disappear
                            ScoreLabel.setText("Score: " + --score);
                            checkVisibility();
                        }
                        else if (view.getId() == R.id.card_fun_img_1) {
                            cardFunImg1.setVisibility(View.GONE);
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
        if (cardFunImg1.getVisibility()  != View.VISIBLE) {
            Intent intent = new Intent(GameActivity.this, EndActivity.class);
            startActivity(intent);
            }
        }

}




