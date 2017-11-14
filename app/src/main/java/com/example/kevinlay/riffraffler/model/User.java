package com.example.kevinlay.riffraffler.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kevinlay on 11/13/17.
 */

public class User {

    String userId;
    List<RaffleTicketModel> raffleTicketsOwned;
    List<RaffleTicketModel> raffleTickets;

    public User() {

    }

    public User(String userId, List<RaffleTicketModel> raffleTicketsOwned, List<RaffleTicketModel> raffleTickets) {
        this.userId = userId;
        this.raffleTicketsOwned = raffleTicketsOwned;
        this.raffleTickets = raffleTickets;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<RaffleTicketModel> getRaffleTicketsOwned() {
        return raffleTicketsOwned;
    }

    public void setRaffleTicketsOwned(List<RaffleTicketModel> raffleTicketsOwned) {
        this.raffleTicketsOwned = raffleTicketsOwned;
    }

    public List<RaffleTicketModel> getRaffleTickets() {
        return raffleTickets;
    }

    public void setRaffleTickets(List<RaffleTicketModel> raffleTickets) {
        this.raffleTickets = raffleTickets;
    }

    public void addTicketsOwned(RaffleTicketModel raffle) {
        raffleTicketsOwned.add(raffle);
    }

    public void addTickets(RaffleTicketModel raffle) {
        raffleTickets.add(raffle);
    }

    public Map<String, Object> toMap(List<RaffleTicketModel> raffleTicketModels) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", this.userId);
        map.put("raffleTickets", this.raffleTickets);
        map.put("raffleTicketsOwned", raffleTicketModels);
        return map;
    }
}
