/*
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are not permitted without written permission form the copyright holders.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.i.medimatch;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.SoundPool;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents the game activity in which the game is played.
 * @author Agata Polejowska
 */
public class GameActivity extends AppCompatActivity {

    /** The label for displaying the current score. */
    private TextView scoreLabel = null;
    /** Stores the total score. */
    private int score = 0;

    /** The label for displaying time passed. */
    private TextView timerLabel;
    /** Defines the start time. */
    private long startTime = 0;
    /** A timer object */
    private Timer timer = new Timer();
    /** A handler object */
    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    /** Used to count the time. */
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

    /** A button for pausing and resuming the timer. */
    private Button timerButton;
    /** A flag for timer pausing and resuming. */
    private boolean pauseFlag = false;

    /** Stores cardviews with medication functions description. */
    private ArrayList<CardView> cardsFun = new ArrayList<>();
    /** Stores cardviews with medication functions images. */
    private ArrayList<CardView> cardsFunImg = new ArrayList<>();
    /** Stores function card with text objects.*/
    private ArrayList<FunctionCard> funCards = new ArrayList<>();
    /** Stores function card with images objects. */
    private ArrayList<FunctionCard> funImgCards = new ArrayList<>();

    /** Stores medication cards objects. */
    private ArrayList<MedicationCard> MedCardsObjects = new ArrayList<>();
    /** Stores the names of medications that user matched correctly. */
    private ArrayList<String> playedName = new ArrayList<>();

    /** An object representing the selected medication. */
    private MedicationCard MedCardSelected;

    /** Stores resources id */
    private int[] imageList = new int[] { R.drawable.thyroid, R.drawable.cholesterol, R.drawable.liver, R.drawable.brain, R.drawable.heart};

    /** The cardview with the medication description in the text format. */
    private CardView cardFun1, cardFun2, cardFun3, cardFun4, cardFun5, cardFun6;
    /** The textview with medication function description. */
    private TextView cardFunText1, cardFunText2, cardFunText3, cardFunText4, cardFunText5, cardFunText6;

    /** The cardview with the medication description as an image. */
    private CardView cardFunImg1, cardFunImg2, cardFunImg3, cardFunImg4, cardFunImg5, cardFunImg6;
    /** The imageview with medication function image. */
    private ImageView cardImg1, cardImg2, cardImg3, cardImg4, cardImg5, cardImg6;

    /** A cardview with the medication name to be matched. */
    private CardView cardName;
    /** A textview with the medication name to be matched. */
    private TextView medNameText;
    /** A cardview displayed after winning the game. */
    private CardView cardWin;

    /** The cardview with function description X an Y coordinates */
    public float cardFun1_x, cardFun1_y, cardFun2_x, cardFun2_y, cardFun3_x, cardFun3_y, cardFun4_x, cardFun4_y, cardFun5_x, cardFun5_y, cardFun6_x, cardFun6_y;
    /** The cardview with image X an Y coordinates */
    public float cardFunImg1_x, cardFunImg1_y, cardFunImg2_x, cardFunImg2_y, cardFunImg3_x, cardFunImg3_y, cardFunImg4_x, cardFunImg4_y, cardFunImg5_x, cardFunImg5_y, cardFunImg6_x, cardFunImg6_y;

    /** The height of the game frame */
    public float frameHeight;

    /** An object for managing and playing audio resources. */
    private SoundPool soundPool;
    /** Stores the loaded sound from the APK resource. */
    private int clickSound, tapSound, correctSound, incorrectSound, winSound;

    /** An object for animations. */
    private Animation animRotate;

    /** Stores answers. */
    private String [] answers;
    /** Stores results. */
    private String [] results = {"Your results: "};


    /* ON CREATE */
    /**
     * Initializing the game activity.
     * @param savedInstanceState a reference to a Bundle object
     */
    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Receive data from Settings Activity
        MedCardsObjects = (ArrayList<MedicationCard>) getIntent().getSerializableExtra("Medications");
        MedCardSelected = (MedicationCard) getIntent().getSerializableExtra("MedicationSelected");

