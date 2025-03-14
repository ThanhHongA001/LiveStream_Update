package com.example.livestream_update.Ringme.Api;


import com.google.gson.annotations.SerializedName;
import com.vtm.ringme.api.RAbsResultData;
import com.vtm.ringme.model.tab_video.Channel;

import java.io.Serializable;

public class RRestChannelInfoModel extends RAbsResultData implements Serializable {

    @SerializedName("data")
    private Channel data;

    public Channel getData() {
        return data;
    }

    @Override
    public String toString() {
        return "RRestChannelInfoModel{" +
                "data=" + data.toString() +
                "code=" + getCode() +
                ", message='" + getMessage() + '\'' +
                '}';
    }
}