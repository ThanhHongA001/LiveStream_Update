package com.example.livestream_update.Ringme.Model;

import com.google.gson.annotations.SerializedName;
import com.vtm.ringme.model.livestream.LivestreamModel;

import java.io.Serializable;
import java.util.ArrayList;

public class HomePage implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("category")
    private String category;
    @SerializedName("priority")
    private int priority;

    private ArrayList<LivestreamModel> listLive;


    public ArrayList<LivestreamModel> getListLive() {
        return listLive;
    }


    public HomePage(int id, String category, int priority) {
        this.id = id;
        this.category = category;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setListLive(ArrayList<LivestreamModel> listLive) {
        this.listLive = listLive;
    }

}
