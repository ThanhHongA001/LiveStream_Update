package com.example.livestream_update.Ringme.Customview;


import android.content.Context;
import android.os.AsyncTask;
import android.text.Html.ImageGetter;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.vtm.R;
import com.vtm.ringme.helper.emoticon.EmoticonManager;
import com.vtm.ringme.helper.emoticon.EmoticonWorkerTask;
import com.vtm.ringme.livestream.listener.SmartTextClickListener;
import com.vtm.ringme.model.ReengMessage;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.values.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by toanvk2 on 7/12/14.
 */
public class EmoTextViewListChat extends AppCompatTextView implements View.OnLongClickListener {
    private static final String TAG = EmoTextViewListChat.class.getSimpleName();
    private Context mContext;
    private ImageGetter imageGetter;
    public OnLongClickListener mLongClickListener;
    public SmartTextClickListener mClickListener;
    private boolean isLongClick;
    private int textId = -1;


    //    String regex = "^(mocha)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    private String regex = "\\b(kakoak)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    private Pattern patternMocha = Pattern.compile(regex);

    public EmoTextViewListChat(Context context) {
        super(context);
        this.mContext = context;
        setLinksClickable(true);
        this.setOnLongClickListener(this);
    }

    public EmoTextViewListChat(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setLinksClickable(true);
        this.setOnLongClickListener(this);
    }

    public EmoTextViewListChat(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        setLinksClickable(true);
        this.setOnLongClickListener(this);
    }

    public int getTextId() {
        return textId;
    }

    public void setTextId(int textId) {
        this.textId = textId;
    }

    @Override
    public boolean onLongClick(View view) {
        Log.i(TAG, "onLongClick");
        isLongClick = true;
        if (mLongClickListener != null) {
            mLongClickListener.onLongClick(view);
        }
        return true;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

    public void setEmoticon(String content, int textId, SmartTextClickListener clickListener,
                            OnLongClickListener longClickListener, ReengMessage objectMessage) {
        this.mLongClickListener = longClickListener;
        this.mClickListener = clickListener;
        if (objectMessage == null) {//loi
            return;
        }
        EmoticonManager emoticonManager = EmoticonManager.getInstance(mContext);
        Spanned spanned = emoticonManager.getSpannedFromEmoticonCache(content);
        if (spanned != null) {
            setTextSpanned(spanned);
        } else {
            setText(content);
            //setText("");
            setTextId(textId);
            ImageGetter imageGetter = EmoticonManager.getInstance(mContext).getImageGetter();
            EmoticonWorkerTask task = new EmoticonWorkerTask(this, emoticonManager, imageGetter, true);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, objectMessage);
        }
    }

    public void setNormalText(String content, SmartTextClickListener clickListener,
                              OnLongClickListener longClickListener) {
        this.mLongClickListener = longClickListener;
        this.mClickListener = clickListener;
        setText(content);
    }

    public void setTextSpanned(Spanned spanned) {
        SpannableString linkableText = new SpannableString(spanned);
        detectlink(Patterns.PHONE, linkableText, Constants.SMART_TEXT.MAX_LENGTH_NUMBER, Constants.SMART_TEXT.TYPE_NUMBER);
        detectlink(Patterns.EMAIL_ADDRESS, linkableText, Constants.SMART_TEXT.MAX_LENGTH_EMAIL, Constants.SMART_TEXT.TYPE_EMAIL);
        detectlink(Patterns.WEB_URL, linkableText, Constants.SMART_TEXT.MAX_LENGTH_URL, Constants.SMART_TEXT.TYPE_URL);
        detectlink(patternMocha, linkableText, Constants.SMART_TEXT.MAX_LENGTH_URL, Constants.SMART_TEXT.TYPE_KAKOAK);
        setText(linkableText, BufferType.SPANNABLE);
        setMovementMethod(SmartLinkMovementMethod.getInstance());
//        Log.i(TAG, "setTextSpanned: " + spanned.toString());
        resetEllipsize(this, spanned);
    }

    protected void resetEllipsize(EmoTextViewListChat textView, Spanned spanned) {
        if (textView.getLayout() != null) {
            int ellipsisStart = textView.getLayout().getEllipsisStart(0);
            // int ellipsisWidth = textView.getLayout().getEllipsizedWidth();
            if (ellipsisStart > 0) {
                int end = Math.min(ellipsisStart, spanned.length());
                CharSequence subSpanned = spanned.subSequence(0, end);
                textView.setText(subSpanned);
                textView.append("...");
            }
        }
    }

    /**
     * emotext chat detail, smart text....long click
     */
    private void detectlink(Pattern pattern, SpannableString linkableText, int minLength,
                            int type) {
        Matcher m = pattern.matcher(linkableText);
        CharSequence temp;
        while (m.find()) {
            temp = linkableText.subSequence(m.start(), m.end());
            if (temp.length() >= minLength) {
                linkableText.setSpan(new SmartLinkClickSpan(m.group(), type), m.start(),
                        m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }


    private class SmartLinkClickSpan extends ClickableSpan {
        String keyword;
        int type;
        private long lastClick;

        public SmartLinkClickSpan(String keyString, int type) {
            this.keyword = keyString;
            this.type = type;
        }

        @Override
        public void onClick(View view) {
            if (lastClick == 0) {
                lastClick = System.currentTimeMillis();
            } else {
                long deltaTime = System.currentTimeMillis() - lastClick;
                Log.i(TAG, "deltaTime: " + deltaTime);
                lastClick = 0;
                if (deltaTime < 300) {
                    return;
                }
            }
            Log.i(TAG, "onclick SmartLinkClickSpan: " + isLongClick);
            if (!isLongClick) {
                if (mClickListener != null) {
                    mClickListener.onSmartTextClick(keyword, type);
                } else {
                    Log.i(TAG, "mClickListener null");
                }
            } else {
                isLongClick = false;
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(ContextCompat.getColor(mContext, R.color.bg_kakoak));
            ds.setUnderlineText(true);
        }

        public String getContext() {
            return keyword;
        }

        @Override
        public CharacterStyle getUnderlying() {
            return super.getUnderlying();
        }
    }

    public class SmartLinkMovementMethod extends LinkMovementMethod {

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            Log.i(TAG, "onTouchEvent SmartLinkMovementMethod: " + buffer.toString());
            int action = event.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();
                x += widget.getScrollX();
                y += widget.getScrollY();
                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);
                ClickableSpan[] link = buffer.getSpans(off, off,
                        ClickableSpan.class);
                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        Log.i(TAG, "MotionEvent.ACTION_UP");
                        link[0].onClick(widget);
                    } else if (action == MotionEvent.ACTION_DOWN) {
                        Log.i(TAG, "MotionEvent.ACTION_DOWN");
                        Selection.setSelection(buffer,
                                buffer.getSpanStart(link[0]),
                                buffer.getSpanEnd(link[0]));
                    }
                    return true;
                } else {
                    Log.i(TAG, "link length = 0");
                    Selection.removeSelection(buffer);
                    Touch.onTouchEvent(widget, buffer, event);
                    return false;
                }
            }
            return Touch.onTouchEvent(widget, buffer, event);
        }
    }

    @Override
    public boolean hasFocusable() {
        return false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
