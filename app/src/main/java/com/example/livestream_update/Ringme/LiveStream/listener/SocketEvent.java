package com.example.livestream_update.Ringme.LiveStream.listener;

public class SocketEvent {
    public static final int TYPE_NONE = 0;
    public static final int TYPE_OPEN = 1;
    public static final int TYPE_SUBSCRIBE = 2;
    public static final int TYPE_CLOSE = 3;
    public static final int TYPE_ERROR = 4;

    private int type = TYPE_NONE;

    public SocketEvent() {
    }

    public SocketEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
