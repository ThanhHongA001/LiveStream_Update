package com.example.livestream_update.Ringme.Customview;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/** 用于需要圆角矩形框背景的RelativeLayout的情况,减少直接使用RelativeLayout时引入的shape资源文件 */
public class RoundRelativeLayout extends RelativeLayout {
    private final RoundViewDelegate delegate;

    public RoundRelativeLayout(Context context) {
        this(context, null);
    }

    public RoundRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        delegate = new RoundViewDelegate(this, context, attrs);
    }

    /** use delegate to set attr */
    public RoundViewDelegate getDelegate() {
        return delegate;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
        }else {
            delegate.setBgSelector();
        }
    }

    public void setBackgroundColorAndPress(int color, int colorPress) {
        delegate.setBackgroundColor(color);
//        delegate.setBackgroundPressColor(colorPress);
    }

    public void changeBackgroundColor(int color) {
        if (delegate != null) {
            delegate.setBackgroundColor(color);
        }
    }
}

