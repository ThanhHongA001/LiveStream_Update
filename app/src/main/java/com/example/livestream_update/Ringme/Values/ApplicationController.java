package com.example.livestream_update.Ringme.Values;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.vtm.ringme.activities.LivestreamFutureActivity;
import com.vtm.ringme.model.PhoneNumber;
import com.vtm.ringme.model.ReengAccount;
import com.vtm.ringme.model.livestream.LivestreamModel;
import com.vtm.ringme.model.tab_video.Channel;
import com.vtm.ringme.model.tab_video.Video;
import com.vtm.ringme.tabvideo.channelDetail.ChannelDetailActivity;
import com.vtm.ringme.values.Constants;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ApplicationController extends Application {
    private SharedPreferences mPref;

    private static ApplicationController mSelf;
    private Gson gson;

    private String configResolutionVideo = "auto";

    public static ApplicationController self() {
        return mSelf;
    }

    private PhoneNumberUtil phoneUtil;


    @Override
    public void onCreate() {
        super.onCreate();
        mSelf = this;
        mPref = getSharedPreferences(Constants.PREFERENCE.PREF_DIR_NAME, Context.MODE_PRIVATE);

    }



    public String getTitle() {
        return "";
    }


    public Boolean isDev() {
        return true;
    }

    public PhoneNumberUtil getPhoneUtil() {
        if (phoneUtil == null) {
            phoneUtil = PhoneNumberUtil.getInstance();
        }
        return phoneUtil;
    }

    public Boolean isEnableOnMedia() {
        return false;
    }


    public String getCurrentLanguage() {
        return "vi";
    }

    public Boolean isVip() {
        return false;
    }

    public Integer getStatusBarHeight() {
        return 10;
    }

    public String getContentConfigByKey(String string) {
        return "24";
    }

    public Integer getTotalVideosViewed() {
        return 0;
    }

    public ArrayList<PhoneNumber> getListNumberUseRingMe() {
        return new ArrayList<PhoneNumber>();
    }

    public Integer getRound() {
        return 0;
    }

    public String getKakoakApi() {
        return "1739157299092.fad894be074d9558f67e87fddda2fee60fbf45cff66ffc76cb149734aaf64d14";
    }

    public PhoneNumber getPhoneNumberFromNumber(String phone) {
        return new PhoneNumber();
    }

    public String getUserName() {
        return "Olbevgggg";
    }

    public String getLastChangeAvatar() {
        return "1736994211878";
    }

    public String getRegionCode() {
        return "VN";
    }

    public String getConfigResolutionVideo() {
        if (configResolutionVideo == null) configResolutionVideo = "";
        return configResolutionVideo;
    }

    public void setConfigResolutionVideo(String configResolutionVideo) {
        this.configResolutionVideo = configResolutionVideo;
    }

    public SharedPreferences getPref() {
        return mPref;
    }

    public Gson getGson() {
        if (gson == null) gson = new Gson();
        return gson;
    }


    public String getAvatarUrl() {
        return "https://freeapikakoak.tls.tl/api/thumbnail/download-orginal?v=d10b1a550730004efbf9e28e38bcb346&ac=%2B67076795340&t=1736994211878&u=%2B67076795340";
    }

    public String getAvatarUrl(String phone, String link, Integer size) {
        return "https://freeapikakoak.tls.tl/api/thumbnail/download-orginal?v=d10b1a550730004efbf9e28e38bcb346&ac=%2B67076795340&t=1736994211878&u=%2B67076795340";
    }

    public String getToken() {
        return "RINGME";
    }


    public RoundedCornersTransformation getRoundedCornersTransformation() {
        return new RoundedCornersTransformation(10, 10);
    }



    public ReengAccount getCurrentAccount() {
        ReengAccount reengAccount = new ReengAccount();
        reengAccount.setId(1);
        reengAccount.setName("Olbevgggg");
        reengAccount.setToken("1953792303173915729980832");
        reengAccount.setLastChangeAvatar("1736994211878");
        reengAccount.setPermission(1);
        reengAccount.setNumberJid("+67076795340");
        return reengAccount;
    }

    public String getJidNumber() {
        return "+67076795340";
    }

    public void openChannelInfo(Activity activity, Channel channel) {
        ChannelDetailActivity.start(activity, channel);
    }

    public void openVideoDetail(final AppCompatActivity activity, final Video video) {
        Intent intent = new Intent(activity, LivestreamFutureActivity.class);
        Bundle bundle = new Bundle();
        LivestreamModel livestreamModel =new LivestreamModel();
        livestreamModel.setChannel(video.getChannel());
        livestreamModel.setUrl(video.getStartMediaUrl());
        livestreamModel.setChannelId(video.getChannelId());
        livestreamModel.setBannerLnk(video.getImagePath());
        livestreamModel.setHlsPlaylink(video.getMediaLink());
        bundle.putSerializable(Constants.KeyData.video, livestreamModel);
        intent.putExtra(Constants.KeyData.data, bundle);
        activity.startActivity(intent);
    }
}