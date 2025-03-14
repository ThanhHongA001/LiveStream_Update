package com.example.livestream_update.Ringme.Helper;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.utils.CommonUtils;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.values.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by toanvk2 on 11/14/2015.
 */
public class ReportHelper {
    private static final String TAG = ReportHelper.class.getSimpleName();
    public static final String DATA_TYPE_TOGHETHER_MUSIC = "TOGHETHER_MUSIC";
    public static final String DATA_TYPE_TAB_CONFIG = "TAB_CONFIG";

    public static final String DATA_TYPE_NEWS_HOME = "NEWS_HOME";
    public static final String DATA_TYPE_NEWS_DETAIL = "NEWS_DETAIL";
    public static final String VIDEO_SERVICE_ERROR = "VIDEO_SERVICE_ERROR";



    public static final String DATA_TYPE_LIVE_STREAM_MESSAGE = "LIVE_STREAM_MESSAGE";
    public static final String DATA_TYPE_LIVE_STREAM_SCREEN = "LIVE_STREAM_SCREEN";


    public static void reportError(final ApplicationController app, final String dataType, final String data) {
        String url = UrlConfigHelper.getInstance(app).getUrlConfigOfFile(Config.UrlEnum.LOG_ERROR);
        Log.f(TAG, "report error: " + dataType + " data: " + data);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "reportError onResponse: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "reportError onErrorResponse: " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                final String timeStamp = String.valueOf(System.currentTimeMillis());
                StringBuilder sb = new StringBuilder();
                sb.append(app.getJidNumber()).append(data).append(dataType)
                        .append(Constants.HTTP.CLIENT_TYPE_STRING).append(Config.REVISION)
                        .append(app.getToken()).append(timeStamp);
                params.put("msisdn", app.getJidNumber());
                params.put("data", data);
                params.put("dataType", dataType);
                params.put("clientType", Constants.HTTP.CLIENT_TYPE_STRING);
                params.put("revision", Config.REVISION);
                params.put("uuid", CommonUtils.getUIID(app));
                params.put("timestamp", timeStamp);
                params.put("security", HttpHelper.encryptDataV2(app, sb.toString(),
                        app.getToken()));
                return params;
            }
        };
        VolleyHelper.getInstance(app).addRequestToQueue(request, TAG, false);
    }


}