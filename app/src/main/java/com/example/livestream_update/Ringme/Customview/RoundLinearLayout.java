package com.example.livestream_update.Ringme.Customview;



import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;


public class RoundLinearLayout extends LinearLayout {
    private final RoundViewDelegate delegate;

    public RoundLinearLayout(Context context) {
        this(context, null);
    }

    public RoundLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        delegate = new RoundViewDelegate(this, context, attrs);
    }


    public RoundViewDelegate getDelegate() {
        return delegate;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (delegate.isWidthHeightEqual() && getWidth() > 0 && getHeight() > 0) {
            int max = Math.max(getWidth(), getHeight());
            int measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY);
            super.onMeasure(measureSpec, measureSpec);
            return;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (delegate.isRadiusHalfHeight()) {
            delegate.setCornerRadius(getHeight() / 2);
        } else {
            delegate.setBgSelector();
        }
    }

    public void setBackgroundColorAndPress(int color, int colorPress) {
        delegate.setBackgroundColor(color);
        delegate.setBackgroundPressColor(colorPress);
    }

    public void setBackgroundColorRound(int color) {
        delegate.setBackgroundColor(color);
    }

    public void setStroke(int color, int strokeWidth) {
        delegate.setStrokeColor(color);
        delegate.setStrokeWidth(strokeWidth);
    }

    public void changeBackgroundColor(int color) {
        if (delegate != null) {
            delegate.setBackgroundColor(color);
        }
    }
}
