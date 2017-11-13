package com.example.kevinlay.riffraffler.model;

/**
 * Created by kevinlay on 11/8/17.
 */

public class MessagesModel {
    private String messageName;
    private int image;

    public MessagesModel(String messageName, int image) {
        this.messageName = messageName;
        this.image = image;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
