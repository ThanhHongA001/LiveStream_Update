package com.example.livestream_update.LiveStream.MainActivity_Channel_Detail;

public class MainActivity_Channel_Detail_Fragment02_Model {
    private String title;
    private String description;
    private String time;
    private int imageResource;

    public MainActivity_Channel_Detail_Fragment02_Model(String title, String description, String time, int imageResource) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public int getImageResource() {
        return imageResource;
    }
}
