package com.example.livestream_update.Ringme.LiveStream.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveStreamCommentModel {
    @SerializedName("site")
    @Expose
    private String site;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("item_type")
    @Expose
    private String itemType;
    @SerializedName("content_url")
    @Expose
    private String contentUrl;
    @SerializedName("msisdn")
    @Expose
    private String msisdn;
    @SerializedName("action_type")
    @Expose
    private String actionType;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("img_url")
    @Expose
    private String imgUrl;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("user_type")
    @Expose
    private int userType;
    @SerializedName("post_action_from")
    @Expose
    private String postActionFrom;
    @SerializedName("stampId_of_url")
    @Expose
    private int stampIdOfUrl;
    @SerializedName("stampId")
    @Expose
    private long stampId;
    @SerializedName("content_action")
    @Expose
    private String contentAction;
    @SerializedName("channel_id")
    @Expose
    private String channelId;
    @SerializedName("channel_name")
    @Expose
    private String channelName;
    @SerializedName("channel_avatar")
    @Expose
    private String channelAvatar;
    @SerializedName("url_temp")
    @Expose
    private String urlTemp;
    @SerializedName("numfollow")
    @Expose
    private int numfollow;

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getPostActionFrom() {
        return postActionFrom;
    }

    public void setPostActionFrom(String postActionFrom) {
        this.postActionFrom = postActionFrom;
    }

    public int getStampIdOfUrl() {
        return stampIdOfUrl;
    }

    public void setStampIdOfUrl(int stampIdOfUrl) {
        this.stampIdOfUrl = stampIdOfUrl;
    }

    public long getStampId() {
        return stampId;
    }

    public void setStampId(long stampId) {
        this.stampId = stampId;
    }

    public String getContentAction() {
        return contentAction;
    }

    public void setContentAction(String contentAction) {
        this.contentAction = contentAction;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelAvatar() {
        return channelAvatar;
    }

    public void setChannelAvatar(String channelAvatar) {
        this.channelAvatar = channelAvatar;
    }

    public String getUrlTemp() {
        return urlTemp;
    }

    public void setUrlTemp(String urlTemp) {
        this.urlTemp = urlTemp;
    }

    public int getNumfollow() {
        return numfollow;
    }

    public void setNumfollow(int numfollow) {
        this.numfollow = numfollow;
    }

    @Override
    public String toString() {
        return "LiveStreamCommentModel{" +
                "site='" + site + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemType='" + itemType + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                ", msisdn='" + msisdn + '\'' +
                ", actionType='" + actionType + '\'' +
                ", url='" + url + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", status='" + status + '\'' +
                ", userType=" + userType +
                ", postActionFrom='" + postActionFrom + '\'' +
                ", stampIdOfUrl=" + stampIdOfUrl +
                ", stampId=" + stampId +
                ", contentAction='" + contentAction + '\'' +
                ", channelId='" + channelId + '\'' +
                ", channelName='" + channelName + '\'' +
                ", channelAvatar='" + channelAvatar + '\'' +
                ", urlTemp='" + urlTemp + '\'' +
                ", numfollow=" + numfollow +
                '}';
    }
}
