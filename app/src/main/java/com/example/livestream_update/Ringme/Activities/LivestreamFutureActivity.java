package com.example.livestream_update.Ringme.Activities;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.vtm.R;
import com.vtm.databinding.RmActivityLivestreamFutureBinding;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.adapter.LivestreamCategoryPagerAdapter;
import com.vtm.ringme.api.HomeApi;
import com.vtm.ringme.api.response.ListVoteLivestreamResponse;
import com.vtm.ringme.base.BaseActivityNew;
import com.vtm.ringme.common.api.http.HttpCallBack;
import com.vtm.ringme.common.utils.ShareUtils;
import com.vtm.ringme.dialog.LivestreamFutureChatBottomSheetDialog;
import com.vtm.ringme.dialog.LivestreamFutureVoteBottomSheetDialog;
import com.vtm.ringme.helper.HttpHelper;
import com.vtm.ringme.helper.TimeHelper;
import com.vtm.ringme.helper.UrlConfigHelper;
import com.vtm.ringme.livestream.activity.LivestreamDetailActivity;
import com.vtm.ringme.livestream.apis.response.LiveStreamFollowResponse;
import com.vtm.ringme.livestream.model.LiveStreamChatMessage;
import com.vtm.ringme.livestream.model.LivestreamChatModel;
import com.vtm.ringme.livestream.model.LivestreamReloadSurveyNotification;
import com.vtm.ringme.livestream.model.LivestreamVoteOptionModel;
import com.vtm.ringme.livestream.model.LivestreamWaitNumberNotification;
import com.vtm.ringme.livestream.socket.SocketManagerV2;
import com.vtm.ringme.livestream.socket.stomp.dto.StompHeader;
import com.vtm.ringme.model.ReengAccount;
import com.vtm.ringme.model.livestream.LivestreamModel;
import com.vtm.ringme.utils.Utilities;
import com.vtm.ringme.values.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LivestreamFutureActivity extends BaseActivityNew {
    private static final String AVATAR_THUMB = "/api/thumbnail/download?v=%1$s&ac=%2$s&t=%3$s&u=%4$s";
    private RmActivityLivestreamFutureBinding binding;
    private LivestreamModel liveStreamModel;
    private LivestreamCategoryPagerAdapter pagerAdapter;
    private String url, userNumber, lastChange;
    private boolean showDesc = false;
    private LivestreamFutureChatBottomSheetDialog chatDialog;
    private LivestreamFutureVoteBottomSheetDialog voteDialog;
    private List<LivestreamChatModel> chatModels;
    public boolean isVoted = false;
    public long numberVote = 0;
    public boolean hasVote = false;
    private Handler handler = new Handler();
    private Runnable runnable;
    private SimpleExoPlayer exoPlayer;
    private long duration = 0;
    private long currentDuration = 0;
    private int reloadTime = 0;
    private AudioManager mAudioManager;
    private boolean isHasAudioFocus = false;
    private final AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = this::handleAudioFocusChange;
    private boolean isLossTransientCanDuck = false;
    private DataSource.Factory mediaDataSourceFactory;
    private DataSource.Factory manifestDataSourceFactory;
    private DefaultBandwidthMeter defaultBandwidthMeter;
    private ApplicationController application;


    private final Player.EventListener mEventListener = new Player.EventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            Log.d("playState", "onLoading: " + playbackState);
            if (binding.player != null) {
                binding.player.setKeepScreenOn(playWhenReady);
                currentDuration = Objects.requireNonNull(binding.player.getPlayer()).getContentDuration();
            }
            if (playbackState == Player.STATE_READY) {
                reloadTime = 0;
                changeAudioFocus(true);
                binding.layoutPrepare.setVisibility(View.GONE);
                binding.player.setUseController(liveStreamModel.getScreenType() == 0);
            } else if (playbackState == Player.STATE_IDLE) {
                reloadTime++;
                if (reloadTime >= 3) {
                    changeAudioFocus(false);
                    onLoadingFail();
                    binding.player.setUseController(false);
                } else {
                    exoPlayer.prepare(buildMediaSource(liveStreamModel.getHlsPlaylink()));
                }
            } else if (playbackState == Player.STATE_BUFFERING) {
                changeAudioFocus(false);
                onLoading();
                binding.player.setUseController(false);
            }
        }
    };

    private void onLoadingFail() {
        binding.layoutPrepare.setVisibility(View.VISIBLE);
        binding.retryLayout.setVisibility(View.VISIBLE);
        binding.loading.setVisibility(View.GONE);
    }

    private void onLoading() {
        binding.layoutPrepare.setVisibility(View.VISIBLE);
        binding.loading.setVisibility(View.VISIBLE);
        binding.retryLayout.setVisibility(View.GONE);
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

    private MediaSource buildMediaSource(String url) {
        Log.d("LivestreamDetailACT", "Link play live:" + url);
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

    @Override
    public ViewBinding getViewBinding() {
        binding = RmActivityLivestreamFutureBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        int windowManager = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        setWindowFlag(windowManager, false);
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));

        application = ApplicationController.self();

        mAudioManager = (AudioManager) ApplicationController.self().getSystemService(AUDIO_SERVICE);
        defaultBandwidthMeter = new DefaultBandwidthMeter();
        manifestDataSourceFactory = new DefaultDataSourceFactory(application, Util.getUserAgent(application, getString(R.string.app_name)));
        mediaDataSourceFactory = createDataSourceFactory(application, Util.getUserAgent(application, getString(R.string.app_name)), defaultBandwidthMeter);

        try {
            ReengAccount account = mApp.getCurrentAccount();
            userNumber = account.getJidNumber();
            lastChange = account.getLastChangeAvatar();
            url = getAvatarUrl(lastChange, userNumber, account.getAvatarVerify());

            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra(Constants.KeyData.data);
            assert bundle != null;
            liveStreamModel = (LivestreamModel) bundle.getSerializable(Constants.KeyData.video);

            exoPlayer = new SimpleExoPlayer.Builder(this).build();
            exoPlayer.addListener(mEventListener);
            binding.player.setPlayer(exoPlayer);

            if (liveStreamModel.getStatus() == 1) {
                exoPlayer.prepare(buildMediaSource(liveStreamModel.getHlsPlaylink()));
            }
            checkStartTimeEvent();
            initView();
            setupPager();
            initConnection();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("LivestreamFuture", "onCreate: " + e);
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


    private void checkStartTimeEvent() {
        if (liveStreamModel.getTimeEventStart() <= System.currentTimeMillis()) {
            navigateToLiveStreamDetailActivity();
        } else {
            handler.postDelayed(runnable = new Runnable() {
                @Override
                public void run() {
                    navigateToLiveStreamDetailActivity();
                }
            }, liveStreamModel.getTimeEventStart() - System.currentTimeMillis());
        }
    }

    private void navigateToLiveStreamDetailActivity() {
        onBackPressed();
        Intent intent = new Intent(LivestreamFutureActivity.this, LivestreamDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KeyData.video, liveStreamModel);
        intent.putExtra(Constants.KeyData.data, bundle);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onBackPressed() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        SocketManagerV2.getInstance().disconnect();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    private static DefaultDataSourceFactory createDataSourceFactory(Context context, String userAgent, TransferListener listener) {
        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, listener, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS, DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
        return new DefaultDataSourceFactory(context, listener, httpDataSourceFactory);
    }

    private void initView() {
        chatModels = new ArrayList<>();
        binding.btnBack.setOnClickListener(view -> onBackPressed());

        Glide.with(this)
                .load(liveStreamModel.getLinkAvatar())
                .placeholder(R.drawable.rm_placeholder)
                .centerCrop()
                .into(binding.imgLivestreamAvatar);

        if (liveStreamModel.getTimeEventStart() != 0) {
            binding.tvStartTime.setText(getString(R.string.live_at, TimeHelper.formatTimeEventMessage(liveStreamModel.getTimeEventStart())));
        }

        if (!liveStreamModel.getIsNotified()) {
            binding.btnNotifyMe.setText(R.string.notify_me);
            binding.btnNotifyMe.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rm_ic_livestream_notify_me, 0, 0, 0);
            binding.btnNotifyMe.setCompoundDrawablePadding(5);
            binding.btnNotifyMe.setBackgroundDrawable(getDrawable(R.drawable.rm_bg_btn_notify_me));
        } else {
            binding.btnNotifyMe.setText(R.string.notified_me);
            binding.btnNotifyMe.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rm_ic_tick_v6, 0, 0, 0);
            binding.btnNotifyMe.setCompoundDrawablePadding(5);
            binding.btnNotifyMe.setBackgroundDrawable(getDrawable(R.drawable.rm_bg_btn_notified_me));
        }

        if (liveStreamModel.isLike()) {
            binding.imgBtnLike.setImageResource(R.drawable.rm_ic_livestream_liked);
            binding.tvBtnLike.setTextColor(getResources().getColor(R.color.main_color_new));
        } else {
            binding.imgBtnLike.setImageResource(R.drawable.rm_ic_livestream_like);
            binding.tvBtnLike.setTextColor(Color.parseColor("#B1B1B4"));
        }

        binding.tvTittle.setText(liveStreamModel.getTitle());
        binding.tvDesc.setText(liveStreamModel.getDescription());

        binding.btnNotifyMe.setOnClickListener(view -> onClickNotifyMe());
        binding.btnLike.setOnClickListener(view -> onClickLike());
        binding.tvTittle.setOnClickListener(view -> onClickTitle());

        chatDialog = new LivestreamFutureChatBottomSheetDialog(LivestreamFutureActivity.this, liveStreamModel, chatModels);
        chatDialog.setAvatarUrl(url);

//        voteDialog = new LivestreamFutureVoteBottomSheetDialog(LivestreamFutureActivity.this, video);

        binding.btnComment.setOnClickListener(view -> {
            if (!chatDialog.isAdded())
                chatDialog.show(getSupportFragmentManager(), null);
        });
        binding.btnShare.setOnClickListener(view -> {
            ShareUtils.shareLivestream(this, "kakoak://livestream/detail?ref=" + liveStreamModel.getId(), "Share");
        });

        if (liveStreamModel.getListVote() != null && liveStreamModel.getListVote().size() > 0) {
            binding.btnVote.setVisibility(View.VISIBLE);
            hasVote = true;
        } else {
            binding.btnVote.setVisibility(View.GONE);
            hasVote = false;
        }
        getListVote();
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
            SocketManagerV2.getInstance().connect(Constants.WebSocket.domain, headerList, liveStreamModel.getId(), liveStreamModel.getChannelId());
        }
    }

    private void onClickNotifyMe() {
        HomeApi.getInstance().registerNotifyLivestream(
                liveStreamModel.getId(),
                liveStreamModel.getTimeEventStart(),
                liveStreamModel.getIsNotified() ? 1 : 0,
                new HttpCallBack() {
                    @Override
                    public void onSuccess(String data) throws Exception {
                        if (!liveStreamModel.getIsNotified()) {
                            binding.btnNotifyMe.setText(R.string.notified_me);
                            binding.btnNotifyMe.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rm_ic_tick_v6, 0, 0, 0);
                            binding.btnNotifyMe.setCompoundDrawablePadding(5);
                            binding.btnNotifyMe.setBackgroundDrawable(getDrawable(R.drawable.rm_bg_btn_notified_me));
                        } else {
                            binding.btnNotifyMe.setText(R.string.notify_me);
                            binding.btnNotifyMe.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rm_ic_livestream_notify_me, 0, 0, 0);
                            binding.btnNotifyMe.setCompoundDrawablePadding(5);
                            binding.btnNotifyMe.setBackgroundDrawable(getDrawable(R.drawable.rm_bg_btn_notify_me));
                        }
                        liveStreamModel.setIsNotified(!liveStreamModel.getIsNotified());
                    }

                    @Override
                    public void onFailure(String message) {
                        super.onFailure(message);
                        ToastUtils.showShort(R.string.error_message_default);
                    }
                }
        );
    }

    private void onClickLike() {
        HomeApi.getInstance().likeLivestream(liveStreamModel.getId(), liveStreamModel.getChannelId(), 1, new HttpCallBack() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(String data) {
                try {
                    Gson gson = new Gson();
                    LiveStreamFollowResponse response = gson.fromJson(data, LiveStreamFollowResponse.class);
                    if (response != null) {
                        binding.imgBtnLike.setImageResource(R.drawable.rm_ic_livestream_liked);
                        binding.tvBtnLike.setTextColor(getResources().getColor(R.color.main_color_new));
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

    private void onClickTitle() {
        if (showDesc) {
            binding.tvDesc.setVisibility(View.GONE);
            binding.tvTittle.setMaxLines(1);
            binding.tvTittle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.rm_ic_triangle_down, 0);
        } else {
            binding.tvDesc.setVisibility(View.VISIBLE);
            binding.tvTittle.setMaxLines(Integer.MAX_VALUE);
            binding.tvTittle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.rm_ic_triangle_up, 0);
        }
        showDesc = !showDesc;
    }

    private void setupPager() {
        pagerAdapter = new LivestreamCategoryPagerAdapter(this);
        binding.viewpager.setAdapter(pagerAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewpager, ((tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(R.string.upcoming);
                    break;
                case 1:
                    tab.setText(R.string.live);
                    break;
                case 2:
                    tab.setText(R.string.other_videos);
                    break;
            }
        })).attach();
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
        url = UrlConfigHelper.getInstance(mApp).getDomainImage() + AVATAR_THUMB;
        return String.format(url, verify, numberEncode, lastChangeAvatar,
                HttpHelper.EncoderUrl(mApp.getJidNumber()));
    }

    public void onClickOtherVideo(LivestreamModel item) {
        Log.e("khanhpq", "kha  "+ item.getStatus());
        if (item.getStatus() == 1) {
            Intent intent = new Intent(this, LivestreamDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.KeyData.video, item);
            intent.putExtra(Constants.KeyData.data, bundle);
            startActivity(intent);
            finish();
        } else if (item.getStatus() == 0) {
            this.liveStreamModel = item;
            initView();
            showDesc = false;
            binding.tvDesc.setVisibility(View.GONE);
            binding.tvTittle.setMaxLines(1);
            binding.tvTittle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.rm_ic_triangle_down, 0);
            SocketManagerV2.getInstance().disconnect();
            initConnection();
        } else if(item.getStatus() == 5){
            navigateToLiveStreamDetailActivity();
        }
    }

    private void getListVote() {
        isVoted = false;
        numberVote = 0;
        HomeApi.getInstance().getListVoteLivestream(liveStreamModel.getId(), new HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                Gson gson = new Gson();
                ListVoteLivestreamResponse response = gson.fromJson(data, ListVoteLivestreamResponse.class);
                try {
                    if (response.getData() != null && response.getData().size() > 0) {
//                        voteDialog.setVoteModel(response.getData().get(0));
                        binding.btnVote.setVisibility(View.VISIBLE);
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
                        voteDialog = LivestreamFutureVoteBottomSheetDialog.newInstance(LivestreamFutureActivity.this, liveStreamModel, response.getData().get(0), isVoted, numberVote);
                        binding.btnVote.setOnClickListener(view -> {
                            if (!voteDialog.isAdded()) {
//                                voteDialog.setVoted(isVoted);
//                                voteDialog.setNumberVote(numberVote);
                                voteDialog.show(getSupportFragmentManager(), null);
                            }
                        });
                        hasVote = true;
                    } else {
                        if (voteDialog != null && voteDialog.getDialog() != null && voteDialog.getDialog().isShowing())
                            voteDialog.getDialog().dismiss();
                        binding.btnVote.setVisibility(View.GONE);
                        hasVote = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    binding.btnVote.setVisibility(View.GONE);
                    hasVote = false;
                }
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
                binding.btnVote.setVisibility(View.GONE);
                hasVote = false;
            }
        });
    }

    public void openVoteDialog() {
        if (voteDialog != null && !voteDialog.isAdded()) {
            voteDialog.show(getSupportFragmentManager(), null);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveWaitNumber(LivestreamWaitNumberNotification event) {
        binding.tvWaitingNumber.setText(getString(R.string.waiting_number, Utilities.shortenLongNumber(event.getNumber())));
        EventBus.getDefault().removeStickyEvent(event);
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveChatMessage(LiveStreamChatMessage chatContent) {
        LivestreamChatModel chatModel = new LivestreamChatModel();
        chatModel.setType(Constants.WebSocket.typeMessage);
        chatModel.setChatMessage(chatContent);
        chatModels.add(chatModel);
        chatDialog.addComment(chatModel);
        EventBus.getDefault().removeStickyEvent(chatContent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveReloadSurvey(LivestreamReloadSurveyNotification event) {
        getListVote();
        EventBus.getDefault().removeStickyEvent(event);
    }
}
