package com.example.livestream_update.Ringme.Api.response;

import com.vtm.ringme.livestream.apis.response.BaseResponse;
import com.google.gson.annotations.SerializedName;

public class VoteResultResponse extends BaseResponse {
    @SerializedName("data")
    private boolean data;

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }
}

