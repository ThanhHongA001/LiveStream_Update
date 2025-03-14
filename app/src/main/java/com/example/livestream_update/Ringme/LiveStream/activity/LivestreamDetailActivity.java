package com.example.livestream_update.Ringme.LiveStream.activity;


import static com.vtm.ringme.values.Constants.MENU.MENU_EXIT;
import static com.vtm.ringme.values.Constants.MENU.MENU_QUALITY;
import static com.vtm.ringme.values.Constants.MENU.MENU_REPORT_VIDEO;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.vtm.ringme.api.ApiCollections;
import com.vtm.ringme.api.ApiSummoner;
import com.vtm.ringme.api.HomeApi;
import com.vtm.ringme.api.response.ListVoteLivestreamResponse;
import com.vtm.ringme.api.response.LivestreamDetailResponse;
import com.vtm.ringme.api.response.ReactionUserResponse;
import com.vtm.ringme.api.response.ReceiveStarResponse;
import com.vtm.ringme.base.BaseDialogFragment;
import com.vtm.ringme.dialog.DialogConfirm;
import com.vtm.ringme.dialog.LivestreamFutureVoteBottomSheetDialog;
import com.vtm.ringme.dialog.SendStarDialog;
import com.vtm.ringme.model.ItemContextMenu;
import com.vtm.ringme.model.ReengAccount;
import com.vtm.ringme.model.livestream.LivestreamModel;
import com.vtm.ringme.tabvideo.LiveStreamIsFollowEvent;
import com.vtm.ringme.utils.InputMethodUtils;
import com.vtm.ringme.utils.ZeroGravityAnimation;
import com.vtm.ringme.values.Constants;
import com.vtm.ringme.values.Direction;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.TimeBar;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.vtm.ringme.ApplicationController;
import com.vtm.R;
import com.vtm.ringme.common.api.http.HttpCallBack;
import com.vtm.ringme.common.api.video.callback.OnChannelInfoCallback;
import com.vtm.ringme.common.api.video.channel.ChannelApi;

import com.vtm.ringme.helper.LogAppHelper;
import com.vtm.ringme.helper.NetworkHelper;
import com.vtm.ringme.helper.UrlConfigHelper;
import com.vtm.ringme.helper.HttpHelper;
import com.vtm.ringme.model.tab_video.Channel;
import com.vtm.ringme.livestream.adapter.LivestreamFunctionAdapter;
import com.vtm.ringme.livestream.adapter.LivestreamPagerAdapter;
import com.vtm.ringme.livestream.apis.response.LiveStreamFollowResponse;
import com.vtm.ringme.livestream.apis.response.LiveStreamGetReportListResponse;
import com.vtm.ringme.livestream.dialog.QualityDialog;
import com.vtm.ringme.livestream.dialog.ReportDialog;
import com.vtm.ringme.livestream.eventbus.EventBusEvents;
import com.vtm.ringme.livestream.eventbus.SubOrUnSubEvent;
import com.vtm.ringme.livestream.fragment.LivestreamChatFragment;
import com.vtm.ringme.livestream.fragment.LivestreamInfoFragment;
import com.vtm.ringme.livestream.model.LiveStreamBlockNotification;
import com.vtm.ringme.livestream.model.LiveStreamLikeNotification;
import com.vtm.ringme.livestream.model.LiveStreamViewNumber;
import com.vtm.ringme.livestream.model.LivestreamDropStarNotification;
import com.vtm.ringme.livestream.model.LivestreamLiveNotification;
import com.vtm.ringme.livestream.model.LivestreamReloadSurveyNotification;
import com.vtm.ringme.livestream.model.LivestreamVoteOptionModel;
import com.vtm.ringme.livestream.model.Report;
import com.vtm.ringme.livestream.socket.ListenerV2;
import com.vtm.ringme.livestream.socket.SocketManagerV2;
import com.vtm.ringme.livestream.socket.stomp.dto.StompHeader;

