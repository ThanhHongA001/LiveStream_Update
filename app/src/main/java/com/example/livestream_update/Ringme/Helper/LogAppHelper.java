package com.example.livestream_update.Ringme.Helper;

import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.common.api.BaseApi;
import com.vtm.ringme.common.api.http.HttpCallBack;
import com.vtm.ringme.common.utils.SharedPrefs;
import com.vtm.ringme.utils.Log;

public class LogAppHelper {
    public static final String API_LOG_APP = "/kakoaklogevent/apikakoak/v1/logevent";
    private static final String TAG = "LogAppHelper";
    private static LogAppHelper mInstance;
    private final SharedPrefs sharedPreferences;
    private BaseApi mApi;
    private long timeStart;
    private long timeEnd;

    public LogAppHelper() {
        sharedPreferences = SharedPrefs.getInstance();
    }

    public static synchronized LogAppHelper getInstance() {
        if (mInstance == null) {
            mInstance = new LogAppHelper();
            mInstance.mApi = new BaseApi(ApplicationController.self());
        }
        return mInstance;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }


    public void logAppAction(String eventName, String actionName) {
        if (!NetworkHelper.isConnectInternet(ApplicationController.self())) {
            return;
        }
        mApi.logApp(eventName, actionName, "0", new HttpCallBack() {
            @Override
            public void onSuccess(String data) throws Exception {
                Log.d(TAG, eventName + " Data: " + data);
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
            }
        });
    }




    public interface ConstantLogApp {


        String EVENT_LIVESTREAM_DETAIL = "EVENT_LIVESTREAM_DETAIL";
        String EVENT_VIDEO_LIKE = "EVENT_VIDEO_LIKE";
        String EVENT_VIDEO_UNLIKE = "EVENT_VIDEO_UNLIKE";
        String EVENT_VIDEO_SHARE = "EVENT_VIDEO_SHARE";
        String EVENT_VIDEO_COMMENT = "EVENT_VIDEO_COMMENT";
        String EVENT_VIDEO_PLAY_ERROR = "EVENT_VIDEO_PLAY_ERROR";
        String EVENT_USER_LOGOUT = "EVENT_USER_LOGOUT";
    }

}
