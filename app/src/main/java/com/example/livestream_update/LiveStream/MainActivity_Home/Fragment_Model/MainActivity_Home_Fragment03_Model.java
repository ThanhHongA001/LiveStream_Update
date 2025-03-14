package com.example.livestream_update.LiveStream.MainActivity_Home.Fragment_Model;

public class MainActivity_Home_Fragment03_Model {
    private String price;
    private String packId;
    private String details;
    private String validity;

    public MainActivity_Home_Fragment03_Model(String price, String packId, String details, String validity) {
        this.price = price;
        this.packId = packId;
        this.details = details;
        this.validity = validity;
    }

    public String getPrice() {
        return price;
    }

    public String getPackId() {
        return packId;
    }

    public String getDetails() {
        return details;
    }

    public String getValidity() {
        return validity;
    }
}
