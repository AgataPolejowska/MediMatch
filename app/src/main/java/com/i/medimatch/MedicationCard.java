package com.i.medimatch;


import java.io.Serializable;
import java.util.ArrayList;

public class MedicationCard implements Serializable {

    private String name;
    private String imageUrl;
    private String function;
    private boolean newCard;
    private static int numberNewCard;
    public ArrayList<FunctionCard> functionsList = new ArrayList<>();

    public MedicationCard(String name, String function) {
        this.name = name;
        this.function = function;
    }

    public String getName() {
        return name;
    }

    public String getFunctionsText() {
        return function;
    }

    public void setFunctions(FunctionCard funCard, FunctionCard funImgCard) {
        this.functionsList.add(funCard);
        this.functionsList.add(funImgCard);
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void isNew(boolean newCard) {
        numberNewCard++;
        this.newCard = newCard;
    }

    public boolean checkNew(){
        return newCard;
    }

    public static void setNumberNewCard(int numberNewCard) {
        MedicationCard.numberNewCard = numberNewCard;
    }

    public static int getNumberNewCard() {
        return numberNewCard;
    }

    public ArrayList<FunctionCard> getFunctions() {
        return functionsList;
    }

    public void setNameSelected(boolean selected) {
    }

}
