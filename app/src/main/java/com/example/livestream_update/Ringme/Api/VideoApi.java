package com.example.livestream_update.Ringme.Api;


import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.common.api.BaseApi;
import com.vtm.ringme.common.api.http.HttpCallBack;
import com.vtm.ringme.helper.ConfigLocalized;
import com.vtm.ringme.helper.EncodeHelper;

public class VideoApi extends BaseApi {


    public static final String POST_NOTIFY_VIDEO = "/notification-service/v1/push/video/like";
    public static final String VIDEO_SEARCH = "/LivestreamAPI/v1/search/livestream";
    public static final String CHANNEL_SEARCH = "/LivestreamAPI/v1/search/channel";
    public static final String LIVESTREAM_CHANNEL = "/LivestreamAPI/v1/my_livestream/streamer";
    public static final String VIDEO_CHAT_LIST = "/LivestreamAPI/v1/chat-video/all";



    private static VideoApi mInstance;

    public VideoApi() {
        super(ApplicationController.self());
    }

    public static VideoApi getInstance() {
        if (mInstance == null) mInstance = new VideoApi();
        return mInstance;
    }

    public void searchChannel(int page, int size, String query, HttpCallBack httpCallBack) {
        String msisdn = ApplicationController.self().getJidNumber();
        long timestamp = System.currentTimeMillis();
        String security = EncodeHelper.getSecurity("", timestamp);
        get(ConfigLocalized.DOMAIN_LIVESTREAM, CHANNEL_SEARCH)
                .putParameter("userId", msisdn)
                .putParameter("timestamp", String.valueOf(timestamp))
                .putParameter("security", security)
                .putParameter("page", String.valueOf(page))
                .putParameter("size", String.valueOf(size))
                .putParameter("q", query)
                .putHeader("Accept-language", "vi")
                .withCallBack(httpCallBack).execute();
    }

    public void searchVideo(int page, int size, String query, HttpCallBack httpCallBack) {
        String msisdn = ApplicationController.self().getJidNumber();
        long timestamp = System.currentTimeMillis();
        String security = EncodeHelper.getSecurity("", timestamp);
        get(ConfigLocalized.DOMAIN_LIVESTREAM, VIDEO_SEARCH)
                .putParameter("userId", msisdn)
                .putParameter("timestamp", String.valueOf(timestamp))
                .putParameter("security", security)
                .putParameter("page", String.valueOf(page))
                .putParameter("size", String.valueOf(size))
                .putParameter("q", query)
                .putParameter("lastHashId", "")
                .putHeader("Accept-language", "en")
                .withCallBack(httpCallBack).execute();
    }



    public void getLivestreamOfChannel(String channelId, HttpCallBack httpCallBack) {
        String msisdn = ApplicationController.self().getJidNumber();
        get(ConfigLocalized.DOMAIN_LIVESTREAM, LIVESTREAM_CHANNEL)
                .putParameter("channelId", channelId)
                .putParameter("page", "0")
                .putParameter("size", "1")
                .putParameter("userId", msisdn)
                .withCallBack(httpCallBack).execute();
    }

    public void getListLiveChatVOD(String roomId, int page, HttpCallBack httpCallBack) {
        get(ConfigLocalized.DOMAIN_LIVESTREAM, VIDEO_CHAT_LIST)
                .putParameter("roomId", roomId)
                .putParameter("pageNo", String.valueOf(page))
                .putParameter("pageSize", String.valueOf(1000))
                .withCallBack(httpCallBack).execute();
    }



}

