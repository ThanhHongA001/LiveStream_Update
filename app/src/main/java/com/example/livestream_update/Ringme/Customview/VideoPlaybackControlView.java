package com.example.livestream_update.Ringme.Customview;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ui.TimeBar;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.RepeatModeUtil;
import com.google.android.exoplayer2.util.Util;
import com.vtm.R;
import com.vtm.ringme.common.utils.image.ImageManager;
import com.vtm.ringme.model.tab_video.Video;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.utils.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by HaiKE on 3/22/18.
 */

public class VideoPlaybackControlView extends FrameLayout /* PlayerControlView*/ {

    /**
     * The default fast forward increment, in milliseconds.
     */
    public static final int DEFAULT_FAST_FORWARD_MS = 10 * 1000;
    /**
     * The default rewind increment, in milliseconds.
     */
    public static final int DEFAULT_REWIND_MS = 5000;
    /**
     * The default show timeout, in milliseconds.
     */
    public static final int DEFAULT_SHOW_TIMEOUT_MS = 2000;
    /**
     * The default repeat toggle modes.
     */
    public static final @RepeatModeUtil.RepeatToggleModes
    int DEFAULT_REPEAT_TOGGLE_MODES =
            RepeatModeUtil.REPEAT_TOGGLE_MODE_NONE;
    /**
     * The maximum number of windows that can be shown in a multi-window time bar.
     */
    public static final int MAX_WINDOWS_FOR_MULTI_WINDOW_TIME_BAR = 100;
    private static final long MAX_POSITION_FOR_SEEK_TO_PREVIOUS = 3000;
    private static final String TAG = "VideoPlaybackControlView";

    static {
        ExoPlayerLibraryInfo.registerModule("goog.exo.ui");
    }

    private final ComponentListener componentListener;
    private final View smallScreenView;
    private final TimeBar timeBar;
    private final StringBuilder formatBuilder;
    private final Formatter formatter;
    private final Timeline.Period period;
    private final Timeline.Window window;
    int currentState;
    private ImageView ivContent;
    private ImageView btnFullScreen;
    private View qualityView;
    //    private ImageView btnAudio;
    private ImageView playButton;
    private ImageView btnReplay;
    private ImageView btnNext;
    private ImageView btnPrevious;
    private TextView btnCancel;
    private ProgressLoading loading;
    private TextView positionView;
    private TextView durationView;
    private CampaignLayout campaignLayout;
    private RelativeLayout layoutVideo;
    private RelativeLayout layoutBottomBar;
    private RelativeLayout layoutTopbar;
    private ProgressTimeBar mBottomProgressBar;
    private View moreView;
    private View btnLive;
    private View viewFixLive;
    private View ivLiveStream;
    private ExoPlayer player;
    private com.google.android.exoplayer2.ControlDispatcher controlDispatcher;
    private CallBackListener callBackListener;
    private @Nullable
    PlaybackPreparer playbackPreparer;
    private boolean isAttachedToWindow;
    private boolean showMultiWindowTimeBar;
    private boolean multiWindowTimeBar;
    private boolean scrubbing;
    private int rewindMs;
    private int fastForwardMs;
    private int showTimeoutMs;
    private @RepeatModeUtil.RepeatToggleModes
    int repeatToggleModes;
    private boolean showShuffleButton;
    private long hideAtMs;
    private long[] adGroupTimesMs;
    private boolean[] playedAdGroups;
    private long[] extraAdGroupTimesMs;
    private boolean[] extraPlayedAdGroups;
    //    Runnable runnableBottomProgress = new Runnable() {
//        @Override
//        public void run() {
//            if (smallScreenView != null && smallScreenView.getVisibility() == VISIBLE)
//                return;
//            updateBottomProgress();
//            mBottomProgressBar.postDelayed(this, 50);
//        }
//    };
    private boolean fast = false;
    /**
     * thực hiện xây dựng tính năng count down
     */
    private boolean isCountDown;
    private ValueAnimator valueAnimator;
    private RelativeLayout layoutCountDown;
    private DonutProgress countDownProgress;
    private TextView tvNext;
    private TextView tvTitleVideoNext;
    private TextView tvDescriptionVideoNext;
    private ImageView ivNext;
    private boolean isCancel;
    private boolean isLive;
    private ArrayList<String> listAdPosition;
    private String oldPosition;
    private boolean isShowEpisode;
    private boolean isExpandedEpisode;
    private boolean isFullScreen;
    private int bottomBarPadding;
    private boolean checkVideoNew = false;
    private boolean isGameStreaming = false;
    private boolean isLiveStream = false;
    private boolean isLandscape;
    private final Runnable hideAction =
            new Runnable() {
                @Override
                public void run() {
                    if (player != null && !player.getPlayWhenReady())
                        return;
                    hide();
                }
            };
    private final Runnable updateProgressAction =
            new Runnable() {
                @Override
                public void run() {
                    updateProgress();
                }
            };

    public VideoPlaybackControlView(Context context) {
        this(context, null);
    }

    public VideoPlaybackControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlaybackControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, attrs);
    }

    public VideoPlaybackControlView(
            Context context, AttributeSet attrs, int defStyleAttr, AttributeSet playbackAttrs) {
        super(context, attrs, defStyleAttr);
        int controllerLayoutId = R.layout.rm_exo_video_playback_control_view;
        rewindMs = DEFAULT_REWIND_MS;
        fastForwardMs = DEFAULT_FAST_FORWARD_MS;
        showTimeoutMs = DEFAULT_SHOW_TIMEOUT_MS;
        repeatToggleModes = DEFAULT_REPEAT_TOGGLE_MODES;
        hideAtMs = C.TIME_UNSET;
        showShuffleButton = false;
        if (playbackAttrs != null) {
            TypedArray a =
                    context
                            .getTheme()
                            .obtainStyledAttributes(playbackAttrs, R.styleable.PlayerControlView, 0, 0);
            try {
                rewindMs = a.getInt(R.styleable.PlayerControlView_rewind_increment, rewindMs);
                fastForwardMs =
                        a.getInt(R.styleable.PlayerControlView_fastforward_increment, fastForwardMs);
                showTimeoutMs = a.getInt(R.styleable.PlayerControlView_show_timeout, showTimeoutMs);
                controllerLayoutId =
                        a.getResourceId(R.styleable.PlayerControlView_controller_layout_id, controllerLayoutId);
                repeatToggleModes = getRepeatToggleModes(a, repeatToggleModes);
                showShuffleButton =
                        a.getBoolean(R.styleable.PlayerControlView_show_shuffle_button, showShuffleButton);
            } finally {
                a.recycle();
            }
        }
        period = new Timeline.Period();
        window = new Timeline.Window();
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());
        adGroupTimesMs = new long[0];
        playedAdGroups = new boolean[0];
        extraAdGroupTimesMs = new long[0];
        extraPlayedAdGroups = new boolean[0];
        componentListener = new ComponentListener();
        controlDispatcher = new com.google.android.exoplayer2.DefaultControlDispatcher();
        bottomBarPadding = Utilities.dpToPx(20);
        LayoutInflater.from(context).inflate(controllerLayoutId, this);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);

        durationView = findViewById(R.id.exo_duration);
        positionView = findViewById(R.id.exo_position);
        loading = findViewById(R.id.exo_loading);
        smallScreenView = findViewById(R.id.exo_small_screen);
        btnFullScreen = findViewById(R.id.exo_fullscreen_icon);
