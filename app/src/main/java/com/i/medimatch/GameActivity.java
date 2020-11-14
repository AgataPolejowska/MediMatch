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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class GameActivity extends AppCompatActivity {

    private enum State {
        PAUSED, WON, LOST, RUNNING;
    }

    private State state = State.RUNNING;

    private TextView scoreLabel = null;
    private int score = 0;

    private TextView timerLabel;
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

            timerLabel.setText(String.format("Timer: %d:%02d", minutes, seconds));
            timerHandler.postDelayed(this, 500);
        }
    };

    private Button timerButton;
    private boolean pauseFlag = false;

    ArrayList<CardView> cardsFun = new ArrayList<>();
    ArrayList<CardView> cardsFunImg = new ArrayList<>();
    ArrayList<FunctionCard> funCards = new ArrayList<>();
    ArrayList<FunctionCard> funImgCards = new ArrayList<>();

    ArrayList<MedicationCard> MedCardsObjects = new ArrayList<>();
    ArrayList<String> playedName = new ArrayList<>();

    MedicationCard MedCardSelected;

    int[] imageList = new int[] { R.drawable.thyroid, R.drawable.cholesterol, R.drawable.liver, R.drawable.brain, R.drawable.heart};

    String [] answers;

    CardView cardFun1, cardFun2, cardFun3, cardFun4, cardFun5, cardFun6;
    TextView cardFunText1, cardFunText2, cardFunText3, cardFunText4, cardFunText5, cardFunText6;

    CardView cardFunImg1, cardFunImg2, cardFunImg3, cardFunImg4, cardFunImg5, cardFunImg6;
    ImageView cardImg1, cardImg2, cardImg3, cardImg4, cardImg5, cardImg6;

    CardView cardName;
    TextView medNameText;

    public float cardFun1_x, cardFun1_y;
    public float cardFun2_x, cardFun2_y;
    public float cardFun3_x, cardFun3_y;
    public float cardFun4_x, cardFun4_y;
    public float cardFun5_x, cardFun5_y;
    public float cardFun6_x, cardFun6_y;

    public float cardFunImg1_x, cardFunImg1_y;
    public float cardFunImg2_x, cardFunImg2_y;
    public float cardFunImg3_x, cardFunImg3_y;
    public float cardFunImg4_x, cardFunImg4_y;
    public float cardFunImg5_x, cardFunImg5_y;
    public float cardFunImg6_x, cardFunImg6_y;

    public float frameHeight;

    private SoundPool soundPool;
    private int clickSound, tapSound, correctSound, incorrectSound;

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

        scoreLabel = findViewById(R.id.scoreLabel);
        scoreLabel.setText("Score: " + score);

        timerLabel = findViewById(R.id.timerLabel);
        timerButton = findViewById(R.id.timerButton);

        // Start timer
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);

        // Menu
        Button openMenu = findViewById(R.id.popup_menu);

        openMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                soundPool.play(clickSound, 1, 1, 0, 0, 1);
                PopupMenu popupMenu = new PopupMenu(GameActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.item_info:
                                InfoDialog infoDialog = new InfoDialog();
                                infoDialog.show(getSupportFragmentManager(), "Information");
                                return true;
                            case R.id.item_mainmenu:
                                startActivity(new Intent(GameActivity.this, MainActivity.class));
                                return true;
                            case R.id.item_settings:
                                startActivity(new Intent(GameActivity.this, SettingsActivity.class));
                                return true;
                            case R.id.item_end:
                                startActivity(new Intent(GameActivity.this, EndActivity.class));
                                return true;
                            case R.id.item_quit:
                                AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                                builder.setMessage("Are you sure you want to exit?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                finishAffinity();
                                                System.exit(0);
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

        // Get frame size
        FrameLayout frameLayout = findViewById(R.id.frame);
        frameHeight = frameLayout.getHeight();

        // Get card size
        FunctionCard.screenWidth = size.x;
        FunctionCard.screenHeight = size.y;

        // Cards coordinates
        float [] cardFunX = new float[] {cardFun1_x, cardFun2_x, cardFun3_x, cardFun4_x, cardFun5_x, cardFun6_x};
        float [] cardFunY = new float[]{cardFun1_y, cardFun2_y, cardFun3_y, cardFun4_y, cardFun5_y, cardFun6_y};
        float [] cardFunImgX = new float[] {cardFunImg1_x, cardFunImg2_x, cardFunImg3_x, cardFunImg4_x, cardFunImg5_x, cardFunImg6_x};
        float [] cardFunImgY = new float[]{cardFunImg1_y, cardFunImg2_y, cardFunImg3_y, cardFunImg4_y, cardFunImg5_y, cardFunImg6_y};

        // Find cards elements and store in arrays
        cardsFun.add(cardFun1 = findViewById(R.id.card_fun_1));
        cardsFun.add(cardFun2 = findViewById(R.id.card_fun_2));
        cardsFun.add(cardFun3 = findViewById(R.id.card_fun_3));
        cardsFun.add(cardFun4 = findViewById(R.id.card_fun_4));
        cardsFun.add(cardFun5 = findViewById(R.id.card_fun_5));
        cardsFun.add(cardFun6 = findViewById(R.id.card_fun_6));

        cardsFunImg.add(cardFunImg1 = findViewById(R.id.card_fun_img_1));
        cardsFunImg.add(cardFunImg2 = findViewById(R.id.card_fun_img_2));
        cardsFunImg.add(cardFunImg3 = findViewById(R.id.card_fun_img_3));
        cardsFunImg.add(cardFunImg4 = findViewById(R.id.card_fun_img_4));
        cardsFunImg.add(cardFunImg5 = findViewById(R.id.card_fun_img_5));
        cardsFunImg.add(cardFunImg6 = findViewById(R.id.card_fun_img_6));

        ArrayList<TextView> cardsFunText = new ArrayList<>();
        cardsFunText.add(cardFunText1 = findViewById(R.id.card_fun_text_1));
        cardsFunText.add(cardFunText2 = findViewById(R.id.card_fun_text_2));
        cardsFunText.add(cardFunText3 = findViewById(R.id.card_fun_text_3));
        cardsFunText.add(cardFunText4 = findViewById(R.id.card_fun_text_4));
        cardsFunText.add(cardFunText5 = findViewById(R.id.card_fun_text_5));
        cardsFunText.add(cardFunText6 = findViewById(R.id.card_fun_text_6));

        ArrayList<ImageView> cardImages = new ArrayList<>();
        cardImages.add(cardImg1 = findViewById(R.id.image_card_1));
        cardImages.add(cardImg2 = findViewById(R.id.image_card_2));
        cardImages.add(cardImg3 = findViewById(R.id.image_card_3));
        cardImages.add(cardImg4 = findViewById(R.id.image_card_4));
        cardImages.add(cardImg5 = findViewById(R.id.image_card_5));
        cardImages.add(cardImg6 = findViewById(R.id.image_card_6));


        // Check if new card is added, if yes set visible associated fun cards
        for (int t = 0; t < MedCardsObjects.size(); t++) {
            if(MedCardsObjects.get(t).checkNew()) {
                Picasso.get().load(MedCardsObjects.get(t).getImageUrl()).into(cardImg6);
                cardFunImg6.setVisibility(View.VISIBLE);
                cardFun6.setVisibility(View.VISIBLE);
                break;
            }
            else{
                cardFunImg6.setVisibility(View.GONE);
                cardFun6.setVisibility(View.GONE);
            }
        }

        // Create FunctionCard objects and add to array
        for (int i = 0; i < cardsFun.size(); i++) {
            funCards.add(new FunctionCard(cardsFun.get(i), cardFunX[i], cardFunY[i]));
            funImgCards.add(new FunctionCard(cardsFunImg.get(i), cardFunImgX[i], cardFunImgY[i]));
        }

        // Set image in function cards
        for (int c = 0; c < imageList.length; c++) {
            funImgCards.get(c).setImage(cardImages.get(c), imageList[c]);
        }

        // Set text in function cards
        for (int t = 0; t < MedCardsObjects.size(); t++) {
            funCards.get(t).setFunctionText(cardsFunText.get(t), (MedCardsObjects.get(t)).getFunctionsText());
        }

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

        // Set the name of checked medication
        medNameText = findViewById(R.id.med_name);
        medNameText.setText(MedCardSelected.getName());

        // Touch listener on cards
       for(CardView card : cardsFun) {
            card.setOnTouchListener(mOnTouchListener);
       }
       for(CardView cardImg : cardsFunImg) {
            cardImg.setOnTouchListener(mOnTouchListener);
       }

        // DragListener on the main card
        cardName = findViewById(R.id.card_name);
        cardName.setOnDragListener(dragListener);


        // Move cards
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timerHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (int k = 0; k < funCards.size(); k++) {
                            (funCards.get(k)).setSpeed(Math.round(FunctionCard.screenWidth / (95.0+(k*10))));
                            (funCards.get(k)).changePosition("LEFT");
                            (funImgCards.get(k)).setSpeed(Math.round(FunctionCard.screenWidth / (120.0+(k*10))));
                            (funImgCards.get(k)).changePosition("RIGHT");
                        }
                    }
                });
            }
        }, 0, 20);


        // Implement sound effects
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

        clickSound = soundPool.load(this, R.raw.click, 1);
        tapSound = soundPool.load(this, R.raw.tap, 1);
        correctSound = soundPool.load(this, R.raw.correct, 1);
        incorrectSound = soundPool.load(this, R.raw.incorrect, 1);

        // Add animation
        animRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);

    }

    /* END OF ON CREATE */




    View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            soundPool.play(tapSound, 1, 1, 0, 0, 1);
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

                        scoreLabel.setText("Score: " + ++score);

                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(cardName);

                        soundPool.play(correctSound, 1, 1, 0, 0, 1);
                        Toast.makeText(getApplicationContext(),"Correct!", Toast.LENGTH_SHORT).show();

                        if (score%2 == 0) {

                            cardName.setCardBackgroundColor(Color.GREEN);
                            cardName.startAnimation(animRotate);
                            YoYo.with(Techniques.FlipOutY)
                                    .duration(700)
                                    .playOn(cardName);

                            State state = State.WON;

                            playNext((MedCardSelected.getName()));

                        }
                        checkVisibility();
                    }
                    else {
                        soundPool.play(incorrectSound, 1, 1, 0, 0, 1);
                        scoreLabel.setText("Score: " + --score);

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


    // Go to the next medication
    private void playNext(String currentName) {
        playedName.add(currentName);

        // Testing
        final StringBuilder builder = new StringBuilder();
        builder.append("You have completed: ");
        for(int j = 0; j < playedName.size(); j++) {
            builder.append(" " +playedName.get(j));
        }
        Toast.makeText(this, builder, Toast.LENGTH_SHORT).show();

        for (MedicationCard MedCard : MedCardsObjects){
            if (currentName.equals(MedCard.getName())){
                continue;
            }
                MedCardSelected = MedCard;
                if((playedName).contains(MedCardSelected.getName())) {
                    continue;
                }

                medNameText.setText(MedCardSelected.getName());
                YoYo.with(Techniques.FlipInY)
                        .duration(900)
                        .playOn(cardName);
                break;
        }
    }



    // Called when user taps the PAUSE Button
    public void pauseGame(View view) {

        soundPool.play(clickSound, 1, 1, 0, 0, 1);

        YoYo.with(Techniques.Pulse)
                .duration(700)
                .playOn(view);

        if (!pauseFlag) {
            pauseFlag = true;
            State state = State.PAUSED;
            // Stop the timer
            timer.cancel();
            timer = null;
            timerHandler.removeCallbacks(timerRunnable);
            // Change button text
            timerButton.setText("START");
        }
        else {
            pauseFlag = false;
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
                                (funCards.get(k)).setSpeed(Math.round(FunctionCard.screenWidth / (95.0+(k*10))));
                                (funCards.get(k)).changePosition("LEFT");
                                (funImgCards.get(k)).setSpeed(Math.round(FunctionCard.screenWidth / (120.0+(k*10))));
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

    public void saveResult() {
        //
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
            }, 1200);
        }

    }

}




