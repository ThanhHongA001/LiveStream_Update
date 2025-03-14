package com.example.livestream_update.Ringme.Api.response;

import com.vtm.ringme.api.BaseResponse;
import com.vtm.ringme.livestream.model.TopDonate;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TopDonateResponse extends BaseResponse {
    @SerializedName("data")
    ArrayList<TopDonate> data;

    public ArrayList<TopDonate> getData() {
        return data;
    }
}
