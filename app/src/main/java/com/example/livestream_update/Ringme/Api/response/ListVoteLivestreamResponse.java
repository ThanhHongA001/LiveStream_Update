package com.example.livestream_update.Ringme.Api.response;


import com.vtm.ringme.livestream.apis.response.BaseResponse;
import com.vtm.ringme.livestream.model.LivestreamVoteModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListVoteLivestreamResponse extends BaseResponse {
    @SerializedName("data")
    private ArrayList<LivestreamVoteModel> data;

    public ArrayList<LivestreamVoteModel> getData() {
        return data;
    }

    public void setData(ArrayList<LivestreamVoteModel> data) {
        this.data = data;
    }
}
