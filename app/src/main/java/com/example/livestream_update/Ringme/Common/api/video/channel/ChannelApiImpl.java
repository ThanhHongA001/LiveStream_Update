package com.example.livestream_update.Ringme.Common.api.video.channel;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.api.HomeApi;
import com.vtm.ringme.api.RRestChannelInfoModel;
import com.vtm.ringme.common.api.BaseApi;
import com.vtm.ringme.common.api.http.HttpCallBack;
import com.vtm.ringme.common.api.video.callback.OnChannelInfoCallback;
import com.vtm.ringme.model.tab_video.Channel;

/**
 * Created by tuanha00 on 3/14/2018.
 */

public class ChannelApiImpl extends BaseApi implements ChannelApi {

    public static final String API_UPLOAD_LEAD_IMAGE = "/serviceapi/upload/uploadLeadImage"; // upload ảnh
    private static final String TAG = "ChannelApiImpl";

    public ChannelApiImpl(ApplicationController application) {
        super(application);
    }

    @Override
    public void callApiSubOrUnsubChannel(final String channelId, boolean follow) {
        if(follow){
            HomeApi.getInstance().liveStreamFollow(channelId, new HttpCallBack() {
                @Override
                public void onSuccess(String data) throws Exception {
                    android.util.Log.e(TAG, "onResponse: 1234");
                }

                @Override
                public void onFailure(String message) {
                    super.onFailure(message);
                }
            });
        } else {
            HomeApi.getInstance().liveStreamUnFollow(channelId, new HttpCallBack() {
                @Override
                public void onSuccess(String data) throws Exception {
                    android.util.Log.e(TAG, "onResponse: 12345");
                }

                @Override
                public void onFailure(String message) {
                    super.onFailure(message);
                }
            });
        }

    }

    @Override
    public void getChannelInfo(String channelId, final OnChannelInfoCallback channelInfoCallback) {
        HomeApi.getInstance().getChannelInfo(channelId, new HttpCallBack() {
            @Override
            public void onSuccess(String data) throws Exception {
                Gson gson = new Gson();
                RRestChannelInfoModel response = gson.fromJson(data, RRestChannelInfoModel.class);
                if (channelInfoCallback == null) return;
                if (response != null && response.getData() != null) {
                    channelInfoCallback.onGetChannelInfoSuccess(response.getData());
                    channelInfoCallback.onGetChannelInfoComplete();
                }
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
                if (channelInfoCallback == null) return;
                channelInfoCallback.onGetChannelInfoError(message);
                channelInfoCallback.onGetChannelInfoComplete();
            }
        });
    }



    public void subscribeChannel(Channel channel, @NonNull HttpCallBack callBack) {
    }
}
