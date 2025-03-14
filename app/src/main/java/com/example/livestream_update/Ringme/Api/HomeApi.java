package com.example.livestream_update.Ringme.Api;

import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.common.api.ApiCallbackV2;
import com.vtm.ringme.common.api.BaseApi;
import com.vtm.ringme.common.api.http.HttpCallBack;
import com.vtm.ringme.helper.ConfigLocalized;
import com.vtm.ringme.helper.HttpHelper;
import com.vtm.ringme.helper.encrypt.EncryptUtil;
import com.vtm.ringme.home.RRestVideoModel;
import com.vtm.ringme.livestream.network.APICallBack;
import com.vtm.ringme.model.ReengAccount;
import com.vtm.ringme.model.tab_video.Video;
import com.vtm.ringme.utils.RRetrofitClientInstance;

import org.json.JSONException;

import java.util.ArrayList;

public class HomeApi extends BaseApi {

    private static HomeApi mInstance;

    public static final String PAGE = "0";
    public static final String SIZE = "10";

    public static final String LIVESTREAM_LIST = "/LivestreamAPI/v2/my_livestream/all"; //"/LivestreamAPI/v1/my_livestream/all";
    public static final String LIVESTREAM_FOLLOW_CHANNEL = "/LivestreamAPI/v1/live_social/follow";
    public static final String LIVESTREAM_LIKE = "/LivestreamAPI/v1/live_social/like";
    public static final String LIVESTREAM_REPORT = "/LivestreamAPI/v1/report_livestream";
    public static final String LIVESTREAM_NEWS = "/LivestreamAPI/v1/news";
    public static final String LIVESTREAM_LIST_CHANNEL = "/LivestreamAPI/v1/channel/list/v2";
    public static final String LIVESTREAM_GET_DETAIL = "/LivestreamAPI/v2/my_livestream/details";
    public static final String LIVESTREAM_DELETE_COMMENT = "/LivestreamAPI/v1/report_comment";
    public static final String LIVESTREAM_REACTION_COMMENT = "/LivestreamAPI/v1/live_social/react_comment";
    public static final String LIVESTREAM_LIST_REACTION_USER = "/LivestreamAPI/v1/live_social/list_reacts_user";
    public static final String LIVESTREAM_LIST_STAR_PACKAGE = "/LivestreamAPI/v1/gift/list-level-star";
    public static final String LIVESTREAM_LIST_DONATE = "/LivestreamAPI/v2/donate/list-package-gift";
    public static final String LIVESTREAM_SEND_STAR = "/LivestreamAPI/v2/donate";
    public static final String LIVESTREAM_STAR_WALLET = "/LivestreamAPI/v2/donate/get-current-star-user";
    public static final String LIVESTREAM_RECEIVE_STAR = "/LivestreamAPI/v1/donate/receive-star";
    public static final String LIVESTREAM_TOP_DONATE = "/LivestreamAPI/v1/donate/list-donate/top";
    public static final String LIVESTREAM_TOP_DONATE_LIVESTREAM = "/LivestreamAPI/v2/donate/get-top-donate-in-livestream";
    public static final String LIVESTREAM_RECHARGE_STAR = "/LivestreamAPI/v2/donate/buy-star";  ///LivestreamAPI/v1/donate/recharge-star
    public static final String LIVESTREAM_REGISTER_NOTIFY = "/LivestreamAPI/api/v1/notification";
    public static final String LIVESTREAM_GET_LIST_VOTE = "/LivestreamAPI/v1/vote/list-all";
    public static final String LIVESTREAM_APPLY_VOTE = "/LivestreamAPI/v1/vote/voted";
    public static final String LIVESTREAM_LIST_PACKAGE_START = "/LivestreamAPI/v1/buy-star/pack_mps/list";
    public static final String LIVESTREAM_LIST_BUY_START_OTP = "/LivestreamAPI/v1/buy-star/pack_mps/reqOTP";
    public static final String LIVESTREAM_LIST_BUY_START_VALIDATE = "/LivestreamAPI/v1/buy-star/pack_mps/inputOTP";


    public static final String getHomePageList = "/RingMeAPI/homepage/get/list";

    private static final String LIVESTREAM_GET_VIDEO_IN_CHANNEL = "video/{id}/channel";



    public HomeApi() {
        super(ApplicationController.self());
    }

