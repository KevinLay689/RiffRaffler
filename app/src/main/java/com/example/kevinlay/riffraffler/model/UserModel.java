package com.example.kevinlay.riffraffler.model;

import java.util.List;

/**
 * Created by kevinlay on 11/13/17.
 */

public class UserModel {

    List<String> messages;
    List<String> myRaffles;
    List<String> raffles;
    String id;

    public UserModel(List<String> messages,List<String> raffles, List<String> myRaffles, String id) {
        this.messages = messages;
        this.raffles = raffles;
        this.myRaffles = myRaffles;
        this.id = id;
    }
}
