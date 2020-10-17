package com.i.medimatch;

import androidx.cardview.widget.CardView;

public class FunctionCard {

    public float x, y, height, width;
    public float speed;
    public CardView cardView;


    public FunctionCard(CardView cardView) {

        cardView.setX(-80.0f);
        this.height = cardView.getHeight();
        this.width = cardView.getWidth();

    }


    public void changePosition(float screen_width, float screen_height) {

        speed = Math.round(screen_width/95.0f);

        x -= speed;

        if(x < 0) {
            x = screen_width;
            y = (float) Math.floor(Math.random() * (screen_height - height));
        }

        cardView.setX(x);
        cardView.setY(y);

    }

}