    public void getListDonate(HttpCallBack httpCallBack){
        String msisdn = ApplicationController.self().getJidNumber();
        get(ConfigLocalized.DOMAIN_LIVESTREAM, LIVESTREAM_LIST_DONATE)
                .withCallBack(httpCallBack)
                .execute();
    }

    public void buyPackageStartOTP(String code,HttpCallBack httpCallBack){
        String msisdn = ApplicationController.self().getJidNumber();
        String md5= ApplicationController.self().getJidNumber() +":"+ code +":"+ ApplicationController.self().getToken();
        String security = EncryptUtil.encryptMD5(md5);

//        BuyStarOtpRequest request = new BuyStarOtpRequest(msisdn, code, security);
        post(ConfigLocalized.DOMAIN_LIVESTREAM, LIVESTREAM_LIST_BUY_START_OTP)
//                .putBody(request)
//                .putHeader("Content-type","application/x-www-form-urlencoded")
                .putParameter("msisdn",msisdn)
                .putParameter("code",code)
                .putParameter("tokenMd5",security)
                .withCallBack(httpCallBack)
                .execute();
    }

    public void getListPackageStart(HttpCallBack httpCallBack){
        String msisdn = ApplicationController.self().getJidNumber();
        get(ConfigLocalized.DOMAIN_LIVESTREAM, LIVESTREAM_LIST_PACKAGE_START)
                .withCallBack(httpCallBack)
                .execute();
    }



    public static HomeApi getInstance() {
        if (mInstance == null) mInstance = new HomeApi();
        return mInstance;
    }

    public void buyPackageStartValidate(String code, String otp, String requestId,HttpCallBack httpCallBack){
        String msisdn = ApplicationController.self().getJidNumber();
        ReengAccount account = ApplicationController.self().getCurrentAccount();
        String security = EncryptUtil.encryptMD5(ApplicationController.self().getJidNumber() +":"+ code +":"+ ApplicationController.self().getToken());
        String avatarUrl = ApplicationController.self().getAvatarUrl(account.getLastChangeAvatar(), account.getJidNumber(), 70);

//        BuyStarValidateRequest request = new BuyStarValidateRequest(msisdn, code, otp,requestId, security);
        post(ConfigLocalized.DOMAIN_LIVESTREAM, LIVESTREAM_LIST_BUY_START_VALIDATE)
                .putParameter("msisdn",msisdn)
                .putParameter("code",code)
                .putParameter("requestIdCp",requestId)
                .putParameter("name",ApplicationController.self().getUserName())
                .putParameter("avatar",avatarUrl)
                .putParameter("otp",otp)
                .putParameter("tokenMd5",security)
                .withCallBack(httpCallBack)
                .execute();
    }

    public void registerNotifyLivestream(String livestreamId,long time, int type, HttpCallBack httpCallBack) {
        postByRequestBody(ConfigLocalized.DOMAIN_LIVESTREAM, LIVESTREAM_REGISTER_NOTIFY)
                .putParameter("userId", ApplicationController.self().getJidNumber())
                .putParameter("livestreamId", livestreamId)
                .putParameter("type", String.valueOf(type))
                .putParameter("timeNotification", String.valueOf(time))
                .putHeader("Content-Type", "application/json")
                .putHeader("Authorization", ApplicationController.self().getToken())
                .withCallBack(httpCallBack).execute();
    }


    public void deleteCommentLivestream(String id, String livestreamId, String streamerId, String avatar, String userName, HttpCallBack httpCallBack) {
        post(ConfigLocalized.DOMAIN_LIVESTREAM, LIVESTREAM_DELETE_COMMENT)
                .putParameter("blockId", "3")
                .putParameter("commentId", id)
                .putParameter("livestreamId", livestreamId)
                .putParameter("streamerId", streamerId)
                .putParameter("userId", ApplicationController.self().getJidNumber())
                .putParameter("avatar", avatar)
                .putParameter("userName", userName)
                .withCallBack(httpCallBack).execute();
    }

    public void reactionComment(int type, String smsgId, String action, HttpCallBack httpCallBack) {
        post(ConfigLocalized.DOMAIN_LIVESTREAM, LIVESTREAM_REACTION_COMMENT)
                .putParameter("type", String.valueOf(type))
                .putParameter("smsgId", smsgId)
                .putParameter("userId", ApplicationController.self().getJidNumber())
                .putParameter("action", action)
                .withCallBack(httpCallBack).execute();
    }

