package com.example.kevinlay.riffraffler.model;

/**
 * Created by kevinlay on 11/13/17.
 */

public class RaffleModel {

    boolean isWinner;
    boolean isActive;
    public String raffleID;

    public RaffleModel() {
        
    }

    public RaffleModel(String raffleID) {
        isWinner = false;
        isActive = true;
        this.raffleID = raffleID;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
