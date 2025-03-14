package com.example.livestream_update.Ringme.Helper;


import com.vtm.ringme.BuildConfig;

/**
 * Created by toanvk2 on 12/10/14.
 */
public class Config {

    public static final String REVISION = "16" + BuildConfig.VERSION_CODE; // version code 53 15547; 54 15548

    public static final class Extras {
        public static final String DEFAULT_ENCODING = "UTF-8";
        public static final String ENCODE_MD5 = "vt13579";
        public static final boolean SMOOTH_SCROLL = false;

        public static final String B_PLUS_ID = "mocha_viettel";
        public static final String B_PLUS_MERCHANT =
                "e9715126c9ed69b14bdf6abc790f571b8beeb98c7563ee5e25fe16be20e6ed990bfe94f8c1e82a87d0e13b521276c599b61d9ac86c3f5cc61cc1e3f16c8f75db";
        public static final String B_PLUS_ACCESS_CODE =
                "nIoC4FDBW83LscgCWmC573RbkLLb8168xFImsnLPXW6NnUxODDSTjFuz5fgS4vNBczuj34WpKBiUT6oVMJ6MfA8si2jFewWw17fMJVVW8fJtHoKnNTJu9jVF36Sw1CwZ";
    }


    public static final class Pattern {
        public static final String PHONE = "^0?[9][0-9][1-9][0-9]{6}$|^0?16[0-9]{8}$|^0?12[0-9]{8}$|^0?1[8-9][0-9]{8" +
                "}$|^[+]?849[0-9][1-9][0-9]{6}$|^[+]?8416[0-9]{8}$|^[+]?8412[0-9]{8}$|^[+]?841[8-9][0-9]{8}$";
        public static final String VIETTEL = "^09[6-8][1-9][0-9]{6,6}$|^016[1-9][1-9][0-9]{6,6}$|^086[1-9][0-9]{6," +
                "6}$|^[+]?849[6-8][1-9][0-9]{6,6}$|^[+]?8416[1-9][1-9][0-9]{6,6}$|^[+]?8486[1-9][0-9]{6,6}$";
        public static final String MORE_PHONE = "^[+]?[0-9]{5}[0-9]*$";
        public static final String LINK_YOUTUBE = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\" +
                ".com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";
    }

    public static final class PREFIX {
        public static final String PROTOCOL_HTTP = "http://";
        public static final String PROTOCOL_HTTPS = "https://";
        public static final String STICKER = "/sticker";
    }

    public enum UrlKeengEnum {
        SERVICE_GET_SONG,
        SERVICE_GET_TOP_SONG,
        MEDIA2_SEARCH_SUGGESTION,
        SERVICE_SEARCH_SONG,
        SERVICE_GET_ALBUM,
        SERVICE_GET_SONG_UPLOAD,
        MEDIA_UPLOAD_SONG,
    }

