package com.example.livestream_update.Ringme.Customview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class BaseTextView extends TextDrawableView {
    public BaseTextView(Context context) {
        super(context);
    }

    public BaseTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        try {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        } catch (Exception e) {
            android.util.Log.e("BaseTextView", e.toString());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        try {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                final CharSequence text = getText();
                setText(null);
                setText(text);
            }
            return super.dispatchTouchEvent(event);
        } catch (Exception e) {
            return false;
        }
    }
}
