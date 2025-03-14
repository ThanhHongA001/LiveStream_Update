package com.example.livestream_update.Ringme.ImageView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.vtm.R;


public class AspectImageView extends androidx.appcompat.widget.AppCompatImageView {

    public static final float DEFAULT_ASPECT = 1f;

    private static final int VERTICAL = 0;
    private static final int HORIZONTAL = 1;

    private float aspect = DEFAULT_ASPECT;

    public AspectImageView(Context context) {
        super(context);
    }

    public AspectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray arr = context.obtainStyledAttributes(attrs, new int[] { R.attr.aspect });
        aspect = arr.getFloat(0, aspect);
        arr.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            height = calculate(width, aspect, VERTICAL);
        } else if (heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST) {
            width = calculate(height, aspect, HORIZONTAL);
        } else {
            throw new IllegalArgumentException("Either width or height should have exact value");
        }

        int specWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int specHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(specWidth, specHeight);
    }

    private int calculate(int size, float aspect, int direction) {
        int wp = getPaddingLeft() + getPaddingRight();
        int hp = getPaddingTop() + getPaddingBottom();
        return direction == VERTICAL
                ? Math.round((size - wp) / aspect) + hp
                : Math.round((size - hp) * aspect) + wp;
    }

}
