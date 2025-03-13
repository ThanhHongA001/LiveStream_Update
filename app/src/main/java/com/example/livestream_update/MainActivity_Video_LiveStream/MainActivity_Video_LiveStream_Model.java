package com.example.livestream_update.MainActivity_Video_LiveStream;

public class MainActivity_Video_LiveStream_Model {
    private String title;
    private String description;
    private String username;
    private String timeAgo;

    public MainActivity_Video_LiveStream_Model(String title, String description, String username, String timeAgo) {
        this.title = title;
        this.description = description;
        this.username = username;
        this.timeAgo = timeAgo;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }

    public String getTimeAgo() {
        return timeAgo;
    }
}
