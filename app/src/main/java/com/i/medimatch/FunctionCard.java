package com.i.medimatch;

public class FunctionCard {

    int id_fun;
    String fun;
    int img_fun;

    public FunctionCard() {

    }

    public int getImage() {
        return img_fun;
    }

    public void setImage(int image) {
        this.img_fun = image;
    }

    public String getFun() {
        return fun;
    }

    public void setFun(String fun) {
        this.fun = fun;
    }

}
