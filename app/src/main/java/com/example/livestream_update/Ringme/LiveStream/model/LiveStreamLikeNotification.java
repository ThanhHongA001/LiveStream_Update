package com.example.livestream_update.Ringme.LiveStream.model;

import java.io.Serializable;

public class LiveStreamLikeNotification implements Serializable {
    private int reactId;
    private String roomID;
    private int totalLike;
    private String cidMessage;
    private String userId;

    public int getType() {
        return reactId;
    }

    public void setType(int type) {
        this.reactId = type;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public int getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    public String getCidMessage() {
        return cidMessage;
    }

    public void setCidMessage(String cidMessage) {
        this.cidMessage = cidMessage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
