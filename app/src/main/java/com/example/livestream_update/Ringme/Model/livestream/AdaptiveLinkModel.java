package com.example.livestream_update.Ringme.Model.livestream;

import com.blankj.utilcode.util.StringUtils;
import com.google.gson.annotations.SerializedName;
import com.vtm.ringme.model.tab_video.Resolution;

import java.io.Serializable;
import java.util.ArrayList;

public class AdaptiveLinkModel implements Serializable {
    @SerializedName("240")
    private String link240;
    @SerializedName("360")
    private String link360;
    @SerializedName("480")
    private String link480;
    @SerializedName("720")
    private String link720;
    @SerializedName("1080")
    private String link1080;
    @SerializedName("src")
    private String linkSrc;

    public String getLink240() {
        return link240;
    }

    public String getLink360() {
        return link360;
    }

    public String getLink480() {
        return link480;
    }

    public String getLink720() {
        return link720;
    }

    public void setLinkSrc(String linkSrc) {
        this.linkSrc = linkSrc;
    }

    public String getLink1080() {
        return link1080;
    }

    public ArrayList<Resolution> setListResolution() {
        ArrayList<Resolution> list = new ArrayList<>();
        if (!StringUtils.isEmpty(link240))
            list.add(new Resolution("240", "240p", link240));
        if (!StringUtils.isEmpty(link240))
            list.add(new Resolution("360", "360p", link360));
        if (!StringUtils.isEmpty(link240))
            list.add(new Resolution("480", "480p", link480));
        if (!StringUtils.isEmpty(link240))
            list.add(new Resolution("720", "720p", link720));
        if (!StringUtils.isEmpty(link240))
            list.add(new Resolution("1080", "1080p", link1080));
        if (!StringUtils.isEmpty(link240))
            list.add(new Resolution("auto", "auto", linkSrc));
        return list;
    }
}
