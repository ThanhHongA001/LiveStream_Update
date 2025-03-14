package com.example.livestream_update.Ringme.Ripple;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vtm.R;
import com.vtm.ringme.shapeofview.shapes.ArcView;

/**
 * thư viện này thuộc bản quyền của hoanganhtuan95ptit@gmail.com
 * Mọi sử dụng đều phải được sự chấp thuận của nhà cung cấp
 */
@SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
public class RippleLayout extends ArcView {

    private static final String COLOR_FAST = "#10FFFFFF";
    private static final String COLOR_NORMAL = "#00FFFFFF";

    private static final long MAX_DURATION = 250L;// thời gian tối đa của tường tác
    private static final long FAST_RIPPLE_DURATION = 150L;

    private boolean animationRunning = false;// animation có đang chạy hay không
    private boolean isSetBackground = false;// đã chuyển màu animation
    private boolean isFast = false;// có đang tua video không
    private long startTime = 0;// bắt đầu tương tác
    private long timeFast = 0;// thời gian tua video

    private View rippleOneView;
    private View rippleTwoView;
    private View rippleThreeView;
    private View bgRippleView;

    private RippleView rippleView;

    private TextView tvTimeFast;

    private Context mContext;

    private OnRippleListener mOnRippleListener;// bộ lắng nghe ripple

    public RippleLayout(@NonNull Context context) {
        this(context, null);
    }

    public RippleLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        inflate(context, R.layout.rm_layout_ripple, this);

        bgRippleView = findViewById(R.id.bg_ripple);
        tvTimeFast = findViewById(R.id.tv_time);

        View oneView = findViewById(R.id.ripple_one);
        View twoView = findViewById(R.id.ripple_two);
        View threeView = findViewById(R.id.ripple_three);

        int position = getArcPosition();
        int fastRippleRes = position == POSITION_LEFT ? R.drawable.rm_ic_ripple_right : R.drawable.rm_ic_ripple_left;

        rippleTwoView = twoView;
        rippleTwoView.setAlpha(0);
        rippleTwoView.setBackgroundResource(fastRippleRes);
        rippleOneView = position == POSITION_LEFT ? oneView : threeView;
        rippleOneView.setAlpha(0);
        rippleOneView.setBackgroundResource(fastRippleRes);
        rippleThreeView = position == POSITION_RIGHT ? oneView : threeView;
        rippleThreeView.setAlpha(0);
        rippleThreeView.setBackgroundResource(fastRippleRes);

        rippleView = new RippleView(mContext);
        addView(rippleView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled() || mOnRippleListener == null) return false;
        onTouchFastForward(event);
        return isFast;
    }

    private void onTouchFastForward(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handlerUp(event);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
    }

    private void handlerUp(MotionEvent event) {

        if (System.currentTimeMillis() - startTime < MAX_DURATION) {
            isFast = true;

            removeCallbacks(mFastForwardRunnable);
            postDelayed(mFastForwardRunnable, MAX_DURATION);

            timeFast = timeFast + 10;

            tvTimeFast.setText(timeFast + " " + mContext.getString(R.string.time_fast_ripple));
            tvTimeFast.setVisibility(VISIBLE);

            mOnRippleListener.onFastRipple(timeFast, this);

            animFastRipple();
            setBackground();

            rippleView.animateRipple(event);
        }
        startTime = System.currentTimeMillis();
    }

    private void setBackground() {
        if (isSetBackground) return;
        bgRippleView.setBackgroundColor(Color.parseColor(COLOR_FAST));
        isSetBackground = true;
    }

    private Runnable mFastForwardRunnable = new Runnable() {
        @Override
        public void run() {
            if (mOnRippleListener == null || tvTimeFast == null) return;

            removeCallbacks(hideRunnable);
            postDelayed(hideRunnable, MAX_DURATION);

            mOnRippleListener.onEndRipple(RippleLayout.this);

            isFast = false;

            timeFast = 0;
            startTime = 0;
        }
    };

    private Runnable hideRunnable = new Runnable() {
        @Override
        public void run() {
            if (mOnRippleListener == null || tvTimeFast == null) return;

            isSetBackground = false;
            bgRippleView.setBackgroundColor(Color.parseColor(COLOR_NORMAL));
            tvTimeFast.setVisibility(INVISIBLE);
        }
    };

    private void animFastRipple() {
        if (animationRunning) return;
        animationRunning = true;

        animRipple(true, rippleOneView, 0f);
        animRipple(true, rippleTwoView, 0.5f);
        animRipple(true, rippleThreeView, 1f);
    }

    private void animRipple(final boolean isFadeIn, final View view, final float percentDuration) {
        view.animate()
                .alpha(isFadeIn ? 1f : 0f)
                .setDuration(FAST_RIPPLE_DURATION)
                .setStartDelay((long) (FAST_RIPPLE_DURATION * percentDuration))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (isFadeIn) {
                            animRipple(false, view, 0f);
                        } else {
                            if (view == rippleThreeView) {
                                animationRunning = false;
                            }
                        }
                    }
                })
                .start();
    }

    public void setOnRippleListener(OnRippleListener onRippleListener) {
        mOnRippleListener = onRippleListener;
    }

    public interface OnRippleListener {
        void onFastRipple(long timeFast, RippleLayout rippleLayout);

        void onEndRipple(RippleLayout rippleLayout);
    }
}
