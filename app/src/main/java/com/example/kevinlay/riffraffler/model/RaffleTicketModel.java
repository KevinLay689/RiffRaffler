package com.example.kevinlay.riffraffler.model;

import java.util.List;

/**
 * Created by kevinlay on 11/13/17.
 */

public class RaffleTicketModel {

    private String raffleId;
    private boolean isActive;
    private String winner;
    private String owner;
    private List<String> usersRegistered;
    private String raffleName;
    private String rafflePrize;
    private String raffleElig;
    private String photo;

    public RaffleTicketModel() {

    }

    public RaffleTicketModel(String raffleId, String owner, List<String> usersRegistered, String raffleName, String raffleElig, String rafflePrize, String photo) {
        this.raffleId = raffleId;
        this.isActive = true;
        this.winner = "";
        this.owner = owner;
        this.raffleName = raffleName;
        this.usersRegistered = usersRegistered;
        this.raffleElig = raffleElig;
        this.rafflePrize = rafflePrize;
        this.photo = photo;
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

    public void setRaffleName(String raffleName) {
        this.raffleName = raffleName;
    }

    public String getRafflePrize() {
        return rafflePrize;
    }

    public void setRafflePrize(String rafflePrize) {
        this.rafflePrize = rafflePrize;
    }

    public String getRaffleElig() {
        return raffleElig;
    }

    public void setRaffleElig(String raffleElig) {
        this.raffleElig = raffleElig;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
