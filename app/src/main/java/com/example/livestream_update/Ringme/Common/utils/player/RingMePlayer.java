package com.example.livestream_update.Ringme.Common.utils.player;

import static android.content.Context.AUDIO_SERVICE;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.vtm.R;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.BuildConfig;
import com.vtm.ringme.customview.VideoPlaybackControlView;
import com.vtm.ringme.customview.VideoPlayerView;
import com.vtm.ringme.helper.NetworkHelper;
import com.vtm.ringme.model.tab_video.AdSense;
import com.vtm.ringme.model.tab_video.Resolution;
import com.vtm.ringme.model.tab_video.Video;
import com.vtm.ringme.utils.InsiderUtils;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.utils.Utilities;
import com.vtm.ringme.values.Constants;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import io.reactivex.annotations.Nullable;

public class RingMePlayer/* implements SpotXAdPlayer.Observer*/ {

    private static final String TAG = "RingMePlayer";
    private static final int ADS_LAG_TIME = 5000;
    private static final int ADS_REQUEST_TIME = 20000;
    private static final int ADS_RETRY_COUNT = 1;
    private static CookieManager DEFAULT_COOKIE_MANAGER;

    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    private final ApplicationController mApp;
    private final SimpleExoPlayer mPlayer;
    private final DataSource.Factory manifestDataSourceFactory;

    private final DataSource.Factory mediaDataSourceFactory;
    private final CopyOnWriteArrayList<Player.EventListener> listeners;
    private final CopyOnWriteArrayList<VideoPlaybackControlView.CallBackListener> callBackListeners;
    private @Nullable
    DefaultBandwidthMeter defaultBandwidthMeter;
    private Video mVideo;
    private String currentMediaUrl = "";
    private String currentSubTitle = "";
    private VideoPlayerView mPlayerView;
    private VideoPlaybackControlView mControlView;
    private EventLogger eventLogger;
    private View infoView;
    private boolean isRelease = false;
    private boolean mini = false;
    private boolean isPaused = false;

    private AudioManager mAudioManager;
    private String lagArr = "";
    private String playArr = "";
    private String bandwidthArr = "";
    private String networkArr = "";
    private String errorDesc = "";
    // Tham so log ads
    private int adsPos = Constants.ADS_POS.FIRST;
    private String adsToken = "";
    private String spotXChannelId;
    private String spotXApiKey;
    private int errorAds;
    private int skipCode;
    private int currentState = 0;
    private long timeBufferFirst = 0;
    private long timeStart = 0;
    private long timeLagStart = 0;
    private long timePlayStart = 0;
    private long totalTimePlay = 0;
    private boolean isStartVideo = false;
    private boolean isVideoPlayed = false;
    private boolean isLogAds = true;
    private WeakHashMap<String, AdSense> mapAds;
    private boolean mIsAdDisplayed;
    private boolean isFullScreen;

    private CountDownTimer countDownTimer = null;
    private CountDownTimer countDownRequestTimer = null;
    private int timeSkipAd = 0;

    //    private SpotXInlineAdPlayer mAdPlayer;
    private Handler mHandlerAd;
    //    private SpotXAdGroup mSpotXAdGroup;
    private Runnable runnableHideAd = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "runnableHideAd");
            if (mPlayerView != null) {
                if (mPlayerView.getContainerAds() != null)
                    mPlayerView.getContainerAds().removeAllViews();
                mPlayerView.hideAds();
                mPlayerView.setPlayAds(false);
                mPlayerView.showProgressLoadingAds(false);
                mPlayerView.showButtonCloseAds(false, timeSkipAd);
            }
        }
    };
    private Runnable runnableShowAd = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "runnableShowAd");
            if (mPlayerView != null) {
                mPlayerView.showAds();
                mPlayerView.setPlayAds(true);
                mPlayerView.showProgressLoadingAds(false);
                mPlayerView.showButtonCloseAds(true, timeSkipAd);
            }
        }
    };
    private Runnable runnableRequestAd = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "runnableRequestAd adsPos: " + adsPos + ", spotXChannelId: " + spotXChannelId + ", spotXApiKey: " + spotXApiKey);
