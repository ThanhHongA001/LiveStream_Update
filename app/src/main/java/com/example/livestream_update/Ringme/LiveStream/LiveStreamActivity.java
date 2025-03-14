package com.example.livestream_update.Ringme.LiveStream;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.OrientationEventListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.vtm.R;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.BuildConfig;
import com.vtm.ringme.common.api.http.HttpCallBack;
import com.vtm.ringme.common.api.video.channel.ChannelApiImpl;
import com.vtm.ringme.common.utils.DeviceUtils;
import com.vtm.ringme.common.utils.ScreenManager;
import com.vtm.ringme.common.utils.image.ImageManager;
import com.vtm.ringme.common.utils.player.RingMePlayer;
import com.vtm.ringme.common.utils.player.RingMePlayerUtil;
import com.vtm.ringme.customview.NonSwipeableViewPager;
import com.vtm.ringme.customview.VideoPlaybackControlView;
import com.vtm.ringme.helper.LogDebugHelper;
import com.vtm.ringme.helper.ReportHelper;
import com.vtm.ringme.helper.TextHelper;
import com.vtm.ringme.helper.encrypt.EncryptUtil;
import com.vtm.ringme.livestream.adapter.ViewCommentAdapter;
import com.vtm.ringme.livestream.fragment.CommentLiveStreamFragment;
import com.vtm.ringme.livestream.fragment.DisableCommentLiveStreamFragment;
import com.vtm.ringme.livestream.listener.PopupLiveStreamListener;
import com.vtm.ringme.livestream.model.ConfigLiveComment;
import com.vtm.ringme.livestream.model.LiveStreamCommentModel;
import com.vtm.ringme.livestream.model.LiveStreamMessage;
import com.vtm.ringme.livestream.network.APICallBack;
import com.vtm.ringme.livestream.network.RetrofitClientInstance;
import com.vtm.ringme.livestream.network.parse.RestLiveStreamMessage;
import com.vtm.ringme.model.tab_video.Channel;
import com.vtm.ringme.model.tab_video.Video;
import com.vtm.ringme.tabvideo.playVideo.dialog.QualityVideoDialog;
import com.vtm.ringme.utils.InputMethodUtils;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.utils.ToastUtils;
import com.vtm.ringme.values.Constants;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LiveStreamActivity extends AppCompatActivity {

    private static final String TAG = LiveStreamActivity.class.getSimpleName();
    @BindView(R.id.ivVideo)
    ImageView ivVideo;
    @BindView(R.id.frVideo)
    FrameLayout frVideo;
    @BindView(R.id.frController)
    FrameLayout frController;
    @BindView(R.id.pagerComment)
    NonSwipeableViewPager pagerComment;
    private ViewCommentAdapter adapter;
    private int currentPagerPos = 0;
    private Video currentVideo;
    private String playerName;
    private ApplicationController app;
    private BottomSheetLiveStream bottomSheetLiveStream;
    private int widthScreen, heightScreen, minHeightComment, maxHeightComment, heightStatusBar;
    private boolean isLikedVideo, isFollowedVideo;
    private boolean isFullScreen = false;
    private long countLive = 1;
    private ConfigLiveComment configLiveComment;
    private int heightBoxComment = 0;
    private boolean isVerticalVideo = false;
    private boolean isBlockSpam = false;
    private boolean isLandscape = false;
    private RingMePlayer ringMePlayer;

    private VideoPlaybackControlView.CallBackListener callBackListener = new VideoPlaybackControlView.CallBackListener() {
        @Override
        public void onPlayerStateChanged(int stage) {

        }

        @Override
        public void onPlayerError(String error) {
//            try {
//                currentPagerPos = 0;
//                if (pagerComment != null) {
//                    pagerComment.setCurrentItem(0);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            LogDebugHelper.getInstance().logDebugContent("LiveStream video player error: " + error);
            ReportHelper.reportError(ApplicationController.self(), ReportHelper.DATA_TYPE_LIVE_STREAM_SCREEN, "LiveStream video player error: " + error);
        }

        @Override
        public void onVisibilityChange(int visibility) {
            Log.i(TAG, "onVisibilityChange: " + visibility);
        }

        @Override
        public void onFullScreen() {
        }

        @Override
        public void onMute(boolean flag) {
        }

        @Override
        public void onTimeChange(long time) {
        }

        @Override
        public void onHaveSeek(boolean flag) {
        }

        @Override
        public void onPlayNextVideo() {
        }

        @Override
        public void onPlayPreviousVideo() {
        }

        @Override
        public void onPlayPause(boolean state) {
        }

        @Override
        public void onSmallScreen() {
        }

        @Override
        public void onMoreClick() {
        }

        @Override
        public void onReplay() {
        }

        @Override
        public void onQuality() {
        }

        @Override
        public void onRequestAds(String position) {
        }

        @Override
        public void onShowAd() {
        }

        @Override
        public void onHideAd() {
        }

        @Override
        public void onHideController() {
        }

        @Override
        public void onShowController() {
        }

        @Override
        public void autoSwitchPlayer() {
            if (ringMePlayer != null) {
                ringMePlayer.setupLiveStream();
                ringMePlayer.setLandscape(isLandscape);
            }
        }

        @Override
        public void onClickViewFrame() {
            if (bottomSheetLiveStream != null) bottomSheetLiveStream.dismiss();
            if (currentVideo == null) return;
            bottomSheetLiveStream = BottomSheetLiveStream.showContextMenu(LiveStreamActivity.this
                    , currentVideo, isLandscape, isVerticalVideo, widthScreen, heightScreen, currentPagerPos, countLive, new PopupLiveStreamListener() {
                        @Override
                        public void subscriberChannel(Channel channel) {
                            isFollowedVideo = true;
                            subscribeChannel(channel);
                            currentVideo.getChannel().setFollow(channel.isFollow());
                            if (CommentLiveStreamFragment.self() != null)
                                CommentLiveStreamFragment.self().updateStatusFollowUi();
                        }

                        @Override
                        public void likeVideo() {
                            LiveStreamActivity.this.likeVideo();
                        }

                        @Override
                        public void commentVideo() {
                            if (pagerComment != null) pagerComment.setCurrentItem(1);
                            //Show ban phim
                            new Handler().postDelayed(() -> {
                                if (CommentLiveStreamFragment.self() != null)
                                    CommentLiveStreamFragment.self().onClickShowKeyboard();
                            }, 200);
                        }

                        @Override
                        public void shareVideo() {
                            LiveStreamActivity.this.shareVideo(false);
                        }

                        @Override
                        public void onFullScreen(boolean isFullScreen) {
                            goToFullScreen(true);
                        }

                        @Override
                        public void qualityVideo() {
                            QualityVideoDialog mSpeedVideoDialog = new QualityVideoDialog(LiveStreamActivity.this);
                            mSpeedVideoDialog.setCurrentVideo(currentVideo);
                            mSpeedVideoDialog.setOnQualityVideoListener((idx, video, resolution) -> {
                                if (currentVideo != null && video != null
                                        && !TextUtils.isEmpty(currentVideo.getId()) && !TextUtils.isEmpty(video.getId())
                                        && currentVideo.getId().equals(video.getId())) {
                                    if (currentVideo.getIndexQuality() == idx) return;
                                    currentVideo.setIndexQuality(idx);
                                    if (app != null)
                                        app.setConfigResolutionVideo(resolution.getKey());
                                    long position;
                                    long duration;
                                    RingMePlayer mPlayer = RingMePlayerUtil.getPlayer(playerName);
                                    if (mPlayer != null) {
                                        position = mPlayer.getCurrentPosition();
                                        duration = mPlayer.getDuration();
                                        mPlayer.prepare(resolution.getVideoPath());
                                        mPlayer.seekTo(Math.min(position, duration));
                                    }

                                }
                            });
                            mSpeedVideoDialog.show();
                        }

                        @Override
                        public void dismissPopup() {
                            bottomSheetLiveStream = null;
                        }

                        @Override
                        public void onBack() {
                            if (isLandscape)
                                goToFullScreen(true);
                            else
                                finish();
                        }

                        @Override
                        public void switchComment() {
                            if (currentPagerPos == 0) {
                                currentPagerPos = 1;
                            } else {
                                currentPagerPos = 0;
                            }
                            if (pagerComment != null) pagerComment.setCurrentItem(currentPagerPos);
                            if (isLandscape) {
                                if (currentPagerPos == 1)
                                    ToastUtils.showToast(LiveStreamActivity.this, getString(R.string.show_comment));
                                else
                                    ToastUtils.showToast(LiveStreamActivity.this, getString(R.string.hide_comment));
                            }
                        }
                    });
        }
    };

    private OrientationEventListener orientationEventListener;
    private int lastOrientation;
    private Runnable runnableWhenLandscape = () -> {
        Log.e(TAG, "runnableWhenLandscape");
        isFullScreen = false;
        goToFullScreen(false);
    };
    private Runnable runnableWhenPortrait = () -> {
        Log.e(TAG, "runnableWhenPortrait");
        isFullScreen = true;
        goToFullScreen(false);
    };
    private Runnable runnableEnableRotateSensor = () -> {
        if (orientationEventListener != null && orientationEventListener.canDetectOrientation()) {
            orientationEventListener.enable();
        }
    };

    private void initOrientationListener() {
        lastOrientation = getRequestedOrientation();
        orientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_UI) {
            int curOrientation;

            @Override
            public void onOrientationChanged(int orientation) {
                if ((orientation >= 0 && orientation <= 45) || (orientation > 315 && orientation <= 360)) {
                    curOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                } else if (orientation > 45 && orientation <= 135) {
                    curOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                } else if (orientation > 135 && orientation <= 225) {
                    curOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                } else if (orientation > 225 && orientation <= 315) {
                    curOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                } else {
                    curOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
                }
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "orientationEventListener onOrientationChanged lastOrientation: " + lastOrientation
                            + " \tcurOrientation: " + curOrientation
                            + " \torientation: " + orientation
                            + " \tactivity.orientation: " + getResources().getConfiguration().orientation
                    );
                if (curOrientation != lastOrientation) {
                    lastOrientation = curOrientation;
                    if (frVideo != null) {
                        frVideo.removeCallbacks(runnableWhenPortrait);
                        frVideo.removeCallbacks(runnableWhenLandscape);
                        if (currentVideo != null && currentVideo.isVideoLandscape() && !DeviceUtils.isDeviceLockRotate(LiveStreamActivity.this)) {
                            if (curOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                                frVideo.postDelayed(runnableWhenPortrait, 400);
                            } else if (curOrientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE || curOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                                frVideo.postDelayed(runnableWhenLandscape, 400);
                            }
                        }
                    }
                }
            }
        };
        orientationEventListener.disable();
    }

    public void enableRotateSensor() {
        if (frVideo != null) {
            frVideo.removeCallbacks(runnableEnableRotateSensor);
            frVideo.postDelayed(runnableEnableRotateSensor, 600);
        }
    }

    public void disableRotateSensor() {
        if (frVideo != null)
            frVideo.removeCallbacks(runnableEnableRotateSensor);
        if (orientationEventListener != null) orientationEventListener.disable();
    }

    public static void startActivity(AppCompatActivity activity, Video video, ConfigLiveComment configLive) {
        Intent intent = new Intent(activity, LiveStreamActivity.class);
        intent.putExtra(Constants.TabVideo.VIDEO, video);
        intent.putExtra("CONFIG_LIVE", configLive);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.rm_activity_livestream);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            Serializable serializable;
            serializable = intent.getSerializableExtra(Constants.TabVideo.VIDEO);
            if (serializable instanceof Video) currentVideo = (Video) serializable;
            serializable = intent.getSerializableExtra("CONFIG_LIVE");
            if (serializable instanceof ConfigLiveComment)
                configLiveComment = (ConfigLiveComment) serializable;
        }
        if (currentVideo == null) {
            ToastUtils.showToast(ApplicationController.self(),ApplicationController.self().getString(R.string.e601_error_but_undefined));
            finish();
        }
        app = (ApplicationController) getApplication();
        int widthTmp = ScreenManager.getWidth(this);
        int heightTmp = ScreenManager.getHeight(this);
        widthScreen = Math.min(widthTmp, heightTmp);
        heightScreen = Math.max(widthTmp, heightTmp);
        maxHeightComment = heightScreen - widthScreen * 9 / 16;
        minHeightComment = heightScreen / 2;
        maxHeightComment = Math.max(minHeightComment, maxHeightComment);
        heightStatusBar = app.getStatusBarHeight();
        Log.d(TAG, "heightScreen: " + heightScreen + "\twidthScreen: " + widthScreen
                + "\tmaxHeightComment: " + maxHeightComment + "\tminHeightComment: " + minHeightComment
                + "\theightStatusBar: " + heightStatusBar
        );
        TextHelper.getInstant().initSensitiveWordPatterns(app);
        TextHelper.getInstant().initBadWordPatterns(app);
        initView();
        initOrientationListener();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }

    private void initView() {
        double aspectRatio = currentVideo.getAspectRatio();
        isVerticalVideo = aspectRatio < 1 && aspectRatio > 0;
        isLandscape = false;
        playerName = String.valueOf(System.nanoTime());
        ringMePlayer = RingMePlayerUtil.getPlayer(playerName);
        ringMePlayer.addPlayerViewTo(frVideo);
        ringMePlayer.addControllerListener(callBackListener);
        ringMePlayer.setupLiveStream();
        ringMePlayer.setLandscape(isLandscape);
        setSizeFrameVideo(aspectRatio);
        initViewPager();
        ImageManager.showImageNormalV2(currentVideo.getImagePath(), ivVideo);
        ivVideo.postDelayed(() -> {
            if (ringMePlayer != null) ringMePlayer.prepare(currentVideo);
            if (CommentLiveStreamFragment.self() != null)
                CommentLiveStreamFragment.self().onChangeOrientation(isLandscape, isVerticalVideo, widthScreen, heightScreen);
            if (DisableCommentLiveStreamFragment.self() != null)
                DisableCommentLiveStreamFragment.self().onChangeOrientation(isLandscape, isVerticalVideo);
            if (bottomSheetLiveStream != null)
                bottomSheetLiveStream.onChangeOrientation(isLandscape);
            if (ringMePlayer != null) ringMePlayer.setLandscape(isLandscape);
        }, 200);
    }

    private void initViewPager() {
        adapter = new ViewCommentAdapter(getSupportFragmentManager());
        adapter.addFragment(DisableCommentLiveStreamFragment.newInstance(), "");
        adapter.addFragment(CommentLiveStreamFragment.newInstance(currentVideo, configLiveComment), "");
        pagerComment.setAdapter(adapter);
        pagerComment.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPagerPos = position;
                if (bottomSheetLiveStream != null)
                    bottomSheetLiveStream.switchComment(currentPagerPos);
                InputMethodUtils.hideSoftKeyboard(LiveStreamActivity.this);
            }
        });
        currentPagerPos = 1;
        pagerComment.setCurrentItem(1);
        pagerComment.setPagingEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ringMePlayer != null) ringMePlayer.removeControllerListener(callBackListener);
        RingMePlayerUtil.getInstance().removerPlayerBy(playerName);
        isLikedVideo = false;
        isFollowedVideo = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ringMePlayer != null) ringMePlayer.setPlayWhenReady(false);
        disableRotateSensor();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ringMePlayer != null) ringMePlayer.setPlayWhenReady(true);
        enableRotateSensor();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public void setSizeFrameVideo(double aspectRatio) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (isVerticalVideo) {
            ViewGroup.LayoutParams layoutParams = frController.getLayoutParams();
            layoutParams.width = widthScreen;
            layoutParams.height = heightScreen;
            frController.setLayoutParams(layoutParams);
            frController.requestLayout();

            RelativeLayout.LayoutParams layoutParamsPager = (RelativeLayout.LayoutParams) pagerComment.getLayoutParams();
            layoutParamsPager.addRule(RelativeLayout.BELOW, 0);
            heightBoxComment = minHeightComment;
            layoutParamsPager.width = widthScreen;
            layoutParamsPager.height = heightBoxComment;
            pagerComment.setLayoutParams(layoutParamsPager);
            pagerComment.requestLayout();
        } else {
            ViewGroup.LayoutParams layoutParams = frController.getLayoutParams();
            layoutParams.width = widthScreen;
            int heightVideo = (int) (widthScreen / aspectRatio);
            heightVideo = Math.min(heightVideo, heightScreen);
            layoutParams.height = heightVideo;
            frController.setLayoutParams(layoutParams);
            frController.requestLayout();

            RelativeLayout.LayoutParams layoutParamsPager = (RelativeLayout.LayoutParams) pagerComment.getLayoutParams();
            layoutParamsPager.addRule(RelativeLayout.BELOW, 0);
            heightBoxComment = heightScreen - heightVideo - heightStatusBar;
            heightBoxComment = Math.max(heightBoxComment, minHeightComment);
            heightBoxComment = Math.min(heightBoxComment, maxHeightComment);
            layoutParamsPager.width = widthScreen;
            layoutParamsPager.height = heightBoxComment;
            pagerComment.setLayoutParams(layoutParamsPager);
            pagerComment.requestLayout();
        }
        LogDebugHelper.getInstance().logDebugContent("LiveStream: video player portrait");
        ReportHelper.reportError(ApplicationController.self(), ReportHelper.DATA_TYPE_LIVE_STREAM_SCREEN, "LiveStream: video player portrait");
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void goToFullScreen(boolean hideComment) {
        if (!isFullScreen) {
            //Fullscreen
            isFullScreen = true;
            if (isVerticalVideo) {
                //Neu la video doc
                isLandscape = false;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                ViewGroup.LayoutParams layoutParams = frController.getLayoutParams();
                layoutParams.width = widthScreen;
                layoutParams.height = heightScreen;
                frController.setLayoutParams(layoutParams);
                frController.requestLayout();

                RelativeLayout.LayoutParams layoutParamsPager = (RelativeLayout.LayoutParams) pagerComment.getLayoutParams();
                layoutParamsPager.addRule(RelativeLayout.BELOW, 0);
                pagerComment.setLayoutParams(layoutParamsPager);
                pagerComment.requestLayout();
                LogDebugHelper.getInstance().logDebugContent("LiveStream: video player portrait");
                ReportHelper.reportError(ApplicationController.self(), ReportHelper.DATA_TYPE_LIVE_STREAM_SCREEN, "LiveStream: video player portrait");
            } else {
                //Neu la video ngang
                isLandscape = true;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                ViewGroup.LayoutParams layoutParams = frController.getLayoutParams();
                layoutParams.width = heightScreen;
                layoutParams.height = widthScreen;
                frController.setLayoutParams(layoutParams);
                frController.requestLayout();

                RelativeLayout.LayoutParams layoutParamsPager = (RelativeLayout.LayoutParams) pagerComment.getLayoutParams();
                layoutParamsPager.addRule(RelativeLayout.BELOW, 0);
                layoutParamsPager.width = heightScreen;
                layoutParamsPager.height = 6 * widthScreen / 10;
                pagerComment.setLayoutParams(layoutParamsPager);
                pagerComment.requestLayout();

                if (hideComment && ivVideo != null) {
                    ivVideo.postDelayed(() -> {
                        currentPagerPos = 0;
                        if (pagerComment != null) pagerComment.setCurrentItem(0);
                    }, 200);
                }
                LogDebugHelper.getInstance().logDebugContent("LiveStream: video player landscape");
                ReportHelper.reportError(ApplicationController.self(), ReportHelper.DATA_TYPE_LIVE_STREAM_SCREEN, "LiveStream: video player landscape");
            }
        } else {
            //Exit fullscreen
            isFullScreen = false;
            isLandscape = false;
            pagerComment.setCurrentItem(currentPagerPos);
            setSizeFrameVideo(currentVideo.getAspectRatio());
        }
        if (CommentLiveStreamFragment.self() != null)
            CommentLiveStreamFragment.self().onChangeOrientation(isLandscape, isVerticalVideo, widthScreen, heightScreen);
        if (DisableCommentLiveStreamFragment.self() != null)
            DisableCommentLiveStreamFragment.self().onChangeOrientation(isLandscape, isVerticalVideo);
        if (bottomSheetLiveStream != null)
            bottomSheetLiveStream.onChangeOrientation(isLandscape);
        if (ringMePlayer != null) ringMePlayer.setLandscape(isLandscape);
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public boolean isLikedVideo() {
        return isLikedVideo;
    }

    public boolean isFollowedVideo() {
        return isFollowedVideo;
    }

    public Video getCurrentVideo() {
        return currentVideo;
    }

    @Override
    public void onBackPressed() {
        if (CommentLiveStreamFragment.self() != null && CommentLiveStreamFragment.self().isShowBoxReplyComment())
            CommentLiveStreamFragment.self().resetParamsReply();
        else if (isLandscape)
            goToFullScreen(true);
        else
            super.onBackPressed();
    }

    public void likeVideo() {
        currentVideo.setLike(!currentVideo.isLike());
        if (currentVideo.isLike()) {
            currentVideo.setTotalLike(currentVideo.getTotalLike() + 1);
        } else {
            currentVideo.setTotalLike(currentVideo.getTotalLike() - 1);
            if (currentVideo.getTotalLike() < 0)
                currentVideo.setTotalLike(0);
        }

        if (CommentLiveStreamFragment.self() != null)
            CommentLiveStreamFragment.self().updateStatusLikeUi();
        if (bottomSheetLiveStream != null)
            bottomSheetLiveStream.likeVideoUi();
    }

    public void shareVideo(boolean shareSocial) {
        //Xu ly client
        if (currentVideo != null) {
            if (shareSocial) {

            } else {

            }
        }
    }

    public boolean isBlockSpam() {
        return isBlockSpam;
    }

    public void setBlockSpam(boolean blockSpam) {
        isBlockSpam = blockSpam;
    }

    public void postMessage(LiveStreamMessage message) {
        if (app == null || configLiveComment == null || isBlockSpam) return;
        JSONObject jsonObject = new JSONObject();
        try {
            String msisdn = app.getJidNumber();
            if (message.getType() == LiveStreamMessage.TYPE_NORMAL) {
                message.setId(EncryptUtil.encryptMD5(msisdn + System.currentTimeMillis()));
            }
            jsonObject.put("avatar", message.getLastAvatar());
            jsonObject.put("countLike", message.getCountLike());
            jsonObject.put("from", message.getNameSender());
            jsonObject.put("id", message.getId());
            jsonObject.put("idRep", message.getIdRep());
            jsonObject.put("isLike", 0);
            jsonObject.put("levelMessage", message.getLevelMessage());
            jsonObject.put("message", message.getContent());
            jsonObject.put("msisdn", msisdn);
            jsonObject.put("roomId", message.getRoomId());
            jsonObject.put("rowId", message.getRowId());
            jsonObject.put("secinf", "");
            jsonObject.put("tags", message.getTags());
            jsonObject.put("time", message.getTimeStamp());
            jsonObject.put("timestamp", message.getTimeStamp());
            jsonObject.put("token", "");
            jsonObject.put("type", message.getType());

            if (currentVideo != null) {
                String actionType;
                if (message.getType() == LiveStreamMessage.TYPE_LIKE_COMMENT)
                    actionType = "LIKECOMMENT";
                else if (message.getType() == LiveStreamMessage.TYPE_UNLIKE_COMMENT)
                    actionType = "UNLIKE";
                else
                    actionType = "COMMENT";

                LiveStreamCommentModel commentModel = new LiveStreamCommentModel();
                commentModel.setSite(currentVideo.getLink());
                commentModel.setItemName(currentVideo.getTitle());
                commentModel.setContentUrl(currentVideo.getLink());
                commentModel.setMsisdn(msisdn);
                commentModel.setActionType(actionType);
                commentModel.setUrl(currentVideo.getLink());
                commentModel.setImgUrl(currentVideo.getImagePath());
                commentModel.setStatus(message.getContent());
                commentModel.setUserType(0);
                commentModel.setPostActionFrom("mochavideo");
                commentModel.setStampId(message.getTimeStamp());
                commentModel.setStampIdOfUrl(0);
                commentModel.setContentAction(actionType);
                commentModel.setChannelId(currentVideo.getChannel().getId());
                commentModel.setChannelName(currentVideo.getChannel().getName());
                commentModel.setChannelAvatar(currentVideo.getChannel().getUrlImage());
                commentModel.setUrlTemp(currentVideo.getLink());
                commentModel.setNumfollow(0);

                Gson gson = new Gson();
                String json = gson.toJson(commentModel);

                jsonObject.put("data", json);
            }

        } catch (Exception e) {
            Log.e(TAG, e);
        }
        String json = jsonObject.toString();
        Log.d(TAG, "json: " + json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

        RetrofitClientInstance retrofitClientInstance = new RetrofitClientInstance(configLiveComment);
        retrofitClientInstance.postMessage(requestBody, new APICallBack<RestLiveStreamMessage>() {
            @Override
            public void onResponse(retrofit2.Response<RestLiveStreamMessage> response) {
                //Danh dau da like video
                if (message.getType() == LiveStreamMessage.TYPE_LIKE_COMMENT && TextUtils.isEmpty(message.getId()))
                    isLikedVideo = true;
            }

            @Override
            public void onError(Throwable error) {
            }
        });
    }

    public void subscribeChannel(Channel channel) {
        ChannelApiImpl channelApi = new ChannelApiImpl(app);
        channelApi.subscribeChannel(channel, new HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                Log.i(TAG, "onSuccess: " + data);
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
                Log.i(TAG, "onFailure: " + message);
                ToastUtils.showToast(LiveStreamActivity.this, getString(R.string.e601_error_but_undefined));
            }
        });
    }

    public void setCountLive(long countLive) {
        if (countLive > 0) {
            this.countLive = countLive;
            if (bottomSheetLiveStream != null)
                bottomSheetLiveStream.setCountLive(countLive);
        }
    }

}
