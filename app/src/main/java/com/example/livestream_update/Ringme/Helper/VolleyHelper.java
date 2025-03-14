package com.example.livestream_update.Ringme.Helper;


import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.BuildConfig;
import com.vtm.ringme.utils.Utilities;
import com.vtm.ringme.values.Constants;


public class VolleyHelper {
    private static final String TAG = VolleyHelper.class.getSimpleName();
    private static VolleyHelper mInstance = null;
    private ApplicationController mApp;
    private RequestQueue mRequestQueue;
    private RetryPolicy mRetryPolicy;

    public static synchronized VolleyHelper getInstance(ApplicationController context) {
        if (mInstance == null) {
            mInstance = new VolleyHelper(context);
            VolleyLog.DEBUG = BuildConfig.DEBUG;
        }
        return mInstance;
    }

    public VolleyHelper(ApplicationController context) {
        mApp = context;
        mRequestQueue = Volley.newRequestQueue(mApp, new VolleyOkHttpStack(mApp));
        mRetryPolicy = new DefaultRetryPolicy(Constants.VOLLEY.VOLLEY_TIMEOUT,
                Constants.VOLLEY.VOLLEY_MAX_NUMBER_RETRY, Constants.VOLLEY.VOLLEY_BACK_OFF_MULTIPLER);
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mApp, new VolleyOkHttpStack(mApp));
        }
        return mRequestQueue;
    }

    public <T> void addRequestToQueue(Request<T> request, String tag, boolean shouldCache) {
        Utilities.addDefaultParamsRequestVolley(request);
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        request.setRetryPolicy(mRetryPolicy);
        request.setShouldCache(shouldCache);        // co dung cache hay khong
        getRequestQueue().add(request);
    }
}