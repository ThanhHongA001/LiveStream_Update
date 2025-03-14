package com.example.livestream_update.Ringme.Model.tab_video;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by tuanha00 on 3/7/2018.
 */

public class Category implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cateName")
    @Expose
    private String name;
    @SerializedName("iconImage")
    @Expose
    private String urlImage;
    private String contentType;
    // api mới trả về
    @SerializedName("headerBanner")
    private String headerBanner;
    @SerializedName("description")
    private String description;

    public String getHeaderBanner() {
        return headerBanner;
    }

    public void setHeaderBanner(String headerBanner) {
        this.headerBanner = headerBanner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
//
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
