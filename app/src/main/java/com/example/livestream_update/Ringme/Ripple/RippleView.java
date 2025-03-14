package com.example.livestream_update.Ringme.Ripple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;


/**
 * thư viện này thuộc bản quyền của hoanganhtuan95ptit@gmail.com
 * Mọi sử dụng đều phải được sự chấp thuận của nhà cung cấp
 */
final class RippleView extends View {

    private static final long TIME_DURATION = 60L;

    private Paint paint;

    private long time = 0;

    private int size = 0;

    private float radio = 1;
    private float x = 0;
    private float y = 0;

    public RippleView(Context context) {
        this(context, null);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setClickable(false);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#FFFFFFFF"));
        paint.setAlpha(10);
    }

    public void animateRipple(final MotionEvent event) {
        x = event.getX();
        y = event.getY();
        time = 10;
        removeCallbacks(drawRunnable);
        post(drawRunnable);
    }

    private Runnable drawRunnable = new Runnable() {
        @Override
        public void run() {
            setVisibility(VISIBLE);
            if (time > TIME_DURATION) {
                setVisibility(INVISIBLE);
                return;
            }
            if (size == 0)
                size = Math.max(getHeight(), getWidth());

            float percent = (float) time / TIME_DURATION;

            radio = size * percent;

            time = time + 1;

            invalidate();
            postDelayed(this, 5);
        }
    };

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawCircle(x, y, radio, paint);
    }

}