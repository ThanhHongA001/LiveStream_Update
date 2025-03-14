package com.example.livestream_update.Ringme.LiveStream.listener;

public interface NetworkConnectivityChangeListener {
    void onConnectivityChanged(boolean isNetworkAvailable, int connectedType);
}