package com.example.livestream_update.Ringme.LiveStream.model;

import java.io.Serializable;

public class LiveStreamGiftMessage implements Serializable {
    private String type;
    private String roomId;
    private String userId;
    private String userName;
    private String avatar;
    private String giftId;
    private String giftImg;
    private String amountStar;
    private String createdAt;
    private String cidMessage;
    private String giftName;
    private String message;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGiftImg() {
        return giftImg;
    }

    public void setGiftImg(String giftImg) {
        this.giftImg = giftImg;
    }

    public String getAmountStar() {
        return amountStar;
    }

    public void setAmountStar(String amountStar) {
        this.amountStar = amountStar;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCidMessage() {
        return cidMessage;
    }

    public void setCidMessage(String cidMessage) {
        this.cidMessage = cidMessage;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public LiveStreamGiftMessage(String type, String roomId, String userId, String userName, String avatar, String giftId, String giftImg, String amountStar, String createdAt, String cidMessage) {
        this.type = type;
        this.roomId = roomId;
        this.userId = userId;
        this.userName = userName;
        this.avatar = avatar;
        this.giftId = giftId;
        this.giftImg = giftImg;
        this.amountStar = amountStar;
        this.createdAt = createdAt;
        this.cidMessage = cidMessage;
    }

    public LiveStreamGiftMessage() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
