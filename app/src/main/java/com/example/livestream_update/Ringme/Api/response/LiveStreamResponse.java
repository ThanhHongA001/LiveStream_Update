package com.example.livestream_update.Ringme.Api.response;

import com.vtm.ringme.livestream.apis.response.BaseResponse;
import com.vtm.ringme.model.livestream.LivestreamModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LiveStreamResponse extends BaseResponse {
    @SerializedName("data")
    ArrayList<LivestreamModel> listLivStream;

    public ArrayList<LivestreamModel> getListLivStream() {
        if(listLivStream == null) {
            listLivStream = new ArrayList<>();
        }
        return listLivStream;
    }
}