//        btnAudio =  findViewById(R.id.btnAudio);
        playButton = findViewById(R.id.exo_play);
        btnReplay = findViewById(R.id.exo_replay);
        btnPrevious = findViewById(R.id.exo_previous);
        btnNext = findViewById(R.id.exo_next);
        btnCancel = findViewById(R.id.btnCancel);
        layoutVideo = findViewById(R.id.layout_video);
        layoutBottomBar = findViewById(R.id.layout_bottombar);
        layoutTopbar = findViewById(R.id.layoutTopbar);
        mBottomProgressBar = findViewById(R.id.progressBarBottom);
        campaignLayout = findViewById(R.id.campaign_Layout);
        if (campaignLayout != null)
            campaignLayout.hide();

        qualityView = findViewById(R.id.exo_quality);
        if (qualityView != null)
            qualityView.setOnClickListener(componentListener);

        moreView = findViewById(R.id.exo_more_view);
        if (moreView != null)
            moreView.setOnClickListener(componentListener);

        btnLive = findViewById(R.id.button_live_stream);
        viewFixLive = findViewById(R.id.layout_fix_icon_live);
        ivLiveStream = findViewById(R.id.iv_live_stream);

        if (playButton != null)
            playButton.setOnClickListener(componentListener);
        if (smallScreenView != null)
            smallScreenView.setOnClickListener(componentListener);
        if (btnFullScreen != null)
            btnFullScreen.setOnClickListener(componentListener);
//        if (btnAudio != null)
//            btnAudio.setOnClickListener(componentListener);
        if (btnReplay != null)
            btnReplay.setOnClickListener(componentListener);
        if (btnPrevious != null)
            btnPrevious.setOnClickListener(componentListener);
        if (btnNext != null)
            btnNext.setOnClickListener(componentListener);
        if (btnCancel != null)
            btnCancel.setOnClickListener(componentListener);

        timeBar = findViewById(R.id.exo_progress);
        if (timeBar != null) {
            timeBar.addListener(componentListener);
        }
