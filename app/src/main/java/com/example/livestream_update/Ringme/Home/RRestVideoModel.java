package com.example.livestream_update.Ringme.Home;

import com.google.gson.annotations.SerializedName;
import com.vtm.ringme.api.RAbsResultData;
import com.vtm.ringme.model.tab_video.Video;

import java.io.Serializable;
import java.util.ArrayList;

public class RRestVideoModel extends RAbsResultData implements Serializable {

    @SerializedName("data")
    private ArrayList<Video> data = new ArrayList<>();

    public ArrayList<Video> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "RestVideoModel{" +
                "data=" + data +
                '}';
    }
}