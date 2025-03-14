package com.example.livestream_update.Ringme.LiveStream.socket;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class ChatMessageRequest implements Serializable {
    private String msgBody;
    private String type;
    private String userId;
    private String userName;
    private String cIdMessage;
    private String sIdMessage;
    private String roomID;
    private String avatar;
    private Long createdAt;
    private final String streamer = "0";

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getcIdMessage() {
        return cIdMessage;
    }

    public void setcIdMessage(String cIdMessage) {
        this.cIdMessage = cIdMessage;
    }

    public String getsIdMessage() {
        return sIdMessage;
    }

    public void setsIdMessage(String sIdMessage) {
        this.sIdMessage = sIdMessage;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public ChatMessageRequest() {
    }

    @Override
    public String toString() {
        try {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
