package com.example.livestream_update.Ringme.Helper;


import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;

import com.vtm.ringme.ApplicationController;

/**
 * Created by thanhnt72 on 5/17/2016.
 */
public class EventOnMediaHelper {

    private static final String TAG = EventOnMediaHelper.class.getSimpleName();
    private static final String PREFIX_HTTP = "http";
    private static final String ADD_PREFIX = "://";
    private static EventOnMediaHelper mInstance;

    public AppCompatActivity mParentActivity;
    public ApplicationController mApplication;
    public Resources mRes;

    public EventOnMediaHelper(AppCompatActivity activity) {
        mParentActivity = activity;
        mApplication = (ApplicationController) mParentActivity.getApplication();
        mRes = mApplication.getResources();
    }

    public static synchronized EventOnMediaHelper getInstance(AppCompatActivity activity) {
        if (mInstance == null) {
            mInstance = new EventOnMediaHelper(activity);
        }
        return mInstance;
    }









    public interface ShareLinkResponse {
        void onSuccess();
    }
}