    public void getListLivestream(int featureId, int page, HttpCallBack httpCallBack) {
//        GetListLivestreamRequest request = new GetListLivestreamRequest(String.valueOf(featureId), String.valueOf(page), SIZE, userId );
        get(ConfigLocalized.DOMAIN_LIVESTREAM, LIVESTREAM_LIST)
                .putParameter("featureId", String.valueOf(featureId))
                .putParameter("page", String.valueOf(page))
                .putParameter("size", SIZE)
                .putParameter("userId", ApplicationController.self().getJidNumber())
                .putHeader("Authorization", ApplicationController.self().getToken())
                .withCallBack(httpCallBack)
                .execute();
    }

    public void getTopDonateLivestream(String streamerId, String livestreamId, HttpCallBack httpCallBack) {
        get(ConfigLocalized.DOMAIN_LIVESTREAM, LIVESTREAM_TOP_DONATE_LIVESTREAM)
                .putParameter("channelId", streamerId)
                .putParameter("livestreamId", livestreamId)
                .withCallBack(httpCallBack).execute();
    }


    public void followLivestreamChannel(String streamId, String streamerId, HttpCallBack httpCallBack) {
        post(ConfigLocalized.DOMAIN_LIVESTREAM, LIVESTREAM_FOLLOW_CHANNEL)
                .putParameter("livestreamId", streamId)
                .putParameter("streamerId", streamerId)
                .putParameter("userId", ApplicationController.self().getJidNumber())
                .withCallBack(httpCallBack)
                .execute();
    }

    public void getListChannelLivestream(int page, int size, HttpCallBack httpCallBack) {
        get(ConfigLocalized.DOMAIN_LIVESTREAM, LIVESTREAM_LIST_CHANNEL)
                .putParameter("userId", ApplicationController.self().getJidNumber())
                .putParameter("page", String.valueOf(page))
                .putParameter("size", String.valueOf(size))
                .withCallBack(httpCallBack).execute();
    }

    public void reportLivestream(String livestreamId, String reportContent, int reportId, String channelId, HttpCallBack httpCallBack) {
        post(ConfigLocalized.DOMAIN_LIVESTREAM, LIVESTREAM_REPORT)
                .putParameter("livestreamId", livestreamId)
                .putParameter("reportContent", reportContent)
                .putParameter("reportId", String.valueOf(reportId))
                .putParameter("userId", ApplicationController.self().getJidNumber())
                .putParameter("streamerId", channelId)
                .withCallBack(httpCallBack).execute();
    }

    public void getListVoteLivestream(String livestreamId, HttpCallBack httpCallBack) {
        get(ConfigLocalized.DOMAIN_LIVESTREAM, LIVESTREAM_GET_LIST_VOTE)
                .putParameter("liveStreamId", livestreamId)
                .putParameter("userId", ApplicationController.self().getJidNumber())
                .putHeader("Content-Type", "application/json")
                .putHeader("Authorization", ApplicationController.self().getToken())
                .withCallBack(httpCallBack).execute();
    }

    public void applyVote(String livestreamId, long surveyId, long voteId, int type, long voteNumber, HttpCallBack httpCallBack) {
        get(ConfigLocalized.DOMAIN_LIVESTREAM, LIVESTREAM_APPLY_VOTE)
                .putParameter("liveStreamId", livestreamId)
                .putParameter("userId", ApplicationController.self().getJidNumber())
                .putParameter("surveyId", String.valueOf(surveyId))
                .putParameter("voteId", String.valueOf(voteId))
                .putParameter("type", String.valueOf(type))
                .putParameter("numberVote", String.valueOf(voteNumber))
                .putHeader("Content-Type", "application/json")
                .putHeader("Authorization", ApplicationController.self().getToken())
                .withCallBack(httpCallBack).execute();
    }


    public void receiveDropStar(String livestreamId, HttpCallBack httpCallBack) {
        post(ConfigLocalized.DOMAIN_LIVESTREAM, LIVESTREAM_RECEIVE_STAR)
                .putParameter("userId", ApplicationController.self().getJidNumber())
                .putParameter("livestreamId", livestreamId)
                .withCallBack(httpCallBack).execute();
    }


