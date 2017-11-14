package com.example.kevinlay.riffraffler.model;

/**
 * Created by kevinlay on 11/8/17.
 */

public class MyRafflesModel {

    String raffleName;
    String image;
    String raffleID;
    boolean isWinner;
    boolean isActive;

    public MyRafflesModel(String raffleName, String image, String raffleID) {
        isActive = true;
        isWinner = false;
        this.raffleName = raffleName;
        this.image = image;
        this.raffleID = raffleID;
    }

    public String getRaffleName() {
        return raffleName;
    }

    public void setRaffleName(String raffleName) {
        this.raffleName = raffleName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
