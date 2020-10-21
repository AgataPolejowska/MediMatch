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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class GameActivity extends AppCompatActivity {

    private TextView ScoreLabel = null;
    private int score = 0;

    private TextView TimerLabel;
    private long startTime = 0;

    private Timer timer = new Timer();
    private Handler timerHandler = new Handler(Looper.getMainLooper());

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

    private Button timerButton;
    private boolean pause_flag = false;

    ArrayList<CardView> cardsFun = new ArrayList<>();
    ArrayList<CardView> cardsFunImg = new ArrayList<>();
    ArrayList<FunctionCard> funCards = new ArrayList<>();
    ArrayList<FunctionCard> funImgCards = new ArrayList<>();

    int[] imageList = new int[]{R.drawable.heart, R.drawable.cholesterol, R.drawable.liver, R.drawable.brain} ;

    HashMap<MedicationCard, ArrayList<FunctionCard>> mednameFunctions = new HashMap<MedicationCard, ArrayList<FunctionCard>>();

    CardView cardFun1, cardFun2, cardFun3, cardFun4;
    TextView cardFunText1, cardFunText2, cardFunText3, cardFunText4;

    CardView cardFunImg1, cardFunImg2, cardFunImg3, cardFunImg4;
    ImageView cardImg1, cardImg2, cardImg3, cardImg4;

    CardView cardName;
    TextView medNameText;

    public float cardFun1_x, cardFun1_y;
    public float cardFun2_x, cardFun2_y;
    public float cardFun3_x, cardFun3_y;
    public float cardFun4_x, cardFun4_y;
    public float cardFunImg1_x, cardFunImg1_y;
    public float cardFunImg2_x, cardFunImg2_y;
    public float cardFunImg3_x, cardFunImg3_y;
    public float cardFunImg4_x, cardFunImg4_y;

    public float frame_height;



    /* ON CREATE */

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        ArrayList<MedicationCard> MedCards = (ArrayList<MedicationCard>)getIntent().getSerializableExtra("Medications");

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
        timerButton = findViewById(R.id.timerButton);

        // Start timer
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);

        // Menu
        Button openMenu = findViewById(R.id.popup_menu);

        openMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(GameActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.item_startagain:
                                startActivity(new Intent(GameActivity.this, MainActivity.class));
                                return true;
                            case R.id.item_info:
                                startActivity(new Intent(GameActivity.this, MainActivity.class));
                                return true;
                            case R.id.item_quit:
                                int pid = android.os.Process.myPid();
                                android.os.Process.killProcess(pid);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });



        // Get screen size
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        FunctionCard.screen_width = size.x;
        FunctionCard.screen_height = size.y;

        cardFun1 = findViewById(R.id.card_fun_1);
        cardFun2 = findViewById(R.id.card_fun_2);
        cardFun3 = findViewById(R.id.card_fun_3);
        cardFun4 = findViewById(R.id.card_fun_4);

        cardsFun.add(cardFun1);
        cardsFun.add(cardFun2);
        cardsFun.add(cardFun3);
        cardsFun.add(cardFun4);

        float [] cardFunX = new float[] {cardFun1_x, cardFun2_x, cardFun3_x, cardFun4_x};
        float [] cardFunY = new float[]{cardFun1_y, cardFun2_y, cardFun3_y, cardFun4_y};
        float [] cardFunImgX = new float[] {cardFunImg1_x, cardFunImg2_x, cardFunImg3_x, cardFunImg4_x};
        float [] cardFunImgY = new float[]{cardFunImg1_y, cardFunImg2_y, cardFunImg3_y, cardFunImg4_y};

        cardFunText1 = findViewById(R.id.card_fun_text_1);
        cardFunText2 = findViewById(R.id.card_fun_text_2);
        cardFunText3 = findViewById(R.id.card_fun_text_3);
        cardFunText4 = findViewById(R.id.card_fun_text_4);

        cardFunImg1 = findViewById(R.id.card_fun_img_1);
        cardFunImg2 = findViewById(R.id.card_fun_img_2);
        cardFunImg3 = findViewById(R.id.card_fun_img_3);
        cardFunImg4 = findViewById(R.id.card_fun_img_4);

        cardsFunImg.add(cardFunImg1);
        cardsFunImg.add(cardFunImg2);
        cardsFunImg.add(cardFunImg3);
        cardsFunImg.add(cardFunImg4);


        ArrayList<ImageView> cardImages = new ArrayList<>();
        cardImages.add(cardImg1 = findViewById(R.id.image_card_1));
        cardImages.add(cardImg2 = findViewById(R.id.image_card_2));
        cardImages.add(cardImg3 = findViewById(R.id.image_card_3));
        cardImages.add(cardImg4 = findViewById(R.id.image_card_4));


        for (int i = 0; i < cardsFun.size(); i++) {
            funCards.add(new FunctionCard(cardsFun.get(i), cardFunX[i], cardFunY[i]));
            funImgCards.add(new FunctionCard(cardsFunImg.get(i), cardFunImgX[i], cardFunImgY[i]));
        }

        // Set image
        for (int c = 0; c < imageList.length; c++) {
            funImgCards.get(c).setImage(cardImages.get(c), imageList[c]);
        }


        cardName = findViewById(R.id.card_name);

        // Set the name of checked medication
        medNameText = findViewById(R.id.med_name);
        medNameText.setText((MedCards.get(0)).getName());


       for(CardView card : cardsFun) {
            card.setOnTouchListener(mOnTouchListener);
        }

        for(CardView cardImg : cardsFunImg) {
            cardImg.setOnTouchListener(mOnTouchListener);
        }

        cardName.setOnDragListener(dragListener);


        FrameLayout frameLayout = findViewById(R.id.frame);
        frame_height = frameLayout.getHeight();


        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timerHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        for (int k = 0; k < funCards.size(); k++) {
                            (funCards.get(k)).setSpeed(Math.round(FunctionCard.screen_width / (95.0+(k*10))));
                            (funCards.get(k)).changePosition("LEFT");
                            (funImgCards.get(k)).setSpeed(Math.round(FunctionCard.screen_width / (120.0+(k*10))));
                            (funImgCards.get(k)).changePosition("RIGHT");
                        }

                    }
                });
            }
        }, 0, 20);



    }


    /* END OF ON CREATE */



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


    public void pauseGame(View view) {
        if (pause_flag == false) {

            pause_flag = true;

            // Stop the timer
            timer.cancel();
            timer = null;

            timerHandler.removeCallbacks(timerRunnable);

            // Change button text
            timerButton.setText("START");
        }
        else {

            pause_flag = false;
            timerButton.setText("PAUSE");

            timerHandler.postDelayed(timerRunnable, 0);

            // Create new timer
            timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    timerHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            for (int k = 0; k < funCards.size(); k++) {
                                (funCards.get(k)).setSpeed(Math.round(FunctionCard.screen_width / (95.0+(k*10))));
                                (funCards.get(k)).changePosition("LEFT");
                                (funImgCards.get(k)).setSpeed(Math.round(FunctionCard.screen_width / (120.0+(k*10))));
                                (funImgCards.get(k)).changePosition("RIGHT");
                            }

                        }
                    });
                }
            }, 0, 20);

        }
    }


    // TODO: CHECK IF ALL CARDS INVISIBLE, CHECK SCORE
        public void checkVisibility() {
        if (cardFunImg1.getVisibility()  != View.VISIBLE) {
            Intent intent = new Intent(GameActivity.this, EndActivity.class);
            startActivity(intent);
            }
        }

}




