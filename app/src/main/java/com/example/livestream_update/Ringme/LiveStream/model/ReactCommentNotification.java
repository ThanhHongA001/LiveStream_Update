package com.example.livestream_update.Ringme.LiveStream.model;

import com.vtm.ringme.livestream.model.ReactionCommentListModel;

public class ReactCommentNotification {
    private String cIdMessage;
    private String sidMessage;
    private String type;
    private String roomId;
    private String userId;
    private ReactionCommentListModel reacts;
    private String createdAt;
    private String action;
    private int numberReacts;

    public String getcIdMessage() {
        return cIdMessage;
    }

    public void setcIdMessage(String cIdMessage) {
        this.cIdMessage = cIdMessage;
    }

    public String getsIdMessage() {
        return sidMessage;
    }

    public void setsIdMessage(String sIdMessage) {
        this.sidMessage = sIdMessage;
    }

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

    public ReactionCommentListModel getReacts() {
        return reacts;
    }

    public void setReacts(ReactionCommentListModel reacts) {
        this.reacts = reacts;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getNumberReaction() {
        return numberReacts;
    }

    public void setNumberReaction(int numberReaction) {
        this.numberReacts = numberReaction;
    }
}
