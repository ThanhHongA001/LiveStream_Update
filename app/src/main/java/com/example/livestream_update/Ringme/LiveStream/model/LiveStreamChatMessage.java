package com.example.livestream_update.Ringme.LiveStream.model;

import com.vtm.ringme.livestream.model.ReactionCommentListModel;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class LiveStreamChatMessage implements Serializable {
    private String msgBody;
    private String type;
    private String userId;
    private String userName;
    private String cIdMessage;
    private String sidMessage;
    private String roomID;
    private String avatar;
    private String createdAt;
    private String cmsgId;
    private String smsgId;
    private String donateFrom;

    private String donateTo;

    private boolean status;

    private String message;

    private String alert;

    @SerializedName("reacts")
    private ReactionCommentListModel reactions;
    private int numberReaction = 0;
    private long timestamp;

    public String getCmsgId() {
        return cmsgId;
    }

    public void setCmsgId(String cmsgId) {
        this.cmsgId = cmsgId;
    }

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
        return sidMessage;
    }

    public void setsIdMessage(String sIdMessage) {
        this.sidMessage = sIdMessage;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public LiveStreamChatMessage() {
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }


    public static LiveStreamChatMessage convert(LiveStreamChatMessage chatMessageResponse){
        chatMessageResponse.setMsgBody(chatMessageResponse.getMsgBody().replace("[DEMO] SEND FROM SERVER:","").trim());
        return chatMessageResponse;
    }

    public String getSmsgId() {
        return smsgId;
    }

    public void setSmsgId(String smsgId) {
        this.smsgId = smsgId;
    }

    public ReactionCommentListModel getReactions() {
        return reactions;
    }

    public void setReactions(ReactionCommentListModel reactions) {
        this.reactions = reactions;
    }

    public int getNumberReaction() {
        return numberReaction;
    }

    public void setNumberReaction(int numberReaction) {
        this.numberReaction = numberReaction;
    }

    public String getDonateFrom() {
        return donateFrom;
    }

    public void setDonateFrom(String donateFrom) {
        this.donateFrom = donateFrom;
    }

    public String getDonateTo() {
        return donateTo;
    }

    public void setDonateTo(String donateTo) {
        this.donateTo = donateTo;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
