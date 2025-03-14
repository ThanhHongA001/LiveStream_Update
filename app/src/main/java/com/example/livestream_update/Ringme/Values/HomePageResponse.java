package com.example.livestream_update.Ringme.Values;

import com.google.gson.annotations.SerializedName;
import com.vtm.ringme.api.BaseResponse;
import com.vtm.ringme.model.HomePage;

import java.util.ArrayList;

public class HomePageResponse extends BaseResponse {
    @SerializedName("data")
    ArrayList<HomePage> data;

    public ArrayList<HomePage> getData() {
        if(data == null) {
            data = new ArrayList<>();
        }
        return data;
    }

    public void setData(ArrayList<HomePage> data) {
        this.data = data;
    }
}

