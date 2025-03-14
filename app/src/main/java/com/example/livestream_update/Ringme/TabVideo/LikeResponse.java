package com.example.livestream_update.Ringme.TabVideo;

import com.google.gson.annotations.SerializedName;
import com.vtm.ringme.api.BaseResponse;

public class LikeResponse extends BaseResponse {
    @SerializedName("data")
    String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
