package com.example.livestream_update.Ringme.Customview;


import android.content.Context;
import android.content.pm.PackageManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.vtm.R;
import com.vtm.ringme.common.utils.SharedPrefs;
import com.vtm.ringme.dialog.PositiveListener;
import com.vtm.ringme.model.tab_video.Channel;
import com.vtm.ringme.tabvideo.DialogConfirm;
import com.vtm.ringme.utils.Utilities;
import com.vtm.ringme.values.Constants;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * quản lý toàn bộ tác vụ đăng ký kênh
 */
public class SubscribeChannelLayout extends RelativeLayout {

    private final Context mContext;
    private final TextView textView;

    private Channel mChannel;
    private Channel myChannel;

    private SubscribeChannelListener subscribeChannelListener;

    public SubscribeChannelLayout(Context context) {
        this(context, null);
    }

    public SubscribeChannelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubscribeChannelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        inflate(context, R.layout.rm_layout_subscribe_channel, this);
        textView = findViewById(R.id.tv_subscriptions_channel);
        textView.setOnClickListener(view -> {
            if (mChannel == null || Utilities.isEmpty(mChannel.getId())
                    || subscribeChannelListener == null || positiveListener == null)
                return;
            if (mChannel.getTypeChannel() == Channel.TypeChanel.OPEN_APP.VALUE) {
                subscribeChannelListener.onOpenApp(mChannel, mChannel.isInstall());
                handlerTypeOpenApp(true);
            } else {
                if (mChannel.isFollow()) {
                    if (!showConfirm()) {
                        positiveListener.onPositive(new Object());
                    }
                } else {
                    positiveListener.onPositive(new Object());
                }
            }

        });

        myChannel = SharedPrefs.getInstance().get(Constants.TabVideo.CACHE_MY_CHANNEL_INFO, Channel.class);
        if (myChannel == null) {
            myChannel = new Channel();
        }

        setVisibility(View.GONE);
    }

    public void setChannel(Channel channel) {
        if (mChannel != null && channel != null && Utilities.notEmpty(mChannel.getId()) && mChannel.getId().equals(channel.getId())
                && (myChannel == null || !mChannel.getId().equals(myChannel.getId()))) {
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.GONE);
        }
        mChannel = channel;
        schedule();
    }

    public void setSubscribeChannelListener(SubscribeChannelListener subscribeChannelListener) {
        this.subscribeChannelListener = subscribeChannelListener;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        schedule();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clear();
    }

    private void schedule() {
        if (mChannel == null
                || Utilities.isEmpty(mChannel.getId())
                || Utilities.equals(mChannel.getId(), myChannel.getId())) {
            return;
        }
        if (mChannel.getTypeChannel() != Channel.TypeChanel.OPEN_APP.VALUE) {
            handlerTypeOther();
        } else {
            removeCallbacks(handelRunnable);
            postDelayed(handelRunnable, 300);
        }
    }

    private void clear() {
        removeCallbacks(handelRunnable);
    }

    private final Runnable handelRunnable = new Runnable() {
        @Override
        public void run() {
            if (mChannel == null || Utilities.isEmpty(mChannel.getId())) return;
            asyncSetText(textView, mChannel, Executors.newFixedThreadPool(5));
        }
    };

    private void asyncSetText(TextView textView, final Channel channel, Executor bgExecutor) {
        final WeakReference<TextView> textViewRef = new WeakReference<>(textView);
        bgExecutor.execute(() -> {
            TextView textView12 = textViewRef.get();
            if (textView12 == null) return;
            final boolean isInstall = isInstalled(textView12.getContext(), channel.getPackageAndroid());
            textView12.post(() -> {
                TextView textView1 = textViewRef.get();
                if (textView1 == null) return;
                channel.setInstall(isInstall);
                handlerTypeOpenApp(isInstall);
            });
        });
    }

    private boolean isInstalled(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException | RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void handlerTypeOther() {
        if (mChannel == null || Utilities.isEmpty(mChannel.getId())) return;
        setVisibility(VISIBLE);
        if (mChannel.isFollow()) {
            textView.setText("Followed");
            textView.setBackgroundResource(R.drawable.rm_bg_boder_follow_short_video);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rm_ic_sub_channel_v5, 0, 0, 0);
            textView.setCompoundDrawablePadding(16);
        } else {
            textView.setText(R.string.onmedia_follow);
            textView.setBackgroundResource(R.drawable.rm_xml_background_episode_press);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.videoColorAccent));
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rm_ic_v5_add, 0, 0, 0);
            textView.setCompoundDrawablePadding(16);
        }
    }

    private void handlerTypeOpenApp(boolean isInstall) {
        if (mChannel == null || Utilities.isEmpty(mChannel.getId())) return;
        setVisibility(VISIBLE);
        if (isInstall) {
            textView.setText(R.string.openApp);
            textView.setBackgroundResource(R.drawable.rm_xml_background_btn_unsubscribe);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.videoColorNormal));
        } else {
            textView.setText(R.string.install);
            textView.setBackgroundResource(R.drawable.rm_xml_background_btn_subscribe);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.videoColorAccent));
        }
    }

    private boolean showConfirm() {
        try {
            DialogConfirm dialogConfirm = new DialogConfirm(mContext, true);
            dialogConfirm.setLabel(mContext.getString(R.string.video_channel_un_follow_v3, mChannel.getName()));
            dialogConfirm.setMessage(mContext.getString(R.string.unSubscriptionConfigMessage));
            dialogConfirm.setNegativeLabel(mContext.getString(R.string.video_channel_un_follow_cancel));
            dialogConfirm.setPositiveLabel(mContext.getString(R.string.onmedia_unfollow));
            dialogConfirm.setPositiveListener(positiveListener);
            dialogConfirm.show();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private final PositiveListener<Object> positiveListener = new PositiveListener<Object>() {
        @Override
        public void onPositive(Object object) {
            if (subscribeChannelListener == null) return;
            subscribeChannelListener.onSub(mChannel);
        }
    };

    public interface SubscribeChannelListener {
        void onOpenApp(Channel channel, boolean isInstall);

        void onSub(Channel channel);
    }
}