//            if (mPlayerView != null && mPlayerView.getContainerAds() != null && mApp.getCurrentActivity() != null) {
//                mPlayerView.getContainerAds().setVisibility(View.INVISIBLE);
//                FrameLayout frameLayout = new FrameLayout(mApp.getCurrentActivity());
//                mPlayerView.getContainerAds().addView(frameLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                mAdPlayer = new SpotXInlineAdPlayer(mPlayerView.getContainerAds(), mApp.getCurrentActivity());
//                mAdPlayer = new SpotXInlineAdPlayer(frameLayout);
//                mAdPlayer.registerObserver(RingMePlayer.this);
////                mAdPlayer.load(mApp.getCurrentActivity());
//                mAdPlayer.load();
//                mPlayerView.showProgressLoadingAds(adsPos > 0);
//                //mPlayerView.getContainerAds().setOnClickListener(onClickAdListener);
//            }
            startCountDownRequestAd();
        }
    };
    private VideoPlaybackControlView.CallBackListener mCallBackListener = new VideoPlaybackControlView.CallBackListener() {
        @Override
        public void onPlayerStateChanged(int stage) {
            try {
                if (Utilities.notEmpty(callBackListeners)) {
                    Log.i(TAG, "VideoPlaybackControlView.CallBackListener onPlayerStateChanged");
                    for (VideoPlaybackControlView.CallBackListener listener : callBackListeners) {
                        if (listener != null) {
                            listener.onPlayerStateChanged(stage);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void onPlayerError(String error) {
            try {
                if (Utilities.notEmpty(callBackListeners)) {
                    for (VideoPlaybackControlView.CallBackListener listener : callBackListeners) {
                        if (listener != null) {
                            listener.onPlayerError(error);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void onVisibilityChange(int visibility) {
            try {
                if (Utilities.notEmpty(callBackListeners)) {
                    for (VideoPlaybackControlView.CallBackListener listener : callBackListeners) {
                        if (listener != null) {
                            listener.onVisibilityChange(visibility);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void onFullScreen() {
            try {
                if (Utilities.notEmpty(callBackListeners)) {
                    for (VideoPlaybackControlView.CallBackListener listener : callBackListeners) {
                        if (listener != null) {
                            listener.onFullScreen();
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void onMute(boolean flag) {
            try {
                if (Utilities.notEmpty(callBackListeners)) {
                    for (VideoPlaybackControlView.CallBackListener listener : callBackListeners) {
                        if (listener != null) {
                            listener.onMute(flag);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void onTimeChange(long time) {
            try {
                if (Utilities.notEmpty(callBackListeners)) {
                    for (VideoPlaybackControlView.CallBackListener listener : callBackListeners) {
                        if (listener != null) {
                            listener.onTimeChange(time);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void onHaveSeek(boolean flag) {
            try {
                if (Utilities.notEmpty(callBackListeners)) {
                    for (VideoPlaybackControlView.CallBackListener listener : callBackListeners) {
                        if (listener != null) {
                            listener.onHaveSeek(flag);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void onPlayNextVideo() {
            try {
                if (Utilities.notEmpty(callBackListeners)) {
                    for (VideoPlaybackControlView.CallBackListener listener : callBackListeners) {
                        if (listener != null) {
                            listener.onPlayNextVideo();
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void onPlayPreviousVideo() {
            try {
                if (Utilities.notEmpty(callBackListeners)) {
                    for (VideoPlaybackControlView.CallBackListener listener : callBackListeners) {
                        if (listener != null) {
                            listener.onPlayPreviousVideo();
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void onPlayPause(boolean state) {
            try {
                if (Utilities.notEmpty(callBackListeners)) {
                    for (VideoPlaybackControlView.CallBackListener listener : callBackListeners) {
                        if (listener != null) {
                            listener.onPlayPause(state);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void onSmallScreen() {
            try {
                if (Utilities.notEmpty(callBackListeners)) {
                    for (VideoPlaybackControlView.CallBackListener listener : callBackListeners) {
                        if (listener != null) {
                            listener.onSmallScreen();
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void onMoreClick() {
            try {
                if (Utilities.notEmpty(callBackListeners)) {
                    for (VideoPlaybackControlView.CallBackListener listener : callBackListeners) {
                        if (listener != null) {
                            listener.onMoreClick();
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void onReplay() {
            try {
                if (Utilities.notEmpty(callBackListeners)) {
                    for (VideoPlaybackControlView.CallBackListener listener : callBackListeners) {
                        if (listener != null) {
                            listener.onReplay();
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void onQuality() {
            try {
                if (Utilities.notEmpty(callBackListeners)) {
                    for (VideoPlaybackControlView.CallBackListener listener : callBackListeners) {
                        if (listener != null) {
                            listener.onQuality();
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void onRequestAds(String position) {
            Log.d(TAG, "onRequestAds position: " + position);
            if (mapAds != null && isLogAds) {
                AdSense adSense = mapAds.get(position);
                if (adSense != null) {
                    spotXChannelId = adSense.getSpotXChannelId();
                    spotXApiKey = adSense.getSpotXApiKey();
                    adsPos = adSense.getStartTimeInt();
                    adsToken = adSense.getToken();
                    if (Utilities.notEmpty(spotXChannelId)) {
                        requestAds(spotXChannelId, spotXApiKey);
                    }
                }
            }
        }

        @Override
        public void onShowAd() {
            try {
                if (Utilities.notEmpty(callBackListeners)) {
                    for (VideoPlaybackControlView.CallBackListener listener : callBackListeners) {
                        if (listener != null) {
                            listener.onShowAd();
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void onHideAd() {
            try {
                if (Utilities.notEmpty(callBackListeners)) {
                    for (VideoPlaybackControlView.CallBackListener listener : callBackListeners) {
                        if (listener != null) {
                            listener.onHideAd();
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void onHideController() {
            try {
                if (Utilities.notEmpty(callBackListeners)) {
                    for (VideoPlaybackControlView.CallBackListener listener : callBackListeners) {
                        if (listener != null) {
                            listener.onHideController();
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void onShowController() {
            try {
                if (Utilities.notEmpty(callBackListeners)) {
                    for (VideoPlaybackControlView.CallBackListener listener : callBackListeners) {
                        if (listener != null) {
                            listener.onShowController();
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void autoSwitchPlayer() {
            try {
                if (Utilities.notEmpty(callBackListeners)) {
                    for (VideoPlaybackControlView.CallBackListener listener : callBackListeners) {
                        if (listener != null) {
                            listener.autoSwitchPlayer();
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }

        @Override
        public void onClickViewFrame() {
            try {
                if (Utilities.notEmpty(callBackListeners)) {
                    for (VideoPlaybackControlView.CallBackListener listener : callBackListeners) {
                        if (listener != null) {
                            listener.onClickViewFrame();
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }
    };
    private Runnable runnableLoadedAd = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "runnableLoadedAd");
//            if (mAdPlayer != null && mSpotXAdGroup != null && Utilities.notEmpty(mSpotXAdGroup.ads)) {
//                mAdPlayer.start();
//                if (!canPlayVideo()) mAdPlayer.pause();
//            } else {
//                callLogAds(errorAds); // fix loi exeption quang cao kho auto next video
//                releaseAds();
//                if (canPlayVideo()) setPlayWhenReady(true);
//            }
        }
    };
    private Runnable runnableOnPlayAd = new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "runnableOnPlayAd");
            cancelCountDownRequestAd();
            showAds();
            if (mPlayer != null && mPlayer.getPlayWhenReady()) setPlayWhenReady(false);
        }
    };
    private Runnable runnableAdError = new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "runnableAdError");
            callLogAds(errorAds);
            releaseAds();
            if (canPlayVideo()) setPlayWhenReady(true);
        }
    };
    private Runnable runnableGroupAdComplete = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "runnableGroupAdComplete");
            callLogAds(errorAds);
            releaseAds();
            if (canPlayVideo()) setPlayWhenReady(true);
            //if (mPlayerView != null) mPlayerView.showController();
        }
    };
    private boolean isSeekBeforeBuffering;
    private boolean isLossTransient = false;
    private boolean isLossTransientCanDuck = false;
    private boolean isHasAudioFocus = false;
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            Log.e(TAG, "onAudioFocusChange: " + focusChange);
            handleAudioFocusChange(focusChange);
        }
    };
    private int countRetryError = 0;
    private Player.EventListener mEventListener = new Player.EventListener() {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            Log.d(TAG, "onPlayerStateChanged playWhenReady: " + playWhenReady + ", playbackState: " + playbackState);
            updateLog(playbackState);
            if (mPlayerView != null) {
                mPlayerView.setKeepScreenOn(playWhenReady);
            }
            if (currentState != playbackState) {
                if (playbackState == Player.STATE_READY) {
                    isVideoPlayed = true;
                    changeAudioFocus(true);
                    if (playWhenReady && isAdDisplayed()) {
                        setPlayWhenReady(false);
                    }
                } else if (playbackState == Player.STATE_ENDED) {
                    changeAudioFocus(false);
                }
            }
            if (playWhenReady && playbackState == Player.STATE_ENDED || playbackState == Player.STATE_READY)
                countRetryError = 0;
            currentState = playbackState;
            if (playbackState == Player.STATE_ENDED && !isLogAds) {
                return;
            }
            try {
                if (Utilities.notEmpty(listeners)) {
                    Log.i(TAG, "Player.EventListener onPlayerStateChanged");
                    for (Player.EventListener listener : listeners) {
                        if (listener != null) {
                            listener.onPlayerStateChanged(playWhenReady, playbackState);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (playWhenReady) isPaused = false;
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            if (mVideo != null && mVideo.isLiveEnd()) {
                if (mPlayerView != null) {
                    releaseAds();
                    cancelCountDownRequestAd();
                    mPlayerView.showUiLiveEnd();
                    mPlayerView.showThumb(mVideo.getImagePath());
                }
                return;
            }
            if (canSwitchPlayer()) {
                switchPlayer(true);
            } else {
                if (mPlayerView != null) {
                    releaseAds();
                    cancelCountDownRequestAd();
                    if (mVideo != null && mVideo.isLive() && countRetryError < 3) {
                        countRetryError++;
                        prepare(currentMediaUrl);
                        return;
                    }
                    mPlayerView.showUiError();
                    if (mVideo != null) {
                        mPlayerView.showThumb(mVideo.getImagePath());
                    }
                }
                try {
                    if (Utilities.notEmpty(listeners)) {
                        for (Player.EventListener listener : listeners) {
                            if (listener != null) {
                                listener.onPlayerError(error);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    String msg = error.getCause().toString();
                    if (error.getCause().getStackTrace() != null && error.getCause().getStackTrace().length > 0) {
                        String msgDetail = error.getCause().getStackTrace()[0].toString();
                        msg += " , " + msgDetail;
                    }
                    errorDesc = msg;
                } catch (Exception e) {
                    e.printStackTrace();
                    errorDesc = "Error media";
                }

                if (mVideo != null)
                    logEnd(mVideo);
            }
            countRetryError = 0;
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            Log.i(TAG, "onPositionDiscontinuity reason: " + reason);
            switch (reason) {
                case Player.DISCONTINUITY_REASON_SEEK:
                case Player.DISCONTINUITY_REASON_SEEK_ADJUSTMENT:
                    if (!isStartVideo) {
                        isSeekBeforeBuffering = true;
                    }
                    break;

                case Player.DISCONTINUITY_REASON_PERIOD_TRANSITION:
                case Player.DISCONTINUITY_REASON_AD_INSERTION:
                case Player.DISCONTINUITY_REASON_INTERNAL:
                default:
                    isSeekBeforeBuffering = false;
                    break;
            }
        }
    };

    public RingMePlayer(ApplicationController app) {
        String whiteList = app.getPref().getString(Constants.PREFERENCE.CONFIG.WHITELIST_DEVICE, Constants.WHITE_LIST);
        if (Utilities.isEmpty(whiteList))
            whiteList = Constants.WHITE_LIST;

        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }

        defaultBandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        DefaultLoadControl loadControl = new DefaultLoadControl
                .Builder()
                .setBufferDurationsMs(0, 0, 0, 0)
                .createDefaultLoadControl();
        eventLogger = new EventLogger(trackSelector);
        listeners = new CopyOnWriteArrayList<>();
        callBackListeners = new CopyOnWriteArrayList<>();

        mAudioManager = (AudioManager) app.getSystemService(AUDIO_SERVICE);
        manifestDataSourceFactory = new DefaultDataSourceFactory(app, Util.getUserAgent(app, app.getString(R.string.app_name)));
        mediaDataSourceFactory = createDataSourceFactory(app, Util.getUserAgent(app, app.getString(R.string.app_name)), defaultBandwidthMeter);
        mPlayer = new SimpleExoPlayer.Builder(app).build();
        mPlayer.removeListener(mEventListener);
        mPlayer.addListener(mEventListener);
        mPlayer.addAnalyticsListener(eventLogger);
        mApp = app;
        mPlayerView = providerPlayerView();
        mHandlerAd = new Handler(Looper.getMainLooper());
    }

    private static DefaultDataSourceFactory createDataSourceFactory(Context context, String userAgent, TransferListener listener) {
        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, listener, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS, DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
        return new DefaultDataSourceFactory(context, listener, httpDataSourceFactory);
    }

    public Video getVideo() {
        return mVideo;
    }

    public SimpleExoPlayer getPlayer() {
        return mPlayer;
    }

    public VideoPlayerView getPlayerView() {
        return mPlayerView;
    }

    public VideoPlaybackControlView getControlView() {
        return mControlView;
    }

    public PlaybackParameters getPlaybackParameters() {
        return mPlayer.getPlaybackParameters();
    }

    public void setPlaybackParameters(PlaybackParameters param) {
        mPlayer.setPlaybackParameters(param);
    }

    public void addPlayerViewTo(View newView) {
        if (mPlayerView == null) return;
        ViewParent viewParent = mPlayerView.getParent();
        if (newView == null || viewParent == newView) return;
        if (newView instanceof ViewGroup) {
            removerPlayerViewFromOldParent();
            ((ViewGroup) newView).addView(mPlayerView);
        }
    }

    public void removerPlayerViewFromOldParent() {
        if (mPlayerView != null) {
            ViewParent oldParent = mPlayerView.getParent();
            if (oldParent instanceof ViewGroup) {
                ((ViewGroup) oldParent).removeView(mPlayerView);
            }
        }
    }

    public void prepareV2(Video video) {
        if (mVideo != null && Utilities.equals(mVideo, video)) {
            return;
        }
        prepare(video);
        if (canPlayVideo())
            setPlayWhenReady(true);
    }

    public void prepare(final Video video) {
        /*
         * log end old video
         */
        logEnd();

        //Log ads truong hop next video
        skipCode = 4;
        callLogAds(errorAds > 0 ? errorAds : -1);


        playVideo(video);
    }

    private boolean canShowAds() {
        int indexShowAds;
        try {
            String indexShowAdsStr = mApp.getContentConfigByKey(Constants.PREFERENCE.CONFIG.VIDEO_INDEX_SHOW_ADS);
            indexShowAds = Integer.parseInt(indexShowAdsStr);
        } catch (Exception e) {
            indexShowAds = -1;
        }
        return (indexShowAds > 0 && mApp.getTotalVideosViewed() > 2 && mApp.getTotalVideosViewed() % indexShowAds == 0);
    }

    private void playVideo(Video video) {
        Log.i(TAG, "playVideo: " + video);
        //        count++;
        switchPlayer(false);
        /*
         * log start video
         */
        if (mapAds == null) mapAds = new WeakHashMap<>();
        mapAds.clear();
        spotXChannelId = "";
        spotXApiKey = "";
        adsPos = Constants.ADS_POS.FIRST;
        adsToken = "";
        try {
            String timeSkipAdStr = mApp.getContentConfigByKey(Constants.PREFERENCE.CONFIG.VIDEO_TIME_SKIP_AD);
            timeSkipAd = Integer.parseInt(timeSkipAdStr);
        } catch (Exception e) {
            timeSkipAd = 0;
        }

        if (video != null) {
            if (mPlayerView != null) mPlayerView.setCover(video.getImagePath());
            if (video.isMovie()) {
                Log.i(TAG, "play movie");
                for (int i = 0; i < video.getListAds().size(); i++) {
                    AdSense ads = video.getListAds().get(i);
                    if (ads != null && ads.isAds() && !mapAds.containsKey(ads.getStartTime())) {
                        mapAds.put(ads.getStartTime(), ads);
                    }
                }
            } else {
                Log.i(TAG, "play video: " + mApp.getTotalVideosViewed());
                if ((video.isMustShowAds() || (video.isShowAdsWithIndex() && canShowAds())) && !video.isLiveEnd()) {
                    for (int i = 0; i < video.getListAds().size(); i++) {
                        AdSense ads = video.getListAds().get(i);
                        if (ads != null && ads.isAds() && !mapAds.containsKey(ads.getStartTime())) {
                            mapAds.put(ads.getStartTime(), ads);
                        }
                    }
                }
            }

            if (mPlayerView != null && mPlayerView.getController() != null) {
                Set<String> keysAds = mapAds.keySet();
                ArrayList<String> listAdsPos = new ArrayList<>(keysAds);
                if (mControlView != null) mControlView.setListAdPosition(listAdsPos);
            }
            if (video.isMovie()) {
                AdSense adSense = mapAds.get("0");
                if (adSense != null) {
                    spotXChannelId = adSense.getSpotXChannelId();
                    spotXApiKey = adSense.getSpotXApiKey();
                    adsPos = adSense.getStartTimeInt();
                    adsToken = adSense.getToken();
                }
            } else {
                if ((video.isMustShowAds() || (video.isShowAdsWithIndex() && canShowAds())) && !video.isLiveEnd()) {
                    AdSense adSense = mapAds.get("0");
                    if (adSense != null) {
                        spotXChannelId = adSense.getSpotXChannelId();
                        spotXApiKey = adSense.getSpotXApiKey();
                        adsPos = adSense.getStartTimeInt();
                        adsToken = adSense.getToken();
                    }
                }
            }
            String mediaUrl = video.getOriginalPath();
            String subTitle = video.getSubTitleUrl();
            if (mApp != null) {
                String configResolution = mApp.getConfigResolutionVideo();
                if (video.isHasListResolution()) {
                    boolean check = false;
                    for (int i = 0; i < video.getListResolution().size(); i++) {
                        Resolution resolution = video.getListResolution().get(i);
                        if (resolution != null && configResolution.equalsIgnoreCase(resolution.getKey())) {
                            video.setIndexQuality(i);
                            check = true;
                            mediaUrl = resolution.getVideoPath();
                            break;
                        }
                    }
                    if (!check) video.setIndexQuality(0);
                }
            }
            video.setStartMediaUrl(mediaUrl);
            logStart(video);
//            prepareWithAds(mediaUrl);
            prepareWithAds(mediaUrl, subTitle); // Add SubTitle
            mVideo = video;
            mVideo.setCallLogEnd(false);
            if (mControlView != null) mControlView.setLive(video.isLive());
            if (mPlayerView != null) {
                mPlayerView.setLive(video.isLive());
                mPlayerView.setLogo(video.getLogo(), video.getLogoPosition());
            }
        }
        if (video != null && video.isLiveEnd()) {
            if (mPlayerView != null) {
                mPlayerView.showUiLiveEnd();
                mPlayerView.showThumb(video.getImagePath());
            }
        } else {
            if (mPlayerView != null) {
                mPlayerView.hideUiError();
            }
        }
        if (mPlayerView != null) {
            View quality = mPlayerView.getQualityView();
            if (quality != null) {
                if (video == null) quality.setVisibility(View.GONE);
//                else quality.setVisibility(video.isHasListResolution() ? View.VISIBLE : View.GONE);
                else quality.setVisibility(View.VISIBLE);
            }
        }
    }

    public void prepare(String mediaUrl) {
        currentMediaUrl = mediaUrl;
        isSeekBeforeBuffering = false;
        Log.e(TAG, "buildMediaSource prepare mediaUrl: " + mediaUrl);
        if (TextUtils.isEmpty(mediaUrl)) {
            releaseAds();
            cancelCountDownRequestAd();
            if (mPlayer != null) mPlayer.stop();
            if (mPlayerView != null) {
                if (mVideo != null && mVideo.isLive()) {
                    mPlayerView.showUiLiveEnd();
                } else {
                    mPlayerView.showUiError();
                }
            }
        } else {
            changeAudioFocus(true);
            if (mPlayer != null) mPlayer.prepare(buildMediaSource(mediaUrl));
        }
    }

    public void prepare(String mediaUrl, String subtitleUrl) {
        currentMediaUrl = mediaUrl;
        currentSubTitle = subtitleUrl;
        isSeekBeforeBuffering = false;
        Log.e(TAG, "buildMediaSource prepare mediaUrl: " + mediaUrl);
        if (TextUtils.isEmpty(mediaUrl)) {
            releaseAds();
            cancelCountDownRequestAd();
            if (mPlayer != null) mPlayer.stop();
            if (mPlayerView != null) {
                if (mVideo != null && mVideo.isLive()) {
                    mPlayerView.showUiLiveEnd();
                } else {
                    mPlayerView.showUiError();
                }
            }
        } else {
            changeAudioFocus(true);
            if (mPlayer != null) {
                if (TextUtils.isEmpty(subtitleUrl)) {
                    mPlayer.prepare(buildMediaSource(mediaUrl));
                } else {
                    // Merge Source
                    MediaSource mediaSource = mergingMediaSource(subtitleUrl, buildMediaSource(mediaUrl), mediaDataSourceFactory);
                    mPlayer.prepare(mediaSource);
                }
            }
        }
    }

    private MediaSource mergingMediaSource(String urlSub, MediaSource buildMediaSource, DataSource.Factory factory) {
        Uri uriSubtitle = Uri.parse(urlSub);
        //  Tùy chọn định dạng
//        MimeTypes.TEXT_SSA MimeTypes.APPLICATION_SUBRIP
        Format textFormat = Format.createTextSampleFormat(null, MimeTypes.TEXT_VTT,
                Format.NO_VALUE, "vi", null);
        MediaSource subtitleSource = new SingleSampleMediaSource.Factory(factory).createMediaSource(uriSubtitle, textFormat, C.TIME_UNSET);
        MergingMediaSource mergedSource = new MergingMediaSource(buildMediaSource, subtitleSource);
        return mergedSource;
    }

    public void reload() {
        if (mPlayerView != null) {
            mPlayerView.hideUiError();
        }
        if (mVideo != null) {
            logStart(mVideo);
//            prepareWithAds(currentMediaUrl); 
            prepareWithAds(currentMediaUrl, currentSubTitle); //  Add Subtitle
        }
    }

    public void release() {
//        Log.e(TAG, "release: ");
        if (mVideo != null) {
            logEnd(mVideo);
            //Log ads truong hop tat video
            skipCode = 5;
            callLogAds(errorAds > 0 ? errorAds : -2);
        }
        isRelease = true;
        listeners.clear();
        callBackListeners.clear();
        mPlayer.removeListener(mEventListener);
        mPlayer.removeAnalyticsListener(eventLogger);
        mPlayerView.setControllerVisibilityListener(null);
        mPlayer.release();
        eventLogger = null;
        Log.i(TAG, "Ads Event: mAdsManager destroy");
        releaseAds();
        cancelCountDownRequestAd();
    }

    public void onHaveSeek(boolean flag) {
        if (mVideo != null)
            mVideo.setSeek(flag);
    }

    public void addListener(Player.EventListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeListener(Player.EventListener listener) {
        listeners.remove(listener);
    }

    public void addControllerListener(VideoPlaybackControlView.CallBackListener listener) {
        if (!callBackListeners.contains(listener))
            callBackListeners.add(listener);
    }

    public void removeControllerListener(VideoPlaybackControlView.CallBackListener listener) {
        callBackListeners.remove(listener);
    }

    public void seekTo(long i) {
        try {
            mPlayer.seekTo(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    public long getBufferedPosition() {
        return mPlayer.getBufferedPosition();
    }

    public long getDuration() {
        return mPlayer.getDuration();
    }

    public int getCurrentWindowIndex() {
        return mPlayer.getCurrentWindowIndex();
    }

    public int getPlaybackState() {
        return mPlayer.getPlaybackState();
    }

    public boolean isMini() {
        return mini;
    }

    public void setMini(boolean mini) {
        this.mini = mini;
    }

    public boolean isRelease() {
        return isRelease;
    }

    public boolean getPlayWhenReady() {
        return mPlayer.getPlayWhenReady();
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        if (!isRelease) {
            if (playWhenReady && !mIsAdDisplayed) {
                mPlayer.setPlayWhenReady(true);
            } else {
                mPlayer.setPlayWhenReady(false);
            }
        }
    }

    public void setFullscreen(boolean isFullscreen) {
        this.isFullScreen = isFullscreen;
        if (mControlView != null)
            if (isFullscreen)
                mControlView.setSmallScreen();
            else
                mControlView.setFullScreen();
    }

    public void setVisibilityInfo(int visibility) {
        if (infoView != null)
            infoView.setVisibility(visibility);
    }

    private boolean canSwitchPlayer() {
        return !VideoPlayerView.useTextureView;
    }

    private void switchPlayer(boolean useTextureView) {
        if (mPlayerView != null) {
            ViewParent parent = mPlayerView.getParent();
            if (parent instanceof ViewGroup && VideoPlayerView.useTextureView != useTextureView) {
                VideoPlayerView.useTextureView = useTextureView;
                removerPlayerViewFromOldParent();
                mPlayerView = providerPlayerView();
                addPlayerViewTo((View) parent);
                long position = mPlayer.getCurrentPosition();
//                prepareWithAds(currentMediaUrl);
                prepareWithAds(currentMediaUrl, currentSubTitle); //  AddSubTitle
                mPlayer.seekTo(position);
                if (mCallBackListener != null) mCallBackListener.autoSwitchPlayer();
            }
        }
    }

    private VideoPlayerView providerPlayerView() {
        removerPlayer();
        VideoPlayerView playerView = new VideoPlayerView(mApp);
        playerView.setShutterBackgroundColor(ContextCompat.getColor(mApp, R.color.videoColorBackgroundVideoDetail));
        playerView.setControllerVisibilityListener(mCallBackListener);
        playerView.setPlayer(mPlayer);
        playerView.setRingMePlayer(this);
        playerView.setPlayAds(false);
        mControlView = playerView.getController();

        if (mVideo != null) {
            if (mControlView != null) mControlView.setLive(mVideo.isLive());
            playerView.setLive(mVideo.isLive());
        }

        if (mControlView != null) {
            infoView = mControlView.findViewById(R.id.layoutTopbar);
        }
        if (isFullScreen) {
            setFullscreen(true);
        }
        updatePlaybackState();

        View quality = playerView.getQualityView();
        if (quality != null) {
            if (mVideo == null) quality.setVisibility(View.GONE);
                //else quality.setVisibility(mVideo.isHasListResolution() ? View.VISIBLE : View.GONE);
            else quality.setVisibility(View.VISIBLE);
        }

        return playerView;
    }

    private void removerPlayer() {
        if (mPlayerView != null) {
            mPlayerView.removerPlayer();
        }
    }

    private MediaSource buildMediaSource(String url) {
        currentMediaUrl = url;
        Uri uri = Uri.parse(url);
        @C.ContentType int type = Util.inferContentType(uri);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(new DefaultDashChunkSource.Factory(mediaDataSourceFactory), manifestDataSourceFactory).createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(new DefaultSsChunkSource.Factory(mediaDataSourceFactory), manifestDataSourceFactory).createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uri);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    /**
     * @param currentVideo thông tìn video mới (video chuẩn bị được play)
     */
    public void logStart(Video currentVideo) {
        isSeekBeforeBuffering = false;
        if (mApp != null)
            if (currentVideo != null) {
                isStartVideo = true;
                timeStart = System.currentTimeMillis();
                currentVideo.setStateLog("START");
                callLogVideo(currentVideo);
                currentVideo.setCallLogEnd(false);

                if (currentVideo.isMovie()) {
                    Map<String, Object> mapParams = new HashMap<>();
                    mapParams.put(InsiderUtils.PARAM_MOVIE_NAME, currentVideo.getTitle());
                    mapParams.put(InsiderUtils.PARAM_MOVIE_COUNTRY, currentVideo.getCountry());
                    mapParams.put(InsiderUtils.PARAM_MOVIE_CATEGORIES, currentVideo.getCategoryName());
                    InsiderUtils.logEvent(mApp, InsiderUtils.EVENT_MOVIE_VIEW_START, mapParams);
                } else {
                    Map<String, Object> mapParams = new HashMap<>();
                    mapParams.put(InsiderUtils.PARAM_VIDEO_NAME, currentVideo.getTitle());
                    mapParams.put(InsiderUtils.PARAM_VIDEO_CATEGORY, currentVideo.getCategoryName());
                    mapParams.put(InsiderUtils.PARAM_VIDEO_CHANNEL, currentVideo.getChannelName());
                    mapParams.put(InsiderUtils.PARAM_VIDEO_TAG, "");
                    mapParams.put(InsiderUtils.PARAM_VIDEO_EPISODE, currentVideo.getChapter());
                    InsiderUtils.logEvent(mApp, InsiderUtils.EVENT_VIDEO_VIEW_START, mapParams);
                }
            }
        isVideoPlayed = false;
    }

    public void logEnd() {
        Log.d(TAG, "logEnd -----");
        if (mVideo != null) {
            logEnd(mVideo);
//            mVideo = null;
        }
    }

    /**
     * @param oldVideo thông tin video cũ, video đã play rồi
     */
    private void logEnd(Video oldVideo) {
        if (oldVideo != null && !oldVideo.isCallLogEnd()) {
            if (currentState == Player.STATE_BUFFERING || currentState == Player.STATE_IDLE) {
                //todo update lagArr
                long timeLag = System.currentTimeMillis() - timeLagStart;
                if (timeLag > 0 && timeLagStart > 0) {
                    lagArr += timeLag + ":" + (isSeekBeforeBuffering ? 1 : 0) + "|";
                    Log.e(TAG, "lagArr: " + lagArr);
                    if (timeBufferFirst == 0) timeBufferFirst = timeLag;
                }
                isSeekBeforeBuffering = false;
            } else if (currentState == Player.STATE_READY) {
                //todo update playArr
                long timePlay = System.currentTimeMillis() - timePlayStart;
                if (timePlay > 0 && timePlayStart > 0) {
                    playArr += timePlay + "|";
                    totalTimePlay += timePlay;
                    Log.e(TAG, "totalTimePlay: " + totalTimePlay + ", playArr: " + playArr);
                    updateBandwidthArr();
                }
            }

            float completionRate;
            if (currentState == Player.STATE_ENDED)
                completionRate = 100;
            else
                completionRate = getCurrentPosition() * 100 / getDuration();
            if (oldVideo.isMovie()) {
                Map<String, Object> mapParams = new HashMap<>();
                mapParams.put(InsiderUtils.PARAM_MOVIE_NAME, oldVideo.getTitle());
                mapParams.put(InsiderUtils.PARAM_MOVIE_COUNTRY, oldVideo.getCountry());
                mapParams.put(InsiderUtils.PARAM_MOVIE_CATEGORIES, oldVideo.getCategoryName());
                mapParams.put(InsiderUtils.PARAM_MOVIE_COMPLETION_RATE, completionRate);
                InsiderUtils.logEvent(mApp, InsiderUtils.EVENT_MOVIE_VIEW_END, mapParams);
            } else {
                Map<String, Object> mapParams = new HashMap<>();
                mapParams.put(InsiderUtils.PARAM_VIDEO_NAME, oldVideo.getTitle());
                mapParams.put(InsiderUtils.PARAM_VIDEO_CATEGORY, oldVideo.getCategoryName());
                mapParams.put(InsiderUtils.PARAM_VIDEO_CHANNEL, oldVideo.getChannelName());
                mapParams.put(InsiderUtils.PARAM_VIDEO_TAG, "");
                mapParams.put(InsiderUtils.PARAM_VIDEO_EPISODE, oldVideo.getChapter());
                mapParams.put(InsiderUtils.PARAM_VIDEO_COMPLETION_RATE, completionRate);
                InsiderUtils.logEvent(mApp, InsiderUtils.EVENT_VIDEO_VIEW_END, mapParams);
            }
            oldVideo.setStateLog("END");
            callLogVideo(oldVideo);
            oldVideo.setCallLogEnd(true);
        }
    }

    private void updateLog(int playbackState) {
        if (playbackState == Player.STATE_BUFFERING) {
            Log.e(TAG, "updateLog STATE_BUFFERING");
            timeLagStart = System.currentTimeMillis();
            if (currentState == Player.STATE_READY || currentState == Player.STATE_ENDED) {
                //todo update playArr
                long timePlay = timeLagStart - timePlayStart;
                if (timePlay > 0 && timePlayStart > 0) {
                    playArr += timePlay + "|";
                    totalTimePlay += timePlay;
                    Log.e(TAG, "totalTimePlay: " + totalTimePlay + ", playArr: " + playArr);
                    updateBandwidthArr();
                }
            }
        } else if (playbackState == Player.STATE_READY) {
            Log.e(TAG, "updateLog STATE_READY");
            if (currentState == Player.STATE_BUFFERING || currentState == Player.STATE_IDLE) {
                timePlayStart = System.currentTimeMillis();
                Log.e(TAG, "timePlayStart: " + timePlayStart);
                if (isStartVideo) {
                    timeBufferFirst = timePlayStart - timeStart;
                    Log.e(TAG, "timeBufferFirst: " + timeBufferFirst);
                    updateBandwidthArr();
                    //Log KQI
                    logKQIBufferVideo(timeBufferFirst, timePlayStart);
                    isStartVideo = false;

                    //Set lan dau tien
                    lagArr += timeBufferFirst + ":" + (isSeekBeforeBuffering ? 1 : 0) + "|";
                } else {
                    //todo update lagArr
                    long timeLag = timePlayStart - timeLagStart;
                    if (timeLag > 0 && timeLagStart > 0) {
                        lagArr += timeLag + ":" + (isSeekBeforeBuffering ? 1 : 0) + "|";
                    }
                }
                Log.e(TAG, "lagArr: " + lagArr);
                isSeekBeforeBuffering = false;
            }
        } else if (playbackState == Player.STATE_IDLE) {
            Log.e(TAG, "updateLog STATE_IDLE");
        } else if (playbackState == Player.STATE_ENDED) {
            Log.e(TAG, "updateLog STATE_ENDED");
        } else {
            Log.e(TAG, "updateLog STATE_OTHER ...");
        }
    }

    private void logKQIBufferVideo(long timeBufferFirst, long timePlayStart) {
        try {
            if (mVideo != null) {
                switch (mVideo.getFromSource()) {

                }
            }
        } catch (Exception e) {

        }
    }

    private void resetParamLogAds() {
        adsPos = Constants.ADS_POS.FIRST;
        adsToken = "";
        spotXChannelId = "";
        spotXApiKey = "";
        skipCode = 0;
        errorAds = 0;
        cancelCountDownRequestAd();
    }

    private void resetParamLog() {
        //timePlay = 0;
        timeBufferFirst = 0;
        lagArr = "";
        bandwidthArr = "";
        networkArr = "";
        playArr = "";
        errorDesc = "";
        timeLagStart = 0;
        timePlayStart = 0;
        totalTimePlay = 0;
        isVideoPlayed = false;
        if (mVideo != null)
            mVideo.resetParam();
    }

    public void callLogAds(int errorAds) {
        if (mVideo != null && !isLogAds && Utilities.notEmpty(spotXChannelId)) {
            Log.i(TAG, "Ads Event Error code: " + errorAds);
            if (mPlayerView != null) mPlayerView.showProgressLoadingAds(false);
            String adsInfo = spotXChannelId + "|" + errorAds + "|" + skipCode;
            mVideo.setAdsInfo(adsInfo);
            Log.i(TAG, "Ads Event info: " + adsInfo);
            if (mApp != null)

                isLogAds = true;
            resetParamLogAds();
            if (currentState == Player.STATE_ENDED && mPlayer != null) {
                try {
                    if (Utilities.notEmpty(listeners)) {
                        Log.i(TAG, "callLogAds Player.EventListener onPlayerStateChanged");
                        for (Player.EventListener listener : listeners) {
                            if (listener != null) {
                                listener.onPlayerStateChanged(mPlayer.getPlayWhenReady(), currentState);
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    private void callLogVideo(Video currentVideo) {
        if (currentVideo == null) return;
        long timePlay = 0;
        String timeLog;
        if (currentVideo.getStateLog().equals("END")) {
            if (currentState == Player.STATE_ENDED)
                timePlay = getDuration();
            else
                timePlay = getCurrentPosition();
            long current = timePlay / 1000;
            if (current >= 5 && current < 15) {
                if (!currentVideo.isLog5()) {
                    currentVideo.setLog5(true);
                    currentVideo.setLog15(false);
                    currentVideo.setLog30(false);
                }
            } else if (current >= 15 && current < 30) {
                if (!currentVideo.isLog15()) {
                    currentVideo.setLog5(true);
                    currentVideo.setLog15(true);
                    currentVideo.setLog30(false);
                }
            } else if (current >= 30) {
                if (!currentVideo.isLog30()) {
                    currentVideo.setLog5(true);
                    currentVideo.setLog15(true);
                    currentVideo.setLog30(true);
                }
            } else {
                currentVideo.setLog5(false);
                currentVideo.setLog15(false);
                currentVideo.setLog30(false);
            }
            timeLog = timeBufferFirst + "|" + timePlay + "|" + (currentVideo.isLog5() ? 1 : 0) + "|" + (currentVideo.isLog15() ? 1 : 0) + "|" +
                    (currentVideo.isLog30() ? 1 : 0) + "|" + (currentVideo.isSeek() ? 1 : 0) + "|" + (isVideoPlayed ? 1 : 0);
        } else {
            timeLog = "0|0|0|0|0|0|0";
            lagArr = "";
            playArr = "";
            errorDesc = "";
            totalTimePlay = 0;
        }
        currentVideo.setTimeLog(timeLog);
        currentVideo.setLagArr(lagArr);
        currentVideo.setBandwidthArr(bandwidthArr);
        currentVideo.setNetworkArr(networkArr);
        currentVideo.setPlayArr(playArr);
        currentVideo.setErrorDes(errorDesc);
        currentVideo.setTotalTimePlay(totalTimePlay);
        currentVideo.setSurfaceName(VideoPlayerView.useTextureView ? "TextureView" : "SurfaceView");
        currentVideo.setCodecNeedsSetOutputSurfaceWorkaround(true);
        try {
            float currentVolume = (float) mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) / (float) mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            currentVideo.setVolume(currentVolume);
        } catch (Exception e) {
            currentVideo.setVolume(0.5f);
        }
        Log.e(TAG, currentVideo.getStateLog() + "| " + "TimeBuffer: " + timeBufferFirst + "| TimePlay: " + timePlay + "| TimeLog: " + timeLog + "| PlayArr: " + playArr + "| LagArr: " + lagArr);
        if (mApp != null)

            resetParamLog();
    }

    private void updateBandwidthArr() {
        bandwidthArr += defaultBandwidthMeter.getBitrateEstimate() + "|";
        Log.e(TAG, "bandwidthArr: " + bandwidthArr);
        networkArr += NetworkHelper.getNetworkSubType(mApp) + "|";
        Log.e(TAG, "networkArr: " + networkArr);
    }

    public void setSkipCode(int skipCode) {
        this.skipCode = skipCode;
    }

    public void setErrorAds(int errorAds) {
        this.errorAds = errorAds;
    }

    private void startCountDown() {
        if (countDownTimer == null) {
            Log.e(TAG, "Ads startCountDown lag");
            countDownTimer = new CountDownTimer(ADS_LAG_TIME, 1000) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    Log.i(TAG, "Ads Event: CountDownTimer Lag onFinish()");
                    //Load video lag
                    errorAds = 200;
                    callLogAds(errorAds);
                    cancelCountDown();
                    releaseAds();
                    if (canPlayVideo()) setPlayWhenReady(true);
                }

            }.start();
        }
    }

    private void restartCountDown() {
        try {
            cancelCountDown();
            startCountDown();
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
    }

    private void cancelCountDown() {
        try {
            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
    }

    public boolean isAdDisplayed() {
        return mIsAdDisplayed;
    }

    public void showHidePreviousButton(boolean isShow) {
        if (mControlView != null) mControlView.showHidePreviousButton(isShow);
    }

    public void showHideNextButton(boolean isShow) {
        if (mControlView != null) mControlView.showHideNextButton(isShow);
    }

    public void updatePlaybackState() {
        if (mControlView != null && mPlayer != null)
            mControlView.processState(mPlayer.getPlaybackState());
    }

    public boolean isLogAds() {
        return isLogAds;
    }

    public void setPaused(boolean pause) {
        isPaused = pause;
        if (BuildConfig.DEBUG) {
            if (!isPaused && mVideo != null && mVideo.isLive() && mPlayer != null) {
                long duration = mPlayer.getDuration();
                if (duration < 0) duration = 0;
                Log.i(TAG, "mPlayer.seekTo duration: " + duration);
                mPlayer.seekTo(duration);
            }
        }
    }

    private boolean canPlayVideo() {
        return isMini();
    }

    public void setMute(boolean flag) {
        if (mPlayer != null) {
            if (flag) {
                mPlayer.setVolume(0f);
            } else {
                mPlayer.setVolume(1f);
            }
        }
    }
//
//    public SpotXAdPlayer getAdPlayer() {
//        return mAdPlayer;
//    }

    public void showReplay(boolean showNextPrevious) {
        setPlayWhenReady(false);
        if (mPlayerView != null) {
            mPlayerView.showController(true);
            mPlayerView.showButtonReload();
            if (showNextPrevious) mPlayerView.setVisiblePreviousAndNextButton(true);
            mPlayerView.showController(true);
        }
    }

    private void handleAudioFocusChange(int focusChange) {
        /*
         * Pause playback during alerts and notifications
         */
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
                Log.i(TAG, "AUDIOFOCUS_LOSS");
                // Stop playback
                if (mPlayer != null && mPlayer.getPlayWhenReady()) {
                    mPlayer.setPlayWhenReady(false);
                    changeAudioFocus(false);
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                Log.i(TAG, "AUDIOFOCUS_LOSS_TRANSIENT");
                // Pause playback
//                if (getAdPlayer() != null && isAdDisplayed()) {
////                    getAdPlayer().pause();
//                    isLossTransient = true;
//                }
                if (mPlayer != null && mPlayer.getPlayWhenReady()) {
                    isLossTransient = true;
                    mPlayer.setPlayWhenReady(false);
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                Log.i(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                // Lower the volume
                if (mPlayer != null && mPlayer.getPlayWhenReady()) {
                    mPlayer.setVolume(36);
                    isLossTransientCanDuck = true;
                }
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                Log.i(TAG, "AUDIOFOCUS_GAIN isLossTransientCanDuck: " + isLossTransientCanDuck + ", isLossTransient: " + isLossTransient);
                // Resume playback
                if (isLossTransientCanDuck) {
                    if (mPlayer != null) mPlayer.setVolume(100);
                    isLossTransientCanDuck = false;
                }
                if (isLossTransient) {
                    if (canPlayVideo()) {
//                        if (getAdPlayer() != null && isAdDisplayed()) {
////                            getAdPlayer().resume();
//                        } else if (mPlayer != null) {
                        setPlayWhenReady(true);
//                        }
                    }
                    isLossTransient = false;
                }
                break;
        }
    }

    private void changeAudioFocus(boolean acquire) {
        if (mAudioManager == null)
            return;

        if (acquire) {
            if (!isHasAudioFocus) {
                final int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mAudioManager.setParameters("bgm_state=true");
                    isHasAudioFocus = true;
                }
                Log.d(TAG, "changeAudioFocus requestAudioFocus: " + result + ", isHasAudioFocus: " + isHasAudioFocus);
            }
        } else {
            if (isHasAudioFocus) {
                final int result = mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
                mAudioManager.setParameters("bgm_state=false");
                isHasAudioFocus = false;
                Log.d(TAG, "changeAudioFocus abandonAudioFocus: " + result);
            }
        }
    }

    private void prepareWithAds(String mediaUrl) {
        Log.d(TAG, "prepareWithAds mediaUrl: " + mediaUrl);
        releaseAds();
        if (Utilities.notEmpty(spotXChannelId)) {
            prepare(mediaUrl);
            requestAds(spotXChannelId, spotXApiKey);
        } else {
            prepare(mediaUrl);
        }
    }

    private void prepareWithAds(String mediaUrl, String subitleUrl) {
        Log.d(TAG, "prepareWithAds mediaUrl: " + mediaUrl + "\nsubitleUrl: " + subitleUrl
                + "\nspotXChannelId: " + spotXChannelId + "\tspotXApiKey: " + spotXApiKey);
        releaseAds();
        if (Utilities.notEmpty(spotXChannelId)) {
            prepare(mediaUrl, subitleUrl);
            requestAds(spotXChannelId, spotXApiKey);
        } else {
            prepare(mediaUrl, subitleUrl);
        }
    }

    public void releaseAds() {
        Log.e(TAG, "releaseAds");
        cancelCountDownRequestAd();
//        if (mSpotXAdGroup != null) {
//            mSpotXAdGroup = null;
//        }
//        if (mAdPlayer != null) {
//            mAdPlayer.unregisterObserver(RingMePlayer.this);
//            mAdPlayer.pause();
//            mAdPlayer.stop();
//            mAdPlayer = null;
//        }
        hideAds();
    }

    private void hideAds() {
        Log.e(TAG, "hideAds");
        mIsAdDisplayed = false;

        if (mHandlerAd != null) {
            mHandlerAd.removeCallbacks(runnableShowAd);
            //mHandlerAd.removeCallbacks(runnableHideAd);
            mHandlerAd.post(runnableHideAd);
        }
        if (mCallBackListener != null) mCallBackListener.onHideAd();
    }

    private void showAds() {
        Log.e(TAG, "showAds");
        mIsAdDisplayed = true;
        if (mHandlerAd != null) {
            mHandlerAd.removeCallbacks(runnableHideAd);
            //mHandlerAd.removeCallbacks(runnableShowAd);
            mHandlerAd.post(runnableShowAd);
        }
        if (mCallBackListener != null) mCallBackListener.onShowAd();
    }

    private void requestAds(String spotXChannelId, String spotXApiKey) {
        Log.e(TAG, "requestAds adsPos: " + adsPos + ", adsToken: " + adsToken
                + "\nspotXChannelId: " + spotXChannelId + "\nspotXApiKey: " + spotXApiKey);
        isLogAds = false;
        releaseAds();
        // create the ads request.
        if (mHandlerAd != null) {
            mHandlerAd.removeCallbacks(runnableRequestAd);
            mHandlerAd.post(runnableRequestAd);
        }
    }

    private void startCountDownRequestAd() {
        cancelCountDownRequestAd();
        if (countDownRequestTimer == null) {
            Log.e(TAG, "startCountDownRequestAd");
            countDownRequestTimer = new CountDownTimer(ADS_REQUEST_TIME, 1000) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    Log.e(TAG, "startCountDownRequestAd onFinish()");
                    //Check neu khong co phan hoi gi thi goi log
                    if (mVideo != null) {
                        errorAds = 0;
                        callLogAds(errorAds);
                    }
                    releaseAds();
                }

            }.start();
        }
    }

    private void cancelCountDownRequestAd() {
        try {
            if (countDownRequestTimer != null) {
                countDownRequestTimer.cancel();
                countDownRequestTimer = null;
                Log.e(TAG, "cancelCountDownRequestAd");
            }
        } catch (Exception e) {
            Log.e(TAG, e);
        }
    }

//    @Override
//    public SpotXAdRequest requestForPlayer(@NonNull SpotXAdPlayer spotXAdPlayer) {
//        Log.e(TAG, "SpotXAd requestForPlayer spotXChannelId: " + spotXChannelId + ", spotXApiKey: " + spotXApiKey);
//        SpotXAdRequest request = new SpotXAdRequest(spotXApiKey);
//        request.channel = spotXChannelId;
//        return request;
//    }

//    @Override
//    public void onLoadedAds(final SpotXAdPlayer spotXAdPlayer, final SpotXAdGroup spotXAdGroup, Exception e) {
//        Log.e(TAG, "SpotXAd onLoadedAds spotXChannelId: " + spotXChannelId + ", spotXApiKey: " + spotXApiKey, e);
//        if (mAdPlayer != null) {
//            Log.e(TAG, "SpotXAd onLoadedAds mAdPlayer is not null");
//            errorAds = 1;
//            mSpotXAdGroup = spotXAdGroup;
//            if (mHandlerAd != null) {
//                mHandlerAd.removeCallbacks(runnableLoadedAd);
//                mHandlerAd.post(runnableLoadedAd);
//            }
//        } else {
//            Log.e(TAG, "SpotXAd onLoadedAds mAdPlayer is null");
//            if (spotXAdPlayer != null) {
//                spotXAdPlayer.stop();
//            }
//        }
//    }
//
//    @Override
//    public void onGroupStart(SpotXAdGroup spotXAdGroup) {
//        Log.e(TAG, "SpotXAd onGroupStart spotXChannelId: " + spotXChannelId + ", spotXApiKey: " + spotXApiKey);
//        errorAds = 50;
//    }
//
//    @Override
//    public void onStart(SpotXAd spotXAd) {
//        Log.e(TAG, "SpotXAd onStart spotXChannelId: " + spotXChannelId + ", spotXApiKey: " + spotXApiKey);
//        //if (mAdPlayer != null) mAdPlayer.resume();
//    }
//
//    @Override
//    public void onPlay(SpotXAd spotXAd) {
//        Log.e(TAG, "SpotXAd onPlay spotXChannelId: " + spotXChannelId + ", spotXApiKey: " + spotXApiKey);
//        if (mAdPlayer != null) {
//            //errorAds = 200;
//            if (mHandlerAd != null) {
//                mHandlerAd.removeCallbacks(runnableOnPlayAd);
//                mHandlerAd.post(runnableOnPlayAd);
//            }
//        }
//    }
//
//    @Override
//    public void onPause(SpotXAd spotXAd) {
//        Log.e(TAG, "SpotXAd onPause spotXChannelId: " + spotXChannelId + ", spotXApiKey: " + spotXApiKey);
//    }
//
//    @Override
//    public void onTimeUpdate(SpotXAd spotXAd, double v) {
//        Log.d(TAG, "SpotXAd onTimeUpdate " + v + ", spotXChannelId: " + spotXChannelId + ", spotXApiKey: " + spotXApiKey);
//    }
//
//    @Override
//    public void onClick(SpotXAd spotXAd) {
//        Log.e(TAG, "SpotXAd onClick spotXChannelId: " + spotXChannelId + ", spotXApiKey: " + spotXApiKey + ", spotXAd: " + spotXAd.url);
//        errorAds = 200;
//        skipCode = 7;
//        callLogAds(errorAds);
//        releaseAds();
//        setPlayWhenReady(isMini());
//    }
//
//    @Override
//    public void onComplete(SpotXAd spotXAd) {
//        Log.e(TAG, "SpotXAd onComplete spotXChannelId: " + spotXChannelId + ", spotXApiKey: " + spotXApiKey);
//        errorAds = 200;
//        skipCode = 1;
//    }
//
//    @Override
//    public void onSkip(SpotXAd spotXAd) {
//        Log.e(TAG, "SpotXAd onSkip spotXChannelId: " + spotXChannelId + ", spotXApiKey: " + spotXApiKey);
//        errorAds = 200;
//        skipCode = 3;
//    }
//
//    @Override
//    public void onUserClose(SpotXAd spotXAd) {
//        Log.e(TAG, "SpotXAd onUserClose spotXChannelId: " + spotXChannelId + ", spotXApiKey: " + spotXApiKey);
//        errorAds = 200;
//        skipCode = 3;
//    }
//
//    @Override
//    public void onError(SpotXAd spotXAd, Exception e) {
//        Log.e(TAG, "SpotXAd onError " + spotXAd.id + ", spotXChannelId: " + spotXChannelId + ", spotXApiKey: " + spotXApiKey);
//        //Log ads
//        errorAds = -1;
//        skipCode = 6;
//        //Neu play quang cao loi lap tuc play video tro lai
//        if (mHandlerAd != null) {
//            mHandlerAd.removeCallbacks(runnableAdError);
//            mHandlerAd.post(runnableAdError);
//        }
//    }
//
//    @Override
//    public void onGroupComplete(SpotXAdGroup spotXAdGroup) {
//        Log.e(TAG, "SpotXAd onGroupComplete  spotXChannelId: " + spotXChannelId + ", spotXApiKey: " + spotXApiKey);
//        if (mHandlerAd != null) {
//            mHandlerAd.removeCallbacks(runnableGroupAdComplete);
//            mHandlerAd.post(runnableGroupAdComplete);
//        }
//    }

    public void setupGameStreaming() {
        if (mControlView != null) {
            mControlView.setupGameStreaming();
        }
    }

    public void setupLiveStream() {
        if (mControlView != null) {
            mControlView.setupLiveStream();
        }
    }

    public void setLandscape(boolean isLandscape) {
        if (mControlView != null) {
            mControlView.setLandscape(isLandscape);
        }
    }

    public boolean isLive() {
        if (mVideo != null) return mVideo.isLive();
        return false;
    }

    public void onClickViewFrame() {
        if (mCallBackListener != null) mCallBackListener.onClickViewFrame();
    }


    public long getTotalTimePlay() {
        if (currentState == Player.STATE_READY && mPlayer != null) {
            long timePlay = System.currentTimeMillis() - timePlayStart;
            if (timePlay > 0 && timePlayStart > 0) {
                return totalTimePlay + timePlay;
            }
        }
        return totalTimePlay;
    }
}