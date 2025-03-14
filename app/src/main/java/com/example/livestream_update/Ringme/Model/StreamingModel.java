package com.example.livestream_update.Ringme.Model;



import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by namnh40 on 3/12/2018.
 */

public class StreamingModel implements Serializable {

    @SerializedName("cache_path")
    private String fileName;
    @SerializedName("key")
    private String key;
    @SerializedName("video_path_no_free_data")
    private String urlNoFreeData;
    @SerializedName("video_path")
    private String urlFreeData;
    @SerializedName("title")
    private String title;

    private boolean isSelected;

    public StreamingModel() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrlNoFreeData() {
        return urlNoFreeData;
    }

    public void setUrlNoFreeData(String urlNoFreeData) {
        this.urlNoFreeData = urlNoFreeData;
    }

    public String getUrlFreeData() {
        return urlFreeData;
    }

    public void setUrlFreeData(String urlFreeData) {
        this.urlFreeData = urlFreeData;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "StreamingModel{" +
                "fileName='" + fileName + '\'' +
                ", key='" + key + '\'' +
                ", urlNoFreeData='" + urlNoFreeData + '\'' +
                ", urlFreeData='" + urlFreeData + '\'' +
                ", title='" + title + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
