package com.i.medimatch;



import java.io.Serializable;
import java.util.ArrayList;

public class MedicationCard implements Serializable {

    public String name;
    public String function;
    public boolean selected = false;
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

    public ArrayList<FunctionCard> getFunctions() {
        return functionsList;
    }

    public void setNameSelected(boolean selected) {
        this.selected = selected;
    }

}
