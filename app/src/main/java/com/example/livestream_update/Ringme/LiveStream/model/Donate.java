package com.example.livestream_update.Ringme.LiveStream.model;

import com.google.gson.annotations.SerializedName;

public class Donate {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String image;
    @SerializedName("amountStar")
    private double amountStar;
    private boolean selected;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public double getAmountStar() {
        return amountStar;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

