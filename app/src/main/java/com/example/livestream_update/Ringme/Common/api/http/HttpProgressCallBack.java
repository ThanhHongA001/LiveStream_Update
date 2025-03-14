package com.example.livestream_update.Ringme.Common.api.http;

public abstract class HttpProgressCallBack extends HttpCallBack {
    public abstract void onProgressUpdate(int position, int sum, int percentage);
}
