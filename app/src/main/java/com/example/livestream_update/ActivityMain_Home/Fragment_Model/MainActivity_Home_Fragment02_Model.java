package com.example.livestream_update.ActivityMain_Home.Fragment_Model;

public class MainActivity_Home_Fragment02_Model {
    private String title;
    private String viewCount;
    private int imageResource;

    public MainActivity_Home_Fragment02_Model(String title, String viewCount, int imageResource) {
        this.title = title;
        this.viewCount = viewCount;
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public String getViewCount() {
        return viewCount;
    }

    public int getImageResource() {
        return imageResource;
    }
}
