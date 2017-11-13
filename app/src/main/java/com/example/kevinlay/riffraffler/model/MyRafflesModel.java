package com.example.kevinlay.riffraffler.model;

/**
 * Created by kevinlay on 11/8/17.
 */

public class MyRafflesModel {

    private String raffleName;
    private int image;

    public MyRafflesModel(String raffleName, int image) {
        this.raffleName = raffleName;
        this.image = image;
    }

    public String getRaffleName() {
        return raffleName;
    }

    public void setRaffleName(String raffleName) {
        this.raffleName = raffleName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}
