package com.example.livestream_update.Ringme.Api;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BaseResponse implements Serializable {
    @SerializedName("code")
    private int code;
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("desc")
    private String desc;

    @SerializedName("errorCode")
    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public BaseResponse(String message, int errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponse() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

