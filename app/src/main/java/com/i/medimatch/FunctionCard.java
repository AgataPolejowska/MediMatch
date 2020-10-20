package com.i.medimatch;

import androidx.cardview.widget.CardView;

public class FunctionCard  {

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

    public void setSpeed(float speed) {
        this.speed = speed;
    }


    public void changePosition() {

        card_x -= speed;

        if(card_x < -700) {
            card_x = screen_width;
            card_y = (float) Math.floor(Math.random() * (screen_height - card_height));
        }

        cardView.setX(card_x);
        cardView.setY(card_y);

    }

}