    public void getUserStarNumber(HttpCallBack httpCallBack) {
        get(ConfigLocalized.DOMAIN_LIVESTREAM, LIVESTREAM_STAR_WALLET)
                .putParameter("userId", ApplicationController.self().getJidNumber())
                .withCallBack(httpCallBack).execute();

    }

    public void getListReactionUser(String roomId, HttpCallBack httpCallBack) {
        get(ConfigLocalized.DOMAIN_LIVESTREAM, LIVESTREAM_LIST_REACTION_USER)
                .putParameter("userId", ApplicationController.self().getJidNumber())
                .putParameter("roomId", roomId)
                .withCallBack(httpCallBack).execute();
    }

    public void sendStar(String message, String giftId, String streamerId, String livestreamId, HttpCallBack httpCallBack) {
        long timestamp = System.currentTimeMillis();
        ReengAccount account = ApplicationController.self().getCurrentAccount();
        String avatarUrl = ApplicationController.self().getAvatarUrl();
        String md5 = ApplicationController.self().getJidNumber() +":"+ streamerId +":"+ livestreamId +":"+ giftId +":"+ ApplicationController.self().getToken() +":"+ timestamp;
        String security = EncryptUtil.encryptMD5(md5);

        post(ConfigLocalized.DOMAIN_LIVESTREAM, LIVESTREAM_SEND_STAR)
                .putParameter("userId",ApplicationController.self().getJidNumber())
                .putParameter("name",account.getName())
                .putParameter("avatar",avatarUrl)
                .putParameter("channelId",streamerId)
                .putParameter("livestreamId",livestreamId)
                .putParameter("giftId",giftId)
                .putParameter("timestamp",""+timestamp)
                .putParameter("tokenMd5",security)
                .putParameter("message",message)
                .withCallBack(httpCallBack).execute();
    }


    public void likeLivestream(String streamId, String streamerId, int reactId, HttpCallBack httpCallBack) {
        post(ConfigLocalized.DOMAIN_LIVESTREAM, LIVESTREAM_LIKE)
                .putParameter("livestreamId", streamId)
                .putParameter("streamerId", streamerId)
                .putParameter("userId", ApplicationController.self().getJidNumber())
                .putParameter("reactId", String.valueOf(reactId))
                .withCallBack(httpCallBack)
                .execute();
    }

    public void getLivestreamDetail(String id, HttpCallBack httpCallBack) {
        get(ConfigLocalized.DOMAIN_LIVESTREAM, LIVESTREAM_GET_DETAIL)
                .putParameter("userId", ApplicationController.self().getJidNumber())
                .putParameter("id", id)
                .withCallBack(httpCallBack).execute();
    }

//    public void getVideosByChannelId(String channelId, int offset, int limit, String lastId, HttpCallBack httpCallBack) {
//        long timestamp = System.currentTimeMillis();
//        String msisdn = ApplicationController.self().getJidNumber();
//        String url = LIVESTREAM_GET_VIDEO_IN_CHANNEL.replace("{id}", channelId);
//        get(ConfigLocalized.DOMAIN_LIVESTREAM, url)
//                .putHeader("Accept-language", "en")
//                .putHeader("Client-Type", "Android")
//                .putHeader("sec-api", "123")
//                .putParameter("msisdn", msisdn)
//                .putParameter("lastHashId", lastId)
//                .putParameter("page", String.valueOf(offset))
//                .putParameter("size", String.valueOf(limit))
//                .putParameter("timestamp", String.valueOf(timestamp))
//                .putParameter("security", "")
//                .execute();
//    }