//        setOnClickListener(componentListener);
        ivContent = findViewById(R.id.ivContent);
        initCountDown();
    }

    @SuppressWarnings("ResourceType")
    private static @RepeatModeUtil.RepeatToggleModes
    int getRepeatToggleModes(
            TypedArray a, @RepeatModeUtil.RepeatToggleModes int repeatToggleModes) {
        return a.getInt(R.styleable.PlayerControlView_repeat_toggle_modes, repeatToggleModes);
    }

    @SuppressLint("InlinedApi")
    private static boolean isHandledMediaKey(int keyCode) {
        return keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD
                || keyCode == KeyEvent.KEYCODE_MEDIA_REWIND
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY
                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE
                || keyCode == KeyEvent.KEYCODE_MEDIA_NEXT
                || keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS;
    }

    /**
     * Returns whether the specified {@code timeline} can be shown on a multi-window time bar.
     *
     * @param timeline The {@link Timeline} to check.
     * @param window   A scratch {@link Timeline.Window} instance.
     * @return Whether the specified timeline can be shown on a multi-window time bar.
     */
    private static boolean canShowMultiWindowTimeBar(Timeline timeline, Timeline.Window window) {
        if (timeline.getWindowCount() > MAX_WINDOWS_FOR_MULTI_WINDOW_TIME_BAR) {
            return false;
        }
        int windowCount = timeline.getWindowCount();
        for (int i = 0; i < windowCount; i++) {
            if (timeline.getWindow(i, window).durationUs == C.TIME_UNSET) {
                return false;
            }
        }
        return true;
    }

    /**
     * chuyển trạng thái button small
     */
    public void setSmallScreen() {
        isFullScreen = true;
        if (smallScreenView != null)
            smallScreenView.setVisibility(VISIBLE);
        if (btnFullScreen != null)
            btnFullScreen.setVisibility(GONE);
    }

    /**
     * chuyển trạng thái button small
     */
    public void setFullScreen() {
        isFullScreen = false;
        if (smallScreenView != null)
            smallScreenView.setVisibility(GONE);
        if (btnFullScreen != null)
            btnFullScreen.setVisibility(VISIBLE);
    }

    /**
     * Returns the {@link Player} currently being controlled by this view, or null if no player is
     * set.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the {@link Player} to control.
     *
     * @param player The {@link Player} to control.
     */
    public void setPlayer(ExoPlayer player) {
        if (this.player == player) {
            return;
        }
        if (this.player != null) {
            this.player.removeListener(componentListener);
        }
        this.player = player;
        if (player != null) {
            player.addListener(componentListener);
        }
        updateAll();
    }

    /**
     * Sets whether the time bar should show all windows, as opposed to just the current one. If the
     * timeline has a period with unknown duration or more than {@link
     * #MAX_WINDOWS_FOR_MULTI_WINDOW_TIME_BAR} windows the time bar will fall back to showing a single
     * window.
     *
     * @param showMultiWindowTimeBar Whether the time bar should show all windows.
     */
    public void setShowMultiWindowTimeBar(boolean showMultiWindowTimeBar) {
        this.showMultiWindowTimeBar = showMultiWindowTimeBar;
        updateTimeBarMode();
    }

    /**
     * Sets the millisecond positions of extra ad markers relative to the start of the window (or
     * timeline, if in multi-window mode) and whether each extra ad has been played or not. The
     * markers are shown in addition to any ad markers for ads in the player's timeline.
     *
     * @param extraAdGroupTimesMs The millisecond timestamps of the extra ad markers to show, or
     *                            {@code null} to show no extra ad markers.
     * @param extraPlayedAdGroups Whether each ad has been played, or {@code null} to show no extra ad
     *                            markers.
     */
    public void setExtraAdGroupMarkers(
            @Nullable long[] extraAdGroupTimesMs, @Nullable boolean[] extraPlayedAdGroups) {
        if (extraAdGroupTimesMs == null) {
            this.extraAdGroupTimesMs = new long[0];
            this.extraPlayedAdGroups = new boolean[0];
        } else {
            Assertions.checkArgument(extraAdGroupTimesMs.length == extraPlayedAdGroups.length);
            this.extraAdGroupTimesMs = extraAdGroupTimesMs;
            this.extraPlayedAdGroups = extraPlayedAdGroups;
        }
        updateProgress();
    }

    /**
     * Sets the {@link CallBackListener}.
     *
     * @param listener The listener to be notified about visibility changes.
     */
    public void setVisibilityListener(CallBackListener listener) {
        this.callBackListener = listener;
    }

    /**
     * Sets the {@link PlaybackPreparer}.
     *
     * @param playbackPreparer The {@link PlaybackPreparer}.
     */
    public void setPlaybackPreparer(@Nullable PlaybackPreparer playbackPreparer) {
        this.playbackPreparer = playbackPreparer;
    }

    /**
     * Sets the {@link com.google.android.exoplayer2.ControlDispatcher}.
     *
     * @param controlDispatcher The {@link com.google.android.exoplayer2.ControlDispatcher}, or null
     *                          to use {@link com.google.android.exoplayer2.DefaultControlDispatcher}.
     */
    public void setControlDispatcher(
            @Nullable com.google.android.exoplayer2.ControlDispatcher controlDispatcher) {
        this.controlDispatcher =
                controlDispatcher == null
                        ? new com.google.android.exoplayer2.DefaultControlDispatcher()
                        : controlDispatcher;
    }

    /**
     * Sets the rewind increment in milliseconds.
     *
     * @param rewindMs The rewind increment in milliseconds. A non-positive value will cause the
     *                 rewind button to be disabled.
     */
    public void setRewindIncrementMs(int rewindMs) {
        this.rewindMs = rewindMs;
        updateNavigation();
    }

    /**
     * Sets the fast forward increment in milliseconds.
     *
     * @param fastForwardMs The fast forward increment in milliseconds. A non-positive value will
     *                      cause the fast forward button to be disabled.
     */
    public void setFastForwardIncrementMs(int fastForwardMs) {
        this.fastForwardMs = fastForwardMs;
        updateNavigation();
    }

    /**
     * Returns the playback controls timeout. The playback controls are automatically hidden after
     * this duration of time has elapsed without user input.
     *
     * @return The duration in milliseconds. A non-positive value indicates that the controls will
     * remain visible indefinitely.
     */
    public int getShowTimeoutMs() {
        return showTimeoutMs;
    }

    /**
     * Sets the playback controls timeout. The playback controls are automatically hidden after this
     * duration of time has elapsed without user input.
     *
     * @param showTimeoutMs The duration in milliseconds. A non-positive value will cause the controls
     *                      to remain visible indefinitely.
     */
    public void setShowTimeoutMs(int showTimeoutMs) {
        this.showTimeoutMs = showTimeoutMs;
        if (isVisible()) {
            // Reset the timeout.
            hideAfterTimeout();
        }
    }

    /**
     * Returns which repeat toggle modes are enabled.
     *
     * @return The currently enabled {@link RepeatModeUtil.RepeatToggleModes}.
     */
    public @RepeatModeUtil.RepeatToggleModes
    int getRepeatToggleModes() {
        return repeatToggleModes;
    }

    /**
     * Sets which repeat toggle modes are enabled.
     *
     * @param repeatToggleModes A set of {@link RepeatModeUtil.RepeatToggleModes}.
     */
    public void setRepeatToggleModes(@RepeatModeUtil.RepeatToggleModes int repeatToggleModes) {
        this.repeatToggleModes = repeatToggleModes;
        if (player != null) {
            @Player.RepeatMode int currentMode = player.getRepeatMode();
            if (repeatToggleModes == RepeatModeUtil.REPEAT_TOGGLE_MODE_NONE
                    && currentMode != Player.REPEAT_MODE_OFF) {
                controlDispatcher.dispatchSetRepeatMode(player, Player.REPEAT_MODE_OFF);
            } else if (repeatToggleModes == RepeatModeUtil.REPEAT_TOGGLE_MODE_ONE
                    && currentMode == Player.REPEAT_MODE_ALL) {
                controlDispatcher.dispatchSetRepeatMode(player, Player.REPEAT_MODE_ONE);
            } else if (repeatToggleModes == RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL
                    && currentMode == Player.REPEAT_MODE_ONE) {
                controlDispatcher.dispatchSetRepeatMode(player, Player.REPEAT_MODE_ALL);
            }
        }
    }

    /**
     * Returns whether the shuffle button is shown.
     */
    public boolean getShowShuffleButton() {
        return showShuffleButton;
    }

    /**
     * Sets whether the shuffle button is shown.
     *
     * @param showShuffleButton Whether the shuffle button is shown.
     */
    public void setShowShuffleButton(boolean showShuffleButton) {
        this.showShuffleButton = showShuffleButton;
    }

    public void showFast() {
        fast = true;
        layoutVideo.setAlpha(1f);
        layoutVideo.setTranslationX(0);
        layoutVideo.setTranslationY(0);
        layoutVideo.setVisibility(VISIBLE);
        layoutBottomBar.setAlpha(1f);
        layoutBottomBar.setTranslationX(0);
        layoutBottomBar.setTranslationY(0);
        layoutBottomBar.setVisibility(VISIBLE);
        if (isGameStreaming) {
            if (btnLive != null) btnLive.setVisibility(GONE);
            if (viewFixLive != null) viewFixLive.setVisibility(GONE);
            if (ivLiveStream != null) ivLiveStream.setVisibility(GONE);
        } else if (isLiveStream) {
            if (btnLive != null) btnLive.setVisibility(GONE);
            if (viewFixLive != null) viewFixLive.setVisibility(GONE);
            if (ivLiveStream != null) ivLiveStream.setVisibility(isLandscape ? GONE : VISIBLE);
        } else if (isLive()) {
            if (btnLive != null)
                btnLive.setVisibility(isFullScreen ? VISIBLE : INVISIBLE);
            if (viewFixLive != null)
                viewFixLive.setVisibility(isFullScreen ? GONE : VISIBLE);
            if (ivLiveStream != null) ivLiveStream.setVisibility(GONE);
        } else {
            if (btnLive != null) btnLive.setVisibility(GONE);
            if (viewFixLive != null) viewFixLive.setVisibility(GONE);
            if (ivLiveStream != null) ivLiveStream.setVisibility(GONE);
        }
        mBottomProgressBar.setAlpha(1f);
        mBottomProgressBar.setTranslationX(0);
        mBottomProgressBar.setTranslationY(0);
        mBottomProgressBar.setVisibility(INVISIBLE);
        isExpandedEpisode = false;
    }

    public void clearFast() {
        fast = false;
        show();
    }

    /**
     * Shows the playback controls. If {@link #getShowTimeoutMs()} is positive then the controls will
     * be automatically hidden after this duration of time has elapsed without user input.
     */
    public void show() {
        if (isLiveStream) {
            if (callBackListener != null) {
                callBackListener.onClickViewFrame();
            }
            return;
        }
        if (isCountDown || fast) return;
        if (isGameStreaming) {
            if (btnLive != null) btnLive.setVisibility(GONE);
            if (viewFixLive != null) viewFixLive.setVisibility(GONE);
            if (ivLiveStream != null) ivLiveStream.setVisibility(GONE);
        } else if (isLive()) {
            if (btnLive != null)
                btnLive.setVisibility(isFullScreen ? VISIBLE : INVISIBLE);
            if (viewFixLive != null)
                viewFixLive.setVisibility(isFullScreen ? GONE : VISIBLE);
            if (ivLiveStream != null) ivLiveStream.setVisibility(GONE);
        } else {
            if (btnLive != null) btnLive.setVisibility(GONE);
            if (viewFixLive != null) viewFixLive.setVisibility(GONE);
            if (ivLiveStream != null) ivLiveStream.setVisibility(GONE);
        }
        if (layoutVideo.getVisibility() != VISIBLE) {
            layoutVideo.animate().cancel();
            layoutVideo.setAlpha(0f);
            layoutVideo.setVisibility(View.VISIBLE);
            layoutVideo.animate().alpha(1f).setListener(null)
                    .setInterpolator(new DecelerateInterpolator()).start();
            if (!isExpandedEpisode) {
                layoutBottomBar.animate().cancel();
                layoutBottomBar.setAlpha(0f);
                layoutBottomBar.setVisibility(View.VISIBLE);
                layoutBottomBar.animate().alpha(1f).translationY(0).setListener(null)
                        .setInterpolator(new DecelerateInterpolator()).start();
            }
            if (callBackListener != null) {
                callBackListener.onVisibilityChange(View.VISIBLE);
                callBackListener.onShowController();
            }
            updateAll();
            requestPlayPauseFocus();
        }
        // Call hideAfterTimeout even if already visible to reset the timeout.
        if (checkVideoNew && currentState != Player.STATE_ENDED) {
            hideAfterTimeout();
        } else if (!checkVideoNew) {
            hideAfterTimeout();
        }

        //an bottom progress bar
        hideBottomProgress();
    }

    public void hideAll() {
        if (fast) return;
        if (getVisibility() == VISIBLE) {
//            if (currentState == Player.STATE_BUFFERING)
//                return;

            setVisibility(GONE);
            if (callBackListener != null) {
                callBackListener.onVisibilityChange(View.GONE);
            }
            removeCallbacks(updateProgressAction);
            removeCallbacks(hideAction);
            hideAtMs = C.TIME_UNSET;

            hideBottomProgress();
        }
    }

    /**
     * Hides the controller.
     */
    public void hide() {
        if (fast) return;
        if (isGameStreaming) {
            if (btnLive != null) btnLive.setVisibility(GONE);
            if (viewFixLive != null) viewFixLive.setVisibility(GONE);
            if (ivLiveStream != null) ivLiveStream.setVisibility(GONE);
        } else if (isLiveStream) {
            if (btnLive != null) btnLive.setVisibility(GONE);
            if (viewFixLive != null) viewFixLive.setVisibility(GONE);
            if (ivLiveStream != null) ivLiveStream.setVisibility(isLandscape ? GONE : VISIBLE);
        } else if (isLive()) {
            if (btnLive != null)
                btnLive.setVisibility(isFullScreen ? VISIBLE : INVISIBLE);
            if (viewFixLive != null)
                viewFixLive.setVisibility(isFullScreen ? GONE : VISIBLE);
            if (ivLiveStream != null) ivLiveStream.setVisibility(GONE);
        } else {
            if (btnLive != null) btnLive.setVisibility(GONE);
            if (viewFixLive != null) viewFixLive.setVisibility(GONE);
            if (ivLiveStream != null) ivLiveStream.setVisibility(GONE);
        }

        //if (player != null && !player.getPlayWhenReady()) return;
        if (callBackListener != null) callBackListener.onHideController();
        if (layoutVideo.getVisibility() == VISIBLE) {
//            if (currentState == Player.STATE_BUFFERING)
//                return;

//            layoutVideo.setVisibility(GONE);
            if (layoutBottomBar.getVisibility() == VISIBLE) {
                layoutBottomBar.animate().cancel();
                layoutBottomBar.setAlpha(1f);
                layoutBottomBar.setTranslationY(0f);
                layoutBottomBar.setVisibility(View.VISIBLE);
                layoutBottomBar.animate()
                        .alpha(0f)
                        .translationY(layoutBottomBar.getHeight())
                        .setInterpolator(new DecelerateInterpolator())
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                if (layoutBottomBar != null) {
                                    layoutBottomBar.setVisibility(View.GONE);
                                }
                                // show bottom progress
                                showBottomProgress();
                            }
                        }).start();
            }
            layoutVideo.animate().cancel();
            layoutVideo.setAlpha(1f);
            layoutVideo.setTranslationY(0f);
            layoutVideo.setVisibility(View.VISIBLE);
            layoutVideo.animate()
                    .alpha(0f)
                    .setInterpolator(new DecelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (fast) return;
                            if (layoutVideo != null) {
                                layoutVideo.setVisibility(View.GONE);
                                if (callBackListener != null) {
                                    callBackListener.onVisibilityChange(View.GONE);
                                }
                            }
                        }
                    }).start();
