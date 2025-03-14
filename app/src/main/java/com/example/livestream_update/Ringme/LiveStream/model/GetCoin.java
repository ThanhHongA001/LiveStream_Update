package com.example.livestream_update.Ringme.LiveStream.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetCoin implements Serializable {
    @SerializedName("level_star")
    private long number;
    private int price = (int) number;
    private boolean isSelected = false;

    public GetCoin(int number) {
        this.number = number;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getPrice() {
        return String.valueOf(price);
    }

    public void setPrice(String price) {
        this.price = Integer.parseInt(price);
    }

    public GetCoin(int number, String price) {
        this.number = number;
        this.price = Integer.parseInt(price);
        this.isSelected = false;
    }
}
