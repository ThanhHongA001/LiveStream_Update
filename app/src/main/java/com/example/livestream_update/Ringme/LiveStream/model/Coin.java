package com.example.livestream_update.Ringme.LiveStream.model;

public class Coin {
    private int id;
    private int currentStar;
    private String userAppId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentStar() {
        return currentStar;
    }

    public void setCurrentStar(int currentStar) {
        this.currentStar = currentStar;
    }

    public String getUserAppId() {
        return userAppId;
    }

    public void setUserAppId(String userAppId) {
        this.userAppId = userAppId;
    }

    public Coin() {
    }
}