//            removeCallbacks(updateProgressAction);
            removeCallbacks(hideAction);
            hideAtMs = C.TIME_UNSET;
        }
    }

    /**
     * Returns whether the controller is currently visible.
     */
    public boolean isVisible() {
        return layoutVideo.getVisibility() == VISIBLE;
    }

    private void hideAfterTimeout() {
        removeCallbacks(hideAction);

//        if (player != null && player.getPlayWhenReady()) {
//            showTimeoutMs = DEFAULT_SHOW_TIMEOUT_MS;
//        }

        if (showTimeoutMs > 0) {
            hideAtMs = SystemClock.uptimeMillis() + showTimeoutMs;
            if (isAttachedToWindow) {
                postDelayed(hideAction, showTimeoutMs);
            }
        } else {
            hideAtMs = C.TIME_UNSET;
        }
    }

    private void updateAll() {
        updatePlayPauseButton();
        updateNavigation();
        updateProgress();

        if (player != null && player.getPlaybackState() == Player.STATE_READY)
            loading.setVisibility(GONE);
    }

    private void updatePlayPauseButton() {
        if (!isVisible() || !isAttachedToWindow) {
            return;
        }
        boolean requestPlayPauseFocus = false;
        boolean playing = player != null && player.getPlayWhenReady();

        if (playButton != null) {
            requestPlayPauseFocus |= playing && playButton.isFocused();
//            playButton.setImageResource(playing ? R.drawable.ic_tab_video_pause : R.drawable.ic_tab_video_play);
            playButton.setImageResource(playing ? R.drawable.rm_icon_pause_movies : R.drawable.rm_ic_v5_om_play);
//            if (playButton.getVisibility() != VISIBLE) {
//                boolean show = player != null && player.getPlaybackState() == Player.STATE_READY;
//                if (show) {
//                    playButton.animate().cancel();
//                    playButton.setAlpha(0f);
//                    playButton.setVisibility(View.VISIBLE);
//                    playButton.animate().alpha(1f).setListener(null)
//                            .setInterpolator(new DecelerateInterpolator()).start();
//                }
//            }
        }
        if (requestPlayPauseFocus) {
            requestPlayPauseFocus();
        }
    }

    private void updateNavigation() {
        if (!isVisible() || !isAttachedToWindow) {
            return;
        }
        Timeline timeline = player != null ? player.getCurrentTimeline() : null;
        boolean haveNonEmptyTimeline = timeline != null && !timeline.isEmpty();
        boolean isSeekable = false;
//        boolean enablePrevious = false;
//        boolean enableNext = false;
        if (haveNonEmptyTimeline && !player.isPlayingAd()) {
            int windowIndex = player.getCurrentWindowIndex();
            timeline.getWindow(windowIndex, window);
            isSeekable = window.isSeekable;
//            enablePrevious =
//                    isSeekable || !window.isDynamic || player.getPreviousWindowIndex() != C.INDEX_UNSET;
//            enableNext = window.isDynamic || player.getNextWindowIndex() != C.INDEX_UNSET;
        }
//        setButtonEnabled(enablePrevious, previousButton);
//        setButtonEnabled(enableNext, nextButton);
//        setButtonEnabled(fastForwardMs > 0 && isSeekable, fastForwardButton);
//        setButtonEnabled(rewindMs > 0 && isSeekable, rewindButton);
        if (timeBar != null) {
            timeBar.setEnabled(isSeekable);
        }
    }

    private void updateTimeBarMode() {
        if (player == null) {
            return;
        }
        multiWindowTimeBar =
                showMultiWindowTimeBar && canShowMultiWindowTimeBar(player.getCurrentTimeline(), window);
    }

    private void updateProgress() {
//        if (!isVisible() || !isAttachedToWindow) {
//            return;
//        }
        if (!isAttachedToWindow) {
            return;
        }

        long position = 0;
        long bufferedPosition = 0;
        long duration = 0;
        if (player != null) {
            long currentWindowTimeBarOffsetUs = 0;
            long durationUs = 0;
            int adGroupCount = 0;
            Timeline timeline = player.getCurrentTimeline();
            if (!timeline.isEmpty()) {
                int currentWindowIndex = player.getCurrentWindowIndex();
                int firstWindowIndex = multiWindowTimeBar ? 0 : currentWindowIndex;
                int lastWindowIndex =
                        multiWindowTimeBar ? timeline.getWindowCount() - 1 : currentWindowIndex;
                for (int i = firstWindowIndex; i <= lastWindowIndex; i++) {
                    if (i == currentWindowIndex) {
                        currentWindowTimeBarOffsetUs = durationUs;
                    }
                    timeline.getWindow(i, window);
                    if (window.durationUs == C.TIME_UNSET) {
                        Assertions.checkState(!multiWindowTimeBar);
                        break;
                    }
                    for (int j = window.firstPeriodIndex; j <= window.lastPeriodIndex; j++) {
                        timeline.getPeriod(j, period);
                        int periodAdGroupCount = period.getAdGroupCount();
                        for (int adGroupIndex = 0; adGroupIndex < periodAdGroupCount; adGroupIndex++) {
                            long adGroupTimeInPeriodUs = period.getAdGroupTimeUs(adGroupIndex);
                            if (adGroupTimeInPeriodUs == C.TIME_END_OF_SOURCE) {
                                if (period.durationUs == C.TIME_UNSET) {
                                    // Don't show ad markers for postrolls in periods with unknown duration.
                                    continue;
                                }
                                adGroupTimeInPeriodUs = period.durationUs;
                            }
                            long adGroupTimeInWindowUs = adGroupTimeInPeriodUs + period.getPositionInWindowUs();
                            if (adGroupTimeInWindowUs >= 0 && adGroupTimeInWindowUs <= window.durationUs) {
                                if (adGroupCount == adGroupTimesMs.length) {
                                    int newLength = adGroupTimesMs.length == 0 ? 1 : adGroupTimesMs.length * 2;
                                    adGroupTimesMs = Arrays.copyOf(adGroupTimesMs, newLength);
                                    playedAdGroups = Arrays.copyOf(playedAdGroups, newLength);
                                }
                                adGroupTimesMs[adGroupCount] = C.usToMs(durationUs + adGroupTimeInWindowUs);
                                playedAdGroups[adGroupCount] = period.hasPlayedAdGroup(adGroupIndex);
                                adGroupCount++;
                            }
                        }
                    }
                    durationUs += window.durationUs;
                }
            }
            duration = C.usToMs(durationUs);
            position = C.usToMs(currentWindowTimeBarOffsetUs);
            bufferedPosition = position;
            if (player.isPlayingAd()) {
                position += player.getContentPosition();
                bufferedPosition = position;
            } else {
                position += player.getCurrentPosition();
                bufferedPosition += player.getBufferedPosition();
            }
            if (timeBar != null) {
                int extraAdGroupCount = extraAdGroupTimesMs.length;
                int totalAdGroupCount = adGroupCount + extraAdGroupCount;
                if (totalAdGroupCount > adGroupTimesMs.length) {
                    adGroupTimesMs = Arrays.copyOf(adGroupTimesMs, totalAdGroupCount);
                    playedAdGroups = Arrays.copyOf(playedAdGroups, totalAdGroupCount);
                }
                System.arraycopy(extraAdGroupTimesMs, 0, adGroupTimesMs, adGroupCount, extraAdGroupCount);
                System.arraycopy(extraPlayedAdGroups, 0, playedAdGroups, adGroupCount, extraAdGroupCount);
                timeBar.setAdGroupTimesMs(adGroupTimesMs, playedAdGroups, totalAdGroupCount);
            }
        }
        if (durationView != null) {
            if (isLive() || isLiveStream || isGameStreaming)
                durationView.setVisibility(GONE);
            else {
                durationView.setVisibility(VISIBLE);
                durationView.setText(Util.getStringForTime(formatBuilder, formatter, duration));
            }
        }
        if (positionView != null && !scrubbing) {
            if (isLive() || isLiveStream || isGameStreaming) {
                positionView.setVisibility(GONE);
                //positionView.setText("Live");
                //positionView.setTextColor(getResources().getColor(R.color.red));
            } else {
                positionView.setVisibility(VISIBLE);
                positionView.setTextColor(getResources().getColor(R.color.white));
                positionView.setText(Util.getStringForTime(formatBuilder, formatter, position));
                if (callBackListener != null) callBackListener.onTimeChange(position);
            }
        }
        if (isGameStreaming) {
            if (btnLive != null) btnLive.setVisibility(GONE);
            if (viewFixLive != null) viewFixLive.setVisibility(GONE);
            if (ivLiveStream != null) ivLiveStream.setVisibility(GONE);
        } else if (isLiveStream) {
            if (btnLive != null) btnLive.setVisibility(GONE);
            if (viewFixLive != null) viewFixLive.setVisibility(GONE);
            if (ivLiveStream != null) ivLiveStream.setVisibility(isLandscape ? GONE : VISIBLE);
        } else if (isLive()) {
            if (btnLive != null)
                btnLive.setVisibility(isFullScreen ? VISIBLE : INVISIBLE);
            if (viewFixLive != null)
                viewFixLive.setVisibility(isFullScreen ? GONE : VISIBLE);
            if (ivLiveStream != null) ivLiveStream.setVisibility(GONE);
        } else {
            if (btnLive != null) btnLive.setVisibility(GONE);
            if (viewFixLive != null) viewFixLive.setVisibility(GONE);
            if (ivLiveStream != null) ivLiveStream.setVisibility(GONE);
        }
        checkShowAd(position);
        if (timeBar != null) {
            if (timeBar instanceof View)
                ((View) timeBar).setVisibility((isLive() || isLiveStream || isGameStreaming) ? INVISIBLE : VISIBLE);
            timeBar.setPosition(position);
            timeBar.setBufferedPosition(bufferedPosition);
            timeBar.setDuration(duration);
        }

        if (mBottomProgressBar != null) {
            mBottomProgressBar.setVisibility((isLive() || isShowEpisode || isLiveStream || isGameStreaming) ? INVISIBLE : VISIBLE);
            mBottomProgressBar.setPosition(position);
            mBottomProgressBar.setBufferedPosition(bufferedPosition);
            mBottomProgressBar.setDuration(duration);
        }

        // Cancel any pending updates and schedule a new one if necessary.
        removeCallbacks(updateProgressAction);
        int playbackState = player == null ? Player.STATE_IDLE : player.getPlaybackState();
        if (playbackState != Player.STATE_IDLE && playbackState != Player.STATE_ENDED) {
            long delayMs;
            if (player.getPlayWhenReady() && playbackState == Player.STATE_READY) {
                float playbackSpeed = player.getPlaybackParameters().speed;
                if (playbackSpeed <= 0.1f) {
                    delayMs = 1000;
                } else if (playbackSpeed <= 5f) {
                    long mediaTimeUpdatePeriodMs = 1000 / Math.max(1, Math.round(1 / playbackSpeed));
                    long mediaTimeDelayMs = mediaTimeUpdatePeriodMs - (position % mediaTimeUpdatePeriodMs);
                    if (mediaTimeDelayMs < (mediaTimeUpdatePeriodMs / 5)) {
                        mediaTimeDelayMs += mediaTimeUpdatePeriodMs;
                    }
                    delayMs =
                            playbackSpeed == 1 ? mediaTimeDelayMs : (long) (mediaTimeDelayMs / playbackSpeed);
                } else {
                    delayMs = 200;
                }
            } else {
                delayMs = 1000;
            }
            postDelayed(updateProgressAction, delayMs);
        }
    }

    private void requestPlayPauseFocus() {
        if (playButton != null) {
            playButton.requestFocus();
        }
    }

    private void setButtonEnabled(boolean enabled, View view) {
        if (view == null) {
            return;
        }
        view.setEnabled(enabled);
        view.setAlpha(enabled ? 1f : 0.3f);
        view.setVisibility(VISIBLE);
    }

    private void previous() {
        Timeline timeline = player.getCurrentTimeline();
        if (timeline.isEmpty()) {
            return;
        }
        int windowIndex = player.getCurrentWindowIndex();
        timeline.getWindow(windowIndex, window);
        int previousWindowIndex = player.getPreviousWindowIndex();
        if (previousWindowIndex != C.INDEX_UNSET
                && (player.getCurrentPosition() <= MAX_POSITION_FOR_SEEK_TO_PREVIOUS
                || (window.isDynamic && !window.isSeekable))) {
            seekTo(previousWindowIndex, C.TIME_UNSET);
        } else {
            seekTo(0);
        }
    }

    private void next() {
        Timeline timeline = player.getCurrentTimeline();
        if (timeline.isEmpty()) {
            return;
        }
        int windowIndex = player.getCurrentWindowIndex();
        int nextWindowIndex = player.getNextWindowIndex();
        if (nextWindowIndex != C.INDEX_UNSET) {
            seekTo(nextWindowIndex, C.TIME_UNSET);
        } else if (timeline.getWindow(windowIndex, window, false).isDynamic) {
            seekTo(windowIndex, C.TIME_UNSET);
        }
    }

