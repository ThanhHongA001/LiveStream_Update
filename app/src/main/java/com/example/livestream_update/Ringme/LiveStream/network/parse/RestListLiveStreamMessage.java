package com.example.livestream_update.Ringme.LiveStream.network.parse;

import com.google.gson.annotations.SerializedName;
import com.vtm.ringme.livestream.model.LiveStreamMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class RestListLiveStreamMessage extends AbsResultData implements Serializable {

    private static final long serialVersionUID = 8409606354873458054L;

    @SerializedName("data")
    private ArrayList<LiveStreamMessage> data = new ArrayList<>();

    @SerializedName("totalLike")
    private int totalLike;

    public ArrayList<LiveStreamMessage> getData() {
        return data;
    }

    public void setData(ArrayList<LiveStreamMessage> data) {
        this.data = data;
    }

    public int getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    @Override
    public String toString() {
        return "RestListLiveStreamMessage [data=" + data + "] errror " + getErrorCode();
    }
}
