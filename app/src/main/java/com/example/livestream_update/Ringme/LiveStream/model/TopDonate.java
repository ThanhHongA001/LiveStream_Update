package com.example.livestream_update.Ringme.LiveStream.model;

import com.google.gson.annotations.SerializedName;

public class TopDonate {
//     "id": 1,
//             "userId": "+67075555555",
//             "name": "test",
//             "avatar": "test",
//             "channelId": 1,
//             "livestreamId": 1,
//             "totalStar": 1

    @SerializedName("name")
    String name;
    int rank;
    @SerializedName("avatar")
    String avatar;
    @SerializedName("totalStar")
    long starNumber;
    @SerializedName("userId")
    String userId;
    @SerializedName("channelId")
    long channelId;
    @SerializedName("livestreamId")
    long livestreamId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getStarNumber() {
        return starNumber;
    }

    public void setStarNumber(long starNumber) {
        this.starNumber = starNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getChannelId() {
        return channelId;
    }

    public long getLivestreamId() {
        return livestreamId;
    }
}
