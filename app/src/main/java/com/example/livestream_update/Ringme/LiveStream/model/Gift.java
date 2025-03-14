package com.example.livestream_update.Ringme.LiveStream.model;

public class Gift {
    private int id;
    private String name;
    private String avatar;
    private int amountStar;
    private boolean isChosen;

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getAmountStar() {
        return amountStar;
    }

    public void setAmountStar(int amountStar) {
        this.amountStar = amountStar;
    }
}

