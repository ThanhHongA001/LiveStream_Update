package com.example.livestream_update.Ringme.LiveStream.model;

import java.io.Serializable;

public class ActionLike implements Serializable {
    private String id;
    private String userId;
    private int livestreamId;
    private boolean status;
    private String upsertAt;
    private int totalLike;

    public int getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getLivestreamId() {
        return livestreamId;
    }

    public void setLivestreamId(int livestreamId) {
        this.livestreamId = livestreamId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getUpsertAt() {
        return upsertAt;
    }

    public void setUpsertAt(String upsertAt) {
        this.upsertAt = upsertAt;
    }
}

