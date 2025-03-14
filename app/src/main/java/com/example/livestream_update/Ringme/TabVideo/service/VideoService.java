package com.example.livestream_update.Ringme.TabVideo.service;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.TimeBar;
import com.vtm.R;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.BuildConfig;
import com.vtm.ringme.common.utils.ScreenManager;
import com.vtm.ringme.common.utils.player.RingMePlayer;
import com.vtm.ringme.common.utils.player.RingMePlayerUtil;
import com.vtm.ringme.customview.ProgressLoading;
import com.vtm.ringme.customview.VideoPlaybackControlView;
import com.vtm.ringme.customview.VideoPlayerView;
import com.vtm.ringme.helper.ReportHelper;
import com.vtm.ringme.model.tab_video.Channel;
import com.vtm.ringme.model.tab_video.Video;
import com.vtm.ringme.tabvideo.listener.OnChannelChangedDataListener;
import com.vtm.ringme.tabvideo.listener.OnVideoChangedDataListener;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.utils.Utilities;
import com.vtm.ringme.values.Constants;

import java.util.List;

@SuppressLint({"NewApi", "ClickableViewAccessibility", "InflateParams", "RtlHardcoded"})
public class VideoService extends Service implements OnChannelChangedDataListener,
        OnVideoChangedDataListener {

    private static final String TAG = "VideoService";
    private static Handler mHandler;

    public static void start(final Context context, Video video, String tag, String action) {
        if (context == null) return;
        stop(context);
        if (!isMyServiceRunning(context, VideoService.class) && VideoService.self() == null) {
            final Intent intent = new Intent(context, VideoService.class);
            intent.putExtra(Constants.TabVideo.VIDEO, video);
            intent.putExtra(Constants.TabVideo.PLAYER_TAG, tag);
            intent.putExtra(Constants.TabVideo.ACTION, action);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                context.startService(intent);
                Log.d(TAG, "startService");
            } else {
                if (mHandler == null)
                    mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        context.startService(intent);
                        Log.d(TAG, "startService");
                    }
                }, 200);
            }
        }
    }

    public static void stop(Context context) {
        if (context == null) return;
        context.stopService(new Intent(context, VideoService.class));
    }

    private static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            List<ActivityManager.RunningServiceInfo> list = manager.getRunningServices(Integer.MAX_VALUE);
            for (int i = 0; i < list.size(); i++) {
                ActivityManager.RunningServiceInfo service = list.get(i);
                if (service != null && service.service != null && serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressLint("StaticFieldLeak")
    private static VideoService self;

    public static VideoService self() {
        return self;
    }

    private static final float SCALE_ZOOM_DEFAULT = 0.5f;
    private static final float SCALE_MINI_DEFAULT = 0.5f;
    private static final float OVERSHOOT_INTERPOLATOR_DEFAULT = 1f;

    private static final long TIME_ANIMATION_DEFAULT = 0L;

    private static final String X = "X";
    private static final String Y = "Y";
    private static final String WIDTH = "WIDTH";
    private static final String HEIGHT = "HEIGHT";

    private RelativeLayout container;
    private View retryView;
    private View fullView;
    private View removeView;
    private View darkView;
    private FrameLayout frameVideo;
    private View rootControllerView;
    private TimeBar progressBarBottom;
    private ProgressLoading loadingView;
    private RingMePlayer mPlayer;

    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private GestureDetector gestureDetector;

    private int statusBarHeight = 0;
    private double radio = 0d;
    private Video currentVideo = null;
    private String mediaName = "";
    private String action = "";
    private String type = "";
    private boolean attached = false;
    private boolean isOpenDetail = false;
    private final boolean isAnimation = false;
    private State currentState = State.MINI;
    private int widthScreen;
    private int heightScreen;

    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand flags: " + flags + ", startId: " + startId);
        try {
            attached = true;
            self = this;
            type = intent.getStringExtra(Constants.TabVideo.TYPE);
            action = intent.getStringExtra(Constants.TabVideo.ACTION);
            mediaName = intent.getStringExtra(Constants.TabVideo.PLAYER_TAG);
            currentVideo = (Video) intent.getSerializableExtra(Constants.TabVideo.VIDEO);
            Log.d(TAG, "onStartCommand currentVideo: " + currentVideo);
            if (currentVideo == null) {
                clearView();
                stopForeground(true);
                stopSelf();
                return START_NOT_STICKY;
            }
            windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            if (display != null) {
                display.getSize(size);
            }
            int widthTmp = size.x;
            int heightTmp = size.y;
            widthScreen = Math.min(widthTmp, heightTmp);
            heightScreen = Math.max(widthTmp, heightTmp);

            if (TextUtils.isEmpty(type))
                type = Constants.TabVideo.VIDEO_DETAIL;
            radio = currentVideo.getAspectRatio();
            if (Double.isNaN(radio) || radio == 0)
                radio = (double) 16 / 9;

            int currentWidth = (int) (widthScreen * SCALE_MINI_DEFAULT);
            int currentHeight = (int) (currentWidth / radio);

            if (currentHeight > heightScreen / 2) {
                currentHeight = heightScreen / 2;
                currentWidth = (int) (currentHeight * radio);
            }

            if (BuildConfig.DEBUG) {
                Log.e(TAG, "onStartCommand widthTmp : " + widthTmp + ", heightTmp : " + heightTmp
                        + ", widthScreen : " + widthScreen + ", heightScreen : " + heightScreen
                        + ", currentWidth : " + currentWidth + ", currentHeight : " + currentHeight
                        + ", ScreenManager.getWidth() : " + ScreenManager.getWidth() + ", ScreenManager.getHeight() : " + ScreenManager.getHeight()
                );
                if (widthScreen != ScreenManager.getWidth())
                    Toast.makeText(getApplicationContext(), "Lỗi lấy sai kích thước màn hình", Toast.LENGTH_LONG).show();
            }
            statusBarHeight = getStatusBarHeight();
            gestureDetector = new GestureDetector(this, new SingleTapConfirm());

            int LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            }

            params = new WindowManager.LayoutParams(LAYOUT_FLAG);
            params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN //Dat cua so trong toan bo man hinh, bo qua cac trang tri xung quanh vien nhu thanh trang thai
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS //Cờ cửa sổ: cho phép cửa sổ mở rộng ra bên ngoài màn hình.
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE //Cờ cửa sổ: cửa sổ này sẽ không bao giờ lấy tiêu điểm nhập chính, vì vậy người dùng không thể gửi khóa hoặc các sự kiện nút khác cho nó.
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL //cho phép mọi sự kiện con trỏ bên ngoài cửa sổ được gửi đến các cửa sổ phía sau nó.
                    | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;

            params.width = currentWidth;
            params.height = currentHeight;
            params.format = PixelFormat.TRANSLUCENT;
            params.x = widthScreen - params.width;
            if (!TextUtils.isEmpty(action) && action.equals(Constants.TabVideo.COMMENT))
                params.y = statusBarHeight + Utilities.dpToPixels(57, getResources());
            else
                params.y = heightScreen - params.height - Utilities.dpToPixels(56, getResources());
            params.alpha = 1.0f;
            params.gravity = Gravity.TOP | Gravity.LEFT;

            windowManager.addView(provideContainer(), params);
            if (mEventListener != null && mPlayer != null)
                mEventListener.onPlayerStateChanged(mPlayer.getPlayWhenReady(), mPlayer.getPlaybackState());

            registerScreenReceiver();
            scheduleProgress();
            registerCall();

            Application application = getApplication();
            if (application instanceof ApplicationController)


            if (mPlayer != null && mPlayer.getPlayerView() != null) {
                mPlayer.getPlayerView().showLogo(false);
                mPlayer.getPlayerView().hideButtonReload();
                mPlayer.getPlayerView().setVisibility(View.VISIBLE);
                if (mPlayer.getPlayerView().isShowUiError()) {
                    loadingView.setVisibility(View.GONE);
                }
            }

            disableController();
        } catch (Exception e) {
            ReportHelper.reportError(ApplicationController.self(), ReportHelper.VIDEO_SERVICE_ERROR, "VideoService onStartCommand " + e.getMessage());
            Log.e(TAG, e);

            clearView();
            stopForeground(true);
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        enableController();
        if (mPlayer != null && mPlayer.getPlayerView() != null) {
            mPlayer.getPlayerView().showButtonReload();
            mPlayer.removeListener(mEventListener);
        }
        if (isOpenDetail && mPlayer != null) {
            SimpleExoPlayer player = mPlayer.getPlayer();
            VideoPlayerView playerView = mPlayer.getPlayerView();
            VideoPlaybackControlView controlView = mPlayer.getControlView();

            if (playerView != null && player != null && controlView != null) {
                playerView.setEnabled(true);
                playerView.setUseController(true);
                playerView.enableFast(false);
                if (mPlayer.getPlayWhenReady()) {
                    controlView.setVisibility(View.GONE);
                } else {
                    controlView.setVisibility(View.VISIBLE);
                }
            }
        }
        if (mHandler != null)
            mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
        cancelScheduleProgress();
        unregisterScreenReceiver();
        clearView();
    }

    /**
     * cung cấp giao diện cho mini
     *
     * @return view
     */
    private View provideContainer() {
        Log.d(TAG, "provideContainer");
        /*
         * khởi tạo view
         */
        container = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.rm_item_video_mini, null);
        retryView = container.findViewById(R.id.iv_retry);
        loadingView = container.findViewById(R.id.loading);
        frameVideo = container.findViewById(R.id.video_view);
        darkView = container.findViewById(R.id.dark_view);
        fullView = container.findViewById(R.id.iv_fullscreen);
        removeView = container.findViewById(R.id.remove_button);
        rootControllerView = container.findViewById(R.id.root_controller);
        progressBarBottom = container.findViewById(R.id.progressBarBottom);

        progressBarBottom.setEnabled(false);
        fullView.setOnClickListener(mOnClickListener);
        retryView.setOnClickListener(mOnClickListener);
        removeView.setOnClickListener(mOnClickListener);
        container.setOnTouchListener(moveOnTouchListener);

        /*
         * lấy player
         */
        mPlayer = RingMePlayerUtil.getInstance().providePlayerBy(mediaName);
        mPlayer.addListener(mEventListener);
        mPlayer.getPlayerView().setEnabled(false);
        mPlayer.getPlayerView().setUseController(false);
        mPlayer.getPlayerView().enableFast(true);
        mPlayer.getPlayerView().getController().setVisibility(View.GONE);
        mPlayer.addPlayerViewTo(frameVideo);

        if (container != null)
            container.setKeepScreenOn(true);
        return container;
    }

    /**
     * lấy kích thước của status bar
     *
     * @return int
     */
    private int getStatusBarHeight() {
        final Resources resources = getResources();
        final int statusBarHeightId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (statusBarHeightId > 0)
            return resources.getDimensionPixelSize(statusBarHeightId);
        return 0;
    }

    @Override
    public void onChannelSubscribeChanged(Channel channel) {
        if (currentVideo == null) return;
        Channel currentChannel = currentVideo.getChannel();
        if (currentChannel == null || channel == null || Utilities.isEmpty(currentChannel.getId()) || Utilities.isEmpty(channel.getId()))
            return;

        if (channel.getId().equals(currentChannel.getId())) {
            currentChannel.setFollow(channel.isFollow());
            currentChannel.setNumFollow(channel.getNumfollow());
        }
    }

    @Override
    public void onVideoLikeChanged(Video video) {
        if (currentVideo == null || video == null || Utilities.isEmpty(currentVideo.getId()) || Utilities.isEmpty(video.getId()))
            return;

        if (currentVideo.getId().equals(video.getId())) {
            currentVideo.setLike(video.isLike());
            currentVideo.setTotalLike(video.getTotalLike());
        }
    }

    @Override
    public void onVideoShareChanged(Video video) {

        if (currentVideo == null || video == null || Utilities.isEmpty(currentVideo.getId()) || Utilities.isEmpty(video.getId()))
            return;

        if (currentVideo.getId().equals(video.getId())) {
            currentVideo.setShare(video.isShare());
            currentVideo.setTotalShare(video.getTotalShare());
        }
    }

    @Override
    public void onVideoCommentChanged(Video video) {

        if (currentVideo == null || video == null || Utilities.isEmpty(currentVideo.getId()) || Utilities.isEmpty(video.getId()))
            return;

        if (currentVideo.getId().equals(video.getId())) {
            currentVideo.setTotalComment(video.getTotalComment());
        }
    }

    @Override
    public void onVideoSaveChanged(Video video) {

        if (currentVideo == null || video == null || Utilities.isEmpty(currentVideo.getId()) || Utilities.isEmpty(video.getId()))
            return;

        if (currentVideo.getId().equals(video.getId())) {
            currentVideo.setSave(video.isSave());
        }
    }

    @Override
    public void onVideoWatchLaterChanged(Video video) {
        if (currentVideo == null || video == null || Utilities.isEmpty(currentVideo.getId()) || Utilities.isEmpty(video.getId()))
            return;

        if (currentVideo.getId().equals(video.getId())) {
            currentVideo.setWatchLater(video.isWatchLater());
        }
    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            if (mPlayer != null && mPlayer.getPlaybackState() != Player.STATE_ENDED) {
                if (currentState == State.MINI) {
                    scaleView(State.ZOOM);
                } else {
                    scaleView(State.MINI);
                }
            }
            return true;
        }
    }

    private void clearView() {
        try {


            release();
            if (attached && windowManager != null && container != null)
                windowManager.removeView(container);
            self = null;
            attached = false;
        } catch (Exception e) {
            ReportHelper.reportError(ApplicationController.self(), ReportHelper.VIDEO_SERVICE_ERROR, "VideoService clearView " + e.getMessage());
        }
    }

    private void release() {
        if (!isOpenDetail)
            RingMePlayerUtil.getInstance().removerPlayerBy(mediaName);
    }

    private final Player.EventListener mEventListener = new Player.EventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (mPlayer != null) {
                if (!mPlayer.isLogAds()) {
                    if (darkView != null) darkView.setVisibility(View.GONE);
                    if (loadingView != null) loadingView.setVisibility(View.GONE);
                } else {
                    if (playbackState == Player.STATE_ENDED) {
                        if (darkView != null) darkView.setVisibility(View.VISIBLE);
                        if (rootControllerView != null)
                            rootControllerView.setVisibility(View.VISIBLE);
                        if (fullView != null) fullView.setVisibility(View.VISIBLE);
                        if (retryView != null) retryView.setVisibility(View.VISIBLE);
                    } else if (playbackState == Player.STATE_BUFFERING) {
                        if (darkView != null) darkView.setVisibility(View.GONE);
                        if (loadingView != null) loadingView.setVisibility(View.VISIBLE);
                    } else if (playbackState == Player.STATE_READY) {
                        if (darkView != null) darkView.setVisibility(View.GONE);
                        if (loadingView != null) loadingView.setVisibility(View.GONE);
                    } else if (playbackState == Player.STATE_IDLE) {
                        if (darkView != null) darkView.setVisibility(View.GONE);
                        if (loadingView != null && mPlayer.getPlayerView() != null)
                            loadingView.setVisibility(mPlayer.getPlayerView().isShowUiError() ? View.GONE : View.VISIBLE);
                    }
                }
                if (container != null)
                    container.setKeepScreenOn(playWhenReady);
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            if (mPlayer != null && mPlayer.getPlayerView() != null) {
                mPlayer.getPlayerView().hideButtonReload();
                loadingView.setVisibility(View.GONE);
            }
        }
    };

    private void registerCall() {
        TelephonyManager telephonyManager =
                (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            telephonyManager.registerTelephonyCallback(getMainExecutor(), new TelephonyCallback());
        } else {
            telephonyManager.listen(phoneListener,
                    PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    private final PhoneStateListener phoneListener = new PhoneStateListener() {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    if (mPlayer != null && mPlayer.getPlayWhenReady())
                        mPlayer.setPlayWhenReady(false);
                    break;
            }
        }
    };
    private final View.OnTouchListener moveOnTouchListener = new View.OnTouchListener() {
        private int initialX;
        private int initialY;
        private float initialTouchX;
        private float initialTouchY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (gestureDetector != null && gestureDetector.onTouchEvent(event)) {
                return true;
            } else if (windowManager != null && v != null && container != null && !isOpenDetail) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        int diffX = (int) (event.getRawX() - initialTouchX);
                        int diffY = (int) (event.getRawY() - initialTouchY);
                        params.x = initialX + diffX;
                        params.y = initialY + diffY;
                        if (attached)
                            windowManager.updateViewLayout(container, params);
                        return true;
                    case MotionEvent.ACTION_UP:
                        int positionNewX;
                        int positionNewY;

                        if (params.x + params.width / 2 < widthScreen / 2) {
                            positionNewX = 0;
                        } else {
                            positionNewX = widthScreen - params.width;
                        }

                        if (params.y < statusBarHeight) {
                            positionNewY = statusBarHeight;
                        } else if (params.y + params.height > heightScreen - Utilities.dpToPixels(56, getResources())) {
                            positionNewY = heightScreen - Utilities.dpToPixels(56, getResources()) - params.height;
                        } else {
                            positionNewY = params.y;
                        }
                        animate(params.x, positionNewX, params.y, positionNewY);
                        return true;
                }
            }
            return false;
        }
    };

    public void animate(int startX, int endX, int startY, int endY) {

        PropertyValuesHolder pvhX = PropertyValuesHolder.ofInt(X, startX, endX);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofInt(Y, startY, endY);

        ValueAnimator translator = ValueAnimator.ofPropertyValuesHolder(pvhX, pvhY);
        translator.setInterpolator(new OvershootInterpolator(OVERSHOOT_INTERPOLATOR_DEFAULT));
        translator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (!attached)
                    return;
                if (windowManager != null && container != null && params != null) {
                    params.x = (Integer) valueAnimator.getAnimatedValue(X);
                    params.y = (Integer) valueAnimator.getAnimatedValue(Y);
                    windowManager.updateViewLayout(container, params);
                }
            }
        });

        translator.setDuration(300);
        translator.start();
    }

    private void scaleView(State state) {
        if (state == currentState) return;
        currentState = state;
        scaleView(/*currentState == State.MINI ? SCALE_MINI_DEFAULT : SCALE_ZOOM_DEFAULT*/);
    }

    private void scaleView(/*float offset*/) {
        if (currentState == State.MINI) {
            if (rootControllerView != null)
                rootControllerView.setVisibility(View.GONE);
        } else {
            if (rootControllerView != null)
                rootControllerView.setVisibility(View.VISIBLE);
        }
        if (fullView != null)
            fullView.setVisibility(View.VISIBLE);
        if (mPlayer.getPlaybackState() != Player.STATE_ENDED)
            scheduleMini();
        if (retryView != null)
            if (mPlayer.getPlaybackState() == Player.STATE_ENDED)
                retryView.setVisibility(View.VISIBLE);
            else
                retryView.setVisibility(View.GONE);
    }

    @SuppressLint("WrongConstant")
    private void registerScreenReceiver() {
        if (screenBroadcastReceiver != null) {
            IntentFilter screenStateFilter = new IntentFilter();
            screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
            screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                registerReceiver(screenBroadcastReceiver, screenStateFilter, Context.RECEIVER_EXPORTED);
            } else {
                registerReceiver(screenBroadcastReceiver, screenStateFilter);
            }
        }
    }

    private void unregisterScreenReceiver() {
        try {
            if (screenBroadcastReceiver != null)
                unregisterReceiver(screenBroadcastReceiver);
        } catch (Exception ignored) {
        }
    }

    private void cancelScheduleProgress() {
        if (container != null && progressRunnable != null)
            container.removeCallbacks(progressRunnable);
    }

    private void scheduleProgress() {
        if (container != null && progressRunnable != null)
            container.removeCallbacks(progressRunnable);
        if (container != null && progressRunnable != null)
            container.post(progressRunnable);
    }

    private final Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            if (mPlayer != null && progressBarBottom != null) {
                if (progressBarBottom instanceof View && currentVideo != null)
                    ((View) progressBarBottom).setVisibility(currentVideo.isLive() ? View.GONE : View.VISIBLE);
                progressBarBottom.setPosition(mPlayer.getCurrentPosition());
                progressBarBottom.setBufferedPosition(mPlayer.getBufferedPosition());
                progressBarBottom.setDuration(mPlayer.getDuration());

                if (container != null && progressRunnable != null)
                    container.postDelayed(progressRunnable, 50L);
            }
        }
    };

    private void scheduleMini() {
        if (container != null && miniRunnable != null)
            container.removeCallbacks(miniRunnable);
        if (container != null && miniRunnable != null)
            container.postDelayed(miniRunnable, 5 * 1000L);
    }

    private final Runnable miniRunnable = new Runnable() {
        @Override
        public void run() {
            scaleView(State.MINI);
        }
    };

    private final BroadcastReceiver screenBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action) || mPlayer == null)
                return;
            switch (action) {
                case Intent.ACTION_SCREEN_OFF:
                    mPlayer.setPlayWhenReady(false);
                    break;
                case Intent.ACTION_SCREEN_ON:
                    mPlayer.setPlayWhenReady(true);
                    break;
            }
        }
    };

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (container != null && miniRunnable != null)
                container.removeCallbacks(miniRunnable);
            if (view == retryView) {
                if (mPlayer != null) {
                    mPlayer.seekTo(0);
                    mPlayer.setPlayWhenReady(true);

                    if (rootControllerView != null)
                        rootControllerView.setVisibility(View.GONE);
                    if (fullView != null)
                        fullView.setVisibility(View.GONE);
                    if (retryView != null)
                        retryView.setVisibility(View.GONE);
                }
            } else if (view == fullView) {
                isOpenDetail = true;
                currentVideo.setTimeCurrent(mPlayer.getCurrentPosition());
                currentVideo.setTimeDuration(mPlayer.getDuration());
                clearView();
                stopForeground(true);
                stopSelf();
//                if (ApplicationController.self() != null)
//                    VideoPlayerActivity.startFromService(VideoService.this, currentVideo, mediaName, false);
            } else if (removeView == view) {
                clearView();
                stopForeground(true);
                stopSelf();
            }
        }
    };

    private void disableController() {
        RingMePlayerUtil.getPlayer(mediaName).getPlayerView().setEnabled(false);
        RingMePlayerUtil.getPlayer(mediaName).getPlayerView().enableFast(true);
        RingMePlayerUtil.getPlayer(mediaName).getPlayerView().setUseController(false);
        RingMePlayerUtil.getPlayer(mediaName).getPlayerView().getController().setVisibility(View.GONE);
    }

    private void enableController() {
        RingMePlayerUtil.getPlayer(mediaName).getPlayerView().setEnabled(true);
        RingMePlayerUtil.getPlayer(mediaName).getPlayerView().enableFast(false);
        RingMePlayerUtil.getPlayer(mediaName).getPlayerView().setUseController(true);
        RingMePlayerUtil.getPlayer(mediaName).getPlayerView().getController().setVisibility(View.VISIBLE);
    }

    private enum State {
        MINI, ZOOM
    }
}
