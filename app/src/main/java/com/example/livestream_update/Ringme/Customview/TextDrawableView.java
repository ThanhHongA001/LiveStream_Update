package com.example.livestream_update.Ringme.Customview;

/*
 * Copyright (c) https://bigzun.blogspot.com/
 * Email: bigzun.com@gmail.com
 * Created by admin on 2020/5/5
 *
 */


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.vtm.R;
import com.vtm.ringme.BuildConfig;


public class TextDrawableView extends AppCompatTextView {

    public TextDrawableView(Context context) {
        super(context);
    }

    public TextDrawableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, android.R.attr.textViewStyle);
    }

    public TextDrawableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @SuppressLint("CustomViewStyleable")
    private void init(AttributeSet attrs, int defStyleAttr) {
        if (attrs != null && (!isInEditMode() || BuildConfig.DEBUG)) {
            Drawable drawableStart = null;
            Drawable drawableEnd = null;
            Drawable drawableTop = null;
            Drawable drawableBottom = null;
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TextDrawable, defStyleAttr, 0);
            Resources res = getResources();
            int resourceId;
            if (res != null) {
                resourceId = ta.getResourceId(R.styleable.TextDrawable_drawable_start, -1);
                if (resourceId > 0) drawableStart = res.getDrawable(resourceId);
                resourceId = ta.getResourceId(R.styleable.TextDrawable_drawable_end, -1);
                if (resourceId > 0) drawableEnd = res.getDrawable(resourceId);
                resourceId = ta.getResourceId(R.styleable.TextDrawable_drawable_top, -1);
                if (resourceId > 0) drawableTop = res.getDrawable(resourceId);
                resourceId = ta.getResourceId(R.styleable.TextDrawable_drawable_bottom, -1);
                if (resourceId > 0) drawableBottom = res.getDrawable(resourceId);
            }
            ta.recycle();
            if (drawableStart != null || drawableTop != null || drawableEnd != null || drawableBottom != null)
                setCompoundDrawablesRelativeWithIntrinsicBounds(drawableStart, drawableTop, drawableEnd, drawableBottom);
        }
    }

}
