package com.example.livestream_update.Ringme.LiveStream.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LivestreamVoteOptionModel implements Serializable {
    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;
    @SerializedName("des")
    private String des;
    @SerializedName("select")
    private boolean select;
    @SerializedName("numberVote")
    private long numberVote;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public long getNumberVote() {
        return numberVote;
    }

    public void setNumberVote(long numberVote) {
        this.numberVote = numberVote;
    }
}
