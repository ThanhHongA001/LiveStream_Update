package com.example.livestream_update.Ringme.Utils;

/*
 * Copyright (c) https://bigzun.blogspot.com/
 * Email: bigzun.com@gmail.com
 * Created by namnh40 on 2019/12/4
 *
 */


import com.vtm.ringme.ApplicationController;

import java.util.Map;

public class InsiderUtils {
    private static final String TAG = "InsiderUtils";

    public static final String NOTIFY_DEEPLINK = "mc_insider_notify_deeplink";
    public static final String NOTIFY_LINK = "mc_insider_notify_link";

    public static final String EVENT_APP_START = "app_start";
    public static final String EVENT_LOGIN_IMPRESSION = "login_impression";
    public static final String EVENT_LOGIN_START = "login_start";
    public static final String EVENT_LOGIN_PHONE_SUCCESS = "login_phone_success";
    public static final String EVENT_LOGIN_OTP_SUBMIT = "login_otp_submit";
    public static final String EVENT_LOGIN_SUCCESS = "login_success";
    public static final String EVENT_VIDEOS_LISTING_VIEW = "videos_listing_view";
    public static final String EVENT_VIDEO_VIEW_START = "video_view_start";
    public static final String EVENT_VIDEO_VIEW_END = "video_view_end";
    public static final String EVENT_MOVIES_LISTING_VIEW = "movies_listing_view";
    public static final String EVENT_MOVIE_VIEW_START = "movie_view_start";
    public static final String EVENT_MOVIE_VIEW_END = "movie_view_end";
    public static final String EVENT_KEYWORD_SEARCH = "keyword_search";

    public static final String PARAM_LOGIN_IMPRESSION_CATEGORY = "login_impression_category";
    public static final String PARAM_LOGIN_START_CATEGORY = "login_start_category";
    public static final String PARAM_VIDEOS_LISTING_CATEGORY = "videos_listing_category";
    public static final String PARAM_VIDEO_NAME = "video_name";
    public static final String PARAM_VIDEO_CATEGORY = "video_category";
    public static final String PARAM_VIDEO_CHANNEL = "video_channel";
    public static final String PARAM_VIDEO_TAG = "video_tag";
    public static final String PARAM_VIDEO_EPISODE = "video_episode";
    public static final String PARAM_VIDEO_COMPLETION_RATE = "video_completion_rate";
    public static final String PARAM_MOVIES_LISTING_CATEGORY = "movies_listing_category";
    public static final String PARAM_MOVIE_NAME = "movie_name";
    public static final String PARAM_MOVIE_COUNTRY = "movie_country";
    public static final String PARAM_MOVIE_CATEGORIES = "movie_categories";
    public static final String PARAM_MOVIE_COMPLETION_RATE = "movie_completion_rate";



    public static void logEvent(ApplicationController application, String eventName, Map<String, Object> mapParams) {
//        if (application == null || eventName == null) return;
//        if (mapParams == null || mapParams.isEmpty()) {
//            Insider.Instance.tagEvent(eventName);
//        } else {
//            Insider.Instance.tagEventWithParameters(eventName, mapParams);
//        }
    }

}