//    private void showController() {
//        layoutVideo.setVisibility(VISIBLE);
//        layoutBottomBar.setVisibility(VISIBLE);
//        hideAfterTimeout();
//    }

    private void rewind() {
        if (rewindMs <= 0) {
            return;
        }
        seekTo(Math.max(player.getCurrentPosition() - rewindMs, 0));
    }

    public void fastForward() {
        if (fastForwardMs <= 0 || player == null) {
            return;
        }
        long durationMs = player.getDuration();
        long seekPositionMs = player.getCurrentPosition() + fastForwardMs;
        if (durationMs != C.TIME_UNSET) {
            seekPositionMs = Math.min(seekPositionMs, durationMs - 1000);
            seekTo(seekPositionMs);

            if (callBackListener != null)
                callBackListener.onHaveSeek(true);
        }
    }

    public void fastPrevious() {
        if (fastForwardMs <= 0 || player == null) {
            return;
        }
        long durationMs = player.getDuration();
        long seekPositionMs = player.getCurrentPosition() - fastForwardMs;
        if (durationMs != C.TIME_UNSET) {
            seekPositionMs = Math.max(seekPositionMs, 1000);
            seekTo(seekPositionMs);
            if (callBackListener != null)
                callBackListener.onHaveSeek(true);
        }
    }

    private void seekTo(long positionMs) {
        seekTo(player.getCurrentWindowIndex(), positionMs);
    }

    private void seekTo(int windowIndex, long positionMs) {
        boolean dispatched = controlDispatcher.dispatchSeekTo(player, windowIndex, positionMs);
        if (!dispatched) {
            // The seek wasn't dispatched. If the progress bar was dragged by the user to perform the
            // seek then it'll now be in the wrong position. Trigger a progress update to snap it back.
            updateProgress();
        }
    }

    private void seekToTimeBarPosition(long positionMs) {
        int windowIndex;
        Timeline timeline = player.getCurrentTimeline();
        if (multiWindowTimeBar && !timeline.isEmpty()) {
            int windowCount = timeline.getWindowCount();
            windowIndex = 0;
            while (true) {
                long windowDurationMs = timeline.getWindow(windowIndex, window).getDurationMs();
                if (positionMs < windowDurationMs) {
                    break;
                } else if (windowIndex == windowCount - 1) {
                    // Seeking past the end of the last window should seek to the end of the timeline.
                    positionMs = windowDurationMs;
                    break;
                }
                positionMs -= windowDurationMs;
                windowIndex++;
            }
        } else {
            windowIndex = player.getCurrentWindowIndex();
        }
        seekTo(windowIndex, positionMs);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedToWindow = true;
        if (hideAtMs != C.TIME_UNSET) {
            long delayMs = hideAtMs - SystemClock.uptimeMillis();
            if (delayMs <= 0) {
                hide();
            } else {
                postDelayed(hideAction, delayMs);
            }
        } else if (isVisible()) {
            hideAfterTimeout();
        }
        updateAll();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttachedToWindow = false;
        removeCallbacks(updateProgressAction);
        removeCallbacks(hideAction);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return dispatchMediaKeyEvent(event) || super.dispatchKeyEvent(event);
    }

    /**
     * Called to process media key events. Any {@link KeyEvent} can be passed but only media key
     * events will be handled.
     *
     * @param event A key event.
     * @return Whether the key event was handled.
     */
    public boolean dispatchMediaKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (player == null || !isHandledMediaKey(keyCode)) {
            return false;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
                fastForward();
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_REWIND) {
                rewind();
            } else if (event.getRepeatCount() == 0) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                        controlDispatcher.dispatchSetPlayWhenReady(player, !player.getPlayWhenReady());
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PLAY:
                        controlDispatcher.dispatchSetPlayWhenReady(player, true);
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PAUSE:
                        controlDispatcher.dispatchSetPlayWhenReady(player, false);
                        break;
                    case KeyEvent.KEYCODE_MEDIA_NEXT:
                        next();
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                        previous();
                        break;
                    default:
                        break;
                }
            }
        }
        return true;
    }

    private boolean isPlaying() {
        return player != null
                && player.getPlaybackState() != Player.STATE_ENDED
                && player.getPlaybackState() != Player.STATE_IDLE
                && player.getPlayWhenReady();
    }

    private void initCountDown() {
        layoutCountDown = findViewById(R.id.layoutCountDown);
        countDownProgress = findViewById(R.id.countDownProgress);
        tvNext = findViewById(R.id.tvNext);
        tvTitleVideoNext = findViewById(R.id.tvTitleVideoNext);
        tvDescriptionVideoNext = findViewById(R.id.tvDescriptionVideoNext);
        ivNext = findViewById(R.id.ivNext);
        if (ivNext != null)
            ivNext.setOnClickListener(componentListener);
    }

    public void setVideoNext(Video video) {
        tvTitleVideoNext.setText(video.getTitle());
        tvDescriptionVideoNext.setText(video.getDescription());
        ImageManager.showImage(video.getImagePath(), ivContent);
    }

    public void setCountDown(boolean countDown) {
        isCountDown = countDown;
        if (isCountDown) {
            startCountDown();
        } else {
            stopCountDown();
        }
    }

    private void stopCountDown() {
        isCountDown = false;
        if (valueAnimator != null) valueAnimator.cancel();
        hideCountDown();
    }

    private void startCountDown() {
        loading.setVisibility(GONE);
        layoutVideo.setVisibility(GONE);
        layoutCountDown.setVisibility(VISIBLE);
        valueAnimator = ValueAnimator.ofFloat(0, 100);
        valueAnimator.setDuration(5 * 1000L);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float) valueAnimator.getAnimatedValue();
                if (countDownProgress != null) countDownProgress.setProgress(value);
            }

        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (callBackListener != null && !isCancel) {
                    callBackListener.onPlayNextVideo();
                    stopCountDown();
                }
            }
        });
        valueAnimator.start();
    }

    public void hideCountDown() {
        layoutCountDown.setVisibility(GONE);
    }

    //HaiKE custom
    public void showLoadingMedia(boolean flag) {
        if (flag) {
            playButton.setVisibility(GONE);
            loading.setVisibility(VISIBLE);
            ivContent.setVisibility(GONE);
        } else {
            playButton.setVisibility(isGameStreaming ? GONE : VISIBLE);
            loading.setVisibility(GONE);
//            hideDeferred();
            hideAfterTimeout();
        }
        hideReplay();
    }

    public void showReplay() {
        playButton.setVisibility(GONE);
        loading.setVisibility(GONE);
        btnReplay.setVisibility(isGameStreaming ? GONE : VISIBLE);

        showHidePreviousButton(false);
        showHideNextButton(false);
    }

    public void showHidePreviousButton(boolean isShow) {
        if (btnPrevious != null)
            btnPrevious.setVisibility(isShow && !isExpandedEpisode ? VISIBLE : GONE);
    }

    public void showHideNextButton(boolean isShow) {
        if (btnNext != null) btnNext.setVisibility(isShow && !isExpandedEpisode ? VISIBLE : GONE);
    }

    public void hideReplay() {
        btnReplay.setVisibility(GONE);
        isCancel = false;
    }

    public void showBottomProgress() {
        if (smallScreenView != null && smallScreenView.getVisibility() == VISIBLE)
            return;
        if (mBottomProgressBar != null) {
            mBottomProgressBar.setVisibility(isLive() || isShowEpisode ? INVISIBLE : VISIBLE);
            mBottomProgressBar.animate().cancel();
            mBottomProgressBar.setAlpha(0f);
            mBottomProgressBar.animate().alpha(1f).start();
//            mBottomProgressBar.post(runnableBottomProgress);
        }
    }

    private void updateBottomProgress() {
        long position = 0;
        long bufferedPosition = 0;
        long duration = 0;
        if (player != null) {
            long currentWindowTimeBarOffsetUs = 0;
            long durationUs = 0;
            int adGroupCount = 0;
            Timeline timeline = player.getCurrentTimeline();
            if (!timeline.isEmpty()) {
                int currentWindowIndex = player.getCurrentWindowIndex();
                int firstWindowIndex = multiWindowTimeBar ? 0 : currentWindowIndex;
                int lastWindowIndex =
                        multiWindowTimeBar ? timeline.getWindowCount() - 1 : currentWindowIndex;
                for (int i = firstWindowIndex; i <= lastWindowIndex; i++) {
                    if (i == currentWindowIndex) {
                        currentWindowTimeBarOffsetUs = durationUs;
                    }
                    timeline.getWindow(i, window);
                    if (window.durationUs == C.TIME_UNSET) {
                        Assertions.checkState(!multiWindowTimeBar);
                        break;
                    }
                    for (int j = window.firstPeriodIndex; j <= window.lastPeriodIndex; j++) {
                        timeline.getPeriod(j, period);
                        int periodAdGroupCount = period.getAdGroupCount();
                        for (int adGroupIndex = 0; adGroupIndex < periodAdGroupCount; adGroupIndex++) {
                            long adGroupTimeInPeriodUs = period.getAdGroupTimeUs(adGroupIndex);
                            if (adGroupTimeInPeriodUs == C.TIME_END_OF_SOURCE) {
                                if (period.durationUs == C.TIME_UNSET) {
                                    // Don't show ad markers for postrolls in periods with unknown duration.
                                    continue;
                                }
                                adGroupTimeInPeriodUs = period.durationUs;
                            }
                            long adGroupTimeInWindowUs = adGroupTimeInPeriodUs + period.getPositionInWindowUs();
                            if (adGroupTimeInWindowUs >= 0 && adGroupTimeInWindowUs <= window.durationUs) {
                                if (adGroupCount == adGroupTimesMs.length) {
                                    int newLength = adGroupTimesMs.length == 0 ? 1 : adGroupTimesMs.length * 2;
                                    adGroupTimesMs = Arrays.copyOf(adGroupTimesMs, newLength);
                                    playedAdGroups = Arrays.copyOf(playedAdGroups, newLength);
                                }
                                adGroupTimesMs[adGroupCount] = C.usToMs(durationUs + adGroupTimeInWindowUs);
                                playedAdGroups[adGroupCount] = period.hasPlayedAdGroup(adGroupIndex);
                                adGroupCount++;
                            }
                        }
                    }
                    durationUs += window.durationUs;
                }
            }
            duration = C.usToMs(durationUs);
            position = C.usToMs(currentWindowTimeBarOffsetUs);
            bufferedPosition = position;
            if (player.isPlayingAd()) {
                position += player.getContentPosition();
                bufferedPosition = position;
            } else {
                position += player.getCurrentPosition();
                bufferedPosition += player.getBufferedPosition();
            }

            if (mBottomProgressBar != null) {
                int extraAdGroupCount = extraAdGroupTimesMs.length;
                int totalAdGroupCount = adGroupCount + extraAdGroupCount;
                if (totalAdGroupCount > adGroupTimesMs.length) {
                    adGroupTimesMs = Arrays.copyOf(adGroupTimesMs, totalAdGroupCount);
                    playedAdGroups = Arrays.copyOf(playedAdGroups, totalAdGroupCount);
                }
                System.arraycopy(extraAdGroupTimesMs, 0, adGroupTimesMs, adGroupCount, extraAdGroupCount);
                System.arraycopy(extraPlayedAdGroups, 0, playedAdGroups, adGroupCount, extraAdGroupCount);
                mBottomProgressBar.setAdGroupTimesMs(adGroupTimesMs, playedAdGroups, totalAdGroupCount);
            }
        }
//        if (timeBar != null) {
//            timeBar.setPosition(position);
//            timeBar.setBufferedPosition(bufferedPosition);
//            timeBar.setDuration(duration);
//        }
        if (mBottomProgressBar != null) {
            mBottomProgressBar.setPosition(position);
            mBottomProgressBar.setBufferedPosition(bufferedPosition);
            mBottomProgressBar.setDuration(duration);
        }
    }

    private void checkShowAd(long position) {
        String pos = String.valueOf(Math.round(position / 1000f) * 1000);
        //Log.i(TAG, "checkShowAd oldPosition: " + oldPosition + ", position: " + position + ", pos: " + pos);
        if (position > 0 && Utilities.notEmpty(listAdPosition) && listAdPosition.contains(pos) && !pos.equals(oldPosition)) {
            Log.d(TAG, "checkShowAd onRequestAds position: " + position + ", pos: " + pos);
            if (callBackListener != null) callBackListener.onRequestAds(pos);
        }
        oldPosition = pos;
    }

    private void hideBottomProgress() {
        if (mBottomProgressBar != null) {
            mBottomProgressBar.animate().cancel();
            mBottomProgressBar.setAlpha(1f);
            mBottomProgressBar.animate().alpha(0f).start();
//            mBottomProgressBar.removeCallbacks(runnableBottomProgress);
        }
    }

    public void processState(int playbackState) {
        Log.i(TAG, "processState: " + playbackState);
        currentState = playbackState;
        updatePlayPauseButton();
        updateProgress();

        if (callBackListener != null)
            callBackListener.onPlayerStateChanged(playbackState);

        if (playbackState == Player.STATE_ENDED) {
            if (fast) return;
            if (checkVideoNew) {
                show();
                showReplay();
                return;
            } else {
                layoutVideo.setVisibility(GONE);
            }
            timeBar.setPosition(player.getDuration());
            if (mBottomProgressBar != null)
                mBottomProgressBar.setPosition(player.getDuration());
            showReplay();
        } else if (playbackState == Player.STATE_BUFFERING) {
            showLoadingMedia(true);
        } else if (playbackState == Player.STATE_READY) {
            showLoadingMedia(false);
        } else if (playbackState == Player.STATE_IDLE) {
            showLoadingMedia(true);
        }
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public View getQualityView() {
        return qualityView;
    }

    public View getViewMore() {
        return moreView;
    }

    public View getViewFullSreen() {
        return btnFullScreen;
    }

    public boolean isCheckVideoNew() {
        return checkVideoNew;
    }

    public void setCheckVideoNew(boolean checkVideoNew) {
        this.checkVideoNew = checkVideoNew;
    }

    public void setListAdPosition(ArrayList<String> listAdPosition) {
        this.listAdPosition = listAdPosition;
    }

    public CampaignLayout getCampaignLayout() {
        return campaignLayout;
    }

    public void showBottomBar(boolean isShow) {
        if (layoutBottomBar != null) {
            layoutBottomBar.setVisibility(isShow ? VISIBLE : GONE);
        }
        if (isGameStreaming) {
            if (btnLive != null) btnLive.setVisibility(GONE);
            if (viewFixLive != null) viewFixLive.setVisibility(GONE);
            if (ivLiveStream != null) ivLiveStream.setVisibility(GONE);
        } else if (isLiveStream) {
            if (btnLive != null) btnLive.setVisibility(GONE);
            if (viewFixLive != null) viewFixLive.setVisibility(GONE);
            if (ivLiveStream != null) ivLiveStream.setVisibility(isLandscape ? GONE : VISIBLE);
        } else if (isLive()) {
            if (btnLive != null)
                btnLive.setVisibility(isFullScreen ? VISIBLE : INVISIBLE);
            if (viewFixLive != null)
                viewFixLive.setVisibility(isFullScreen ? GONE : VISIBLE);
            if (ivLiveStream != null) ivLiveStream.setVisibility(GONE);
        } else {
            if (btnLive != null) btnLive.setVisibility(GONE);
            if (viewFixLive != null) viewFixLive.setVisibility(GONE);
            if (ivLiveStream != null) ivLiveStream.setVisibility(GONE);
        }
    }

    public void showEpisode(boolean isShow) {
        Log.d(TAG, "showEpisode isShowEpisode: " + isShowEpisode);
        isShowEpisode = isShow;
        if (layoutBottomBar != null) {
            try {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layoutBottomBar.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, (isShowEpisode && isFullScreen) ? bottomBarPadding : 0);
                layoutBottomBar.setLayoutParams(layoutParams);
                layoutBottomBar.requestLayout();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setEnablePreviousButton(boolean flag) {
        if (btnPrevious != null) {
            btnPrevious.setEnabled(flag);
            btnPrevious.setAlpha(flag ? 1f : 0.4f);
        }
    }

    public void setEnableNextButton(boolean flag) {
        if (btnNext != null) {
            btnNext.setEnabled(flag);
            btnNext.setAlpha(flag ? 1f : 0.4f);
        }
    }

    public void setVisiblePreviousAndNextButton(boolean flag) {
        if (btnNext != null)
            btnNext.setVisibility(flag && !isExpandedEpisode ? VISIBLE : GONE);
        if (btnPrevious != null)
            btnPrevious.setVisibility(flag && !isExpandedEpisode ? VISIBLE : GONE);
    }

    public void setExpandedEpisode(boolean expandedEpisode) {
        isExpandedEpisode = expandedEpisode;
    }

    public void setupGameStreaming() {
        isGameStreaming = true;
        if (moreView != null) moreView.setVisibility(GONE);
        if (btnFullScreen != null) btnFullScreen.setVisibility(GONE);
        if (playButton != null) playButton.setVisibility(GONE);
    }

    public void setupLiveStream() {
        isLiveStream = true;
        setLandscape(isLandscape);
    }

    public void setLandscape(boolean isLandscape) {
        this.isLandscape = isLandscape;
        if (btnLive != null) btnLive.setVisibility(GONE);
        if (viewFixLive != null) viewFixLive.setVisibility(GONE);
        if (ivLiveStream != null) ivLiveStream.setVisibility(isLandscape ? GONE : VISIBLE);
    }

    /**
     * ListenerV2 to be notified about changes of the visibility of the UI control.
     */
    public interface CallBackListener {
        void onPlayerStateChanged(int stage);

        void onPlayerError(String error);

        void onVisibilityChange(int visibility);

        void onFullScreen();

        void onMute(boolean flag);

        void onTimeChange(long time);

        void onHaveSeek(boolean flag);

        void onPlayNextVideo();

        void onPlayPreviousVideo();

        void onPlayPause(boolean state);

        void onSmallScreen();

        void onMoreClick();

        void onReplay();

        void onQuality();

        void onRequestAds(String position);

        void onShowAd();

        void onHideAd();

        void onHideController();

        void onShowController();

        void autoSwitchPlayer();

        void onClickViewFrame();
    }

    public abstract static class DefaultCallbackListener implements CallBackListener {

        @Override
        public void onPlayerStateChanged(int stage) {

        }

        @Override
        public void onPlayerError(String error) {

        }

        @Override
        public void onVisibilityChange(int visibility) {

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
        public void onPlayPreviousVideo() {

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

        }

        @Override
        public void onClickViewFrame() {

        }
    }

    private final class ComponentListener
            implements TimeBar.OnScrubListener, OnClickListener, Player.EventListener {

        @Override
        public void onScrubStart(TimeBar timeBar, long position) {
            removeCallbacks(hideAction);
            scrubbing = true;
        }

        @Override
        public void onScrubMove(TimeBar timeBar, long position) {
            if (positionView != null) {
                if (isLive()) {
                    positionView.setVisibility(GONE);
                } else {
                    positionView.setVisibility(VISIBLE);
                    positionView.setText(Util.getStringForTime(formatBuilder, formatter, position));
                }
            }
        }

        @Override
        public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
            scrubbing = false;
            if (!canceled && player != null) {
                seekToTimeBarPosition(position);
            }
            hideAfterTimeout();

            if (callBackListener != null)
                callBackListener.onHaveSeek(true);
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            Log.i(TAG, "onPlayerStateChanged playWhenReady: " + playWhenReady + ", playbackState: " + playbackState);
            processState(playbackState);
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
//            super.onPlayerError(error);
            if (callBackListener != null) {
                try {
                    String msg = error.getCause().toString();
                    if (error.getCause().getStackTrace() != null && error.getCause().getStackTrace().length > 0) {
                        String msgDetail = error.getCause().getStackTrace()[0].toString();
                        msg += " , " + msgDetail;
                    }
                    callBackListener.onPlayerError(msg);
//                    //Show retry khi play loi
//                    layoutVideo.setVisibility(VISIBLE);
//                    showReplay();
                } catch (Exception ex) {
                    callBackListener.onPlayerError("Error media");
                }
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            updateNavigation();
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            updateNavigation();
        }

        @Override
        public void onPositionDiscontinuity(@Player.DiscontinuityReason int reason) {
            updateNavigation();
            updateProgress();
        }

        @Override
        public void onTimelineChanged(
                Timeline timeline, Object manifest, @Player.TimelineChangeReason int reason) {
            updateNavigation();
            updateTimeBarMode();
            updateProgress();
        }

        @Override
        public void onClick(View view) {
            if (player == null) return;
            boolean isPlaying = !player.getPlayWhenReady();
            if (playButton == view) {
                controlDispatcher.dispatchSetPlayWhenReady(player, isPlaying);
                if (callBackListener != null)
                    callBackListener.onPlayPause(isPlaying);
                if (isPlaying) setShowTimeoutMs(DEFAULT_SHOW_TIMEOUT_MS);
            } else if (btnReplay == view) {
                player.seekTo(0);
                player.setPlayWhenReady(true);
                btnReplay.setVisibility(GONE);
                if (callBackListener != null) {
                    callBackListener.onPlayPause(player.getPlayWhenReady());
                    callBackListener.onReplay();
                }
                setShowTimeoutMs(DEFAULT_SHOW_TIMEOUT_MS);
            } else if (btnCancel == view) {
                isCancel = true;
                stopCountDown();
                layoutVideo.setVisibility(VISIBLE);
                showReplay();
            } else if (ivNext == view) {
                if (callBackListener != null && !isCancel) {
                    callBackListener.onPlayNextVideo();
                    stopCountDown();
                }
            } else if (btnFullScreen == view) {
                if (callBackListener != null)
                    callBackListener.onFullScreen();
            } else if (smallScreenView == view) {
                if (callBackListener != null)
                    callBackListener.onSmallScreen();
            } else if (moreView == view) {
                if (callBackListener != null)
                    callBackListener.onMoreClick();
            } else if (qualityView == view) {
                if (callBackListener != null)
                    callBackListener.onQuality();
            } else if (btnPrevious == view) {
                if (callBackListener != null) {
                    callBackListener.onPlayPreviousVideo();
                    stopCountDown();
                }
            } else if (btnNext == view) {
                if (callBackListener != null) {
                    callBackListener.onPlayNextVideo();
                    stopCountDown();
                }
            }
        }
    }
}