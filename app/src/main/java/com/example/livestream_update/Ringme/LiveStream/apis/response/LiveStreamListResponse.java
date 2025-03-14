package com.example.livestream_update.Ringme.LiveStream.apis.response;

import com.vtm.ringme.model.livestream.LivestreamModel;
import com.google.gson.annotations.SerializedName;


import java.io.Serializable;
import java.util.List;

public class LiveStreamListResponse extends BaseResponse implements Serializable {
    @SerializedName("data")
    private List<LivestreamModel> liveStreamVideos;

    public LiveStreamListResponse() {
    }

    public List<LivestreamModel> getLiveStreamVideos() {
        return liveStreamVideos;
    }

    public void setLiveStreamVideos(List<LivestreamModel> liveStreamVideos) {
        this.liveStreamVideos = liveStreamVideos;
    }
}
