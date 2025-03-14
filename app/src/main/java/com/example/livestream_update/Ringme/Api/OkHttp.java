package com.example.livestream_update.Ringme.Api;


import com.vtm.ringme.utils.Log;

import okhttp3.OkHttpClient;


public class OkHttp {
    private static final String TAG = OkHttp.class.getSimpleName();
    private static OkHttpClient okHttpClient;

    static {
        Log.i(TAG, "init OkHttp ");
        okHttpClient = new OkHttpClient.Builder().build();
    }

    //private static OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    public static OkHttpClient getClient() {
        return okHttpClient;
    }

    public static synchronized OkHttpClient reCreateClient() {
        okHttpClient = new OkHttpClient.Builder().build();
        return okHttpClient;
    }
}