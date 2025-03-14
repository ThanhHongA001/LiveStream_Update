package com.example.livestream_update.LiveStream.MainActivity_List_LiveStream.Fragment_Model;

public class MainActivity_List_LiveStream_Fragment03_Model {
    private int imageResource;
    private String title;

    // Constructor
    public MainActivity_List_LiveStream_Fragment03_Model(int imageResource, String title) {
        this.imageResource = imageResource;
        this.title = title;
    }

    // Getter cho ImageView
    public int getImageResource() {
        return imageResource;
    }

    // Getter cho TextView
    public String getTitle() {
        return title;
    }
}
