package com.example.livestream_update.Ringme.Customview;

/*
 * Copyright (c) https://bigzun.blogspot.com/
 * Email: bigzun.com@gmail.com
 * Created by namnh40 on 2020/2/27
 *
 */


import android.content.Context;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.vtm.R;
import com.vtm.ringme.livestream.listener.SmartTextClickListener;


public class SmartLinkClickSpan extends ClickableSpan {
    private final Context context;
    private final String keyword;
    private final int type;
    private long lastClick;
    private boolean clickable = true;
    private SmartTextClickListener listener;

    public SmartLinkClickSpan(Context context, String keyString, int type) {
        this.context = context;
        this.keyword = keyString;
        this.type = type;
    }

    public SmartLinkClickSpan setListener(SmartTextClickListener listener) {
        this.listener = listener;
        return this;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    @Override
    public void onClick(View view) {
        if (clickable) {
            if (lastClick == 0) {
                lastClick = System.currentTimeMillis();
            } else {
                long deltaTime = System.currentTimeMillis() - lastClick;
                lastClick = 0;
                if (deltaTime < 300) {
                    return;
                }
            }
            if (listener != null) {
                listener.onSmartTextClick(keyword, type);
            }
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        if (context != null) {
            ds.setColor(ContextCompat.getColor(context, R.color.bg_kakoak));
        }
        ds.setUnderlineText(true);
    }

    public String getContent() {
        return keyword;
    }

    @Override
    public CharacterStyle getUnderlying() {
        return super.getUnderlying();
    }
}
