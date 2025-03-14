package com.example.livestream_update.Ringme.Customview;


import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ControlDispatcher;
import com.google.android.exoplayer2.DefaultControlDispatcher;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Player.DiscontinuityReason;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.ApicFrame;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.CaptionStyleCompat;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.ResizeMode;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SubtitleView;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.RepeatModeUtil;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import com.vtm.R;
import com.vtm.ringme.base.utils.ImageBusiness;
import com.vtm.ringme.common.utils.image.ImageManager;
import com.vtm.ringme.common.utils.player.RingMePlayer;
import com.vtm.ringme.model.tab_video.Video;
import com.vtm.ringme.ripple.RippleLayout;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.utils.Utilities;

import java.util.List;

/**
 * Created by HaiKE on 3/22/18.
 */

public class VideoPlayerView extends FrameLayout {

    private static final int SURFACE_TYPE_NONE = 0;
    private static final int SURFACE_TYPE_SURFACE_VIEW = 1;
    private static final int SURFACE_TYPE_TEXTURE_VIEW = 2;

    public static boolean useTextureView = false;

    private final AspectRatioFrameLayout contentFrame;
    private final View shutterView;
    private final View surfaceView;
    private final ImageView imvCover;
    private final ImageView artworkView;
    private final SubtitleView subtitleView;
    private final VideoPlaybackControlView controller;
    private final ComponentListener componentListener;
    private final FrameLayout overlayFrameLayout;
    private final View rootVolume;
    private final ProgressTimeBar progressVolume;
    private TextView btnCloseAds;
    private FrameLayout containerAds;
    private View viewControlAds;
    private ProgressBar progressShowAds;
    private TextView tvShowAds;
    private ImageView ivLogoTopLeft, ivLogoTopRight, ivLogoBottomLeft, ivLogoBottomRight;

    private ExoPlayer player;
    private RingMePlayer ringMePlayer;
    private boolean useController;
    private boolean useArtwork;
    private Bitmap defaultArtwork;
    private int controllerShowTimeoutMs;
    private boolean controllerAutoShow;
    private boolean controllerHideDuringAds;
    private boolean controllerHideOnTouch;
    private int textureViewRotation;
    private boolean isLive = false;
    private boolean isShowLogo = false;
    private int logoPos = 0;
    private GestureDetector gestureDetector;

    public void setLive(boolean live) {
        isLive = live;
        enableFast(!isEnabled());
    }

    public VideoPlayerView(Context context) {
        this(context, null);
        setBackgroundColor(Color.parseColor("#000000"));
    }

