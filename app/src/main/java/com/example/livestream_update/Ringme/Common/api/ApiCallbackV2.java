package com.example.livestream_update.Ringme.Common.api;

import org.json.JSONException;

public interface ApiCallbackV2<T> extends ApiCallback {
    void onSuccess(String msg, T result) throws JSONException;
}