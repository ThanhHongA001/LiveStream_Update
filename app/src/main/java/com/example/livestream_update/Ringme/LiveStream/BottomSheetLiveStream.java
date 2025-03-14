package com.example.livestream_update.Ringme.LiveStream;

import android.annotation.SuppressLint;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.vtm.ringme.base.RoundTextView;
import com.vtm.ringme.customview.SubscribeChannelLayout;
import com.vtm.ringme.dialog.ShareBottomDialog;
import com.vtm.ringme.helper.LogDebugHelper;
import com.vtm.ringme.helper.ReportHelper;
import com.vtm.ringme.livestream.listener.OnSingleClickListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.util.Util;
import com.vtm.ringme.ApplicationController;
import com.vtm.R;
import com.vtm.ringme.common.utils.image.ImageManager;
import com.vtm.ringme.helper.TextHelper;

import com.vtm.ringme.model.tab_video.Channel;
import com.vtm.ringme.model.tab_video.Video;
import com.vtm.ringme.livestream.listener.PopupLiveStreamListener;
import com.vtm.ringme.livestream.widget.CountUpTimer;
import com.vtm.ringme.imageview.CircleImageView;
import com.vtm.ringme.tabvideo.channelDetail.ChannelDetailActivity;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.utils.Utilities;

import java.lang.ref.WeakReference;
import java.util.Formatter;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BottomSheetLiveStream implements View.OnClickListener {

    private static final String TAG = BottomSheetLiveStream.class.getSimpleName();

    ImageView ivFullScreen;
    ImageView ivQuality;
    SubscribeChannelLayout btnSubscribeChannel;
    TextView tvSubscriptionsChannel;
    FrameLayout llSubscription;
    CircleImageView ivChannel;
    TextView tvChannelName;
    TextView tvNumberSubscriptionsChannel;
    RelativeLayout reChannelInfo;
    TextView tvTitleVideo;
    TextView tvDesVideo;
    ImageView ivHear;
    TextView tvNumberHear;
    LinearLayout llControllerHear;
    ImageView ivComment;
    TextView tvNumberComment;
    LinearLayout llControllerComment;
    ImageView ivShare;
    TextView tvNumberShare;
    LinearLayout llControllerShare;
    RoundTextView tvShareLandscape;
    RoundTextView tvSharePortrait;

    ImageView ivClose;
    RoundTextView tvLiveTime;
    RoundTextView tvTotalConnect;
    ImageView ivSwitchComment;
    RelativeLayout rootView;
    NestedScrollView layoutDesc;

    private ShareBottomDialog bottomSheetDialog;
    private View viewParent;
    private ApplicationController mApplication;
    private Video currentVideo;
    private boolean isLandscape;
    private boolean isVerticalVideo;
    private int currentPos;
    private long countLive;

    private PopupLiveStreamListener listener;
    private StringBuilder formatBuilder;
    private Formatter formatter;
    private CountUpTimer countUpTimer;
    private AppCompatActivity activity;
    private SubscribeChannelLayout.SubscribeChannelListener mSubscribeChannelListener = new SubscribeChannelLayout.SubscribeChannelListener() {
        @Override
        public void onOpenApp(Channel channel, boolean isInstall) {
            Utilities.openApp(activity, channel.getPackageAndroid());
        }

        @Override
        public void onSub(Channel channel) {

            if (listener != null) listener.subscriberChannel(channel);
        }
    };
    private int widthScreen;
    private int heightScreen;

    public BottomSheetLiveStream(AppCompatActivity activity, Video vid, boolean isLandscape, boolean isVerticalVideo
            , int widthScreen, int heightScreen, int pos, long countLive, PopupLiveStreamListener listener) {
        this.viewParent = LayoutInflater.from(activity).inflate(R.layout.rm_view_share_live_stream, null);
        this.activity = activity;
        this.mApplication = (ApplicationController) activity.getApplicationContext();
        this.listener = listener;
        this.currentVideo = vid;
        this.isLandscape = isLandscape;
        this.isVerticalVideo = isVerticalVideo;
        this.currentPos = pos;
        this.countLive = countLive;
        this.widthScreen = widthScreen;
        this.heightScreen = heightScreen;

        rootView = viewParent.findViewById(R.id.rootView);
        ivClose = viewParent.findViewById(R.id.ivClose);
        tvLiveTime = viewParent.findViewById(R.id.tvLiveTime);
        tvTotalConnect = viewParent.findViewById(R.id.tvTotalConnect);
        ivSwitchComment = viewParent.findViewById(R.id.ivSwitchComment);
        ivFullScreen = viewParent.findViewById(R.id.ivFullScreen);
        ivQuality = viewParent.findViewById(R.id.ivQuality);
        btnSubscribeChannel = viewParent.findViewById(R.id.btn_subscribe_channel);
        tvSubscriptionsChannel = viewParent.findViewById(R.id.tvSubscriptionsChannel);
        llSubscription = viewParent.findViewById(R.id.ll_subscription);
        ivChannel = viewParent.findViewById(R.id.ivChannel);
        tvChannelName = viewParent.findViewById(R.id.tvChannelName);
        tvNumberSubscriptionsChannel = viewParent.findViewById(R.id.tvNumberSubscriptionsChannel);
        reChannelInfo = viewParent.findViewById(R.id.reChannelInfo);
        tvTitleVideo = viewParent.findViewById(R.id.tvTitleVideo);
        tvDesVideo = viewParent.findViewById(R.id.tvDesVideo);
        ivHear = viewParent.findViewById(R.id.ivHear);
        tvNumberHear = viewParent.findViewById(R.id.tvNumberHear);
        llControllerHear = viewParent.findViewById(R.id.llControllerHear);
        ivComment = viewParent.findViewById(R.id.ivComment);
        tvNumberComment = viewParent.findViewById(R.id.tvNumberComment);
        llControllerComment = viewParent.findViewById(R.id.llControllerComment);
        ivShare = viewParent.findViewById(R.id.ivShare);
        tvNumberShare = viewParent.findViewById(R.id.tvNumberShare);
        llControllerShare = viewParent.findViewById(R.id.llControllerShare);
        tvShareLandscape = viewParent.findViewById(R.id.tvShareLandscape);
        tvSharePortrait = viewParent.findViewById(R.id.tvSharePortrait);
        layoutDesc = viewParent.findViewById(R.id.layout_desc);

        ivFullScreen.setOnClickListener(this);
        ivQuality.setOnClickListener(this);
        ivHear.setOnClickListener(this);
        ivComment.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        tvShareLandscape.setOnClickListener(this);
        tvSharePortrait.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        ivSwitchComment.setOnClickListener(this);
        rootView.setOnClickListener(this);
        ivFullScreen.setVisibility(isVerticalVideo ? View.GONE : View.VISIBLE);
        ivQuality.setVisibility(Utilities.isEmpty(currentVideo.getListResolution()) ? View.GONE : View.VISIBLE);
        onChangeOrientation(isLandscape);

        bottomSheetDialog = new ShareBottomDialog(activity, R.style.BottomSheetLiveStreamDialogTheme);
        bottomSheetDialog.setContentView(viewParent);
        Channel channel = currentVideo.getChannel();
        if (channel != null) {
            ImageManager.showImageCircleV2(channel.getUrlImage(), ivChannel);
            tvChannelName.setText(channel.getName());
            btnSubscribeChannel.setChannel(channel);
            btnSubscribeChannel.setSubscribeChannelListener(mSubscribeChannelListener);

            long numberFollow = channel.getNumfollow();
            if (numberFollow == 0) {
                if (tvNumberSubscriptionsChannel.getVisibility() != View.GONE) {
                    tvNumberSubscriptionsChannel.setVisibility(View.GONE);
                }
            } else {
                if (tvNumberSubscriptionsChannel.getVisibility() != View.VISIBLE) {
                    tvNumberSubscriptionsChannel.setVisibility(View.VISIBLE);
                }
                ExecutorService executor = Executors.newFixedThreadPool(5);
                numberToShorten(tvNumberSubscriptionsChannel, channel.getId(), channel.getNumfollow(), executor, new NumberCallback() {
                    @Override
                    public void onNumberCompleted(TextView textView, String id, String number) {
                        if (currentVideo == null || currentVideo.getChannel() == null
                                || Utilities.isEmpty(id)) return;
                        if (tvNumberSubscriptionsChannel == textView && Utilities.equals(id, currentVideo.getChannel().getId())) {
                            tvNumberSubscriptionsChannel.setText(String.format(ApplicationController.self().getString(R.string.people_subscription), number));
                        } else if (tvNumberHear == textView && Utilities.equals(id, currentVideo.getId())) {
                            tvNumberHear.setText(number);
                        } else if (tvNumberComment == textView && Utilities.equals(id, currentVideo.getId())) {
                            tvNumberComment.setText(number);
                        } else if (tvNumberShare == textView && Utilities.equals(id, currentVideo.getId())) {
                            tvNumberShare.setText(number);
                        } /*else if (tvNumberSeen == textView && Utilities.equals(id, currentVideo.getId())) {
                            if (currentVideo.getTotalView() <= 0) {
                                tvNumberSeen.setVisibility(View.GONE);
                            } else {
                                tvNumberSeen.setVisibility(View.VISIBLE);
                                tvNumberSeen.setText(String.format(currentVideo.getTotalView() <= 1 ?
                                        ApplicationController.self().getString(R.string.view) :
                                        ApplicationController.self().getString(R.string.video_views), number));
                            }
                        }*/
                    }
                });
                executor.shutdown();
            }
        }
        tvTitleVideo.setText(currentVideo.getTitle());
        if (TextUtils.isEmpty(currentVideo.getDescription())) {
            layoutDesc.setVisibility(View.GONE);
        } else {
            layoutDesc.setVisibility(View.VISIBLE);
            tvDesVideo.setText(currentVideo.getDescription());
            try {
                int heightText = 0;
                int maxHeight = 0;
                if (isLandscape) {
                    maxHeight = widthScreen / 4;
                    heightText = TextHelper.getHeight(activity, currentVideo.getDescription(), 16, heightScreen);
                } else {
                    maxHeight = heightScreen / 5;
                    heightText = TextHelper.getHeight(activity, currentVideo.getDescription(), 16, widthScreen);
                }
                Log.e(TAG, "heightText: " + heightText + ", \tmaxHeight: " + maxHeight);
                if (heightText > 0) {
                    heightText = Math.min(heightText, maxHeight);
                    layoutDesc.getLayoutParams().height = heightText;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        reChannelInfo.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (channel != null) {
                    ChannelDetailActivity.start(activity, channel);
                    dismiss();
                }
            }
        });
        if (currentVideo.isLike())
            ivHear.setImageResource(R.drawable.rm_ic_onmedia_like_press);
        else
            ivHear.setImageResource(R.drawable.rm_ic_onmedia_like);
        if (currentVideo.getTotalLike() > 0)
            tvNumberHear.setText(String.valueOf(currentVideo.getTotalLike()));
        if (currentVideo.getTotalComment() > 0)
            tvNumberComment.setText(String.valueOf(currentVideo.getTotalComment()));
        if (currentVideo.getTotalShare() > 0)
            tvNumberShare.setText(String.valueOf(currentVideo.getTotalShare()));

        loadData();
        bottomSheetDialog.setOnDismissListener(dialogInterface -> {
            if (countUpTimer != null)
                countUpTimer.cancel();
            if (listener != null) listener.dismissPopup();
        });
        bottomSheetDialog.setOnShowListener(dialogInterface -> {
            setHeightBottomDialog(bottomSheetDialog);
        });
    }

    public static BottomSheetLiveStream showContextMenu(AppCompatActivity activity, Video video
            , boolean isLandscape, boolean isVerticalVideo, int widthScreen, int heightScreen, int pos, long countLive, PopupLiveStreamListener listener) {
        BottomSheetLiveStream bottomSheetContextMenu = new BottomSheetLiveStream(activity, video, isLandscape, isVerticalVideo, widthScreen, heightScreen, pos, countLive, listener);
        bottomSheetContextMenu.show();
        LogDebugHelper.getInstance().logDebugContent("LiveStream: Show video info dialog");
        ReportHelper.reportError(ApplicationController.self(), ReportHelper.DATA_TYPE_LIVE_STREAM_SCREEN, "LiveStream: Show video info dialog");
        return bottomSheetContextMenu;
    }

    private static void numberToShorten(TextView textView, final String id, final long numberView, Executor executor, @NonNull NumberCallback numberCallback) {
        final Handler handler = new Handler();
        final WeakReference<TextView> textViewRef = new WeakReference<>(textView);
        final WeakReference<NumberCallback> numberCallbackRef = new WeakReference<>(numberCallback);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                TextView textView = textViewRef.get();
                NumberCallback numberCallback = numberCallbackRef.get();
                if (textView == null || numberCallback == null) return;
                final String numberStr = Utilities.shortenLongNumber(numberView);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = textViewRef.get();
                        NumberCallback numberCallback = numberCallbackRef.get();
                        if (textView == null || numberCallback == null) return;
                        numberCallback.onNumberCompleted(textView, id, numberStr);
                    }
                });
            }
        });
    }

    private void loadData() {
        long startLive = currentVideo.getStartLiveTime();
        long endLive = currentVideo.getEndLiveTime();
        long duration = endLive - startLive;
        long start = System.currentTimeMillis() - startLive;
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());
        countUpTimer = new CountUpTimer(duration, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(int second) {
                tvLiveTime.setText(activity.getString(R.string.live_stream) + " " + Util.getStringForTime(formatBuilder, formatter, (start + second)));
            }
        };
        if (start > 0 && duration > 0) {
            countUpTimer.cancel();
            countUpTimer.start();
        } else {
            tvLiveTime.setText(activity.getString(R.string.live_stream));
        }
        switchComment(currentPos);
        setCountLive(countLive);
    }

    private void show() {
        if (bottomSheetDialog == null) return;
        bottomSheetDialog.show();
    }

    public void onChangeOrientation(boolean isLandscape) {
        tvSharePortrait.setVisibility(isLandscape ? View.GONE : View.VISIBLE);
        tvShareLandscape.setVisibility(isLandscape ? View.VISIBLE : View.GONE);
        ivClose.setVisibility(isLandscape ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.ivFullScreen:
                if (listener != null) listener.onFullScreen(!isLandscape);
                break;
            case R.id.ivQuality:
                if (listener != null) listener.qualityVideo();
                break;
            case R.id.ivHear:
                if (listener != null) listener.likeVideo();
                break;
            case R.id.ivComment:
                if (listener != null)
                    listener.commentVideo();
                break;
            case R.id.ivShare:
            case R.id.tvShareLandscape:
            case R.id.tvSharePortrait:
                if (listener != null) listener.shareVideo();
                break;
            case R.id.ivClose:
                if (listener != null) listener.onBack();
                break;

            case R.id.ivSwitchComment:
                if (listener != null) listener.switchComment();
                break;
        }
    }

    public void switchComment(int pos) {
        ivSwitchComment.setImageResource(pos == 0 ? R.drawable.rm_ic_comment_hidden : R.drawable.rm_ic_comment_showed);
    }

    public void setCountLive(long countLive) {
        if (countLive > 0) {
            tvTotalConnect.setText(String.valueOf(countLive));
        }
    }

    public void likeVideoUi() {
        if (currentVideo.isLike()) {
            ivHear.setImageResource(R.drawable.rm_ic_onmedia_like_press);
        } else {
            ivHear.setImageResource(R.drawable.rm_ic_onmedia_like);
        }
        if (currentVideo.getTotalLike() > 0) {
            tvNumberHear.setText(String.valueOf(currentVideo.getTotalLike()));
            tvNumberHear.setVisibility(View.VISIBLE);
        } else {
            tvNumberHear.setText("");
            tvNumberHear.setVisibility(View.GONE);
        }
    }

    public void dismiss() {
        if (bottomSheetDialog != null) {
            bottomSheetDialog.dismiss();
            bottomSheetDialog = null;
        }
    }

    public void setWidthScreen(int widthScreen) {
        this.widthScreen = widthScreen;
    }

    public void setHeightScreen(int heightScreen) {
        this.heightScreen = heightScreen;
    }

    private void setHeightBottomDialog(final ShareBottomDialog dialog) {
        if (dialog != null) {
            final BottomSheetBehavior behavior = dialog.getBottomSheetBehavior();
            if (behavior != null) {
//            if (isLandscape)
                behavior.setPeekHeight(Math.max(widthScreen, heightScreen));
//            else
//                behavior.setPeekHeight(Math.max(widthScreen, heightScreen));
                behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        Log.d(TAG, "bottomSheetBehavior onStateChanged newState: " + newState);
                        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                            if (dialog != null) dialog.dismiss();
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        //Log.d(TAG, "bottomSheetBehavior onSlide slideOffset: " + slideOffset);
                    }
                });
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }
    }

    interface NumberCallback {
        void onNumberCompleted(TextView textView, String id, String number);
    }

}
