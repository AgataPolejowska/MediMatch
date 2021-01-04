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

import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

/**
 * Represents the function of a medication.
 * @author Agata Polejowska
 */
public class FunctionCard {

    /** The cardview of the function card */
    private final CardView cardView;
    /** The cardview X coordinate */
    private float cardX;
    /** The cardview Y coordinate */
    private float cardY;
    /** The cardview height */
    private final float cardHeight;
    /** The cardview width */
    private final float cardWidth;
    /** The cardview speed */
    private float speed;

    /** The width of the user screen */
    public static float screenWidth;
    /** The height of the user screen */
    public static float screenHeight;

    /**
     * Constructor - setting the parameters of the object and user screen.
     * @param cardView the cardview of the function card
     * @param x the cardview X-coordinate
     * @param y the cardview Y-coordinate
     */
    public FunctionCard(CardView cardView, float x, float y) {
        this.cardView = cardView;
        this.cardHeight = cardView.getHeight();
        this.cardWidth = cardView.getWidth();
        this.cardX = x;
        this.cardY = y;
    }

    /**
     * Setting the text in the function card cardview.
     * @param cardFunText the textview with function description
     * @param functionText the text describing the medication function
     */
    public void setFunctionText(TextView cardFunText, String functionText) {
        cardFunText.setText(functionText);
        cardFunText.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
    }

    /**
     * Setting the image in the function card cardview.
     * @param imgCard the imageview with the image associated with medication function
     * @param resourceId the image resource id
     */
    public void setImage(ImageView imgCard, int resourceId) {
        imgCard.setImageResource(resourceId);
    }

    /**
     * Getting the cardview id.
     * @return the cardview id
     */
    public int getCardViewId() {
        return cardView.getId();
    }

    /**
     * Setting the speed of the cardview.
     * @param speed speed at which the cardview is moving
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * Changing the position of the cardview in the game frame.
     * @param direction the direction in which the card is moving
     */
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
