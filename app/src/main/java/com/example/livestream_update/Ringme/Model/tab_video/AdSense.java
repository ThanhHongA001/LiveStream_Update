package com.example.livestream_update.Ringme.Model.tab_video;

import com.google.gson.annotations.SerializedName;
import com.vtm.ringme.utils.Utilities;

import java.io.Serializable;

public class AdSense implements Serializable {
    @SerializedName("ads_type")
    private String typeAds;
    @SerializedName("url_adsense")
    private String urlAds;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("spotx_channel_id")
    private String spotXChannelId;
    @SerializedName("spotx_api_key")
    private String spotXApiKey;
    @SerializedName("token")
    private String token;

    public AdSense() {
    }

    public boolean isAds() {
        return "ads".equals(typeAds) && Utilities.notEmpty(urlAds) && Utilities.notEmpty(startTime);
    }

    public boolean isVideo() {
        return "video".equals(typeAds) && Utilities.notEmpty(typeAds) && Utilities.notEmpty(startTime);
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getStartTimeInt() {
        try {
            return Integer.parseInt(startTime);
        } catch (Exception e) {
        }
        return -1;
    }

    public String getSpotXChannelId() {
        return spotXChannelId;
    }

    public String getSpotXApiKey() {
        return spotXApiKey;
    }

    public String getToken() {
        if (token == null) token = "";
        return token;
    }

    @Override
    public String toString() {
        return "AdSense{" +
                "typeAds='" + typeAds + '\'' +
                ", urlAds='" + urlAds + '\'' +
                ", startTime='" + startTime + '\'' +
                ", spotXChannelId='" + spotXChannelId + '\'' +
                ", spotXApiKey='" + spotXApiKey + '\'' +
                '}';
    }
}
