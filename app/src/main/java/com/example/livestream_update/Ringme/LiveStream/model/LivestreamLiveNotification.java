package com.example.livestream_update.Ringme.LiveStream.model;

public class LivestreamLiveNotification {
    public static String typeStop = "STOP";
    String roomId;
    String contentType;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