    public enum UrlEnum {
        //bankplus/genotp/v2
        LIXI_GENOTP,
        //bankplus/payment/v2
        LIXI_TRANSFER_MONEY,
        //bankplusads/click
        LIXI_CLICK,
        //api/media/download
        FILE_DOWNLOAD_URL,
        ///api/media/upload
        FILE_UPLOAD_URL,
        //user/setDeviceInfo
        SET_DEVICE_ID,
        //sendlog
        REPORT_ERROR,
        //contact/getContact/v4
        GET_CONTACT,
        //contact/setContact/v3
        SET_CONTACT,
        //contact/addContact/v3
        ADD_CONTACT,
        //contact/removeContact/v3
        REMOVE_CONTACT,
        //v2/sms/sendInviteSms/v1
        INVITE_FRIENDS,
        //config/getConfig/v2.2
        GET_CONTENT_CONFIG,
        //sticker/getAllCollection/v2.2
        GET_ALL_STICKER,
        //downloadSticker/v21
        DOWNLOAD_STICKER_COLLECTION,
        //sms/v4/send
        RINGME_2_SMS,
        //restore/message/v2
        RESTORE_MESSAGE_V2,
        //musicRoom/getList/v4
        GET_STRANGER_MUSIC,
        //musicRoom/post/v2
        POST_STRANGER_MUSIC,
        //musicRoom/cancel/v2
        CANCEL_STRANGER_MUSIC,
        //musicRoom/accept/v2
        ACCEPT_STRANGER_MUSIC,
        //v2/config/getOfficalAcount
        GET_OFFICER_LIST,
        //keeng/v1/accept
        ACCEPT_STRANGER_KEENG,
        //msisdn/v23
        AUTO_DETECT_URL_V23,
        //genotp/v22
        GEN_OTP_INTERNATIONAL,
        //user/setUserInfo/v3
        SET_USER_INFO,
        //user/getUserInfo/v3
        GET_USER_INFO,
        //msisdn/free/v23
        AUTO_DETECT_FREE_URL_V23,
        //genotp/free/v22
        GEN_OTP_FREE,
        //upload/avatar
        AVATAR_UPLOAD,
        //invite/sms/diemthi
        INVITE_NOP_HOSO,
        //user/setState/v1
        CHANGE_STATUS,
        //star/follow/v1
        ROOM_CHAT_FOLLOW,
        //translate
        GG_TRANSLATE,
        //anonymous/getInfo/v1
        ANONYMOUS_DETAIL,
        //RingMeAPI/user/getProfile/v1
        GET_PROFILE_URL,
        //api/profile/upload
        IMAGE_PROFILE_UPLOAD_URL,
        //api/cover/upload
        IMAGE_COVER_UPLOAD_URL,
        //user/removeImage
        REMOVE_IMAGE_PROFILE,
        //user/setPermission
        SET_PERMISSION,
        //onMediaBackendBiz/onmedia/getComments/v2
        ONMEDIA_GET_COMMENT_V2,
        //onmedia/getLikes
        ONMEDIA_GET_LIKE_V2,
        //onmedia/getShares
        ONMEDIA_GET_SHARE_V2,
        //onmedia/getMetadata
        ONMEDIA_GET_METADATA,
        //
        ONMEDIA_REPORT_VIOLATION,
        //
        ONMEDIA_UNFOLLOW,
        //onMediaBackendBiz/onmedia/getNotifies/v1
        ONMEDIA_GET_NOTIFY_V1,
        //onMediaBackendBiz/onmedia/resetNotify/v1
        ONMEDIA_RESET_NOTIFY_V1,
        //
        ONMEDIA_GET_NUMBER_NOTIFY,
        //onMediaBackendBiz/onmedia/getUserTimeline/v3
        ONMEDIA_GET_USER_TIMELINE_V3,
        //
        ONMEDIA_LOG_CLICK_LINK,
        //report/spam/star/v2
        REPORT_ROOM,
        //keeng/getLogTogether
        LOG_LISTEN_TOGETHER,
        //keeng/getProfile/v2
        GET_PROFILE_V2,
        //Cai nay thay bang getfeed cua onMedia nen ko can nua
        //keeng/getFriendProfile/v2
        GET_FRIEND_PROFILE_V2,
        //musicRoom/getListStarV2/v2
        GET_STRANGER_STAR_MUSIC,
        //crbt/dedicate/v1
        CRBT_GIFT,
        //crbt/accept/v1
        CRBT_ACCEPT,
        //onmedia/getFeedNotify
        ONMEDIA_GET_FEED_NOTIFY,
        //star/starRoomTab
        GET_STAR_ROOM_TAB,
        //v1/sharefblog/savelog
        LOG_SHARE_FB,
        //mobileapps/get/v1
        GET_APP_LIST,
        //mobileapps/log/v1
        SEND_LOG_APP,
        //
        GET_METADATA_WITH_ACTION,
        //onMediaBackendBiz/onmedia/getLikes/statistic/v2
        GET_LIKE_TITLE_V2,
        //
        GET_PACK_DATA_INFO,
        //fakemo/send/v2
        FAKE_MO,
        //v1/appdriver/getListApp
        GET_APP_DRIVER,
        // -1 notify onmedia
        REDUCE_TOTAL_NOTIFY,
        //api/group/upload
        AVATAR_GROUP_UPLOAD,
        //api/group/download
        AVATAR_GROUP_DOWNLOAD,
        //user/setting/hideStrangleHistory
        SET_HIDE_STRANGER_HISTORY,
        //appdriver/redirectUrl
        APP_DRIVER_REDIRECT,
        //musicRoom/getLisStrangerAround
        STRANGER_AROUND,
        //onMediaBackendBiz/onmedia/actionApp/v6
        ONMEDIA_ACTION_APP_V6,
        //fortune/double/v1
        GET_FORTUNE,
        //onMediaBackendBiz/onmedia/getDetailNotify/v1
        ONMEDIA_GET_DETAIL_NOTIFY_V1,
        //musicRoom/delete
        DELETE_STRANGER_HISTORY,
        //onMediaBackendBiz/offical/follow
        ONMEDIA_FOLLOW_OFFICIAL,
        //onMediaBackendBiz/offical/getProfile
        ONMEDIA_GET_PROFILE_OFFICIAL,
        //onMediaBackendBiz/offical/getActivities
        ONMEDIA_GET_ACTIVITIES_OFFICIAL_V2,
        //onMediaBackendBiz/onmedia/unfollowfeed
        ONMEDIA_UNFOLLOW_OFFICIAL_TIMELINE,
        //inapp/actionSms/v1
        INAPP_ACTION_SMS_SENT,
        //inapp/checkSmsFree/v1
        INAPP_CHECK_FREE_SMS,
        //inviteroom/inviteRoom/v1
        REQUEST_INVITE_ROOM,
        ///RingMeAPI/search/all/v1
        SEARCH_USER_RINGME,
        //onMediaBackendBiz/onmedia/discovery/list/v1
//        ONMEDIA_GET_CONTENT_DISCOVERY,
        //user/setting/autoConvertSmsOut?msisdn=%1$s&type=%2$s&timestamp=%3$s&security=%4$s
        AUTO_SMS_OUT,
        //onmedia/relationship/sender/requestFriend
        SOCIAL_SEND_REQUEST,
        //onmedia/relationship/receiver/acceptRequest
        SOCIAL_ACCEPT_REQUEST,
        //onmedia/relationship/sender/cancelRequest
        SOCIAL_CANCEL_MY_REQUEST,
        //onmedia/relationship/receiver/cancelRequest
        SOCIAL_CANCEL_FRIEND_REQUEST,
        //onmedia/relationship/cancelFriend
        SOCIAL_CANCEL_FRIEND,
        //onmedia/relationship/status
        SOCIAL_GET_DETAIL,
        //onmedia/relationship/receiver/pendingRequests
        SOCIAL_GET_FRIEND_REQUESTS,
        //onmedia/relationship/list/friends
        SOCIAL_GET_FRIENDS,
        //poll/v1/create
        POLL_CREATE,
        //poll/v1/vote
        POLL_VOTE,
        //poll/v1/find/byId
        POLL_GET_DETAIL,
        //poll/v1/find/voters/byPoll
        POLL_GET_ITEM_DETAIL,
        //game/luckywheel/v3/spin
        LUCKY_WHEEL_SPIN,
        //game/luckywheel/v1/turn/askingHelp
        LUCKY_WHEEL_SOS_SEND,
        //game/luckywheel/v1/turn/acceptHelp
        LUCKY_WHEEL_SOS_ACCEPT,
        LUCKY_WHEEL_GET_BUDGET,
        //onMediaBackendBiz/onmedia/item/getLikesAndComment/v1 //ko dung nau
        ONMEDIA_GET_LIKE_AND_COMMENT_IMAGE,
        //RingMeAPI/accumulate/getListAccumulate/v4
        GET_LIST_ACCUMULATE_V4,
        //RingMeAPI/accumulate/log
        LOG_ACCUMULATE,
        //accumulate/convertPoint/v2
        ACCUMULATE_CONVERT,
        //RingMeAPI/accumulate/getPoint/v1
        ACCUMULATE_GET_POINT,
        //campaign/submit/v2
        DEEPLINK_CAMPAIGN,
        //game/qr/v1/scan
        SCAN_QR_CODE,
        ///feedback/getServiceActionList/v2
        FEEDBACK_GET_DEFAULT_V2,
        //feedback/getAction/v2
        FEEDBACK_SEND_KEY_V2,
        //api/onbox/list-video
        GET_LIST_VIDEO_HOT,
        ///api/onbox/detail-video?id=%1$s&msisdn=%2$s
        GET_DETAIL_VIDEO,
        //api/onbox/search?q=%1$s
        SEARCH_VIDEO,
        //sms/receive
        SETTING_RECEIVE_SMSOUT,
        //config/getOfficalAcount/v2?msisdn=%1$s&timestamp=%2$s&security=%3$s
        GET_LIST_OFFICER_ACCOUNT,
        //config/searchOfficalAcc/v1?msisdn=%1$s&timestamp=%2$s&security=%3$s&content=%4$s
        SEARCH_OFFICER_ACCOUNT,
        //memory/getListTemplate
        BOOK_GET_TEMPLATES,
        //memory/getTemplateDetail
        BOOK_GET_TEMPLATE_DETAIL,
        //memory/getListSticker
        BOOK_GET_STICKERS,
        //api/media/upload-luubut
        BOOK_UPLOAD_IMAGE,
        //memory/save
        BOOK_SAVE,
        //memory/assigned
        BOOK_ASSIGN_PAGE,
        //memory/getLstMemory
        BOOK_GET_LIST_BOOK,
        //memory/getMemory
        BOOK_GET_BOOK_DETAIL,
        //memory/getListAssignedPage
        BOOK_GET_PAGES_ASSIGNED,
        //proccesMemory/v1
        BOOK_PROCESS_BOOK,
        //memory/getListBackground
        BOOK_GET_BACKGROUND,
        //memory/getListSong
        BOOK_GET_MUSIC,
        //memory/getLstVote
        BOOK_VOTE_GET_LIST,
        //memory/getVoteDetail
        BOOK_VOTE_GET_DETAIL,
        //memory/voteMemory
        BOOK_VOTE,
        //document/getList
        GET_GROUP_DOCUMENT,
        //game/read/news/v1/success
        SPONSOR_READ_SUCCESS,
        //onMediaBackendBiz/onmedia/getDetailUrl
        ONMEDIA_GET_DETAIL_URL,
        //onMediaBackendBiz/onmedia/album
        ONMEDIA_UPLOAD_ALBUM,
        //onMediaBackendBiz/onmedia/getImageDetail/v3
        ONMEDIA_GET_IMAGE_DETAIL_V3,
        //onMediaBackendBiz/onmedia/getHomeTimelinePaging/v6
        ONMEDIA_GET_HOME_TIMELINE_V6,
        //onMediaBackendBiz/onmedia/relationship/suggest/om
        ONMEDIA_GET_LIST_SUGGEST_FRIEND_V2,
        //game/listgame/v1
        GET_LIST_GAME,
        //api/onbox/upload-link
        SHARE_SIEU_HAI,
        //callout/getRemainTime
        CALL_SUBSCRIPTION_GET_REMAIN,
        //api/media/upload-onmedia
        UPLOAD_IMAGE_SOCIAL_ONMEDIA,
        //api/keeng/video-keeng
        GET_LIST_VIDEO_DISCOVERY,
        //confideStrangers/post
        POST_STRANGER_CONFIDE,
        //confideStrangers/cancel
        CANCEL_STRANGER_CONFIDE,
        //confideStrangers/accept
        ACCEPT_STRANGER_CONFIDE,
        //confideStrangers/getList
        GET_STRANGER_CONFIDE,
        //user/setBirthdayReminder
        SET_REMINDER,
        GET_VALUE_BUDGET_LUCKY_WHEEL,
        //call_logger/freecall_api
        LOGGER_CALL,
        //api/onbox/seach-video
        SEARCH_VIDEO_NEXT,
        //api/onbox/detail-video-sieu-hai
        DETAIL_VIDEO_NEXT,
        //onmedia/relationship/suggest/ct
        GET_SUGGEST_CONTACT,
        //sendlogs/save
        UPLOAD_LOG,
        LOG_VIEW_SIEUHAI,
        //block/getBlockList/v5
        GET_BLOCKLIST_V5,
        //block/setBlockChat/v5
        SET_BLOCKLIST_V5,
        //RingMeAPI/call_logger/report_bwe
        LOGGER_CALL_QUALITY,
        //game/shaking/shake/v1
        GAME_SHAKE,
        //RingMeAPI/lixi//open
        LIXI_OPEN,
        //RingMeAPI/lixi//listimage
        LIXI_GET_LIST_IMAGE,
        //RingMeAPI/lixi/log
        LIXI_LOG_BPLUS_FAIL,
        //avno/register
        AVNO_REGISTER,
        //avno/suggestion
        AVNO_SUGGEST,
        //avno/search
        AVNO_SEARCH,
        //RingMeAPI/game/shaking/womenday/v1
        GET_GIFT_WOMEN,
        //RingMeAPI/rating/call
        RATING_CALL,
        //RingMeAPI/avno/payment/scratchCard
        AVNO_PAYMENT_SCRATCH_CARD,
        //RingMeAPI/avno/payment/convertSpoint
        AVNO_PAYMENT_CONVERT_SPOINT,
        //api/profile/uploadic
        AVNO_UPLOAD_IC,
        //api/profile/removeic
        AVNO_REMOVE_IC,
        //RingMeAPI/mucroom/getlist/v2
        GET_LIST_GROUP,
        //api/log/upload
        UPLOAD_LOG_DEBUG,
        ///RingMeAPI/avno/notify
        BROADCAST_CHANGE_NUMBER_AVNO,
        //RingMeAPI/callout/cancelPkg
        CALLOUT_CANCEL_PACKAGE,
        //RingMeAPI/callout/registerPkg
        CALLOUT_REGISTER_PACKAGE,
        //RingMeAPI/callout/trying
        CALL_SUBSCRIPTION_SET_FREE_FROM_CALLOUTGUIDE,
        //RingMeAPI/callout/setFree
        CALLOUT_REGISTER_FREE,     //setfree
        //
        AVNO_ABORT,
        //
        SEND_LOG_KPI,
        //RingMeAPI/location/postLocation
        POST_LOCATION,
        ///RingMeAPI/genotp/getCountry
        GET_COUNTRY,
        //RingMeAPI/callout/setCalledLimited
        CALLOUT_SET_CALL_LIMITED,
        //RingMeAPI/avno/registerPkg/v2
        AVNO_REGISTER_PACKAGE_V2,
        //RingMeAPI/avno/cancelPkg/v2
        AVNO_CANCEL_PACKAGE_V2,
        //RingMeAPI/config/getPopup
        GET_POPUP_INTRO,
        //RingMeAPI/sendlogs/save/v2
        LOG_ERROR,
        //RingMeAPI/callout/getInfo
        CALLOUT_GET_INFO,
        //RingMeAPI/callout/getSavingStatistic
        SAVING_STATISTIC
    }


    public static class Features {

        public static final boolean FLAG_SUPPORT_SEARCH_MESSAGE = true;
        public static final boolean FLAG_SUPPORT_PERMISSION_GUIDELINE = true;
    }

}