    public void getVideosByChannelId(String channelId, int offset, int limit, String lastId, final ApiCallbackV2<ArrayList<Video>> callback) {
        RRetrofitClientInstance retrofitClientInstance = new RRetrofitClientInstance();
        retrofitClientInstance.getVideoByChannel(channelId, offset, lastId, new APICallBack<RRestVideoModel>() {
            @Override
            public void onResponse(retrofit2.Response<RRestVideoModel> response) {
                if (response != null && response.body() != null && response.body().getData() != null) {
                    if (callback != null) {
                        try {
                            if(response.body().getData().size() > 0)
                            {
                                ArrayList<Video> list;
                                String lastId = response.body().getData().get(response.body().getData().size() - 1).getLastId();
                                list = response.body().getData();
                                callback.onSuccess(lastId, list);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (callback != null) {
                    callback.onComplete();
                }
            }

            @Override
            public void onError(Throwable error) {
                if (callback != null) {
                    callback.onError(error.getMessage());
                    callback.onComplete();
                }
            }
        });
    }

    public void liveStreamFollow(String id, HttpCallBack httpCallBack){
        long timestamp = System.currentTimeMillis();
        String msisdn = ApplicationController.self().getJidNumber();
        String security = HttpHelper.encryptDataV2(ApplicationController.self(), ApplicationController.self().getJidNumber() + timestamp + ApplicationController.self().getToken() + timestamp, ApplicationController.self().getToken());

        get(ConfigLocalized.DOMAIN_LIVESTREAM, "/LivestreamAPI/v1/channel/"+id+"/follow")
                .putHeader("Accept-language", "en")
                .putParameter("msisdn", msisdn)
                .putParameter("timestamp", String.valueOf(timestamp))
                .putParameter("security", security)
                .withCallBack(httpCallBack)
                .execute();
    }

    public void liveStreamUnFollow(String id, HttpCallBack httpCallBack){
        long timestamp = System.currentTimeMillis();
        String msisdn = ApplicationController.self().getJidNumber();
        String security = HttpHelper.encryptDataV2(ApplicationController.self(), ApplicationController.self().getJidNumber() + timestamp + ApplicationController.self().getToken() + timestamp, ApplicationController.self().getToken());

        get(ConfigLocalized.DOMAIN_LIVESTREAM, "/LivestreamAPI/v1/channel/"+id+"/unfollow")
                .putHeader("Accept-language", "en")
                .putParameter("msisdn", msisdn)
                .putParameter("timestamp", String.valueOf(timestamp))
                .putParameter("security", security)
                .withCallBack(httpCallBack)
                .execute();
    }

    public void getChannelInfo(String id, HttpCallBack httpCallBack){
        long timestamp = System.currentTimeMillis();
        String msisdn = ApplicationController.self().getJidNumber();
        String security = HttpHelper.encryptDataV2(ApplicationController.self(), ApplicationController.self().getJidNumber() + timestamp + ApplicationController.self().getToken() + timestamp, ApplicationController.self().getToken());

        get(ConfigLocalized.DOMAIN_LIVESTREAM, "/LivestreamAPI/v1/channel/"+id+"/info")
                .putHeader("Accept-language", "en")
                .putParameter("msisdn", msisdn)
                .putParameter("timestamp", String.valueOf(timestamp))
                .putParameter("security", security)
                .withCallBack(httpCallBack)
                .execute();
    }

    public void getVideoLiveStreamInfo(String id, HttpCallBack httpCallBack){
        long timestamp = System.currentTimeMillis();
        String msisdn = ApplicationController.self().getJidNumber();
        String security = HttpHelper.encryptDataV2(ApplicationController.self(), ApplicationController.self().getJidNumber() + timestamp + ApplicationController.self().getToken() + timestamp, ApplicationController.self().getToken());

        get(ConfigLocalized.DOMAIN_LIVESTREAM, "/LivestreamAPI/v2/video/"+id+"/info")
                .putHeader("Accept-language", "en")
                .putParameter("msisdn", msisdn)
                .putParameter("timestamp", String.valueOf(timestamp))
                .putParameter("security", security)
                .withCallBack(httpCallBack)
                .execute();
    }

    public void getVideoLiveStreamRelated(String id,int page, int size, HttpCallBack httpCallBack){
        long timestamp = System.currentTimeMillis();
        String msisdn = ApplicationController.self().getJidNumber();
        String security = HttpHelper.encryptDataV2(ApplicationController.self(), ApplicationController.self().getJidNumber() + timestamp + ApplicationController.self().getToken() + timestamp, ApplicationController.self().getToken());

        get(ConfigLocalized.DOMAIN_LIVESTREAM, "/LivestreamAPI/v2/video/"+id+"/related")
                .putHeader("Accept-language", "en")
                .putParameter("msisdn", msisdn)
                .putParameter("size", String.valueOf(size))
                .putParameter("page", String.valueOf(page))
                .putParameter("lastHashId", "1")
                .putParameter("timestamp", String.valueOf(timestamp))
                .putParameter("security", security)
                .withCallBack(httpCallBack)
                .execute();
    }
}
