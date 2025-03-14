package com.example.livestream_update.Ringme.Customview;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.vtm.R;
import com.vtm.ringme.common.utils.player.RingMePlayer;
import com.vtm.ringme.utils.Utilities;

import java.lang.ref.WeakReference;

public class CampaignLayout extends RelativeLayout {

    private static final String Y = "y";
    private long TIME_DURATION = 20000;
    private long TIME_START_RUN = 5000;
    private long TIME_REPEAT_RUN = 30000;

    private @NonNull
    TextView tvCampaign;

    private @Nullable
    SimpleExoPlayer mPlayer;
    private @Nullable
    String campaign;

    private @Nullable
    ValueAnimator mTranslator;
    private @Nullable
    AnimatorUpdateListener mAnimatorUpdateListener;
    private @Nullable
    ScheduleRunnable mScheduleRunnable;
    private @Nullable
    ViewPropertyAnimator mViewPropertyAnimator;

    private boolean startAnimation;

    public CampaignLayout(Context context) {
        this(context, null);
    }

    public CampaignLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CampaignLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.rm_layout_campaign, this);
        tvCampaign = findViewById(R.id.tv_campaign);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setPlayer(@NonNull RingMePlayer mPlayer) {
        setPlayer(mPlayer.getPlayer());
    }

    public void setPlayer(@Nullable SimpleExoPlayer mPlayer) {
        this.mPlayer = mPlayer;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
        startAnimation = false;
        cancelCampaign();
        hide();
        if (Utilities.notEmpty(campaign)) {
            tvCampaign.setText(this.campaign);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        if (mScheduleRunnable == null) {
//            mScheduleRunnable = new ScheduleRunnable(this);
//        }
//        hide();
//        removeCallbacks(mScheduleRunnable);
//        post(mScheduleRunnable);
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        removeCallbacks(mScheduleRunnable);
//        cancelCampaign();
//        hide();
//        super.onDetachedFromWindow();
//    }

    public void showTextRun() {
        if (mScheduleRunnable == null) {
            mScheduleRunnable = new ScheduleRunnable(this);
        }
        hide();
        removeCallbacks(mScheduleRunnable);
        post(mScheduleRunnable);
    }

    public void hideTextRun() {
        removeCallbacks(mScheduleRunnable);
        cancelCampaign();
        hide();
        super.onDetachedFromWindow();
    }

    private void schedule() {
        if (mPlayer != null
                && mPlayer.getCurrentPosition() >= TIME_START_RUN
                && !startAnimation
                && Utilities.notEmpty(campaign)
                && isEnabled()) {
            animCampaign(0);
            removeCallbacks(mScheduleRunnable);
        } else {
            postDelayed(mScheduleRunnable, 200);
        }
    }

    private void cancelCampaign() {
        if (mViewPropertyAnimator != null) {
            mViewPropertyAnimator.setListener(null);
            mViewPropertyAnimator.cancel();
        }
    }

    private void animCampaign(long delayStart) {
        cancelCampaign();
        tvCampaign.setTranslationX(getWidth());
        mViewPropertyAnimator = tvCampaign.animate()
                .translationX(-(tvCampaign.getWidth()))
                .setDuration(TIME_DURATION)
                .setStartDelay(delayStart)
                .setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        show();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        animCampaign(TIME_REPEAT_RUN);
                        hide(1000);
                    }
                });
        mViewPropertyAnimator.start();
    }

    private void show() {
        startAnimation = true;
        translatorY(getHeight(), Utilities.dpToPx(20), 1000);
    }

    public void hide() {
        hide(0);
    }

    private void hide(int duration) {
        startAnimation = false;
        translatorY(getHeight(), 0, duration);
    }

    private void translatorY(int from, int to, int duration) {
        if (mTranslator != null) {
            mTranslator.removeUpdateListener(mAnimatorUpdateListener);
            mTranslator.cancel();
            mTranslator = null;
        }
        if (mAnimatorUpdateListener == null) {
            mAnimatorUpdateListener = new AnimatorUpdateListener(this);
        }
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofInt(Y, from, to);
        mTranslator = ValueAnimator.ofPropertyValuesHolder(pvhY);
        mTranslator.addUpdateListener(mAnimatorUpdateListener);
        mTranslator.setDuration(duration);
        mTranslator.start();
    }

    private void translatorY(int y) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null) return;
        layoutParams.height = y;
        setLayoutParams(layoutParams);
    }

    public void setDurationTime(int seconds) {
        if (seconds > 0) TIME_DURATION = seconds * 1000;
    }

    public void setStartTime(int seconds) {
        if (seconds >= 0) TIME_START_RUN = seconds * 1000;
    }

    public void setReplayTime(int seconds) {
        if (seconds > 0) TIME_REPEAT_RUN = seconds * 1000;
    }

    private static class ScheduleRunnable implements Runnable {

        private WeakReference<CampaignLayout> campaignLayoutWeakReference;

        ScheduleRunnable(CampaignLayout campaignLayout) {
            this.campaignLayoutWeakReference = new WeakReference<>(campaignLayout);
        }

        @Override
        public void run() {
            @Nullable CampaignLayout campaignLayout = campaignLayoutWeakReference.get();
            if (campaignLayout != null) {
                campaignLayout.schedule();
            }
        }

    }

    private static class AnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        private WeakReference<CampaignLayout> campaignLayoutWeakReference;

        AnimatorUpdateListener(CampaignLayout campaignLayout) {
            this.campaignLayoutWeakReference = new WeakReference<>(campaignLayout);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            @Nullable CampaignLayout campaignLayout = campaignLayoutWeakReference.get();
            if (campaignLayout != null) {
                campaignLayout.translatorY((Integer) valueAnimator.getAnimatedValue());
            }
        }

    }
}
