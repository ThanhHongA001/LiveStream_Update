package com.example.livestream_update.Ringme.Model.tab_video;

import java.io.Serializable;

public class Resolution implements Serializable, Comparable<Resolution> {

    private String key = "";
    private String title = "";
    private String video_path = "";

    public Resolution() {
    }

    public Resolution(String key, String title, String video_path) {
        this.key = key;
        this.title = title;
        this.video_path = video_path;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String type) {
        this.title = type;
    }

    public String getVideoPath() {
        return video_path;
    }

    public void setVideoPath(String video_path) {
        this.video_path = video_path;
    }

    @Override
    public int compareTo(Resolution o) {
        return this.key.compareTo(o.getKey());
    }
}
