package com.example.livestream_update.Ringme.Event;

public class BacktoChannelEvent {
    boolean isCheck;

    public BacktoChannelEvent(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
