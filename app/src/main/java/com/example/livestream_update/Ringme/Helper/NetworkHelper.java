package com.example.livestream_update.Ringme.Helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.vtm.ringme.utils.Log;

/**
 * Created by ThaoDV on 6/13/14.
 */
public class NetworkHelper {
    private static final String TAG = NetworkHelper.class.getSimpleName();
    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 0;
    public static final int TYPE_NOT_CONNECTED = 2;
    private int connectedType = -1;
    private static boolean mIsNetworkAvailable = false;
    private static NetworkHelper instance;

    private NetworkHelper() {

    }

    public static synchronized NetworkHelper getInstance() {
        if (instance == null) {
            instance = new NetworkHelper();
        }
        return instance;
    }



    public static boolean isConnectInternet(Context context) {
        NetworkInfo i = null;
        try {
            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            i = conMgr.getActiveNetworkInfo();
            /*final android.net.NetworkInfo wifi = conMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            final android.net.NetworkInfo mobile = conMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);*/
        } catch (Exception e) {
            Log.e(TAG, "isConnectInternet", e);
        }
        if (i != null) {
            Log.d(TAG, "isConnectInternet: " + i.isConnected() + " --- type: " + i.getType()
                    + " *** " + i.getTypeName());
        }
        return i != null && i.isConnected() && i.isAvailable();
    }




    public static int checkTypeConnection(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }


    public static String getNetworkSubType(Context context) {
        String networkType = "";
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                networkType = "WIFI";
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                networkType = activeNetwork.getSubtypeName();
            }
        }
        return networkType;
    }

    public interface NetworkChangedCallback {
        void onNetworkChanged(boolean isNetworkAvailable);
    }
}