    public VideoPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        setBackgroundColor(Color.parseColor("#000000"));
    }

    public VideoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) {
            contentFrame = null;
            shutterView = null;
            imvCover = null;
            surfaceView = null;
            artworkView = null;
            subtitleView = null;
            controller = null;
            componentListener = null;
            overlayFrameLayout = null;
            rootVolume = null;
            progressVolume = null;
            btnCloseAds = null;
            containerAds = null;
            viewControlAds = null;
            progressShowAds = null;
            tvShowAds = null;
            ivLogoTopLeft = null;
            ivLogoTopRight = null;
            ivLogoBottomLeft = null;
            ivLogoBottomRight = null;

            ImageView logo = new ImageView(context);
            if (Util.SDK_INT >= 23) {
                configureEditModeLogoV23(getResources(), logo);
            } else {
                configureEditModeLogo(getResources(), logo);
            }
            addView(logo);
            return;
        }

        boolean shutterColorSet = false;
        int shutterColor = 0;
        int playerLayoutId = R.layout.rm_exo_simple_player_view;
        boolean useArtwork = true;
        int defaultArtworkId = 0;
        boolean useController = true;
        int surfaceType = SURFACE_TYPE_SURFACE_VIEW;
        int resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT;
        int controllerShowTimeoutMs = VideoPlaybackControlView.DEFAULT_SHOW_TIMEOUT_MS;
        boolean controllerHideOnTouch = true;
        boolean controllerAutoShow = true;
        boolean controllerHideDuringAds = true;
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PlayerView, 0, 0);
            try {
                shutterColorSet = a.hasValue(R.styleable.PlayerView_shutter_background_color);
                shutterColor = a.getColor(R.styleable.PlayerView_shutter_background_color, shutterColor);
                playerLayoutId = a.getResourceId(R.styleable.PlayerView_player_layout_id, playerLayoutId);
                useArtwork = a.getBoolean(R.styleable.PlayerView_use_artwork, useArtwork);
                defaultArtworkId =
                        a.getResourceId(R.styleable.PlayerView_default_artwork, defaultArtworkId);
                useController = a.getBoolean(R.styleable.PlayerView_use_controller, useController);
                surfaceType = a.getInt(R.styleable.PlayerView_surface_type, surfaceType);
                resizeMode = a.getInt(R.styleable.PlayerView_resize_mode, resizeMode);
                controllerShowTimeoutMs =
                        a.getInt(R.styleable.PlayerView_show_timeout, controllerShowTimeoutMs);
                controllerHideOnTouch =
                        a.getBoolean(R.styleable.PlayerView_hide_on_touch, controllerHideOnTouch);
                controllerAutoShow = a.getBoolean(R.styleable.PlayerView_auto_show, controllerAutoShow);
                controllerHideDuringAds =
                        a.getBoolean(R.styleable.PlayerView_hide_during_ads, controllerHideDuringAds);
            } finally {
                a.recycle();
            }
        }

        LayoutInflater.from(context).inflate(playerLayoutId, this);
        componentListener = new ComponentListener();
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);

        // Content frame.
        contentFrame = findViewById(R.id.exo_content_frame);
        if (contentFrame != null) {
            setResizeModeRaw(contentFrame, resizeMode);
        }

        imvCover = findViewById(R.id.imvCover);

        // Shutter view.
        shutterView = findViewById(R.id.exo_shutter);
        if (shutterView != null && shutterColorSet) {
            shutterView.setBackgroundColor(shutterColor);
        }

        // Create a surface view and insert it into the content frame, if there is one.
        if (contentFrame != null && surfaceType != SURFACE_TYPE_NONE) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            surfaceView = surfaceType == SURFACE_TYPE_TEXTURE_VIEW || useTextureView ? new TextureView(context) : new SurfaceView(context);
            surfaceView.setLayoutParams(params);

            contentFrame.addView(surfaceView, 0);
        } else {
            surfaceView = null;
        }

        if (surfaceView instanceof TextureView) {
            Log.i(TAG, "surfaceView is TextureView");
        }

        // Overlay frame layout.
        overlayFrameLayout = findViewById(R.id.exo_overlay);

        // Artwork view.
        artworkView = findViewById(R.id.exo_artwork);
        this.useArtwork = useArtwork && artworkView != null;
        if (defaultArtworkId != 0) {
            defaultArtwork = BitmapFactory.decodeResource(context.getResources(), defaultArtworkId);
        }

        // Subtitle view.
        subtitleView = findViewById(R.id.exo_subtitles);
        if (subtitleView != null) {
//            subtitleView.setUserDefaultStyle();
//            subtitleView.setUserDefaultTextSize();
            int defaultSubtitleColor = Color.argb(255, 218, 218, 218);
            int bgColor = Color.argb(33, 0, 0, 0);
            int outlineColor = Color.argb(255, 43, 43, 43);
            Typeface subtitleTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_medium.ttf");
            CaptionStyleCompat style = new CaptionStyleCompat(defaultSubtitleColor,
                    bgColor, Color.TRANSPARENT,
                    CaptionStyleCompat.EDGE_TYPE_OUTLINE,
                    outlineColor, subtitleTypeface);
            subtitleView.setStyle(style);
            subtitleView.setFractionalTextSize(SubtitleView.DEFAULT_TEXT_SIZE_FRACTION * 1.0f);
        }

        /*
         * khởi tạo fastForwardView;
         */
        initFastView();

        // Playback control view.
        VideoPlaybackControlView customController = findViewById(R.id.exo_controller1);
        View controllerPlaceholder = findViewById(R.id.exo_controller_placeholder);
        if (customController != null) {
            this.controller = customController;
        } else if (controllerPlaceholder != null) {
            // Propagate attrs as playbackAttrs so that VideoPlaybackControlView's custom attributes are
            // transferred, but standard FrameLayout attributes (e.g. background) are not.
            this.controller = new VideoPlaybackControlView(context, null, 0, attrs);
            controller.setLayoutParams(controllerPlaceholder.getLayoutParams());
            ViewGroup parent = ((ViewGroup) controllerPlaceholder.getParent());
            int controllerIndex = parent.indexOfChild(controllerPlaceholder);
            parent.removeView(controllerPlaceholder);
            parent.addView(controller, controllerIndex);
        } else {
            this.controller = null;
        }
        this.controllerShowTimeoutMs = controller != null ? controllerShowTimeoutMs : 0;
        this.controllerHideOnTouch = controllerHideOnTouch;
        this.controllerAutoShow = controllerAutoShow;
        this.controllerHideDuringAds = controllerHideDuringAds;
        this.useController = useController && controller != null;
        hideController();
        setBackgroundColor(Color.parseColor("#000000"));

        rootVolume = findViewById(R.id.root_volume);
        progressVolume = findViewById(R.id.progress_volume);
        containerAds = findViewById(R.id.container_ads);
        btnCloseAds = findViewById(R.id.button_close_ads);
        viewControlAds = findViewById(R.id.layout_control_ads);
        progressShowAds = findViewById(R.id.progress_show_ads);
        tvShowAds = findViewById(R.id.tv_show_ads);
        ivLogoTopLeft = findViewById(R.id.iv_logo_top_left);
        ivLogoTopRight = findViewById(R.id.iv_logo_top_right);
        ivLogoBottomLeft = findViewById(R.id.iv_logo_bottom_left);
        ivLogoBottomRight = findViewById(R.id.iv_logo_bottom_right);

        if (btnCloseAds != null) {
            btnCloseAds.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "Ads click skip manual ...");
                    showButtonCloseAds(false, 0);
                    setPlayAds(false);
                    if (ringMePlayer != null) {
                        ringMePlayer.setSkipCode(2);
                        ringMePlayer.callLogAds(200);

                        ringMePlayer.releaseAds();
                        ringMePlayer.setPlayWhenReady(true);
                    }
                }
            });
        }
        if (viewControlAds != null) viewControlAds.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        initUiError();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (controller != null) {
            controller.setEnabled(enabled);
        }
        enableFast(!enabled);
    }

    private Runnable runnableShowButtonClose = new Runnable() {
        @Override
        public void run() {
            if (ringMePlayer != null) {
                ringMePlayer.setErrorAds(200);
                if (btnCloseAds != null && ringMePlayer.isAdDisplayed()) {
                    btnCloseAds.setVisibility(VISIBLE);
                }
            }
        }
    };

    public void showButtonCloseAds(boolean flag, int timeSkipAd) {
        if (btnCloseAds != null) {
            btnCloseAds.setVisibility(GONE);
            btnCloseAds.removeCallbacks(runnableShowButtonClose);
            if (flag && timeSkipAd > 0) {
                btnCloseAds.postDelayed(runnableShowButtonClose, timeSkipAd * 1000);
            }
        }
    }

    public void showProgressLoadingAds(boolean show) {
        Log.i(TAG, "showProgressLoadingAds: " + show);
        if (viewControlAds != null) viewControlAds.setVisibility(show ? VISIBLE : GONE);
        if (progressShowAds != null) progressShowAds.setVisibility(show ? VISIBLE : GONE);
        if (tvShowAds != null) tvShowAds.setVisibility(show ? VISIBLE : GONE);
        if (show) {
            hideController();
        }
    }

    /**
     * Switches the view targeted by a given {@link Player}.
     *
     * @param player        The player whose target view is being switched.
     * @param oldPlayerView The old view to detach from the player.
     * @param newPlayerView The new view to attach to the player.
     */
    public static void switchTargetView(
            @NonNull Player player,
            @Nullable PlayerView oldPlayerView,
            @Nullable PlayerView newPlayerView) {
        if (oldPlayerView == newPlayerView) {
            return;
        }
        // We attach the new view before detaching the old one because this ordering allows the player
        // to swap directly from one surface to another, without transitioning through a state where no
        // surface is attached. This is significantly more efficient and achieves a more seamless
        // transition when using platform provided video decoders.
        if (newPlayerView != null) {
            newPlayerView.setPlayer(player);
        }
        if (oldPlayerView != null) {
            oldPlayerView.setPlayer(null);
        }
    }

    /**
     * Returns the player currently set on this view, or null if no player is set.
     */
    public Player getPlayer() {
        return player;
    }

    public void removerPlayer() {
        if (this.player != null) {
            this.player.removeListener(componentListener);
            ExoPlayer.VideoComponent oldVideoComponent = this.player.getVideoComponent();
            if (oldVideoComponent != null) {
                oldVideoComponent.removeVideoListener(componentListener);
                if (surfaceView instanceof TextureView) {
                    oldVideoComponent.clearVideoTextureView((TextureView) surfaceView);
                } else if (surfaceView instanceof SurfaceView) {
                    oldVideoComponent.clearVideoSurfaceView((SurfaceView) surfaceView);
                }
            }
            ExoPlayer.TextComponent oldTextComponent = this.player.getTextComponent();
            if (oldTextComponent != null) {
                oldTextComponent.removeTextOutput(componentListener);
            }
            this.player = null;
            this.ringMePlayer = null;
        }
    }

    public void setRingMePlayer(RingMePlayer ringMePlayer) {

        this.ringMePlayer = ringMePlayer;
    }

    public RingMePlayer getRingMePlayer() {
        return ringMePlayer;
    }

    /**
     * Set the {@link Player} to use.
     * <p>
     * <p>To transition a {@link Player} from targeting one view to another, it's recommended to use
     * {@link #switchTargetView(Player, PlayerView, PlayerView)} rather than this method. If you do
     * wish to use this method directly, be sure to attach the player to the new view <em>before</em>
     * calling {@code setPlayer(null)} to detach it from the old one. This ordering is significantly
     * more efficient and may allow for more seamless transitions.
     *
     * @param player The {@link Player} to use.
     */
    public void setPlayer(ExoPlayer player) {
        if (this.player == player) {
            return;
        }
        if (this.player != null) {
            this.player.removeListener(componentListener);
            ExoPlayer.VideoComponent oldVideoComponent = this.player.getVideoComponent();
            if (oldVideoComponent != null) {
                oldVideoComponent.removeVideoListener(componentListener);
                if (surfaceView instanceof TextureView) {
                    oldVideoComponent.clearVideoTextureView((TextureView) surfaceView);
                } else if (surfaceView instanceof SurfaceView) {
                    oldVideoComponent.clearVideoSurfaceView((SurfaceView) surfaceView);
                }
            }
            ExoPlayer.TextComponent oldTextComponent = this.player.getTextComponent();
            if (oldTextComponent != null) {
                oldTextComponent.removeTextOutput(componentListener);
            }
        }
        this.player = player;
        if (useController) {
            controller.setPlayer(player);
        }

        if (shutterView != null) {
            shutterView.setVisibility(VISIBLE);
        }

        if (subtitleView != null) {
            subtitleView.setCues(null);
        }
        if (player != null) {
            ExoPlayer.VideoComponent newVideoComponent = player.getVideoComponent();
            if (newVideoComponent != null) {
                if (surfaceView instanceof TextureView) {
                    newVideoComponent.setVideoTextureView((TextureView) surfaceView);
                } else if (surfaceView instanceof SurfaceView) {
                    newVideoComponent.setVideoSurfaceView((SurfaceView) surfaceView);
                }
                newVideoComponent.addVideoListener(componentListener);
            }
            ExoPlayer.TextComponent newTextComponent = player.getTextComponent();
            if (newTextComponent != null) {
                newTextComponent.addTextOutput(componentListener);
            }
            player.addListener(componentListener);
//            maybeShowController(false);
            updateForCurrentTrackSelections();
        } else {
            hideController();
            hideArtwork();
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (surfaceView instanceof SurfaceView) {
            // Work around https://github.com/google/ExoPlayer/issues/3160.
            surfaceView.setVisibility(visibility);
        }
    }

    /**
     * Sets the resize mode.
     *
     * @param resizeMode The resize mode.
     */
    public void setResizeMode(@ResizeMode int resizeMode) {
        Assertions.checkState(contentFrame != null);
        contentFrame.setResizeMode(resizeMode);
    }

    public void setRefreshMode(float aspectRatio) {
        if (contentFrame != null)
            contentFrame.setAspectRatio(aspectRatio);
    }

    /**
     * Returns whether artwork is displayed if present in the media.
     */
    public boolean getUseArtwork() {
        return useArtwork;
    }

    /**
     * Sets whether artwork is displayed if present in the media.
     *
     * @param useArtwork Whether artwork is displayed.
     */
    public void setUseArtwork(boolean useArtwork) {
        Assertions.checkState(!useArtwork || artworkView != null);
        if (this.useArtwork != useArtwork) {
            this.useArtwork = useArtwork;
            updateForCurrentTrackSelections();
        }
    }

    /**
     * Returns the default artwork to display.
     */
    public Bitmap getDefaultArtwork() {
        return defaultArtwork;
    }

    /**
     * Sets the default artwork to display if {@code useArtwork} is {@code true} and no artwork is
     * present in the media.
     *
     * @param defaultArtwork the default artwork to display.
     */
    public void setDefaultArtwork(Bitmap defaultArtwork) {
        if (this.defaultArtwork != defaultArtwork) {
            this.defaultArtwork = defaultArtwork;
            updateForCurrentTrackSelections();
        }
    }

    /**
     * Returns whether the playback controls can be shown.
     */
    public boolean getUseController() {
        return useController;
    }

    /**
     * Sets whether the playback controls can be shown. If set to {@code false} the playback controls
     * are never visible and are disconnected from the player.
     *
     * @param useController Whether the playback controls can be shown.
     */
    public void setUseController(boolean useController) {
        Assertions.checkState(!useController || controller != null);
        if (this.useController == useController) {
            return;
        }
        this.useController = useController;
        if (useController) {
            controller.setPlayer(player);
        } else if (controller != null) {
            controller.hideAll();
            controller.setPlayer(null);
        }
    }

    /**
     * Sets the background color of the {@code exo_shutter} view.
     *
     * @param color The background color.
     */
    public void setShutterBackgroundColor(int color) {
        if (shutterView != null) {
            shutterView.setBackgroundColor(color);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (player != null && player.isPlayingAd()) {
            // Focus any overlay UI now, in case it's provided by a WebView whose contents may update
            // dynamically. This is needed to make the "Skip ad" button focused on Android TV when using
            // IMA [Internal: b/62371030].
            overlayFrameLayout.requestFocus();
            return super.dispatchKeyEvent(event);
        }
        boolean isDpadWhenControlHidden =
                isDpadKey(event.getKeyCode()) && useController && !controller.isVisible();
//        maybeShowController(true);
        return isDpadWhenControlHidden || dispatchMediaKeyEvent(event) || super.dispatchKeyEvent(event);
    }

    /**
     * Called to process media key events. Any {@link KeyEvent} can be passed but only media key
     * events will be handled. Does nothing if playback controls are disabled.
     *
     * @param event A key event.
     * @return Whether the key event was handled.
     */
    public boolean dispatchMediaKeyEvent(KeyEvent event) {
        return useController && controller.dispatchMediaKeyEvent(event);
    }

    /**
     * Shows the playback controls. Does nothing if playback controls are disabled.
     * <p>
     * <p>The playback controls are automatically hidden during playback after {{@link
     * #getControllerShowTimeoutMs()}}. They are shown indefinitely when playback has not started yet,
     * is paused, has ended or failed.
     */
    public void showController() {
        showController(shouldShowControllerIndefinitely());
    }

    /**
     * Hides the playback controls. Does nothing if playback controls are disabled.
     */
    public void hideController() {
        if (controller != null) {
            controller.hide();
        }
    }

    /**
     * Returns the playback controls timeout. The playback controls are automatically hidden after
     * this duration of time has elapsed without user input and with playback or buffering in
     * progress.
     *
     * @return The timeout in milliseconds. A non-positive value will cause the controller to remain
     * visible indefinitely.
     */
    public int getControllerShowTimeoutMs() {
        return controllerShowTimeoutMs;
    }

    /**
     * Sets the playback controls timeout. The playback controls are automaticaFlly hidden after this
     * duration of time has elapsed without user input and with playback or buffering in progress.
     *
     * @param controllerShowTimeoutMs The timeout in milliseconds. A non-positive value will cause the
     *                                controller to remain visible indefinitely.
     */
    public void setControllerShowTimeoutMs(int controllerShowTimeoutMs) {
        Assertions.checkState(controller != null);
        this.controllerShowTimeoutMs = controllerShowTimeoutMs;
        if (controller.isVisible()) {
            // Update the controller's timeout if necessary.
            showController();
        }
    }

    /**
     * Returns whether the playback controls are hidden by touch events.
     */
    public boolean getControllerHideOnTouch() {
        return controllerHideOnTouch;
    }

    /**
     * Sets whether the playback controls are hidden by touch events.
     *
     * @param controllerHideOnTouch Whether the playback controls are hidden by touch events.
     */
    public void setControllerHideOnTouch(boolean controllerHideOnTouch) {
        Assertions.checkState(controller != null);
        this.controllerHideOnTouch = controllerHideOnTouch;
    }

    /**
     * Returns whether the playback controls are automatically shown when playback starts, pauses,
     * ends, or fails. If set to false, the playback controls can be manually operated with {@link
     * #showController()} and {@link #hideController()}.
     */
    public boolean getControllerAutoShow() {
        return controllerAutoShow;
    }

    /**
     * Sets whether the playback controls are automatically shown when playback starts, pauses, ends,
     * or fails. If set to false, the playback controls can be manually operated with {@link
     * #showController()} and {@link #hideController()}.
     *
     * @param controllerAutoShow Whether the playback controls are allowed to show automatically.
     */
    public void setControllerAutoShow(boolean controllerAutoShow) {
        this.controllerAutoShow = controllerAutoShow;
    }

    /**
     * Sets whether the playback controls are hidden when ads are playing. Controls are always shown
     * during ads if they are enabled and the player is paused.
     *
     * @param controllerHideDuringAds Whether the playback controls are hidden when ads are playing.
     */
    public void setControllerHideDuringAds(boolean controllerHideDuringAds) {
        this.controllerHideDuringAds = controllerHideDuringAds;
    }

    /**
     * Set the {@link VideoPlaybackControlView.CallBackListener}.
     *
     * @param listener The listener to be notified about visibility changes.
     */
    public void setControllerVisibilityListener(VideoPlaybackControlView.CallBackListener listener) {
        Assertions.checkState(controller != null);
        controller.setVisibilityListener(listener);
    }

    /**
     * Sets the {@link PlaybackPreparer}.
     *
     * @param playbackPreparer The {@link PlaybackPreparer}.
     */
    public void setPlaybackPreparer(@Nullable PlaybackPreparer playbackPreparer) {
        Assertions.checkState(controller != null);
        controller.setPlaybackPreparer(playbackPreparer);
    }

    /**
     * Sets the {@link ControlDispatcher}.
     *
     * @param controlDispatcher The {@link ControlDispatcher}, or null to use {@link
     *                          DefaultControlDispatcher}.
     */
    public void setControlDispatcher(@Nullable ControlDispatcher controlDispatcher) {
        Assertions.checkState(controller != null);
        controller.setControlDispatcher(controlDispatcher);
    }

    /**
     * Sets the rewind increment in milliseconds.
     *
     * @param rewindMs The rewind increment in milliseconds. A non-positive value will cause the
     *                 rewind button to be disabled.
     */
    public void setRewindIncrementMs(int rewindMs) {
        Assertions.checkState(controller != null);
        controller.setRewindIncrementMs(rewindMs);
    }

    /**
     * Sets the fast forward increment in milliseconds.
     *
     * @param fastForwardMs The fast forward increment in milliseconds. A non-positive value will
     *                      cause the fast forward button to be disabled.
     */
    public void setFastForwardIncrementMs(int fastForwardMs) {
        Assertions.checkState(controller != null);
        controller.setFastForwardIncrementMs(fastForwardMs);
    }

    /**
     * Sets which repeat toggle modes are enabled.
     *
     * @param repeatToggleModes A set of {@link RepeatModeUtil.RepeatToggleModes}.
     */
    public void setRepeatToggleModes(@RepeatModeUtil.RepeatToggleModes int repeatToggleModes) {
        Assertions.checkState(controller != null);
        controller.setRepeatToggleModes(repeatToggleModes);
    }

    /**
     * Sets whether the shuffle button is shown.
     *
     * @param showShuffleButton Whether the shuffle button is shown.
     */
    public void setShowShuffleButton(boolean showShuffleButton) {
        Assertions.checkState(controller != null);
        controller.setShowShuffleButton(showShuffleButton);
    }

    /**
     * Sets whether the time bar should show all windows, as opposed to just the current one.
     *
     * @param showMultiWindowTimeBar Whether to show all windows.
     */
    public void setShowMultiWindowTimeBar(boolean showMultiWindowTimeBar) {
        Assertions.checkState(controller != null);
        controller.setShowMultiWindowTimeBar(showMultiWindowTimeBar);
    }

    /**
     * Gets the view onto which video is rendered. This is a:
     * <p>
     * <ul>
     * <li>{@link SurfaceView} by default, or if the {@code surface_type} attribute is set to {@code
     * surface_view}.
     * <li>{@link TextureView} if {@code surface_type} is {@code texture_view}.
     * <li>{@code null} if {@code surface_type} is {@code none}.
     * </ul>
     *
     * @return The {@link SurfaceView}, {@link TextureView} or {@code null}.
     */
    public View getVideoSurfaceView() {
        return surfaceView;
    }

    /**
     * Gets the overlay {@link FrameLayout}, which can be populated with UI elements to show on top of
     * the player.
     *
     * @return The overlay {@link FrameLayout}, or {@code null} if the layout has been customized and
     * the overlay is not present.
     */
    public FrameLayout getOverlayFrameLayout() {
        return overlayFrameLayout;
    }

    /**
     * Gets the {@link SubtitleView}.
     *
     * @return The {@link SubtitleView}, or {@code null} if the layout has been customized and the
     * subtitle view is not present.
     */
    public SubtitleView getSubtitleView() {
        return subtitleView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (player != null && gestureDetector != null) {
            gestureDetector.onTouchEvent(ev);
        }
        if (!useController || player == null || ev.getActionMasked() != MotionEvent.ACTION_DOWN) {
            return false;
        }
        if (!controller.isVisible()) {
            maybeShowController(true);
        } else if (controllerHideOnTouch) {
            controller.hide();
        }
        return false;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        if (!useController || player == null) {
            return false;
        }
        maybeShowController(true);
        return true;
    }

    /**
     * Shows the playback controls, but only if forced or shown indefinitely.
     */
    private void maybeShowController(boolean isForced) {
        if (isPlayingAd() && controllerHideDuringAds) {
            return;
        }
        if (useController) {
            boolean wasShowingIndefinitely = controller.isVisible() && controller.getShowTimeoutMs() <= 0;
            boolean shouldShowIndefinitely = shouldShowControllerIndefinitely();
            if (isForced || wasShowingIndefinitely || shouldShowIndefinitely) {
                showController(shouldShowIndefinitely);
            }
        }
    }

    private boolean shouldShowControllerIndefinitely() {
        if (player == null) {
            return true;
        }
        int playbackState = player.getPlaybackState();
        return controllerAutoShow
                && (playbackState == Player.STATE_IDLE
                || playbackState == Player.STATE_ENDED
                || !player.getPlayWhenReady());
    }

    public void showController(boolean showIndefinitely) {
        if (!useController || controller == null) {
            return;
        }
        controller.setShowTimeoutMs(showIndefinitely ? 0 : controllerShowTimeoutMs);
        controller.show();
    }

    private boolean isPlayingAd() {
        return player != null && player.isPlayingAd() && player.getPlayWhenReady();
    }

    private void updateForCurrentTrackSelections() {
        if (player == null) {
            return;
        }
        TrackSelectionArray selections = player.getCurrentTrackSelections();
        for (int i = 0; i < selections.length; i++) {
            if (player.getRendererType(i) == C.TRACK_TYPE_VIDEO && selections.get(i) != null) {
                // Video enabled so artwork must be hidden. If the shutter is closed, it will be opened in
                // onRenderedFirstFrame().
                hideArtwork();
                return;
            }
        }
        // Video disabled so the shutter must be closed.
        if (shutterView != null) {
            shutterView.setVisibility(VISIBLE);
        }

//        if ((player.getPlaybackState() == Player.STATE_READY || player.getPlaybackState() == SimplePlayer.STATE_IDLE) && shutterView != null)
//            shutterView.setVisibility(INVISIBLE);

        // Display artwork if enabled and available, else hide it.
        if (useArtwork) {
            for (int i = 0; i < selections.length; i++) {
                TrackSelection selection = selections.get(i);
                if (selection != null) {
                    for (int j = 0; j < selection.length(); j++) {
                        Metadata metadata = selection.getFormat(j).metadata;
                        if (metadata != null && setArtworkFromMetadata(metadata)) {
                            return;
                        }
                    }
                }
            }
            if (setArtworkFromBitmap(defaultArtwork)) {
                return;
            }
        }
        // Artwork disabled or unavailable.
        hideArtwork();
    }

    private boolean setArtworkFromMetadata(Metadata metadata) {
        for (int i = 0; i < metadata.length(); i++) {
            Metadata.Entry metadataEntry = metadata.get(i);
            if (metadataEntry instanceof ApicFrame) {
                byte[] bitmapData = ((ApicFrame) metadataEntry).pictureData;
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
                return setArtworkFromBitmap(bitmap);
            }
        }
        return false;
    }

    private boolean setArtworkFromBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            if (bitmapWidth > 0 && bitmapHeight > 0) {
                if (contentFrame != null) {
                    contentFrame.setAspectRatio((float) bitmapWidth / bitmapHeight);
                }
                artworkView.setImageBitmap(bitmap);
                artworkView.setVisibility(VISIBLE);
                return true;
            }
        }
        return false;
    }

    private void hideArtwork() {
        if (artworkView != null) {
            artworkView.setImageResource(android.R.color.transparent); // Clears any bitmap reference.
            artworkView.setVisibility(INVISIBLE);
        }
    }

    @TargetApi(23)
    private static void configureEditModeLogoV23(Resources resources, ImageView logo) {
        logo.setImageDrawable(resources.getDrawable(com.google.android.exoplayer2.R.drawable.exo_edit_mode_logo, null));
        logo.setBackgroundColor(resources.getColor(R.color.exo_edit_mode_background_color, null));
    }

    @SuppressWarnings("deprecation")
    private static void configureEditModeLogo(Resources resources, ImageView logo) {
        logo.setImageDrawable(resources.getDrawable(com.google.android.exoplayer2.R.drawable.exo_edit_mode_logo));
        logo.setBackgroundColor(resources.getColor(R.color.exo_edit_mode_background_color));
    }

    @SuppressWarnings("ResourceType")
    private static void setResizeModeRaw(AspectRatioFrameLayout aspectRatioFrame, int resizeMode) {
        aspectRatioFrame.setResizeMode(resizeMode);
    }

    /**
     * Applies a texture rotation to a {@link TextureView}.
     */
    private static void applyTextureViewRotation(TextureView textureView, int textureViewRotation) {
        float textureViewWidth = textureView.getWidth();
        float textureViewHeight = textureView.getHeight();
        if (textureViewWidth == 0 || textureViewHeight == 0 || textureViewRotation == 0) {
            textureView.setTransform(null);
        } else {
            Matrix transformMatrix = new Matrix();
            float pivotX = textureViewWidth / 2;
            float pivotY = textureViewHeight / 2;
            transformMatrix.postRotate(textureViewRotation, pivotX, pivotY);

            // After rotation, scale the rotated texture to fit the TextureView size.
            RectF originalTextureRect = new RectF(0, 0, textureViewWidth, textureViewHeight);
            RectF rotatedTextureRect = new RectF();
            transformMatrix.mapRect(rotatedTextureRect, originalTextureRect);
            transformMatrix.postScale(
                    textureViewWidth / rotatedTextureRect.width(),
                    textureViewHeight / rotatedTextureRect.height(),
                    pivotX,
                    pivotY);
            textureView.setTransform(transformMatrix);
        }
    }

    @SuppressLint("InlinedApi")
    private boolean isDpadKey(int keyCode) {
        return keyCode == KeyEvent.KEYCODE_DPAD_UP
                || keyCode == KeyEvent.KEYCODE_DPAD_UP_RIGHT
                || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
                || keyCode == KeyEvent.KEYCODE_DPAD_DOWN_RIGHT
                || keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                || keyCode == KeyEvent.KEYCODE_DPAD_DOWN_LEFT
                || keyCode == KeyEvent.KEYCODE_DPAD_LEFT
                || keyCode == KeyEvent.KEYCODE_DPAD_UP_LEFT
                || keyCode == KeyEvent.KEYCODE_DPAD_CENTER;
    }

    public void refreshUi() {
        if (contentFrame != null) {
            contentFrame.setAspectRatio(1f);
        }
    }

    public void setCountDown(boolean isCountDown) {
        enableFast(isCountDown);
        if (controller != null)
            controller.setCountDown(isCountDown);
    }

    public void setVideoNext(Video video) {
        if (controller != null)
            controller.setVideoNext(video);
    }

    public void setVisibilityListener(VideoPlaybackControlView.CallBackListener listener) {
        if (controller != null)
            controller.setVisibilityListener(listener);
    }

    /**
     * cập nhật giao diện volume
     *
     * @param currentVolume volume mới
     * @param maxVolume     volume max
     */
    public void updateVolume(int currentVolume, int maxVolume) {
        if (progressVolume != null) {
            if (rootVolume != null && rootVolume.getVisibility() != VISIBLE)
                rootVolume.setVisibility(VISIBLE);
            hideVolumeTimeOut();
            progressVolume.setDuration(maxVolume);
            progressVolume.setPosition(currentVolume);
        }
    }

    /**
     * ẩn volume khi time out
     */
    private void hideVolumeTimeOut() {
        if (hideVolumeRunnable != null && rootVolume != null)
            rootVolume.removeCallbacks(hideVolumeRunnable);
        if (hideVolumeRunnable != null && rootVolume != null)
            rootVolume.postDelayed(hideVolumeRunnable, VideoPlaybackControlView.DEFAULT_SHOW_TIMEOUT_MS);
    }

    /**
     * bộ định thời ẩn volume
     */
    private Runnable hideVolumeRunnable = new Runnable() {
        @Override
        public void run() {
            if (rootVolume != null)
                rootVolume.setVisibility(GONE);
        }
    };

    private final class ComponentListener
            implements TextOutput, VideoListener, OnLayoutChangeListener, Player.EventListener {

        // TextOutput implementation

        @Override
        public void onCues(List<Cue> cues) {
            if (subtitleView != null) {
                subtitleView.onCues(cues);
            }
        }

        // VideoListener implementation

        @Override
        public void onVideoSizeChanged(
                int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            if (contentFrame == null) {
                return;
            }
            float videoAspectRatio =
                    (height == 0 || width == 0) ? 1 : (width * pixelWidthHeightRatio) / height;

            if (surfaceView instanceof TextureView) {
                // Try to apply rotation transformation when our surface is a TextureView.
                if (unappliedRotationDegrees == 90 || unappliedRotationDegrees == 270) {
                    // We will apply a rotation 90/270 degree to the output texture of the TextureView.
                    // In this case, the output video's width and height will be swapped.
                    videoAspectRatio = 1 / videoAspectRatio;
                }
                if (textureViewRotation != 0) {
                    surfaceView.removeOnLayoutChangeListener(this);
                }
                textureViewRotation = unappliedRotationDegrees;
                if (textureViewRotation != 0) {
                    // The texture view's dimensions might be changed after layout step.
                    // So add an OnLayoutChangeListener to apply rotation after layout step.
                    surfaceView.addOnLayoutChangeListener(this);
                }
                applyTextureViewRotation((TextureView) surfaceView, textureViewRotation);
            }

            contentFrame.setAspectRatio(videoAspectRatio);
        }

        @Override
        public void onRenderedFirstFrame() {
            if (shutterView != null) {
                shutterView.setVisibility(INVISIBLE);
            }
        }

        @Override
        public void onTracksChanged(TrackGroupArray tracks, TrackSelectionArray selections) {
            updateForCurrentTrackSelections();
        }

        // Player.EventListener implementation

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            Log.i(TAG, "onPlayerStateChanged playWhenReady: " + playWhenReady + ", playbackState: " + playbackState);
            if (playWhenReady && playbackState == Player.STATE_READY) {
                if (imvCover != null) imvCover.setVisibility(INVISIBLE);
            } else if (playbackState == Player.STATE_ENDED) {
                if (imvCover != null) imvCover.setVisibility(VISIBLE);
            }

            if (isPlayingAd() && controllerHideDuringAds) {
                hideController();
            } else {
//                maybeShowController(false);
            }
        }

        @Override
        public void onPositionDiscontinuity(@DiscontinuityReason int reason) {
            if (isPlayingAd() && controllerHideDuringAds) {
                hideController();
            }
        }

        // OnLayoutChangeListener implementation

        @Override
        public void onLayoutChange(
                View view,
                int left,
                int top,
                int right,
                int bottom,
                int oldLeft,
                int oldTop,
                int oldRight,
                int oldBottom) {
            applyTextureViewRotation((TextureView) view, textureViewRotation);
        }
    }

    private RippleLayout fastPreviousLayout;
    private RippleLayout fastForwardLayout;

    public void enableFast(boolean disable) {
        if (isLive) {
            if (fastPreviousLayout != null)
                fastPreviousLayout.setEnabled(false);
            if (fastForwardLayout != null)
                fastForwardLayout.setEnabled(false);
        } else {
            if (fastPreviousLayout != null)
                fastPreviousLayout.setEnabled(!disable);
            if (fastForwardLayout != null)
                fastForwardLayout.setEnabled(!disable);
        }
    }

    private void initFastView() {
        fastPreviousLayout = findViewById(R.id.fast_previous_layout);
        fastForwardLayout = findViewById(R.id.fast_forward_layout);

        if (fastPreviousLayout != null)
            fastPreviousLayout.setOnRippleListener(mOnRippleListener);
        if (fastForwardLayout != null)
            fastForwardLayout.setOnRippleListener(mOnRippleListener);
    }

    private static final String TAG = "VideoPlayerView";
    private RippleLayout.OnRippleListener mOnRippleListener = new RippleLayout.OnRippleListener() {
        @Override
        public void onFastRipple(long timeFast, RippleLayout rippleLayout) {
            if (rippleLayout != null && controller != null) {
                if (rippleLayout == fastForwardLayout) {
                    /*
                     * tua tiến
                     */
                    controller.fastForward();
                } else {
                    /*
                     * tua lùi về
                     */
                    controller.fastPrevious();
                }
                controller.showFast();
                controller.removeCallbacks(refreshRunnable);
                controller.postDelayed(refreshRunnable, 200L);
            }
        }

        @Override
        public void onEndRipple(RippleLayout rippleLayout) {

        }
    };

    private Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            if (controller != null)
                controller.clearFast();
        }
    };

    public VideoPlaybackControlView getController() {
        return controller;
    }

    public void showCover(String url) {
        if (imvCover != null) {
            imvCover.setVisibility(VISIBLE);
            ImageBusiness.setVideoPlayer(imvCover, url);
        }
        hideUiError();
    }

    public void setCover(String url) {
        if (imvCover != null) {
            ImageBusiness.setVideoPlayer(imvCover, url);
        }
    }

    public View getQualityView() {
        if (controller != null)
            return controller.getQualityView();
        return null;
    }

    public void setPlayAds(boolean playAds) {
        enableFast(playAds);
    }

    //Xu ly quang cao

    public FrameLayout getContainerAds() {
        return containerAds;
    }

    public void showAds() {
        if (containerAds != null)
            containerAds.setVisibility(VISIBLE);
//        collapse(containerAds);
    }

    public void hideAds() {
        if (containerAds != null)
            containerAds.setVisibility(GONE);
//        animateAdContainerClose();
    }

    private void animateAdContainerClose() {
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(containerAds.getHeight(), 0);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                containerAds.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                containerAds.requestLayout();
            }
        });
        valueAnimator.start();
    }

    private static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    @Nullable
    private RelativeLayout viewError;
    @Nullable
    private ImageView btnReload;
    @Nullable
    private TextView tvError;
    @Nullable
    private View viewErrorDetail, viewLiveEnd;
    @Nullable
    private View btnRetry;
    @Nullable
    private ImageView ivThumb;

    private boolean showUiError;

    private void initUiError() {
        viewError = findViewById(R.id.layout_error);
        btnReload = findViewById(R.id.iv_reload);
        btnRetry = findViewById(R.id.button_retry);
        tvError = findViewById(R.id.tv_error);
        viewErrorDetail = findViewById(R.id.layout_error_detail);
        viewLiveEnd = findViewById(R.id.layout_live_end);
        ivThumb = findViewById(R.id.iv_thumb);

        if (viewError != null) viewError.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ringMePlayer != null && ringMePlayer.isLive()) {
                    ringMePlayer.onClickViewFrame();
                }
            }
        });
        if (btnReload != null) btnReload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ringMePlayer != null) {
                    ringMePlayer.reload();
                }
            }
        });

        if (btnRetry != null) btnRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ringMePlayer != null) {
                    ringMePlayer.reload();
                }
            }
        });

        hideUiError();
    }

    public void showUiError() {
        if (viewError != null) {
            showUiError = true;
            viewError.setVisibility(View.VISIBLE);
        }
        if (viewErrorDetail != null) viewErrorDetail.setVisibility(VISIBLE);
        if (viewLiveEnd != null) viewLiveEnd.setVisibility(GONE);
        if (isLive) {
            if (tvError != null) tvError.setText(R.string.video_live_error);
            if (btnReload != null) btnReload.setVisibility(GONE);
            if (btnRetry != null) btnRetry.setVisibility(VISIBLE);
        } else {
            if (tvError != null) tvError.setText(R.string.e601_error_but_undefined);
            if (btnReload != null) btnReload.setVisibility(VISIBLE);
            if (btnRetry != null) btnRetry.setVisibility(GONE);
        }
        if (ivThumb != null) ivThumb.setVisibility(GONE);
    }

    public void showUiLiveEnd() {
        if (viewError != null) {
            showUiError = true;
            viewError.setVisibility(View.VISIBLE);
        }
        if (viewErrorDetail != null) viewErrorDetail.setVisibility(GONE);
        if (viewLiveEnd != null) viewLiveEnd.setVisibility(VISIBLE);
    }

    public void hideUiError() {
        if (viewError != null) {
            showUiError = false;
            viewError.setVisibility(View.GONE);
        }
    }

    public void showButtonReload() {
        if (viewError != null) viewError.setClickable(true);
        if (viewErrorDetail != null) viewErrorDetail.setVisibility(VISIBLE);
        if (viewLiveEnd != null) viewLiveEnd.setVisibility(GONE);
        if (isLive) {
            if (tvError != null) tvError.setText(R.string.video_live_error);
            if (btnReload != null) btnReload.setVisibility(GONE);
            if (btnRetry != null) btnRetry.setVisibility(VISIBLE);
        } else {
            if (tvError != null) tvError.setText(R.string.e601_error_but_undefined);
            if (btnReload != null) btnReload.setVisibility(VISIBLE);
            if (btnRetry != null) btnRetry.setVisibility(GONE);
        }
    }

    public void hideButtonReload() {
        if (viewError != null) viewError.setClickable(false);
        if (btnReload != null) btnReload.setVisibility(GONE);
        if (btnRetry != null) btnRetry.setVisibility(GONE);
    }

    public boolean isShowUiError() {
        return showUiError;
    }

    public void setResize219(boolean isResize219) {
        setResizeMode(isResize219 ? AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH : AspectRatioFrameLayout.RESIZE_MODE_FIT);
    }

    public void showThumb(String url) {
        if (ivThumb != null) {
            ivThumb.setVisibility(VISIBLE);
            ImageManager.showImage(url, ivThumb);
        }
    }

    public void setLogo(String logo, int logoPosition) {
        logoPos = logoPosition;
        Log.d(TAG, "setLogo logoPosition: " + logoPosition + ", logo: " + logo);
        isShowLogo = (logoPosition == 1 || logoPosition == 2 || logoPosition == 3 || logoPosition == 4) && Utilities.notEmpty(logo);
        if (ivLogoTopLeft != null) ivLogoTopLeft.setVisibility(GONE);
        if (ivLogoTopRight != null) ivLogoTopRight.setVisibility(GONE);
        if (ivLogoBottomLeft != null) ivLogoBottomLeft.setVisibility(GONE);
        if (ivLogoBottomRight != null) ivLogoBottomRight.setVisibility(GONE);
        switch (logoPosition) {
            case 1:
                ImageBusiness.setLogo(ivLogoTopLeft, logo);
                break;
            case 2:
                ImageBusiness.setLogo(ivLogoTopRight, logo);
                break;
            case 3:
                ImageBusiness.setLogo(ivLogoBottomRight, logo);
                break;
            case 4:
                ImageBusiness.setLogo(ivLogoBottomLeft, logo);
                break;
        }
    }

    public void showLogo(boolean flag) {
        if (flag && isShowLogo) {
            switch (logoPos) {
                case 1:
                    if (ivLogoTopLeft != null) ivLogoTopLeft.setVisibility(VISIBLE);
                    break;
                case 2:
                    if (ivLogoTopRight != null) ivLogoTopRight.setVisibility(VISIBLE);
                    break;
                case 3:
                    if (ivLogoBottomRight != null) ivLogoBottomRight.setVisibility(VISIBLE);
                    break;
                case 4:
                    if (ivLogoBottomLeft != null) ivLogoBottomLeft.setVisibility(VISIBLE);
                    break;
            }
        } else {
            if (ivLogoTopLeft != null) ivLogoTopLeft.setVisibility(GONE);
            if (ivLogoTopRight != null) ivLogoTopRight.setVisibility(GONE);
            if (ivLogoBottomRight != null) ivLogoBottomRight.setVisibility(GONE);
            if (ivLogoBottomLeft != null) ivLogoBottomLeft.setVisibility(GONE);
        }
    }

    public void setGestureDetector(GestureDetector gestureDetector) {
        this.gestureDetector = gestureDetector;
    }

    public void showBottomBar(boolean isShow) {
        if (controller != null) {
            controller.showBottomBar(isShow);
        }
    }

    public void showEpisode(boolean isShow) {
        if (controller != null) {
            controller.showEpisode(isShow);
        }
    }

    public void setEnablePreviousButton(boolean flag) {
        if (controller != null)
            controller.setEnablePreviousButton(flag);
    }

    public void setEnableNextButton(boolean flag) {
        if (controller != null)
            controller.setEnableNextButton(flag);
    }

    public void setVisiblePreviousAndNextButton(boolean flag) {
        if (controller != null)
            controller.setVisiblePreviousAndNextButton(flag);
    }

    public void setExpandedEpisode(boolean isShow) {
        if (controller != null) {
            controller.setExpandedEpisode(isShow);
        }
    }
}