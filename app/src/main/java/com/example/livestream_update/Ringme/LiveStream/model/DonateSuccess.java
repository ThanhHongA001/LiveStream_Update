package com.example.livestream_update.Ringme.LiveStream.model;

public class DonateSuccess {
    private long timeDone;
    private int streamerId;
    private int amountSuccess;
    private int currentAmountStar;
    private String userId;

    public long getTimeDone() {
        return timeDone;
    }

    public void setTimeDone(long timeDone) {
        this.timeDone = timeDone;
    }

    public int getStreamerId() {
        return streamerId;
    }

    public void setStreamerId(int streamerId) {
        this.streamerId = streamerId;
    }

    public int getAmountSuccess() {
        return amountSuccess;
    }

    public void setAmountSuccess(int amountSuccess) {
        this.amountSuccess = amountSuccess;
    }

    public int getCurrentAmountStar() {
        return currentAmountStar;
    }

    public void setCurrentAmountStar(int currentAmountStar) {
        this.currentAmountStar = currentAmountStar;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
