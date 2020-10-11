package com.i.medimatch;

import android.content.ClipData;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


public class GameActivity extends AppCompatActivity {

    private TextView ScoreLabel = null;
    private int score = 0;
    boolean answer = false;

    CardView cardFun, cardName;


    public GameActivity() {
        // Empty constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ScoreLabel = (TextView) findViewById(R.id.scoreLabel);

        cardFun = (CardView) findViewById(R.id.card_fun);
        cardName = (CardView) findViewById(R.id.card_name);

        cardFun.setOnLongClickListener(longClickListener);
        cardName.setOnDragListener(dragListener);


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
