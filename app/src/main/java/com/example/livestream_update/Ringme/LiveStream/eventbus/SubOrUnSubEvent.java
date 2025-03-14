package com.example.livestream_update.Ringme.LiveStream.eventbus;

public class SubOrUnSubEvent {
    private boolean isSuccess;

    public SubOrUnSubEvent(boolean b) {
        this.isSuccess = b;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
