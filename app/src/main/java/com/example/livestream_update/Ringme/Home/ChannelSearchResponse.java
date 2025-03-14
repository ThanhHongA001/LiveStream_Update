package com.example.livestream_update.Ringme.Home;

import com.google.gson.annotations.SerializedName;
import com.vtm.ringme.model.tab_video.Channel;

import java.util.List;

public class ChannelSearchResponse {
    @SerializedName("data")
    private List<Channel> data;

    public List<Channel> getData() {
        return data;
    }

    public void setData(List<Channel> data) {
        this.data = data;
    }
}
