package com.i.medimatch;

import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;


public class FunctionCard {

    private final CardView cardView;
    private float cardX;
    private float cardY;
    private final float cardHeight;
    private final float cardWidth;
    private float speed;

    public static float screenWidth;
    public static float screenHeight;

    public FunctionCard(CardView cardView, float x, float y) {
        this.cardView = cardView;
        this.cardHeight = cardView.getHeight();
        this.cardWidth = cardView.getWidth();
        this.cardX = x;
        this.cardY = y;
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
            case "LEFT":
                cardX -= (speed-2);

                if(cardX < -700.0f) {
                    cardX = screenWidth;
                    cardY = (float) Math.floor(Math.random() * (screenHeight - cardHeight));
                }

                cardView.setX(cardX);
                cardView.setY(cardY);

                break;

            case "RIGHT":
                cardX += (speed-2);

                if(cardX > screenWidth) {
                    cardX = -700.0f;
                    cardY = (float) Math.floor(Math.random() * (screenHeight - cardHeight));
                }

                cardView.setX(cardX);
                cardView.setY(cardY);

                break;
        }
    }
}
