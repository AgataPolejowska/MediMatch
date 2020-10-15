package com.i.medimatch;

import java.io.Serializable;
import java.util.ArrayList;

public class MedicationCard implements Serializable {

    public String name;
    public boolean checked;
    public ArrayList<String> functions = new ArrayList<String>();

    public MedicationCard(String name, boolean checked) {
        this.name = name;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getFunctions() {
        return functions;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setNameChecked(boolean checked) {
        this.checked = checked;
    }

    public void setFunctions(ArrayList<String> functions) {
        //
    }


}
