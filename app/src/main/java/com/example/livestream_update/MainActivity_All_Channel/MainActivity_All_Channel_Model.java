package com.example.livestream_update.MainActivity_All_Channel;

public class MainActivity_All_Channel_Model {
    private String userName;
    private String followersCount;

    public MainActivity_All_Channel_Model(String userName, String followersCount) {
        this.userName = userName;
        this.followersCount = followersCount;
    }

    public String getUserName() {
        return userName;
    }

    public String getFollowersCount() {
        return followersCount;
    }
}
