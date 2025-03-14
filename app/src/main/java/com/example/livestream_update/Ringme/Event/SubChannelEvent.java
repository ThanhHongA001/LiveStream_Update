package com.example.livestream_update.Ringme.Event;

public class SubChannelEvent {
    private boolean isCheck;

    public SubChannelEvent(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
