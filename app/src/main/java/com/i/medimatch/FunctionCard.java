package com.i.medimatch;

import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;


public class FunctionCard {

    public float card_x, card_y, card_height, card_width;
    public static float screen_width;
    public static float screen_height;
    public float speed;
    public CardView cardView;


    public FunctionCard(CardView cardView, float x, float y) {
        this.cardView = cardView;
        this.card_height = cardView.getHeight();
        this.card_width = cardView.getWidth();
        this.card_x = x;
        this.card_y = y;
    }

    public void setFunctionText(TextView cardFunText, String functionText) {
        cardFunText.setText(functionText);
        cardFunText.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
    }

    public void setImage(ImageView imgCard, int resourceId) {
        imgCard.setImageResource(resourceId);
    }

    public int getCardViewId() {
        return cardView.getId();
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void changePosition(String direction) {

        switch(direction) {
            case "DOWN":

                card_y += 10;

                if(cardView.getY() > screen_height) {
                    card_x = (float) Math.floor(Math.random()*(screen_width - card_width));
                    card_y = -100.0f;
                }

                cardView.setX(card_x);
                cardView.setY(card_y);

                break;

            case "LEFT":

                card_x -= (speed-2);

                if(card_x < -700.0f) {
                    card_x = screen_width;
                    card_y = (float) Math.floor(Math.random() * (screen_height - card_height));
                }

                cardView.setX(card_x);
                cardView.setY(card_y);

                break;

            case "RIGHT":

                card_x += (speed-2);

                if(card_x > screen_width) {
                    card_x = -700.0f;
                    card_y = (float) Math.floor(Math.random() * (screen_height - card_height));
                }

                cardView.setX(card_x);
                cardView.setY(card_y);

                break;
        }


    }

}
