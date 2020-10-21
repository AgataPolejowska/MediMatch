package com.i.medimatch;


import java.io.Serializable;
import java.util.ArrayList;

public class MedicationCard implements Serializable {

    public String name;
    public boolean selected;
    public ArrayList<FunctionCard> functions = new ArrayList<>();

    public MedicationCard(String name, boolean selected) {
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setFunctions(ArrayList <FunctionCard> funCards) {
        this.functions = funCards;
    }

    public ArrayList<FunctionCard> getFunctions() {
        return functions;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setNameSelected(boolean selected) {
        this.selected = selected;
    }

}