import com.vtm.ringme.tabvideo.playVideo.dialog.OptionsVideoDialog;
import com.vtm.ringme.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LivestreamDetailActivity extends AppCompatActivity implements TimeBar.OnScrubListener,
        TextWatcher, View.OnClickListener, ListenerV2.ChatFunctionClickListener, OnChannelInfoCallback {
    private static final int MAX_UPDATE_INTERVAL_MS = 1000;
    private static final String AVATAR_FULL = "/api/thumbnail/download-orginal?v=%1$s&ac=%2$s&t=%3$s&u=%4$s";
    LivestreamFunctionAdapter functionAdapter;
    List<Constants.ChatFunction> functions;
    //View
    PlayerView player;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView viewNumber;
    TextView tvPosition, tvPositionVOD, tvDuration;
    AppCompatImageView btnFullscreen, imageUpcomming;
    AppCompatImageView btnBack, btnBackLoading;
    DefaultTimeBar timeBar, timeBarVOD;
    TextView tvLive;
    TextView liveTime;
    //    AppCompatImageView btnRewind, btnForward;
    RelativeLayout layoutPrepare;
    ProgressBar loading;
    LinearLayout retryLayout, layoutViewer;
    TextView retryButton;
    TextView tvEndLive;
    ConstraintLayout layoutPlayer;
    LinearLayout tabController;
    //View chat
    EditText chatMessage;
    ImageButton imgSend;
    RecyclerView rcvFunctions;
    AppCompatImageView btnShowViewPager;
    SendStarDialog sendStarDialog;
    TextView edtPlaceholder;
    LinearLayout layoutTextBox;
    RelativeLayout handler;
    LinearLayout voteBar;
    //adapter
    LivestreamPagerAdapter adapter;
    boolean isHidden = false;
    private LivestreamModel video;
    private SimpleExoPlayer exoPlayer;
    private boolean isFullScreen = false;
    private boolean scrubbing;
    private StringBuilder formatBuilder;
    private Formatter formatter;
    private final AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = this::handleAudioFocusChange;
    private Runnable updateProgressAction;
    private Runnable updateTimeBarLatency;
    private long duration = 0;
    private long currentDuration = 0;
    private ZeroGravityAnimation animation;
    private ViewGroup container;
    private Dialog optionsVideoDialog;
    private Dialog speedVideoDialog;
    private int reloadTime = 0;
    private final Player.EventListener mEventListener = new Player.EventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            Log.d("playState", "onLoading: " + playbackState);
            if (player != null) {
                player.setKeepScreenOn(playWhenReady);
                currentDuration = Objects.requireNonNull(player.getPlayer()).getContentDuration();
            }
            if (playbackState == Player.STATE_READY) {
                reloadTime = 0;
                changeAudioFocus(true);
                layoutPrepare.setVisibility(View.GONE);
                player.setUseController(video.getScreenType() == 0);
            } else if (playbackState == Player.STATE_IDLE) {
                reloadTime++;
                if (reloadTime >= 3) {
                    changeAudioFocus(false);
                    onLoadingFail();
                    player.setUseController(false);
                } else {
//                    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(LivestreamDetailActivity.this, Util.getUserAgent(LivestreamDetailActivity.this, null));
//                    mediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(video.getHlsPlaylink()));
                    exoPlayer.prepare(buildMediaSource(video.getHlsPlaylink()));
                }
            } else if (playbackState == Player.STATE_BUFFERING) {
                changeAudioFocus(false);
                onLoading();
                player.setUseController(false);
            }
        }

        @Override
        public void onPlayerError(@NonNull ExoPlaybackException error) {
            if (NetworkHelper.checkTypeConnection(LivestreamDetailActivity.this) != NetworkHelper.TYPE_WIFI) {
                checkData();
            }
        }
    };
    private Dialog qualityDialog;
    private ReportDialog reportDialog;
    private ApplicationController application;
    private boolean isLive = true;
    private AudioManager mAudioManager;
    private boolean isHasAudioFocus = false;
    private boolean isLossTransientCanDuck = false;
    private AppCompatImageView btnSetting;
    private int distance;
    private String url, userNumber, lastChange;
    private boolean isFollow = false;
    private LivestreamChatFragment chatFragment;
    private DataSource.Factory manifestDataSourceFactory;
    private DefaultBandwidthMeter defaultBandwidthMeter;
    private DataSource.Factory mediaDataSourceFactory;
    private LivestreamInfoFragment infoFragment;
    private LivestreamFutureVoteBottomSheetDialog voteDialog;
    public boolean isVoted = false;
    public long numberVote = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rm_activity_livestream_detail);
        try {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            int windowManager = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            setWindowFlag(windowManager, false);
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color_new));
            application = ApplicationController.self();

            mAudioManager = (AudioManager) ApplicationController.self().getSystemService(AUDIO_SERVICE);
            defaultBandwidthMeter = new DefaultBandwidthMeter();
            manifestDataSourceFactory = new DefaultDataSourceFactory(application, Util.getUserAgent(application, getString(R.string.app_name)));
            mediaDataSourceFactory = createDataSourceFactory(application, Util.getUserAgent(application, getString(R.string.app_name)), defaultBandwidthMeter);
            formatBuilder = new StringBuilder();
            formatter = new Formatter(formatBuilder, Locale.getDefault());
            updateProgressAction = this::updateProgress;
            updateTimeBarLatency = this::removeTimeBarLatency;


            ReengAccount account = application.getCurrentAccount();
            userNumber = account.getJidNumber();
            lastChange = account.getLastChangeAvatar();
            url = getAvatarUrl(lastChange, userNumber, account.getAvatarVerify());

            initView();
            initData();
            switchScreen();
            setBtnBack();
            seekToMax();
            removeTimeBarLatency();
            changeAudioFocus(true);
            hideAndShowViewPager();
            initReactAnimation();
            new Handler().postDelayed(this::updateProgress, 500);
            handleKeyboardShow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static DefaultDataSourceFactory createDataSourceFactory(Context context, String userAgent, TransferListener listener) {
        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, listener, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS, DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
        return new DefaultDataSourceFactory(context, listener, httpDataSourceFactory);
    }


    private void initView() {
        player = findViewById(R.id.jz_player);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.viewpager);
        viewNumber = findViewById(R.id.viewNumber);
        tvPosition = findViewById(R.id.position);
        btnFullscreen = findViewById(R.id.bt_fullscreen);
        imageUpcomming = findViewById(R.id.image_upcomming);
        btnSetting = findViewById(R.id.btn_setting);
        btnBack = findViewById(R.id.btn_back);
        timeBar = findViewById(R.id.livestream_time_bar);
        tvLive = findViewById(R.id.tv_live);
        liveTime = findViewById(R.id.live);

        chatMessage = findViewById(R.id.chat_content);
        imgSend = findViewById(R.id.button_send);
        rcvFunctions = findViewById(R.id.function_list);
        btnShowViewPager = findViewById(R.id.btn_show_pager);
        edtPlaceholder = findViewById(R.id.edt_placeholder);
        layoutTextBox = findViewById(R.id.layout_text_box);
        handler = findViewById(R.id.handler);
        layoutPrepare = findViewById(R.id.layout_prepare);
        loading = findViewById(R.id.loading);
        retryLayout = findViewById(R.id.retry_layout);
        retryButton = findViewById(R.id.retry_btn);
        tvEndLive = findViewById(R.id.tv_end_live);
        tvPositionVOD = findViewById(R.id.exo_position);
        tvDuration = findViewById(R.id.exo_duration);
        timeBarVOD = findViewById(R.id.exo_progress);
        layoutViewer = findViewById(R.id.layout_viewer);
        btnBackLoading = findViewById(R.id.btn_back_loading);
        layoutPlayer = findViewById(R.id.player_view);
        tabController = findViewById(R.id.tab_controller);
        voteBar = findViewById(R.id.voteBar);

        btnSetting.setOnClickListener(v -> handlerMore());
    }

    private void handlerMore() {
        if (this.isFinishing()) return;
        if (optionsVideoDialog != null && optionsVideoDialog.isShowing())
            optionsVideoDialog.dismiss();
        //Khoi tao menu list item
        ArrayList<ItemContextMenu> listItems = new ArrayList<>();
        listItems.add(new ItemContextMenu("Quality", R.drawable.rm_ic_newui_quality_video, null, MENU_QUALITY));
        listItems.add(new ItemContextMenu("Report", R.drawable.rm_ic_flag_option, null, MENU_REPORT_VIDEO));
        listItems.add(new ItemContextMenu("Cancel", R.drawable.rm_ic_close_option, null, MENU_EXIT));


        //Khoi tao optionDialog
        optionsVideoDialog = new OptionsVideoDialog(this, true)
                .setHasTitle(false)
                .setListItem(listItems)
                .setListener((itemId, entry) -> {
                    switch (itemId) {
                        case MENU_QUALITY:
                            handlerQuality();
                            break;
                        case MENU_REPORT_VIDEO:
                            openReport();
                            break;
                    }
                });
        optionsVideoDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        optionsVideoDialog.show();
    }

    public void handlerQuality() {
        if (this.isFinishing()) return;
        if (qualityDialog != null && qualityDialog.isShowing())
            qualityDialog.dismiss();

        qualityDialog = new QualityDialog(this)
                .setCurrentVideo(video.getAdaptiveLinks(), video.getHlsPlaylink())
                .setOnQualityVideoListener((idx, resolution) -> {
                    if (video != null && !TextUtils.isEmpty(video.getId())) {
                        if (video.getIndexQuality() == idx)
                            return;
                        video.setIndexQuality(idx);
                        if (application != null)
                            application.setConfigResolutionVideo(resolution.getKey());
                        long position;
                        long duration;
                        if (exoPlayer != null) {
                            position = exoPlayer.getCurrentPosition();
                            duration = exoPlayer.getDuration();
                            exoPlayer.prepare(buildMediaSource(resolution.getVideoPath()));
                            exoPlayer.seekTo(Math.min(position, duration));
                        }
                    }
                });
        qualityDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        qualityDialog.show();

    }

    private void openReport() {
        try {
            reportDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getReportTypeList() {
        List<Report> reports = new ArrayList<>();
        ApiCollections apiCollections = ApiSummoner.liveStreamApi();
        apiCollections.getLivestreamReportList()
                .enqueue(new Callback<LiveStreamGetReportListResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LiveStreamGetReportListResponse> call, @NonNull Response<LiveStreamGetReportListResponse> response) {
                        try {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    if (response.body().getCode() == 200) {
                                        if (response.body().getReportList().size() > 0) {
                                            reports.addAll(response.body().getReportList());
                                            reportDialog = new ReportDialog(LivestreamDetailActivity.this, reports, reportDetail ->
                                                    Log.d("1231231", reportDetail.getDescription()), video.getId(), video.getChannel().getId());
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LiveStreamGetReportListResponse> call, @NonNull Throwable throwable) {

                    }
                });
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constants.KeyData.data);
        video = (LivestreamModel) bundle.getSerializable(Constants.KeyData.video);
        setUpScreenType();

        tvPositionVOD.setVisibility(video.getStatus() == 1 ? View.GONE : View.VISIBLE);
        tvDuration.setVisibility(video.getStatus() == 1 ? View.GONE : View.VISIBLE);
        timeBarVOD.setVisibility(video.getStatus() == 1 ? View.GONE : View.VISIBLE);
        tvLive.setVisibility(video.getStatus() == 1 ? View.VISIBLE : View.GONE);
        tvPosition.setVisibility(video.getStatus() == 1 ? View.VISIBLE : View.GONE);
        timeBar.setVisibility(View.GONE);
        layoutViewer.setVisibility(video.getStatus() == 1 ? View.VISIBLE : View.GONE);
        if (video.getStatus() == 0) {
            imageUpcomming.setVisibility(View.VISIBLE);
        } else {
            imageUpcomming.setVisibility(View.GONE);
        }

        exoPlayer = new SimpleExoPlayer.Builder(this).build();
        exoPlayer.addListener(mEventListener);
        player.setPlayer(exoPlayer);

        if (video.getStatus() == 1) {
            exoPlayer.prepare(buildMediaSource(video.getHlsPlaylink()));
        }
        Glide.with(this).load(video.getLinkAvatar()).placeholder(R.drawable.rm_df_image_home).centerCrop().into(imageUpcomming);


        timeBar.addListener(this);

        adapter = new LivestreamPagerAdapter(getSupportFragmentManager());
        infoFragment = new LivestreamInfoFragment(video);
        adapter.addFragment(infoFragment, getString(R.string.streamer_info));
        chatFragment = LivestreamChatFragment.newInstance(bundle, this);
        adapter.addFragment(chatFragment, getString(R.string.livestream_chat));
        getListReactionUser();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() != 1) {
                    InputMethodUtils.hideSoftKeyboard(LivestreamDetailActivity.this);
                    infoFragment.getTopDonate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() != 1) {
                    InputMethodUtils.hideSoftKeyboard(LivestreamDetailActivity.this);
                    infoFragment.getTopDonate();
                }
            }
        });
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        tabLayout.setupWithViewPager(viewPager);
        String totalView = String.valueOf(video.getTotalView());
        viewNumber.setText(totalView);
        if (functions == null) {
            functions = new ArrayList<>();
        } else {
            functions.clear();
        }
        if (video.isEnableDonate()) {
            functions.add(new Constants.ChatFunction(Constants.ChatFunction.TYPE_DONATE));
        }
        functions.add(new Constants.ChatFunction(Constants.ChatFunction.TYPE_LIKE));
        functions.add(new Constants.ChatFunction(Constants.ChatFunction.TYPE_HEART));
        functions.add(new Constants.ChatFunction(Constants.ChatFunction.TYPE_HAHA));
        functions.add(new Constants.ChatFunction(Constants.ChatFunction.TYPE_WOW));
        functions.add(new Constants.ChatFunction(Constants.ChatFunction.TYPE_SAD));
        functions.add(new Constants.ChatFunction(Constants.ChatFunction.TYPE_ANGRY));
        functionAdapter = new LivestreamFunctionAdapter(functions);

        rcvFunctions.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcvFunctions.setAdapter(functionAdapter);
        if (video.isBlockComment()) {
            edtPlaceholder.setText(getString(R.string.you_are_block));
            edtPlaceholder.setOnClickListener(view -> {
                ToastUtils.showToast(this, getString(R.string.you_are_block_full));
            });
        } else {
            edtPlaceholder.setText(R.string.enter_message);
            Log.e("TAG-sub", "updateViewComment: 5");
            edtPlaceholder.setOnClickListener(this);

        }
        chatMessage.addTextChangedListener(this);
        imgSend.setOnClickListener(this);
        functionAdapter.setListener(this);
        retryButton.setOnClickListener(this);
        getReportTypeList();
        getLivestreamDetail(video.getId());
        if (video.getListVote() != null && video.getListVote().size() > 0) {
            voteBar.setVisibility(View.VISIBLE);
        } else {
            voteBar.setVisibility(View.GONE);
        }
        getListVote();
        LogAppHelper.getInstance().logAppAction(LogAppHelper.ConstantLogApp.EVENT_LIVESTREAM_DETAIL, video.getId());
    }

    private void initConnection() {
        if (!SocketManagerV2.getInstance().isConnected()) {
            List<StompHeader> headerList;
            ReengAccount account = ApplicationController.self().getCurrentAccount();
            headerList = new ArrayList<>();
            headerList.add(new StompHeader(Constants.WebSocket.connectorUser, account.getJidNumber().replaceAll("\\s+", "")));
            headerList.add(new StompHeader(Constants.WebSocket.connectorToken, account.getToken().replaceAll("\\s+", "")));
            headerList.add(new StompHeader(Constants.WebSocket.connectorUsername, account.getName().replaceAll("\\s+", "")));
            headerList.add(new StompHeader(Constants.WebSocket.connectorContentType, Constants.WebSocket.connectorContentTypeValue));
            SocketManagerV2.getInstance().connect(Constants.WebSocket.domain, headerList, video.getId(), video.getChannelId());
        }
    }

    @Override
    public void onBackPressed() {
        SocketManagerV2.getInstance().disconnect();
        EventBus.getDefault().unregister(this);
        exoPlayer.removeListener(mEventListener);
        exoPlayer.stop();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onBackPressed();
    }


    @SuppressLint("CheckResult")
    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(true);
        }
        changeAudioFocus(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        exoPlayer.setPlayWhenReady(false);
        changeAudioFocus(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveViewNumber(LiveStreamViewNumber number) {
        viewNumber.setText(String.valueOf(number.getNumber()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void switchToChat(EventBusEvents.SwitchToChatEvent event) {
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        if (tab != null)
            tab.select();
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void stopLivestream(LivestreamLiveNotification event) {
        if (Objects.equals(event.getContentType(), LivestreamLiveNotification.typeStop)) {
            Log.e("TAG-live", "onStartLiveStreamEvent: " + event.getContentType());
            changeAudioFocus(false);
            setPlayWhenReady(false);
            onEndLive();
            player.setUseController(false);
        } else if (Objects.equals(event.getContentType(), Constants.WebSocket.typeStream)) {
            Log.e("TAG-live", "onStartLiveStreamEvent: " + event.getContentType());
            video.setStatus(1);
            imageUpcomming.setVisibility(View.GONE);
            tvPositionVOD.setVisibility(video.getStatus() == 1 ? View.GONE : View.VISIBLE);
            tvDuration.setVisibility(video.getStatus() == 1 ? View.GONE : View.VISIBLE);
            timeBarVOD.setVisibility(video.getStatus() == 1 ? View.GONE : View.VISIBLE);
            tvLive.setVisibility(video.getStatus() == 1 ? View.VISIBLE : View.GONE);
            tvPosition.setVisibility(video.getStatus() == 1 ? View.GONE : View.VISIBLE);
            timeBar.setVisibility(View.GONE);
            layoutViewer.setVisibility(video.getStatus() == 1 ? View.VISIBLE : View.GONE);
            exoPlayer.prepare(buildMediaSource(video.getHlsPlaylink()));
        }
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        exoPlayer.setPlayWhenReady(false);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void switchScreen() {
        View decorView = getWindow().getDecorView();

        int uiOptionsHide = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        int uiOptionsShow = View.SYSTEM_UI_FLAG_VISIBLE;

        btnFullscreen.setOnClickListener(view ->
        {
            if (!isFullScreen) {
                btnFullscreen.setImageDrawable(ContextCompat
                        .getDrawable(getApplicationContext(), R.drawable.exo_controls_fullscreen_exit));
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                decorView.setSystemUiVisibility(uiOptionsHide);
                player.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                KeyboardUtils.hideSoftInput(this);
            } else {
                btnFullscreen.setImageDrawable(ContextCompat
                        .getDrawable(getApplicationContext(), R.drawable.exo_controls_fullscreen_enter));
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                decorView.setSystemUiVisibility(uiOptionsShow);
                player.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 0);
                layoutParams.dimensionRatio = "H,16:9";
                player.setLayoutParams(layoutParams);
            }
            isFullScreen = !isFullScreen;
        });
    }

    private void setBtnBack() {
        btnBack.setOnClickListener(view -> {
            onBackAction();
        });

        btnBackLoading.setOnClickListener(view -> {
            onBackAction();
        });
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void onBackAction() {
        View decorView = getWindow().getDecorView();
        int uiOptionsShow = View.SYSTEM_UI_FLAG_VISIBLE;
        if (!isFullScreen) {
            onBackPressed();
        } else {
            btnFullscreen.setImageDrawable(ContextCompat
                    .getDrawable(getApplicationContext(), R.drawable.exo_controls_fullscreen_enter));
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            decorView.setSystemUiVisibility(uiOptionsShow);
            player.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 0);
            layoutParams.dimensionRatio = "H,16:9";
            player.setLayoutParams(layoutParams);
        }
        isFullScreen = !isFullScreen;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onScrubStart(@NonNull TimeBar timeBar, long position) {
        scrubbing = true;
        if (tvPosition != null) {
            tvPosition.setVisibility(View.VISIBLE);
            duration = Objects.requireNonNull(player.getPlayer()).getContentDuration();
            tvPosition.setText("-" + Util.getStringForTime(formatBuilder, formatter, (duration - 10000) - position));
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onScrubMove(@NonNull TimeBar timeBar, long position) {
        if (tvPosition != null) {
            duration = Objects.requireNonNull(player.getPlayer()).getContentDuration();
            tvPosition.setText("-" + Util.getStringForTime(formatBuilder, formatter, (duration - 10000) - position));
        }
    }

    @Override
    public void onScrubStop(@NonNull TimeBar timeBar, long position, boolean canceled) {
        scrubbing = false;
        Objects.requireNonNull(player.getPlayer()).seekTo(position);
        updateProgress();
    }

    @SuppressLint({"SetTextI18n", "ResourceType"})
    private void updateProgress() {
        @Nullable Player player = this.player.getPlayer();
        long position = 0;
        long bufferedPosition = 0;
        if (player != null) {
            duration = player.getContentDuration();

            position = player.getContentPosition();
            bufferedPosition = player.getContentBufferedPosition();
        }
        if (tvPosition != null && !scrubbing) {
            tvPosition.setText("-" + Util.getStringForTime(formatBuilder, formatter, (duration - 10000) - position));
            if (((duration - 10000) - position) > 5000 && video.getStatus() == 1) {
                isLive = false;
                tvPosition.setVisibility(View.GONE);
            } else {
                isLive = true;
                tvPosition.setVisibility(View.GONE);
            }
        }
        if (timeBar != null) {
            timeBar.setPosition(position);
            timeBar.setBufferedPosition(bufferedPosition);
        }

        // Cancel any pending updates and schedule a new one if necessary.
        this.player.removeCallbacks(updateProgressAction);
        int playbackState = player == null ? Player.STATE_IDLE : player.getPlaybackState();
        if (player != null && player.isPlaying()) {
            long mediaTimeDelayMs =
                    timeBar != null ? timeBar.getPreferredUpdateDelay() : MAX_UPDATE_INTERVAL_MS;

            // Limit delay to the start of the next full second to ensure position display is smooth.
            long mediaTimeUntilNextFullSecondMs = 1000 - position % 1000;
            mediaTimeDelayMs = Math.min(mediaTimeDelayMs, mediaTimeUntilNextFullSecondMs);

            // Calculate the delay until the next update in real time, taking playback speed into account.
            float playbackSpeed = player.getPlaybackParameters().speed;
            long delayMs = playbackSpeed > 0 ? (long) (mediaTimeDelayMs / playbackSpeed) : MAX_UPDATE_INTERVAL_MS;

            // Constrain the delay to avoid too frequent / infrequent updates.
            delayMs = Util.constrainValue(delayMs, 16, MAX_UPDATE_INTERVAL_MS);
            this.player.postDelayed(updateProgressAction, delayMs);
        } else if (playbackState != Player.STATE_ENDED && playbackState != Player.STATE_IDLE) {
            this.player.postDelayed(updateProgressAction, MAX_UPDATE_INTERVAL_MS);
        }
    }

    @SuppressLint("SetTextI18n")
    private void removeTimeBarLatency() {
        timeBar.setDuration(currentDuration - 10000);
        liveTime.setText(getString(R.string.live)/* + " " + Util.getStringForTime(formatBuilder, formatter, currentDuration)*/);
        if (currentDuration < (duration + 5000)) {
            currentDuration += 1000;
        } else {
            currentDuration = duration;
        }
        timeBar.postDelayed(updateTimeBarLatency, 1000);
    }

    private void seekToMax() {
        duration = Objects.requireNonNull(player.getPlayer()).getContentDuration();
        player.getPlayer().seekTo(duration);
        timeBar.setPosition(duration);
    }

    @Override
    public void onResume() {
        super.onResume();
        getChannelInfo();
        seekToMax();
    }

    private void handleAudioFocusChange(int focusChange) {
        /*
         * Pause playback during alerts and notifications
         */
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
                // Stop playback
                if (exoPlayer != null && exoPlayer.getPlayWhenReady()) {
                    exoPlayer.setPlayWhenReady(false);
                    changeAudioFocus(false);
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Pause playback
                if (exoPlayer != null && exoPlayer.getPlayWhenReady()) {
                    exoPlayer.setPlayWhenReady(false);
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lower the volume
                if (exoPlayer != null && exoPlayer.getPlayWhenReady()) {
                    exoPlayer.setVolume(36);
                    isLossTransientCanDuck = true;
                }
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                // Resume playback
                if (isLossTransientCanDuck) {
                    if (exoPlayer != null) exoPlayer.setVolume(100);
                    isLossTransientCanDuck = false;
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
            }
        } else {
            if (isHasAudioFocus) {
                mAudioManager.setParameters("bgm_state=false");
                isHasAudioFocus = false;
            }
        }
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        exoPlayer.setPlayWhenReady(playWhenReady);
    }

    //Chat function
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String content = chatMessage.getText().toString().trim();
        boolean hasContent = !TextUtils.isEmpty(content);
        if (hasContent) {
            imgSend.setVisibility(View.VISIBLE);
            edtPlaceholder.setText(content);
        } else {
            imgSend.setVisibility(View.GONE);
            edtPlaceholder.setText(R.string.enter_message);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_send) {
            if (TextUtils.isEmpty(chatMessage.getText().toString().trim())) {
                return;
            } else {
                if (video.getEnableChat() == 2) { //todo chat only follow
                    if (isFollow) {
                        if (SocketManagerV2.getInstance().isConnected()) {
                            SocketManagerV2.getInstance().sendMessage(chatMessage.getText().toString().trim());
                        } else {
                            ToastUtils.makeText(this, R.string.error_message_default);
                        }
                    } else {
                        ToastUtils.showToast(this, getString(R.string.only_fan));
                    }
                } else {
                    if (video.getEnableChat() == 1) { //todo chat all
                        if (SocketManagerV2.getInstance().isConnected()) {
                            SocketManagerV2.getInstance().sendMessage(chatMessage.getText().toString().trim());
                        } else {
                            ToastUtils.makeText(this, R.string.error_message_default);
                        }
                    }
                }
            }
            chatMessage.setText("");
            edtPlaceholder.setText(R.string.enter_message);
        }

        if (view.getId() == R.id.edt_placeholder) {
            if (!video.isBlockComment()) {
                switchToTabChat();
                chatMessage.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(chatMessage, InputMethodManager.SHOW_IMPLICIT);
            } else {
                ToastUtils.showToast(this, getString(R.string.you_are_block_full));
            }
        }
        if (view.getId() == R.id.retry_btn) {
//            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, null));
//            mediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(video.getHlsPlaylink()));
            exoPlayer.prepare(buildMediaSource(video.getHlsPlaylink()));
        }
    }

    @Override
    public void onFunctionClick(int functionType) {
        try {
            float scale = 0.5f;
            if (StringUtils.isEmpty(lastChange)) {
                scale = 0.35f;
            }
            if (functionType == Constants.ChatFunction.TYPE_LIKE || functionType == Constants.ChatFunction.TYPE_LIKED) {
                doActionLike(1);
                animation.setScalingFactor(scale);
                onClickFlyEmoji(R.drawable.rm_ic_react_like_png);
            }
            if (functionType == Constants.ChatFunction.TYPE_HEART) {
                doActionLike(2);
                animation.setScalingFactor(scale);
                onClickFlyEmoji(R.drawable.rm_ic_react_heart_png);
            }
            if (functionType == Constants.ChatFunction.TYPE_HAHA) {
                doActionLike(3);
                animation.setScalingFactor(scale);
                onClickFlyEmoji(R.drawable.rm_ic_react_haha_png);
            }
            if (functionType == Constants.ChatFunction.TYPE_WOW) {
                doActionLike(4);
                animation.setScalingFactor(scale);
                onClickFlyEmoji(R.drawable.rm_ic_react_wow_png);
            }
            if (functionType == Constants.ChatFunction.TYPE_SAD) {
                doActionLike(5);
                animation.setScalingFactor(scale);
                onClickFlyEmoji(R.drawable.rm_ic_react_sad_png);
            }
            if (functionType == Constants.ChatFunction.TYPE_ANGRY) {
                doActionLike(6);
                animation.setScalingFactor(scale);
                onClickFlyEmoji(R.drawable.rm_ic_react_angry_png);
            }

            if (functionType == Constants.ChatFunction.TYPE_DONATE) {
                openDialogSendStars();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("reactError", "onFunctionClick: " + e);
        }
    }

    public void openDialogSendStars() {
        sendStarDialog = new SendStarDialog(this, video);
        sendStarDialog.show();
    }

    private void doActionLike(int reatId) {
        HomeApi.getInstance().likeLivestream(video.getId(), video.getChannelId(), reatId, new HttpCallBack() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(String data) {
                try {
                    Gson gson = new Gson();
                    LiveStreamFollowResponse response = gson.fromJson(data, LiveStreamFollowResponse.class);
                    if (response != null) {
                        for (Constants.ChatFunction function : functions) {
                            function.setChosen(false);
                        }
                        functions.get(reatId).setChosen(true);
                        functionAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
                Log.d("likeFail", "onFailure: " + message);
            }
        });
    }

    private void hideAndShowViewPager() {
        ViewTreeObserver observer = viewPager.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                distance = viewPager.getMeasuredHeight();
            }
        });
        btnShowViewPager.setOnClickListener(view -> {
            if (isHidden) {
                if (video.getScreenType() == 0)
                    btnShowViewPager.setImageResource(R.drawable.rm_ic_rounded_white_arrow_down);
                else
                    btnShowViewPager.setImageResource(R.drawable.rm_ic_rounded_white_arrow_down_alpha50);
                viewPager.animate().translationY((float) 0);
                viewPager.animate().alpha(1f);
            } else {
                if (video.getScreenType() == 0)
                    btnShowViewPager.setImageResource(R.drawable.rm_ic_rounded_white_arrow_up);
                else
                    btnShowViewPager.setImageResource(R.drawable.rm_ic_rounded_white_arrow_up_alpha50);
                viewPager.animate().translationY((float) distance);
                viewPager.animate().alpha(0f);
            }
            isHidden = !isHidden;
        });
    }

    public void switchToTabChat() {
        if (viewPager.getCurrentItem() != 1) {
            viewPager.setCurrentItem(1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveLikeNumber(LiveStreamLikeNotification liveStreamLikeNotification) throws IOException {
//        tvLike.setText(String.valueOf(liveStreamLikeNotification.getTotalLike()));
        if (Objects.equals(liveStreamLikeNotification.getUserId(), application.getJidNumber())) {
            return;
        }
        if (liveStreamLikeNotification.getType() == 1) {
            animation.setScalingFactor(0.25f);
            flyEmoji(R.drawable.rm_ic_react_like_png);
        } else if (liveStreamLikeNotification.getType() == 2) {
            animation.setScalingFactor(0.25f);
            flyEmoji(R.drawable.rm_ic_react_heart_png);
        } else if (liveStreamLikeNotification.getType() == 3) {
            animation.setScalingFactor(0.25f);
            flyEmoji(R.drawable.rm_ic_react_haha_png);
        } else if (liveStreamLikeNotification.getType() == 4) {
            animation.setScalingFactor(0.25f);
            flyEmoji(R.drawable.rm_ic_react_wow_png);
        } else if (liveStreamLikeNotification.getType() == 5) {
            animation.setScalingFactor(0.25f);
            flyEmoji(R.drawable.rm_ic_react_sad_png);
        } else if (liveStreamLikeNotification.getType() == 6) {
            animation.setScalingFactor(0.25f);
            flyEmoji(R.drawable.rm_ic_react_angry_png);
        }

        EventBus.getDefault().removeStickyEvent(liveStreamLikeNotification);
    }

    public void flyEmoji(final int resId) throws IOException {
        animation.setImage(resId);
        animation.play(this, container, false);
    }

    public void onClickFlyEmoji(final int resId) throws IOException {
        animation.play(this, container, true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                animation.setImage(resId);
            }
        }, 500);
    }

    private void onLoading() {
        layoutPrepare.setVisibility(View.VISIBLE);
        loading.setVisibility(View.VISIBLE);
        retryLayout.setVisibility(View.GONE);
        tvEndLive.setVisibility(View.GONE);
    }

    private void onLoadingFail() {
        layoutPrepare.setVisibility(View.VISIBLE);
        retryLayout.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        tvEndLive.setVisibility(View.GONE);
    }

    private void onEndLive() {
        layoutPrepare.setVisibility(View.VISIBLE);
        tvEndLive.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        retryLayout.setVisibility(View.GONE);
        exoPlayer.setPlayWhenReady(false);
    }

    private void setWindowFlag(final int bits, boolean on) {
        WindowManager.LayoutParams winParams = getWindow().getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        getWindow().setAttributes(winParams);
    }

    public String getAvatarUrl(String lastChangeAvatar, String number, String verify) {
        if (TextUtils.isEmpty(lastChangeAvatar) || TextUtils.isEmpty(number)) {
            return "";
        }
        String numberEncode = HttpHelper.EncoderUrl(number);
        String url;
        url = UrlConfigHelper.getInstance(application).getDomainImage() + AVATAR_FULL;
        return String.format(url, verify, numberEncode, lastChangeAvatar,
                HttpHelper.EncoderUrl(application.getJidNumber()));
    }

    private void initReactAnimation() {
        animation = new ZeroGravityAnimation(this);
        animation.setCount(1);
        animation.setScalingFactor(0.25f);
        animation.setOriginationDirection(Direction.BOTTOM);
        animation.setDestinationDirection(Direction.TOP);
        animation.setAnimationListener(new Animation.AnimationListener() {
                                           @Override
                                           public void onAnimationStart(Animation animation) {

                                           }

                                           @Override
                                           public void onAnimationEnd(Animation animation) {

                                           }

                                           @Override
                                           public void onAnimationRepeat(Animation animation) {

                                           }
                                       }
        );
        container = findViewById(R.id.animation_holder);
        animation.execute(url);
    }

    private void getChannelInfo() {
        ChannelApi channelApi = new ChannelApi() {

            @Override
            public void callApiSubOrUnsubChannel(String id, boolean follow) {

            }

            @Override
            public void getChannelInfo(String id, OnChannelInfoCallback channelInfoCallback) {

            }

        };

        if (channelApi != null && video.getChannel() != null) {
            channelApi.getChannelInfo(video.getChannel().getId(), this);
        }
    }

    @Override
    public void onGetChannelInfoSuccess(Channel channel) {
        isFollow = channel.isFollow();
    }

    @Override
    public void onGetChannelInfoError(String s) {

    }

    @Override
    public void onGetChannelInfoComplete() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveBlockNotification(LiveStreamBlockNotification event) {
        if (event.getUserId().equals(application.getJidNumber())) {
            if (event.getBlockId().equals("5")) {
                edtPlaceholder.setText(getString(R.string.you_are_block));
                edtPlaceholder.setOnClickListener(view -> {
                    ToastUtils.showToast(this, getString(R.string.you_are_block_full));
                });
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                //Find the currently focused view, so we can grab the correct window token from it.
                View view = this.getCurrentFocus();
                //If no view currently has focus, create a new one, just so we can grab a window token from it
                if (view == null) {
                    view = new View(this);
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } else if (event.getBlockId().equals("6")) {
                edtPlaceholder.setText(R.string.enter_message);
                edtPlaceholder.setOnClickListener(view -> {
                    switchToTabChat();
                    chatMessage.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(chatMessage, InputMethodManager.SHOW_IMPLICIT);
                });
                ToastUtils.showToast(this, getString(R.string.unblock_live_chat_notify_message));
            }
        }
        EventBus.getDefault().removeStickyEvent(event);
    }

    public void onDismissReportDialog() {
        if (isFullScreen) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void handleKeyboardShow() {
        KeyboardUtils.registerSoftInputChangedListener(this.getWindow(), height -> {
            if (layoutTextBox != null && handler != null) {
                layoutTextBox.setVisibility(height > 0 ? View.VISIBLE : View.GONE);
                handler.setVisibility(height > 0 ? View.GONE : View.VISIBLE);
                tabController.setVisibility(height > 0 ? View.INVISIBLE : View.VISIBLE);
            }
        });
    }

    private void setUpScreenType() {
        if (video.getScreenType() == 0) {
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 0);
            layoutParams.dimensionRatio = "H,16:9";
            player.setLayoutParams(layoutParams);
            layoutPlayer.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            btnShowViewPager.setImageResource(R.drawable.rm_ic_rounded_white_arrow_down);
            edtPlaceholder.setBackgroundResource(R.drawable.rm_layout_inputtext_background);
        } else {
            player.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
            layoutPlayer.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            btnShowViewPager.setImageResource(R.drawable.rm_ic_rounded_white_arrow_down_alpha50);
            edtPlaceholder.setBackgroundResource(R.drawable.rm_layout_inputtext_background_alpha50);
        }
    }

    private void getLivestreamDetail(String id) {
        HomeApi.getInstance().getLivestreamDetail(id, new HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                Gson gson = new Gson();
                LivestreamDetailResponse response = gson.fromJson(data, LivestreamDetailResponse.class);
                LivestreamModel livestreamModel = response.getData();
                EventBus.getDefault().postSticky(new LiveStreamIsFollowEvent(response.getData().getChannel().isFollow()));
                if (livestreamModel.isBlockComment()) {
                    edtPlaceholder.setText(getString(R.string.you_are_block));
                    edtPlaceholder.setOnClickListener(view -> {
                        ToastUtils.showToast(LivestreamDetailActivity.this, getString(R.string.you_are_block_full));
                    });
                } else {
//                    if(livestreamModel.getEnableChat() == 2){
//                        updateViewComment();
//                    } else {
                    edtPlaceholder.setText(R.string.enter_message);

                    edtPlaceholder.setOnClickListener(view -> {
                        switchToTabChat();
                        chatMessage.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(chatMessage, InputMethodManager.SHOW_IMPLICIT);
                    });
                }
            }
        });
    }

    private void getListReactionUser() {
        HomeApi.getInstance().getListReactionUser(video.getId(), new HttpCallBack() {
            @Override
            public void onSuccess(String data) throws Exception {
                Gson gson = new Gson();
                ReactionUserResponse response = gson.fromJson(data, ReactionUserResponse.class);
                if (response != null && response.getData() != null) {
                    if (response.getData().size() > 0) {
                        chatFragment.setListReactionUser(response.getData());
                    }
                }
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                initConnection();
            }
        });
    }

    boolean isCheckData;

    private void checkData() {
        if (isCheckData) return;
        isCheckData = true;

    }

    public void showDialogPackageData() {
        DialogConfirm dialogConfirm = DialogConfirm.newInstance(
                getString(R.string.notification),
                getString(R.string.title_package_kakoak_data),
                DialogConfirm.CONFIRM_TYPE,
                R.string.package_kakoak25,
                R.string.package_kakoak50);
        dialogConfirm.setSelectListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void dialogRightClick(int value) {
                dialogConfirm.dismiss();
                showDialogConfirmPackage("Kakoak50");
            }

            @Override
            public void dialogLeftClick() {
                dialogConfirm.dismiss();
                showDialogConfirmPackage("Kakoak25");
            }
        });
        dialogConfirm.show(getSupportFragmentManager(), "showDialogPackageData");
    }

    public void showDialogConfirmPackage(String packageName) {
        String content;
        if (packageName.equals("Kakoak25")) {
            content = String.format(getString(R.string.title_confirm_package_data), "25cents/7-days buy 900 MB");
        } else {
            content = String.format(getString(R.string.title_confirm_package_data), "50cents/7-days buy 2 GB");
        }
        DialogConfirm dialogConfirm = new DialogConfirm.Builder(DialogConfirm.INTRO_TYPE)
                .setTitle(getString(R.string.notification))
                .setMessage(content)
                .setTitleRightButton(R.string.ok)
                .create();
        dialogConfirm.setSelectListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void dialogRightClick(int value) {
                registerPackage(packageName);
            }

            @Override
            public void dialogLeftClick() {
                dialogConfirm.dismiss();
            }
        });
        dialogConfirm.show(getSupportFragmentManager(), "showDialogConfirmPackage");
    }

    private void registerPackage(String packageName) {

    }

    private MediaSource buildMediaSource(String url) {
        Log.d("LivestreamDetailACT", "Link play live:" + url);
//        url = "http://apicdn.tls.tl/routing-livestream/router/0387994712/5d68783e6954c685adde989be27074d6/auto/playlist.m3u8";
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveDropStar(LivestreamDropStarNotification event) {
        if (event.getIsDone() == 0) {
            initDialogDropStar();
        }
        EventBus.getDefault().removeStickyEvent(event);
    }

    public void initDialogDropStar() {
        Dialog dialogDropStar = new Dialog(this);
        if (dialogDropStar.getWindow() != null) {
            dialogDropStar.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
            dialogDropStar.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            WindowManager.LayoutParams wlp = dialogDropStar.getWindow().getAttributes();
            wlp.gravity = Gravity.CENTER;
            dialogDropStar.getWindow().setAttributes(wlp);
        }

        dialogDropStar.setContentView(R.layout.rm_dialog_drop_star);
        dialogDropStar.setCancelable(false);
        dialogDropStar.setCanceledOnTouchOutside(false);

        dialogDropStar.findViewById(R.id.btn_take_now).setOnClickListener(view -> {
            dialogDropStar.dismiss();
            callApiReceiveStar();
        });

        dialogDropStar.findViewById(R.id.btn_close).setOnClickListener(v -> {
            dialogDropStar.dismiss();
        });
        dialogDropStar.show();
    }

    private void callApiReceiveStar() {
        HomeApi.getInstance().receiveDropStar(video.getId(), new HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                try {
                    Gson gson = new Gson();
                    ReceiveStarResponse response = gson.fromJson(data, ReceiveStarResponse.class);
                    if (response != null) {
                        if (response.getData() != 0) {
                            ToastUtils.showToast(LivestreamDetailActivity.this,
                                    getString(R.string.receive_star_success_message, response.getData(),
                                            video.getChannel().getName()), 3000, 0, null);
                            if (sendStarDialog != null && sendStarDialog.isShowing())
                                sendStarDialog.getUserStarNumber();
                        } else {
                            ToastUtils.showToast(LivestreamDetailActivity.this,
                                    getString(R.string.receive_star_fail_message), 3000, 0, null);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
                ToastUtils.showToast(LivestreamDetailActivity.this, getString(R.string.error_message_default));
            }
        });
    }

    private void getListVote() {
        isVoted = false;
        numberVote = 0;
        HomeApi.getInstance().getListVoteLivestream(video.getId(), new HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                Gson gson = new Gson();
                ListVoteLivestreamResponse response = gson.fromJson(data, ListVoteLivestreamResponse.class);
                try {
                    if (response.getData() != null && response.getData().size() > 0) {
//                        voteDialog.setVoteModel(response.getData().get(0));
                        voteBar.setVisibility(View.VISIBLE);
                        for (LivestreamVoteOptionModel item : response.getData().get(0).getVoteDtos()) {
                            if (item.isSelect()) {
                                isVoted = true;
                                numberVote = item.getNumberVote();
//                                if (voteDialog.getDialog() != null && voteDialog.getDialog().isShowing()) {
//                                    voteDialog.setVoted(isVoted);
//                                    voteDialog.setNumberVote(numberVote);
//                                    voteDialog.initView();
//                                }
                                break;
                            }
                        }
                        voteDialog = LivestreamFutureVoteBottomSheetDialog.newInstance(LivestreamDetailActivity.this, video, response.getData().get(0), isVoted, numberVote);
                        voteBar.setOnClickListener(view -> {
                            if (!voteDialog.isAdded()) {
//                                voteDialog.setVoted(isVoted);
//                                voteDialog.setNumberVote(numberVote);
                                voteDialog.show(getSupportFragmentManager(), null);
                            }
                        });
                    } else {
                        if (voteDialog != null && voteDialog.getDialog() != null && voteDialog.getDialog().isShowing())
                            voteDialog.getDialog().dismiss();
                        voteBar.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    voteBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
                voteBar.setVisibility(View.GONE);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveReloadSurvey(LivestreamReloadSurveyNotification event) {
        getListVote();
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onSubOrUnSubEvent(SubOrUnSubEvent event) {
        isFollow = event.isSuccess();
        EventBus.getDefault().removeStickyEvent(event);
    }
}
