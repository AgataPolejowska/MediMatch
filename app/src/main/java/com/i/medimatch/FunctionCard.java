package com.i.medimatch;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class FunctionCard  {

    public float card_x, card_y, card_height, card_width;
    public static float screen_width;
    public static float screen_height;
    public float speed;
    public CardView cardView;
    public TextView cardFunText;
    public String functionText;


    public FunctionCard(CardView cardView, float x, float y) {

        this.cardView = cardView;
        this.card_height = cardView.getHeight();
        this.card_width = cardView.getWidth();
        this.card_x = x;
        this.card_y = y;

    }

    public void setFunctionText(String functionText, TextView cardFunText) {
        this.functionText = functionText;
        cardFunText.setText(functionText);
    }

    public ImageView setImage(ImageView imgCard, int resourceId) {
        imgCard.setImageResource(resourceId);
        return imgCard;
    }


    public void setSpeed(float speed) {
        this.speed = speed;
    }


    public void changePosition(String direction) {

        cardView.setX(-100.0f);
        cardView.setY(-100.0f);

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

                if(card_x < -700) {
                    card_x = screen_width;
                    card_y = (float) Math.floor(Math.random() * (screen_height - card_height));
                }

                cardView.setX(card_x);
                cardView.setY(card_y);

                break;

            case "RIGHT":

                card_x += (speed-2);

                if(card_x > screen_width) {
                    card_x = -100.0f;
                    card_y = (float) Math.floor(Math.random() * (screen_height - card_height));
                }

                cardView.setX(card_x);
                cardView.setY(card_y);

                break;
        }


    }

}
