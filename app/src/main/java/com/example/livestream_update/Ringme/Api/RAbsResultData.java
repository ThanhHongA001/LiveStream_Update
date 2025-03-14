package com.example.livestream_update.Ringme.Api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RAbsResultData implements Serializable {
    private static final long serialVersionUID = -5702141301177401541L;

    @SerializedName("code")
    private int code;

    @SerializedName("desc")
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess(){
        return  code == 200;
    }

    @Override
    public String toString() {
        return "AbsResultData{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
