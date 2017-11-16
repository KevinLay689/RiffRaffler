package com.example.kevinlay.riffraffler.model;

import java.util.List;

/**
 * Created by kevinlay on 11/13/17.
 */

public class RaffleTicketModel {

    String raffleId;
    boolean isActive;
    String winner;
    String owner;
    List<String> usersRegistered;
    String raffleName;

    public RaffleTicketModel() {

    }

    public RaffleTicketModel(String raffleId, String owner, List<String> usersRegistered, String raffleName) {
        this.raffleId = raffleId;
        this.isActive = true;
        this.winner = "";
        this.owner = owner;
        this.raffleName = raffleName;
        this.usersRegistered = usersRegistered;
    }

    public String getRaffleId() {
        return raffleId;
    }

    public void setRaffleId(String raffleId) {
        this.raffleId = raffleId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getUsersRegistered() {
        return usersRegistered;
    }

    public void setUsersRegistered(List<String> usersRegistered) {
        this.usersRegistered = usersRegistered;
    }

    public void addUsers(String userId) {
        usersRegistered.add(userId);
    }

    public String getRaffleName() {
        return raffleName;
    }

}
