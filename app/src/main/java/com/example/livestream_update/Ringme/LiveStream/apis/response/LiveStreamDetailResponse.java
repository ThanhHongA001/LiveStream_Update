package com.example.livestream_update.Ringme.LiveStream.apis.response;

import com.vtm.ringme.api.BaseResponse;
import com.vtm.ringme.model.livestream.LivestreamModel;
import com.google.gson.annotations.SerializedName;

public class LiveStreamDetailResponse extends BaseResponse {
    @SerializedName("data")
    private LivestreamModel video;

    public LivestreamModel getVideo() {
        return video;
    }

    public void setVideo(LivestreamModel video) {
        this.video = video;
    }
}
