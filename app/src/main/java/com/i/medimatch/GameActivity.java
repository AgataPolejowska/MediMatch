package com.i.medimatch;


import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class GameActivity extends AppCompatActivity {

    private enum State {
        PAUSED, WON, LOST, RUNNING;
    }

    private State state = State.RUNNING;

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

    ArrayList<MedicationCard> MedCardsObjects = new ArrayList<>();
    ArrayList<String> playedName = new ArrayList<>();

    MedicationCard MedCardSelected;

    int[] imageList = new int[] {R.drawable.cholesterol, R.drawable.liver, R.drawable.brain, R.drawable.heart};

    String [] answers;

    CardView cardFun1, cardFun2, cardFun3, cardFun4, cardFun5;
    TextView cardFunText1, cardFunText2, cardFunText3, cardFunText4, cardFunText5;

    CardView cardFunImg1, cardFunImg2, cardFunImg3, cardFunImg4, cardFunImg5;
    ImageView cardImg1, cardImg2, cardImg3, cardImg4, cardImg5;

    CardView cardName;
    TextView medNameText;

    public float cardFun1_x, cardFun1_y;
    public float cardFun2_x, cardFun2_y;
    public float cardFun3_x, cardFun3_y;
    public float cardFun4_x, cardFun4_y;
    public float cardFun5_x, cardFun5_y;

    public float cardFunImg1_x, cardFunImg1_y;
    public float cardFunImg2_x, cardFunImg2_y;
    public float cardFunImg3_x, cardFunImg3_y;
    public float cardFunImg4_x, cardFunImg4_y;
    public float cardFunImg5_x, cardFunImg5_y;

    public float frame_height;

    private SoundPool soundPool;
    private int click_sound, tap_sound, correct_sound, incorrect_sound;

    Animation animRotate;


    /* ON CREATE */

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Receive data from Settings Activity
        MedCardsObjects = (ArrayList<MedicationCard>)getIntent().getSerializableExtra("Medications");
        MedCardSelected = (MedicationCard) getIntent().getSerializableExtra("MedicationSelected");
        playedName.add(MedCardSelected.getName());

        // Testing
        final StringBuilder builder = new StringBuilder();
        builder.append("You have chosen: ");
        builder.append(MedCardSelected.getName());
        Toast.makeText(this, builder, Toast.LENGTH_SHORT).show();


        ScoreLabel = findViewById(R.id.scoreLabel);
        ScoreLabel.setText("Score: " + score);

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
                soundPool.play(click_sound, 1, 1, 0, 0, 1);
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                                builder.setMessage("Are you sure you want to exit?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                finishAffinity();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
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

        cardsFun.add(cardFun1 = findViewById(R.id.card_fun_1));
        cardsFun.add(cardFun2 = findViewById(R.id.card_fun_2));
        cardsFun.add(cardFun3 = findViewById(R.id.card_fun_3));
        cardsFun.add(cardFun4 = findViewById(R.id.card_fun_4));
        cardsFun.add(cardFun5 = findViewById(R.id.card_fun_5));

        cardsFunImg.add(cardFunImg1 = findViewById(R.id.card_fun_img_1));
        cardsFunImg.add(cardFunImg2 = findViewById(R.id.card_fun_img_2));
        cardsFunImg.add(cardFunImg3 = findViewById(R.id.card_fun_img_3));
        cardsFunImg.add(cardFunImg4 = findViewById(R.id.card_fun_img_4));
        cardsFunImg.add(cardFunImg5 = findViewById(R.id.card_fun_img_5));

        // Cards coordinates
        float [] cardFunX = new float[] {cardFun1_x, cardFun2_x, cardFun3_x, cardFun4_x, cardFun5_x};
        float [] cardFunY = new float[]{cardFun1_y, cardFun2_y, cardFun3_y, cardFun4_y, cardFun5_y};
        float [] cardFunImgX = new float[] {cardFunImg1_x, cardFunImg2_x, cardFunImg3_x, cardFunImg4_x, cardFun5_x};
        float [] cardFunImgY = new float[]{cardFunImg1_y, cardFunImg2_y, cardFunImg3_y, cardFunImg4_y, cardFun5_y};

        ArrayList<TextView> cardsFunText = new ArrayList<>();
        cardsFunText.add(cardFunText1 = findViewById(R.id.card_fun_text_1));
        cardsFunText.add(cardFunText2 = findViewById(R.id.card_fun_text_2));
        cardsFunText.add(cardFunText3 = findViewById(R.id.card_fun_text_3));
        cardsFunText.add(cardFunText4 = findViewById(R.id.card_fun_text_4));
        cardsFunText.add(cardFunText5 = findViewById(R.id.card_fun_text_5));

        ArrayList<ImageView> cardImages = new ArrayList<>();
        cardImages.add(cardImg1 = findViewById(R.id.image_card_1));
        cardImages.add(cardImg2 = findViewById(R.id.image_card_2));
        cardImages.add(cardImg3 = findViewById(R.id.image_card_3));
        cardImages.add(cardImg4 = findViewById(R.id.image_card_4));
        cardImages.add(cardImg5 = findViewById(R.id.image_card_5));

        // Create FunctionCard objects and add to array
        for (int i = 0; i < cardsFun.size(); i++) {
            funCards.add(new FunctionCard(cardsFun.get(i), cardFunX[i], cardFunY[i]));
            funImgCards.add(new FunctionCard(cardsFunImg.get(i), cardFunImgX[i], cardFunImgY[i]));
        }

        // Set image in function cards
        for (int c = 0; c < imageList.length; c++) {
            funImgCards.get(c).setImage(cardImages.get(c), imageList[c]);
        }

        // Different approach
 /*       for(MedicationCard MedCardObj : MedCardsObjects) {

            switch(MedCardObj.getName()) {
                case "AXTIL":
                    MedCardObj.setImgId(R.drawable.heart);
                    break;
                case "RIDLIP":
                    MedCardObj.setImgId(R.drawable.cholesterol);
                    break;
                case "SYLIMAROL":
                    MedCardObj.setImgId(R.drawable.liver);
                    break;
                case "ENCEPHABOL":
                    MedCardObj.setImgId(R.drawable.brain);
                    break;
            }

        }
*/
        // Set text in function cards
        for (int t = 0; t < MedCardsObjects.size(); t++) {
            funCards.get(t).setFunctionText(cardsFunText.get(t), (MedCardsObjects.get(t)).getFunctionsText());
        }

        cardName = findViewById(R.id.card_name);

        // Set the name of checked medication
        medNameText = findViewById(R.id.med_name);
        medNameText.setText(MedCardSelected.getName());


       for(CardView card : cardsFun) {
            card.setOnTouchListener(mOnTouchListener);
        }

        for(CardView cardImg : cardsFunImg) {
            cardImg.setOnTouchListener(mOnTouchListener);
        }

        // DragListener
        cardName.setOnDragListener(dragListener);

        // Frame size
        FrameLayout frameLayout = findViewById(R.id.frame);
        frame_height = frameLayout.getHeight();


        // Set objects functions: text and image cards
        for (int r = 0; r < MedCardsObjects.size(); r++) {
            MedCardsObjects.get(r).setFunctions(funCards.get(r), funImgCards.get(r));
        }


        // Associate functions with selected object
        for (MedicationCard med : MedCardsObjects) {
             if (MedCardSelected.getName().equals(med.getName())) {
                MedCardSelected.setFunctions(((med.getFunctions()).get(0)), (med.getFunctions()).get(1));
            }
        }

        // Move cards
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
        tap_sound = soundPool.load(this, R.raw.tap, 1);
        correct_sound = soundPool.load(this, R.raw.correct, 1);
        incorrect_sound = soundPool.load(this, R.raw.incorrect, 1);

        animRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);

    }

    /* END OF ON CREATE */




    View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            soundPool.play(tap_sound, 1, 1, 0, 0, 1);
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

                    cardName.setCardBackgroundColor(Color.parseColor("#01A9F2"));
                    medNameText.setTextColor(Color.WHITE);

                    break;

                case DragEvent.ACTION_DRAG_EXITED:

                    cardName.setCardBackgroundColor(Color.WHITE);
                    medNameText.setTextColor(Color.parseColor("#01A9F2"));

                    break;

                case DragEvent.ACTION_DROP:

                    cardName.setCardBackgroundColor(Color.WHITE);
                    medNameText.setTextColor(Color.parseColor("#01A9F2"));

                    view.setVisibility(View.GONE);

                    if (view.getId() == (MedCardSelected.getFunctions().get(0)).getCardViewId() ||
                            view.getId() == (MedCardSelected.getFunctions().get(1)).getCardViewId()) {

                        ScoreLabel.setText("Score: " + ++score);

                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(cardName);

                        soundPool.play(correct_sound, 1, 1, 0, 0, 1);
                        Toast.makeText(getApplicationContext(),"Correct!", Toast.LENGTH_SHORT).show();

                        if (score%2 == 0) {

                            playedName.add(medNameText.getText().toString());

                            cardName.setCardBackgroundColor(Color.GREEN);
                            cardName.startAnimation(animRotate);
                            YoYo.with(Techniques.FlipOutY)
                                    .duration(700)
                                    .playOn(cardName);

                            State state = State.WON;
                            playNext();

                        }
                        checkVisibility();
                    }
                    else {
                        soundPool.play(incorrect_sound, 1, 1, 0, 0, 1);
                        ScoreLabel.setText("Score: " + --score);

                        YoYo.with(Techniques.Shake)
                                .duration(900)
                                .playOn(cardName);

                        Toast.makeText(getApplicationContext(),"Incorrect :(",Toast.LENGTH_SHORT).show();
                        checkVisibility();
                    }

                    break;
            }
            return true;
        }
    };

    private void playNext() {

            boolean flag = false;
/*
            for(int k = 0; k < playedName.size(); k++) {
                if (medNameText.getText() == playedName.get(k)) {
                    flag = false;
                }
                else {
                    flag = true;
                }
            }
*/
            for(int i = 0; i < MedCardsObjects.size(); i++) {
                if (medNameText.getText() != MedCardsObjects.get(i).getName()) {
                        MedCardSelected = MedCardsObjects.get(i);
                        flag = true;
                        for(int j = 0; j < playedName.size(); j++) {
                            if (MedCardSelected.getName() == playedName.get(j)) {
                                flag = false;
                                break;
                            }
                        }
                }
            }

            if(flag) {
                medNameText.setText(MedCardSelected.getName());
                cardName.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FlipInY)
                        .duration(900)
                        .playOn(cardName);
            }

    }


    public void pauseGame(View view) {

        soundPool.play(click_sound, 1, 1, 0, 0, 1);

        YoYo.with(Techniques.Pulse)
                .duration(700)
                .playOn(view);

        if (!pause_flag) {

            pause_flag = true;
            State state = State.PAUSED;

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


    public void saveAnswers() {
        answers = new String[4];

        for (int i = 0; i < 4; i++) {
            answers[i] = MedCardsObjects.get(i).getName();
            answers[i] += " - " + (MedCardsObjects.get(i).getFunctionsText().toLowerCase()) + "\n";
        }
    }


    public void checkVisibility() {
        int counter = 0;

        for (int i = 0; i < funCards.size(); i++) {
            if (cardsFun.get(i).getVisibility() != View.VISIBLE) {
                counter++;
            }
            if (cardsFunImg.get(i).getVisibility() != View.VISIBLE) {
                counter++;
            }
        }

        if (counter == (cardsFun.size() + cardsFunImg.size())) {
            State state = State.LOST;
            // Start End Activity with delay
            timerHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent2 = new Intent(GameActivity.this, EndActivity.class);
                    saveAnswers();
                    intent2.putExtra("answers", answers);
                    startActivity(intent2);
                }
            }, 1000);
        }

    }

}




