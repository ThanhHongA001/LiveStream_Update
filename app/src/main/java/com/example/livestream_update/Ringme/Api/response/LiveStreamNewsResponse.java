package com.example.livestream_update.Ringme.Api.response;

import com.vtm.ringme.livestream.apis.response.BaseResponse;
import com.vtm.ringme.model.LivestreamNews;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LiveStreamNewsResponse extends BaseResponse {
    @SerializedName("data")
    ArrayList<LivestreamNews> listNews;

    public ArrayList<LivestreamNews> getListNews() {
        return listNews;
    }
}
