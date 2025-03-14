package com.example.livestream_update.Ringme.LiveStream.model;

import com.google.gson.annotations.SerializedName;
import com.vtm.ringme.livestream.model.LivestreamVoteOptionModel;

import java.io.Serializable;
import java.util.ArrayList;

public class LivestreamVoteModel implements Serializable {
    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;
    @SerializedName("timeEnd")
    private long timeEnd;
    @SerializedName("createdDate")
    private long createdDate;
    @SerializedName("modifiedDate")
    private long modifiedDate;
    @SerializedName("voteDtos")
    private ArrayList<LivestreamVoteOptionModel> voteDtos;

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

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public ArrayList<LivestreamVoteOptionModel> getVoteDtos() {
        return voteDtos;
    }

    public void setVoteDtos(ArrayList<LivestreamVoteOptionModel> voteDtos) {
        this.voteDtos = voteDtos;
    }
}
