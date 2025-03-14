package com.example.livestream_update.Ringme.LiveStream.network.parse;

import com.vtm.ringme.livestream.model.LiveStreamMessage;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RestLiveStreamMessage extends AbsResultData implements Serializable {

    private static final long serialVersionUID = 8409606354873458054L;

    @SerializedName("data")
    private LiveStreamMessage data;

    public LiveStreamMessage getData() {
        return data;
    }

    public void setData(LiveStreamMessage data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RestLiveStreamMessage [data=" + data + "] errror " + getErrorCode();
    }
}
