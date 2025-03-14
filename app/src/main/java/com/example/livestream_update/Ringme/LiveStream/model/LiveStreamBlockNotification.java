package com.example.livestream_update.Ringme.LiveStream.model;

import java.io.Serializable;

public class LiveStreamBlockNotification implements Serializable {
    private String type;
    private String streamerId;
    private String userId;
    private String roomID;
    private String blockId;
    private String blockType;
    private String cidMessage;
    private String chatId;

    public String getStreamerId() {
        return streamerId;
    }

    public void setStreamerId(String streamerId) {
        this.streamerId = streamerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getBlockType() {
        return blockType;
    }

    public void setBlockType(String blockType) {
        this.blockType = blockType;
    }

    public String getCidMessage() {
        return cidMessage;
    }

    public void setCidMessage(String cidMessage) {
        this.cidMessage = cidMessage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
