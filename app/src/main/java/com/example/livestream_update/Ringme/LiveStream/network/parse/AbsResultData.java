package com.example.livestream_update.Ringme.LiveStream.network.parse;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AbsResultData implements Serializable {
    private static final long serialVersionUID = -5702141301177401541L;

    @SerializedName("code")
    private int errorCode;

    @SerializedName("desc")
    private String message;

    public int getErrorCode() {
        return errorCode;
    }

}