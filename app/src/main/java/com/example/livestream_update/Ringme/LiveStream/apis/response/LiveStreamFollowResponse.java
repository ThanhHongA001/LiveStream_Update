package com.example.livestream_update.Ringme.LiveStream.apis.response;

import com.google.gson.annotations.SerializedName;
import com.vtm.ringme.livestream.model.FollowSuccess;

public class LiveStreamFollowResponse extends BaseResponse {
    @SerializedName("data")
    private FollowSuccess data;

    public FollowSuccess getData() {
        return data;
    }

    public void setData(FollowSuccess data) {
        this.data = data;
    }
}
