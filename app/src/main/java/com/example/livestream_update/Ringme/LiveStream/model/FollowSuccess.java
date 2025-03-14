package com.example.livestream_update.Ringme.LiveStream.model;

public class FollowSuccess {
    private String id;
    private String userId;
    private int streamerId;
    private boolean status;
    private int totalLike;
    private String upsertAt;

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

    public int getStreamerId() {
        return streamerId;
    }

    public void setStreamerId(int streamerId) {
        this.streamerId = streamerId;
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

    public int getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }
}
