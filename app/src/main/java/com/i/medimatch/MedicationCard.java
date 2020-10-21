package com.i.medimatch;

import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;

public class MedicationCard implements Serializable {

    public String name;
    public boolean checked;
    public ArrayList<FunctionCard> functions = new ArrayList<>();

    public MedicationCard(String name, boolean checked) {
        this.name = name;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public ArrayList<FunctionCard> setFunctions() {
        return functions;
    }

    public ArrayList<FunctionCard> getFunctions() {
        return functions;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setNameChecked(boolean checked) {
        this.checked = checked;
    }

}
