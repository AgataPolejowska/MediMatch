package com.i.medimatch;

import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


public class GameActivity extends AppCompatActivity {

    CardView cardFun, cardName;

    public GameActivity() {
        // Empty constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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

            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    final View view = (View) event.getLocalState();

                    if (view.getId() == R.id.card_fun) {
                        cardName.setCardBackgroundColor(Color.LTGRAY);
                    }
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    break;
            }

            return true;
        }
    };
}
