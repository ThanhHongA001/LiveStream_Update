package com.example.livestream_update.Ringme.TabVideo;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.vtm.R;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.customview.TextDrawableView;
import com.vtm.ringme.values.Constants;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkTextView extends TextDrawableView {

    private static final String TAG = "LinkTextView";
    private static final String regex = "\\b(mocha)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    private static final Pattern patternMocha = Pattern.compile(regex);
    private static final int TYPE_READ_MORE = -1000;
    private static final int LENGTH_READ_MORE_DEFAULT = 80;

    private int mColorLink;
    private int mColorReadMore;
    private int lengthReadMore = LENGTH_READ_MORE_DEFAULT;
    private int readMore = R.string.readMore;

    private OnLinkListener onLinkListener;
    private OnReadMoreListener onReadMoreListener;

    public LinkTextView(Context context) {
        this(context, null);
    }

    public LinkTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinkTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mColorLink = ContextCompat.getColor(context, R.color.bg_kakoak);
        mColorReadMore = ContextCompat.getColor(context, R.color.videoColorNormal);
    }

    public void setColorLink(@ColorRes int color) {
        mColorLink = ContextCompat.getColor(ApplicationController.self(), color);
    }

    public void setColorReadMore(@ColorRes int color) {
        mColorReadMore = ContextCompat.getColor(ApplicationController.self(), color);
    }

    public void setReadMore(@StringRes int readMore) {
        this.readMore = readMore;
    }

    public void setLengthReadMore(int lengthReadMore) {
        this.lengthReadMore = lengthReadMore;
    }

    public void setOnLinkListener(OnLinkListener onLinkListener) {
        this.onLinkListener = onLinkListener;
    }

    public void setOnReadMoreListener(OnReadMoreListener onReadMoreListener) {
        this.onReadMoreListener = onReadMoreListener;
    }

    public void asyncSetText(CharSequence text, boolean isReadMore) {
//        text = text + " https://www.facebook.com/vsa.asia/photos/a.378515382599121/553037501813574/?type=3&theater";
        if (TextUtils.isEmpty(text)) {
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);

        SmartLinkClickSpan readMoreSpan = new SmartLinkClickSpan(getContext().getString(readMore), mColorReadMore, TYPE_READ_MORE);
        readMoreSpan.onReadMoreListener = onReadMoreListener;
        readMoreSpan.onLinkListener = onLinkListener;

        ExecutorService executor = Executors.newFixedThreadPool(5);
        asyncSetText(executor, this, new Handler(), lengthReadMore, text.toString(), isReadMore, mColorLink, readMoreSpan, onReadMoreListener, onLinkListener);
        executor.shutdown();
    }

    private static void asyncSetText(Executor executor, TextView textView, Handler handler, final int lengthReadMore, final String content, final boolean isReadMore, final int colorLink, final SmartLinkClickSpan readMoreClickSpan, final OnReadMoreListener onReadMoreListener, final OnLinkListener onLinkListener) {
        final WeakReference<TextView> textViewRef = new WeakReference<>(textView);
        final WeakReference<Handler> handlerRef = new WeakReference<>(handler);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                TextView textView = textViewRef.get();
                Handler handler = handlerRef.get();
                if (textView == null || handler == null) return;

                SpannableStringBuilder spannableString = new SpannableStringBuilder(content);
                try {
                    if (isReadMore) {
                        spannableString = handlerReadMore(content, lengthReadMore, colorLink, readMoreClickSpan, onReadMoreListener, onLinkListener);
                    } else {
                        spannableString = handlerLink(content, content.length(), colorLink, readMoreClickSpan, onReadMoreListener, onLinkListener);
                    }
                } catch (Exception ignored) {
                }
                final SpannableStringBuilder spannableStringFinal = spannableString;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = textViewRef.get();
                        if (textView == null) return;
                        textView.setText(spannableStringFinal, BufferType.SPANNABLE);
                        textView.setMovementMethod(LinkTouchMovementMethod.getInstance());
                        textView.setHighlightColor(Color.TRANSPARENT);
                    }
                });
            }
        });
    }

    private static SpannableStringBuilder handlerReadMore(CharSequence content, int lengthReadMore, int colorLink, SmartLinkClickSpan readMoreClickSpan, OnReadMoreListener onReadMoreListener, OnLinkListener onLinkListener) {
        if (content.length() < lengthReadMore) lengthReadMore = content.length();
        return handlerLink(content, lengthReadMore, colorLink, readMoreClickSpan, onReadMoreListener, onLinkListener);
    }

    private static SpannableStringBuilder handlerLink(CharSequence content, int lengthReadMore, int colorLink, SmartLinkClickSpan readMoreClickSpan, OnReadMoreListener onReadMoreListener, OnLinkListener onLinkListener) {
        SpannableStringBuilder linkableText = new SpannableStringBuilder(content);
        detectLink(Patterns.WEB_URL, linkableText, Constants.SMART_TEXT.MAX_LENGTH_URL, Constants.SMART_TEXT.TYPE_URL, colorLink, onReadMoreListener, onLinkListener);

        for (int i = lengthReadMore; i < content.length(); i++) {
            if (i == content.length() - 1 || content.charAt(i) == 32 || content.charAt(i) == 13 || content.charAt(i) == 3) {
                lengthReadMore = i;
                break;
            }
        }

        if (lengthReadMore < 0) lengthReadMore = content.length();
        if (lengthReadMore < content.length() - 1) {
            linkableText = new SpannableStringBuilder(linkableText.subSequence(0, lengthReadMore));
            linkableText.append("...");
            linkableText.append(readMoreClickSpan.content);
            linkableText.setSpan(readMoreClickSpan, lengthReadMore + 4, linkableText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return linkableText;
    }

    private static void detectLink(Pattern pattern, SpannableStringBuilder linkableText, int minLength, int type, int colorLink, OnReadMoreListener onReadMoreListener, OnLinkListener onLinkListener) {
        Matcher m = pattern.matcher(linkableText);
        CharSequence temp;
        while (m.find()) {
            temp = linkableText.subSequence(m.start(), m.end());
            if (temp.length() >= minLength) {
                if (type == Constants.SMART_TEXT.TYPE_URL && !URLUtil.isValidUrl(m.group()))
                    continue;
                SmartLinkClickSpan linkClickSpan = new SmartLinkClickSpan(m.group(), colorLink, type);
                linkClickSpan.onReadMoreListener = onReadMoreListener;
                linkClickSpan.onLinkListener = onLinkListener;
                linkableText.setSpan(linkClickSpan, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public static class LinkTouchMovementMethod extends LinkMovementMethod {

        private SmartLinkClickSpan mPressedSpan;

        @Override
        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mPressedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null) {
                    mPressedSpan.isPressed = true;
                    Selection.setSelection(spannable, spannable.getSpanStart(mPressedSpan), spannable.getSpanEnd(mPressedSpan));
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                SmartLinkClickSpan touchedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null && touchedSpan != mPressedSpan) {
                    mPressedSpan.isPressed = false;
                    mPressedSpan = null;
                    Selection.removeSelection(spannable);
                }
            } else {
                if (mPressedSpan != null) {
                    mPressedSpan.isPressed = false;
                    super.onTouchEvent(textView, spannable, event);
                }
                mPressedSpan = null;
                Selection.removeSelection(spannable);
            }
            return true;
        }

        private SmartLinkClickSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {
            int x = (int) event.getX() - textView.getTotalPaddingLeft() + textView.getScrollX();
            int y = (int) event.getY() - textView.getTotalPaddingTop() + textView.getScrollY();

            Layout layout = textView.getLayout();
            int position = layout.getOffsetForHorizontal(layout.getLineForVertical(y), x);

            SmartLinkClickSpan[] link = spannable.getSpans(position, position, SmartLinkClickSpan.class);
            SmartLinkClickSpan touchedSpan = null;
            if (link.length > 0 && positionWithinTag(position, spannable, link[0])) {
                touchedSpan = link[0];
            }

            return touchedSpan;
        }

        private boolean positionWithinTag(int position, Spannable spannable, Object tag) {
            return position >= spannable.getSpanStart(tag) && position <= spannable.getSpanEnd(tag);
        }
    }

    private static class SmartLinkClickSpan extends ClickableSpan {

        String content;

        int colorLinkNormal;
        int colorLinkPressed = 0xFFADADAD;
        int colorBgNormal = 0x00ffffff;
        int colorBgPressed = 0x00ffffff;

        int type;

        boolean isPressed;

        OnReadMoreListener onReadMoreListener;
        OnLinkListener onLinkListener;

        SmartLinkClickSpan(String content, int colorLink, int type) {
            this.content = content;
            this.colorLinkNormal = colorLink;
            this.type = type;
        }

        private long mLastClickTime = 0;

        @Override
        public void onClick(View view) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
            mLastClickTime = SystemClock.elapsedRealtime();
            if (type == TYPE_READ_MORE) {
                if (onReadMoreListener != null) onReadMoreListener.onReadMore();
            } else {
                if (onLinkListener != null) onLinkListener.onLink(content, type);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(isPressed ? colorLinkPressed : colorLinkNormal);
            ds.bgColor = isPressed ? colorBgPressed : colorBgNormal;
            ds.setAntiAlias(true);
            ds.setUnderlineText(false);
            ds.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        }

        public String getContext() {
            return content;
        }

        @Override
        public CharacterStyle getUnderlying() {
            return super.getUnderlying();
        }
    }

    public interface OnLinkListener {
        void onLink(String content, int type);
    }

    public interface OnReadMoreListener {
        void onReadMore();
    }
}