        scoreLabel = findViewById(R.id.scoreLabel);
        scoreLabel.setText("Score: " + score);

        // Get current date
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        results[0] += currentDate;

        timerLabel = findViewById(R.id.timerLabel);
        timerButton = findViewById(R.id.timerButton);

        // Start timer
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);

        // Popup Menu
        Button openMenu = findViewById(R.id.popup_menu);

        openMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                soundPool.play(clickSound, 1, 1, 0, 0, 1);
                PopupMenu popupMenu = new PopupMenu(GameActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.item_info:
                                InfoDialog infoDialog = new InfoDialog();
                                infoDialog.show(getSupportFragmentManager(), "Information");
                                return true;
                            case R.id.item_mainmenu:
                                MedicationCard.setNumberNewCard(0);
                                startActivity(new Intent(GameActivity.this, MainActivity.class));
                                return true;
                            case R.id.item_settings:
                                MedicationCard.setNumberNewCard(0);
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

        // Find win card
        cardWin = findViewById(R.id.card_win);

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
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(audioAttributes)
                .build();

        clickSound = soundPool.load(this, R.raw.click, 1);
        tapSound = soundPool.load(this, R.raw.tap, 1);
        correctSound = soundPool.load(this, R.raw.correct, 1);
        incorrectSound = soundPool.load(this, R.raw.incorrect, 1);
        winSound = soundPool.load(this, R.raw.win, 1);

        // Add animation
        animRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
    }

    /* END OF ON CREATE */


    View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            soundPool.play(tapSound, 1, 1, 0, 0, 1);
            cardName.setCardBackgroundColor(Color.WHITE);
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, myShadowBuilder, v, 0);
            return false;
        }
    };

    View.OnDragListener dragListener = new View.OnDragListener() {
        @SuppressLint("SetTextI18n")
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

                        if (score%2 == 0 && score > 0) {
                            cardName.setCardBackgroundColor(Color.GREEN);
                            cardName.startAnimation(animRotate);
                            YoYo.with(Techniques.FlipOutY)
                                    .duration(700)
                                    .playOn(cardName);


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


    /**
     * Proceeds to the next medication to be matched.
     * @param currentName the name of medication currently played
     */
    private void playNext(String currentName) {

        playedName.add(currentName);

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


    /**
     * Called when the user taps the PAUSE Button.
     * @param view the user interface component
     */
    @SuppressLint("SetTextI18n")
    public void pauseGame(View view) {

        soundPool.play(clickSound, 1, 1, 0, 0, 1);

        YoYo.with(Techniques.Pulse)
                .duration(700)
                .playOn(view);

        if (!pauseFlag) {
            pauseFlag = true;

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

    /** Saves answers. */
    public void saveAnswers() {
        answers = new String[4];
        for (int i = 0; i < 4; i++) {
            answers[i] = MedCardsObjects.get(i).getName();
            answers[i] += " - " + (MedCardsObjects.get(i).getFunctionsText().toLowerCase()) + "\n";
        }
    }

    /** Saves results and stops timer. */
    public void saveResults() {
        timer.cancel();
        results[0] += " " + timerLabel.getText().toString().toLowerCase();
    }

    /** Checks if cardviews are visible. Under certain conditions proceeds to the next activity. */
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
            saveResults();

            if (score == 10 || score == 12) {
                saveAnswers();
                soundPool.play(winSound, 1, 1, 0, 0, 1);
                // Show win card
                cardWin.setVisibility(View.VISIBLE);
            }

            // Start End Activity with delay
            timerHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent2 = new Intent(GameActivity.this, EndActivity.class);
                    intent2.putExtra("answers", answers);
                    intent2.putExtra("results", results);
                    startActivity(intent2);
                }
            }, 6200);
        }

    }

}




