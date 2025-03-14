package com.example.livestream_update.Ringme.Model.livestream;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ListResolution implements Serializable {

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("video_path")
    @Expose
    private String videoPath;
    @SerializedName("video_path_no_free_data")
    @Expose
    private String videoPathNoFreeData;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getVideoPathNoFreeData() {
        return videoPathNoFreeData;
    }

    public void setVideoPathNoFreeData(String videoPathNoFreeData) {
        this.videoPathNoFreeData = videoPathNoFreeData;
    }

}
