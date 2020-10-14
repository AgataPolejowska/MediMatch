package com.i.medimatch;

import java.io.Serializable;

public class MedicationCard implements Serializable {

    public String name;
    public boolean checked;

    public MedicationCard(String name, boolean checked) {
        this.name = name;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setNameChecked(boolean checked) {
        this.checked = checked;
    }

    public void setName(String name) {
        this.name = name;
    }

}
