package com.example.livestream_update.Ringme.LiveStream.apis.response;

import com.google.gson.annotations.SerializedName;
import com.vtm.ringme.livestream.model.DonateSuccess;

public class LiveStreamDonateResponse extends BaseResponse {
    public DonateSuccess getReturnData() {
        return returnData;
    }

    public void setReturnData(DonateSuccess returnData) {
        this.returnData = returnData;
    }

    @SerializedName("data")
    private DonateSuccess returnData;
}
