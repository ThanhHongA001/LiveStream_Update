package com.example.livestream_update.Ringme.LiveStream.apis.response;

import com.google.gson.annotations.SerializedName;
import com.vtm.ringme.livestream.model.ActionLike;

public class LiveStreamLikeResponse extends BaseResponse {
    @SerializedName("data")
    private ActionLike data;

    public ActionLike getData() {
        return data;
    }

    public void setData(ActionLike data) {
        this.data = data;
    }
}
