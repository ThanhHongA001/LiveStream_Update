package com.example.livestream_update.Ringme.LiveStream.apis.response;

import com.google.gson.annotations.SerializedName;
import com.vtm.ringme.livestream.model.Gift;

import java.io.Serializable;
import java.util.List;

public class LiveStreamGiftResponse extends BaseResponse implements Serializable {
    @SerializedName("data")
    private List<Gift> giftResponses;

    public List<Gift> getGiftResponses() {
        return giftResponses;
    }

    public void setGiftResponses(List<Gift> giftResponses) {
        this.giftResponses = giftResponses;
    }
}
