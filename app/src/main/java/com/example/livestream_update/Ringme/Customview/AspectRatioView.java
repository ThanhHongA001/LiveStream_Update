package com.example.livestream_update.Ringme.Customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.vtm.R;
import com.vtm.ringme.utils.Log;

public class AspectRatioView extends RelativeLayout {
    private float mAspectRatio = 0f;

    public AspectRatioView(Context context) {
        this(context, null, 0);
    }

    public AspectRatioView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public AspectRatioView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RmAspectRatioView, defStyle, 0);

        mAspectRatio = a.getFloat(R.styleable.RmAspectRatioView_aspectRatioView, 0);

        if (mAspectRatio == 0f) {
            Log.e("You must specify an aspect ratio when using the AspectRatioView.");
        }
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;
        if (mAspectRatio != 0) {
            if (widthSize > 0) {
                width = widthSize;
                height = (int) (width / mAspectRatio);
                int exactWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                int exactHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
                super.onMeasure(exactWidthSpec, exactHeightSpec);
            } else {
                height = heightSize;
                width = (int) (height / mAspectRatio);
                int exactWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                int exactHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
                super.onMeasure(exactWidthSpec, exactHeightSpec);
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}

