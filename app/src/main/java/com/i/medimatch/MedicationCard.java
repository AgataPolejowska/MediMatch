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


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents the medication.
 * @author Agata Polejowska
 */
public class MedicationCard implements Serializable {

    /** The name of the medication. */
    private String name;
    /** The image URL that is associated with the medication function. */
    private String imageUrl;
    /** The medication function description. */
    private String function;
    /** A flag defining if the medication card is newly added by the user. */
    private boolean newCard;
    /** Stores the number of new cards created. */
    private static int numberNewCard;
    /** Stores the functions of the medication. */
    public ArrayList<FunctionCard> functionsList = new ArrayList<>();

    /**
     * Constructor - setting the parameters of the object
     * @param name the name of the medication
     * @param function the function of the medication
     */
    public MedicationCard(String name, String function) {
        this.name = name;
        this.function = function;
    }

    /**
     * Getting the name of the medication.
     * @return the name of the medication
     */
    public String getName() {
        return name;
    }

    /**
     * Getting the medication function.
     * @return the medication function
     */
    public String getFunctionsText() {
        return function;
    }

    /**
     * Setting the medication functions.
     * @param funCard the function card with text
     * @param funImgCard the funciton card with image
     */
    public void setFunctions(FunctionCard funCard, FunctionCard funImgCard) {
        this.functionsList.add(funCard);
        this.functionsList.add(funImgCard);
    }

    /**
     * Setting the image URL.
     * @param imageUrl the image URL
     */
    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    /**
     * Getting the image URL.
     * @return the image URL
     */
    public String getImageUrl(){
        return imageUrl;
    }

    /**
     * Incrementing the number of new cards if user added the card.
     * @param newCard the flag defining if the medication card is newly added by the user
     */
    public void isNew(boolean newCard) {
        numberNewCard++;
        this.newCard = newCard;
    }

    /**
     * Returning the flag defining if the medication card is newly added by the user.
     * @return the flag defining if the medication card is newly added by the user
     */
    public boolean checkNew(){
        return newCard;
    }

    /**
     * Setting the number of new cards added.
     * @param numberNewCard the number of new cards added
     */
    public static void setNumberNewCard(int numberNewCard) {
        MedicationCard.numberNewCard = numberNewCard;
    }

    /**
     * Getting the number of new cards added.
     * @return numberNewCard the number of new cards added
     */
    public static int getNumberNewCard() {
        return numberNewCard;
    }

    /**
     * Getting functions in the form of cardviews associated with the medication
     * @return functions in the form of cardviews associated with the medication
     */
    public ArrayList<FunctionCard> getFunctions() {
        return functionsList;
    }

}
