package com.example.livestream_update.Ringme.LiveStream.network;

import retrofit2.Response;

public interface APICallBack<T> {
    void onResponse(Response<T> response);

    void onError(Throwable error);
}
