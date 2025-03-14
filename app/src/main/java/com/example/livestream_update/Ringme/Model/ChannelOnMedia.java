package com.example.livestream_update.Ringme.Model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by thanhnt72 on 4/11/2018.
 */

public class ChannelOnMedia implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("url_avatar")
    private String avatarUrl;

    @SerializedName("url_images_cover")
    private String coverUrl;

    @SerializedName("numVideo")
    private int numVideo;

    @SerializedName("isMyChannel")
    private int isMyChannel;

    @SerializedName("is_registered")
    private int isRegistered;

    @SerializedName("numfollow")
    private long numFollow;

    @SerializedName("categoryname")
    private String categoryName;

    @SerializedName("categoryid")
    private int categoryId;

    @SerializedName("is_follow")
    private int isFollow;

    @SerializedName("type")
    private int type;

    @SerializedName("hasFilmGroup")
    private int hasFilmGroup;

    @SerializedName("createdDate")
    private long createdDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        if (!TextUtils.isEmpty(name))
            return name;
        return categoryName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getNumVideo() {
        return numVideo;
    }

    public void setNumVideo(int numVideo) {
        this.numVideo = numVideo;
    }

    public int getIsMyChannel() {
        return isMyChannel;
    }

    public void setIsMyChannel(int isMyChannel) {
        this.isMyChannel = isMyChannel;
    }

    public int getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(int isRegistered) {
        this.isRegistered = isRegistered;
    }

    public long getNumFollow() {
        return numFollow;
    }

    public void setNumFollow(long numFollow) {
        this.numFollow = numFollow;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHasFilmGroup() {
        return hasFilmGroup;
    }

    public void setHasFilmGroup(int hasFilmGroup) {
        this.hasFilmGroup = hasFilmGroup;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public void setFollow(boolean isFollow) {
        this.isFollow = isFollow ? 1 : 0;
    }

    public boolean isFollow() {
        return isFollow == 1;
    }

    public void setMyChannel(boolean isMyChannel) {
        this.isMyChannel = isMyChannel ? 1 : 0;
    }

    public boolean isMyChannel() {
        return isMyChannel == 1;
    }

    public void setRegistered(boolean isRegistered) {
        this.isRegistered = isRegistered ? 1 : 0;
    }

    public boolean isRegistered() {
        return isRegistered == 1;
    }


    @Override
    public String toString() {
        return "ChannelOnMedia{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", numVideo=" + numVideo +
                ", isMyChannel=" + isMyChannel +
                ", isRegistered=" + isRegistered +
                ", numFollow=" + numFollow +
                ", categoryName='" + categoryName + '\'' +
                ", categoryId=" + categoryId +
                ", isFollow=" + isFollow +
                ", type=" + type +
                '}';
    }
}
