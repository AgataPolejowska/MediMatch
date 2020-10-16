package com.i.medimatch;

import androidx.cardview.widget.CardView;

public class FunctionCard {

    public float x, y, height, width;
    public double speed;
    public float screen_height, screen_width;
    public CardView cardView;

    public FunctionCard(float x, float y, CardView cardView) {
        this.x = x;
        this.y = y;
        this.height = cardView.getHeight();
        this.width = cardView.getWidth();
    }

    public void setSpeed(double v) {
        this.speed = v;
    }

    public void changePosition() {

        x -= speed;

        if(x < 0) {
            x = screen_width;
            y = (float) Math.floor(Math.random() * (screen_height - height));
        }

        cardView.setX(x);
        cardView.setY(y);

    }

}
