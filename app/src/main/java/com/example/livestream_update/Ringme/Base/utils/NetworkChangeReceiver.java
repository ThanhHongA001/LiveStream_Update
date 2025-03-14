package com.example.livestream_update.Ringme.Base.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by huongnd38 on 10/12/17.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    public interface NetworkStateListener {
        void onNetworkStateChange(boolean hasConnect);
    }

    Context mContext;
    boolean mHasConnection;

    public NetworkChangeReceiver() {

    }

    public void unregister() {
        if (mContext != null) {
            try {
                mContext.unregisterReceiver(this);
            } catch (Exception ex) {
                Log.e("NetworkChangeReceiver", "unregister has exception: " + ex);
            }
        }
        mContext = null;
    }

    public boolean hasConnection() {
        if (mContext != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null) return false;
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        } else {
            return false;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean currentStatus = hasConnection();
        if (currentStatus == mHasConnection) return; //no change
        mHasConnection = currentStatus;
        if (mContext != null && mContext instanceof NetworkStateListener) {
            ((NetworkStateListener) mContext).onNetworkStateChange(mHasConnection);
        }
    }
}