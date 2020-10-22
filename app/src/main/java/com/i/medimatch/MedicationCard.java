package com.i.medimatch;


import java.io.Serializable;
import java.util.ArrayList;

public class MedicationCard implements Serializable {

    public String name;
    public boolean selected;
    public ArrayList<FunctionCard> functionsList = new ArrayList<>();

    public MedicationCard(String name, boolean selected) {
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
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
