package com.example.livestream_update.Ringme.Common.api.http;

/**
 * Created by tuanha00 on 2/7/2018.
 */


public abstract class HttpCallBack {
    public abstract void onSuccess(String data) throws Exception;

    public void onFailure(String message) {
    }

    public void onCompleted() {
    }
}

