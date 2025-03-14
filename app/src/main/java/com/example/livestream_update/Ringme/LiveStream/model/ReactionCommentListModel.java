package com.example.livestream_update.Ringme.LiveStream.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ReactionCommentListModel implements Serializable {
    @SerializedName("1")
    private int likeNumber = 0;
    @SerializedName("2")
    private int heartNumber = 0;
    @SerializedName("3")
    private int hahaNumber = 0;
    @SerializedName("4")
    private int wowNumber = 0;
    @SerializedName("5")
    private int sadNumber = 0;
    @SerializedName("6")
    private int angryNumber = 0;

    public int getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(int likeNumber) {
        this.likeNumber = likeNumber;
    }

    public int getHeartNumber() {
        return heartNumber;
    }

    public void setHeartNumber(int heartNumber) {
        this.heartNumber = heartNumber;
    }

    public int getHahaNumber() {
        return hahaNumber;
    }

    public void setHahaNumber(int hahaNumber) {
        this.hahaNumber = hahaNumber;
    }

    public int getWowNumber() {
        return wowNumber;
    }

    public void setWowNumber(int wowNumber) {
        this.wowNumber = wowNumber;
    }

    public int getSadNumber() {
        return sadNumber;
    }

    public void setSadNumber(int sadNumber) {
        this.sadNumber = sadNumber;
    }

    public int getAngryNumber() {
        return angryNumber;
    }

    public void setAngryNumber(int angryNumber) {
        this.angryNumber = angryNumber;
    }

    public ArrayList<ReactionCommentModel> getListReaction() {
        ArrayList<ReactionCommentModel> list = new ArrayList<>();
        if (likeNumber != 0) {
            list.add(new ReactionCommentModel("1", likeNumber));
        }
        if (heartNumber != 0) {
            list.add(new ReactionCommentModel("2", heartNumber));
        }
        if (hahaNumber != 0) {
            list.add(new ReactionCommentModel("3", hahaNumber));
        }
        if (wowNumber != 0) {
            list.add(new ReactionCommentModel("4", wowNumber));
        }
        if (sadNumber != 0) {
            list.add(new ReactionCommentModel("5", sadNumber));
        }
        if (angryNumber != 0) {
            list.add(new ReactionCommentModel("6", angryNumber));
        }
        return list;
    }
}
