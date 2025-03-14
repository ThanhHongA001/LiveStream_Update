package com.example.livestream_update.Ringme.Model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VideoMainHomeChannel implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("channelName")
    private String  channelName;
    @SerializedName("channelAvatar")
    private  String channelAvatar;
    @SerializedName("headerBanner")
    private String headerBanner;
    @SerializedName("description")
    private String description;
    @SerializedName("numFollows")
    private int numFollows;
    @SerializedName("numVideos")
    private int numVideos;
    @SerializedName("isOfficial")
    private int isOfficial;
    @SerializedName("createdFrom")
    private long createdFrom;
    @SerializedName("isFollow")
    private String isFollow;
    @SerializedName("isOwner")
    private int isOwner;
    @SerializedName("url")
    private String url;
    @SerializedName("state")
    private String state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getHeaderBanner() {
        return headerBanner;
    }

    public void setHeaderBanner(String headerBanner) {
        this.headerBanner = headerBanner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumFollows() {
        return numFollows;
    }

    public void setNumFollows(int numFollows) {
        this.numFollows = numFollows;
    }

    public int getNumVideos() {
        return numVideos;
    }

    public void setNumVideos(int numVideos) {
        this.numVideos = numVideos;
    }

    public int getIsOfficial() {
        return isOfficial;
    }

    public void setIsOfficial(int isOfficial) {
        this.isOfficial = isOfficial;
    }

    public long getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(long createdFrom) {
        this.createdFrom = createdFrom;
    }

    public String getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(String isFollow) {
        this.isFollow = isFollow;
    }

    public int getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(int isOwner) {
        this.isOwner = isOwner;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public VideoMainHomeChannel(int id, String channelName, String channelAvatar, int numFollows) {
        this.id = id;
        this.channelName = channelName;
        this.channelAvatar = channelAvatar;
        this.numFollows = numFollows;
    }

    public VideoMainHomeChannel() {
    }
}
