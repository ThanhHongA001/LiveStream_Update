package com.example.livestream_update.Ringme.Customview;


import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.MotionEvent;

import com.vtm.ringme.helper.TextHelper;
import com.vtm.ringme.helper.emoticon.TagEmoticonManager;
import com.vtm.ringme.helper.emoticon.TagEmoticonWorkerTask;
import com.vtm.ringme.livestream.listener.SmartTextClickListener;
import com.vtm.ringme.model.TagRingMe;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.values.Constants;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by thanhnt72 on 4/6/2016.
 */
public class TagTextView extends TextDrawableView {
    private static final String TAG = TagTextView.class.getSimpleName();
    private int textId = -1;
    //    private String regex = "\\b(mocha|mcinsider)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
//    private Pattern patternMocha = Pattern.compile(regex);
    private SmartTextClickListener smartTextClickListener = null;

    public TagTextView(Context context) {
        super(context);
    }

    public TagTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setEmoticon(Context context, String content, int textId, Object objectParent) {
        if (objectParent == null) {//loi
            return;
        }
        TagEmoticonManager emoticonManager = TagEmoticonManager.getInstance(context);
        Spanned spanned = emoticonManager.getSpannedFromEmoticonCache(content);
        if (spanned != null) {
            setTextSpanned(spanned);
        } else {
            setText(content);
            setTextId(textId);
            Html.ImageGetter imageGetter = TagEmoticonManager.getInstance(context).getImageGetter();
            TagEmoticonWorkerTask task = new TagEmoticonWorkerTask(this, emoticonManager, imageGetter);
            task.setTypeText(TagEmoticonWorkerTask.TEXT_FROM_ONMEDIA);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, objectParent);
        }
    }

    public void setTextSpanned(Spanned spanned) {
        try {
            if (spanned == null) {
                super.setText("");
            } else {
                if (smartTextClickListener == null) {
                    setText(spanned);
                } else {
                    SpannableString linkableText = new SpannableString(spanned);
                    Context context = getContext();
                    detectLink(context, Patterns.WEB_URL, linkableText, Constants.SMART_TEXT.MAX_LENGTH_URL, Constants.SMART_TEXT.TYPE_URL, smartTextClickListener);
                    setText(linkableText, BufferType.SPANNABLE);
                    setMovementMethod(EmoTextViewListChat.SmartLinkMovementMethod.getInstance());
                }
                resetEllipsize(this, spanned);
            }
        } catch (Exception e) {
            Log.e(TAG, "setTextSpanned", e);
        }
    }

    private static void detectLink(Context context, Pattern pattern, SpannableString linkableText
            , int minLength, int type, SmartTextClickListener smartTextClickListener) {
        Matcher m = pattern.matcher(linkableText);
        CharSequence temp;
        while (m.find()) {
            temp = linkableText.subSequence(m.start(), m.end());
            if (temp.length() >= minLength) {
                linkableText.setSpan(new SmartLinkClickSpan(context, m.group(), type).setListener(smartTextClickListener)
                        , m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }


    public void setEmoticon(Context context, String content, int textId, Object objectParent, SmartTextClickListener smartTextClickListener) {
        this.smartTextClickListener = smartTextClickListener;
        setEmoticon(context, content, textId, objectParent);
    }

    public void setEmoticonWithTag(Context context, String content, int textId, Object objectParent
            , ArrayList<TagRingMe> listTag, TagRingMe.OnClickTag onClickTagListener, SmartTextClickListener smartTextClickListener) {
        this.smartTextClickListener = smartTextClickListener;
        setEmoticonWithTag(context, content, textId, objectParent, listTag, onClickTagListener);
    }

    public void setEmoticonWithTag(Context context, String content, int textId, Object objectParent
            , ArrayList<TagRingMe> listTag, TagRingMe.OnClickTag onClickTagListener) {
        if (objectParent == null) {//loi
            return;
        }
        TagEmoticonManager emoticonManager = TagEmoticonManager.getInstance(context);
        Spanned spanned = emoticonManager.getSpannedFromEmoticonCache(content);
        if (spanned != null) {
            Log.i(TAG, "load from cache: " + content);
            setMovementMethod(LinkMovementMethod.getInstance());
            setTextSpanned(spanned);
        } else {
            setText(content);
            setTextId(textId);
            Html.ImageGetter imageGetter = TagEmoticonManager.getInstance(context).getImageGetter();
            TagEmoticonWorkerTask task = new TagEmoticonWorkerTask(this, emoticonManager, imageGetter,
                    onClickTagListener, listTag, context);
            task.setTypeText(TagEmoticonWorkerTask.TEXT_FROM_ONMEDIA);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, objectParent);
        }
    }

    public int getTextId() {
        return textId;
    }

    public void setTextId(int tag) {
        this.textId = tag;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (getMovementMethod() != null) {
            getMovementMethod().onTouchEvent(this, (Spannable) getText(), event);
        }
        boolean ret = super.onTouchEvent(event);
        return ret;
    }

    @Override
    protected void onDetachedFromWindow() {
        textId = 0;
        super.onDetachedFromWindow();
    }

    public void setText(String text) {
        try {
            if (text != null) {
                text = TextHelper.getInstant().escapeXml(text);
            }

            if (text == null) {
                super.setText("");
            } else {
                Spanned spanned = TextHelper.fromHtml(text);
                setText(spanned);
                // test ellipsize
                resetEllipsize(this, spanned);
            }
        } catch (Exception e) {
            Log.e(TAG, "setText", e);
        }
    }





    private void resetEllipsize(TagTextView textView, Spanned spanned) {
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
}
