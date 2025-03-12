package com.example.livestream_update.ActivityMain_Home.Fragment_Model;

public class MainActivity_Home_Fragment04_Model {
    private int imageResource;
    private String livestreamName;
    private int viewCount;

    public MainActivity_Home_Fragment04_Model(int imageResource, String livestreamName, int viewCount) {
        this.imageResource = imageResource;
        this.livestreamName = livestreamName;
        this.viewCount = viewCount;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getLivestreamName() {
        return livestreamName;
    }

    public int getViewCount() {
        return viewCount;
    }
}
