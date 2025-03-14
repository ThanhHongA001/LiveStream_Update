package com.example.livestream_update.Ringme.Customview;


import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.vtm.ringme.helper.TextHelper;
import com.vtm.ringme.helper.emoticon.EmoticonManager;
import com.vtm.ringme.helper.emoticon.EmoticonWorkerTask;
import com.vtm.ringme.helper.emoticon.TagEmoticonManager;
import com.vtm.ringme.helper.emoticon.TagEmoticonWorkerTask;
import com.vtm.ringme.livestream.listener.SmartTextClickListener;
import com.vtm.ringme.model.TagRingMe;
import com.vtm.ringme.utils.Log;

import java.util.ArrayList;

/**
 * Created by thanhnt72 on 1/9/2018.
 */

public class EmoTagTextViewListChat extends EmoTextViewListChat {

    private static final String TAG = EmoTagTextViewListChat.class.getSimpleName();

    public EmoTagTextViewListChat(Context context) {
        super(context);
//        setLinksClickable(true);
    }

    public EmoTagTextViewListChat(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setLinksClickable(true);
    }

    public EmoTagTextViewListChat(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        setLinksClickable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (getMovementMethod() != null) {
            getMovementMethod().onTouchEvent(this, (Spannable) getText(), event);
        }
        boolean ret = super.onTouchEvent(event);
        return ret;
    }

    public void setText(String text) {
        try {
            if (text != null) {
                text = TextHelper.getInstant().escapeXml(text);
                if (text != null) {
                    Spanned spanned = TextHelper.fromHtml(text);
                    setText(spanned);
                    // test ellipsize
                    resetEllipsize(this, spanned);
                } else {
                    super.setText("");
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "setText", e);
        }
    }

    public void setEmoticonWithTag(Context context, String content, int textId, Object objectParent,
                                   ArrayList<TagRingMe> listTag, TagRingMe.OnClickTag onClickTagListener,
                                   SmartTextClickListener clickListener,
                                   OnLongClickListener longClickListener) {
        if (objectParent == null) {//loi
            return;
        }
        this.mClickListener = clickListener;
        this.mLongClickListener = longClickListener;
        if(listTag == null || listTag.isEmpty()){
            EmoticonManager emoticonManager = EmoticonManager.getInstance(context);
            Spanned spanned = emoticonManager.getSpannedFromEmoticonCache(content);
            if (spanned != null) {
                setTextSpanned(spanned);
            } else {
                setText(content);
                setTextId(textId);
                Html.ImageGetter imageGetter = EmoticonManager.getInstance(context).getImageGetter();
                EmoticonWorkerTask task = new EmoticonWorkerTask(this, emoticonManager, imageGetter, false);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, objectParent);
            }
        } else {
            TagEmoticonManager emoticonManager = TagEmoticonManager.getInstance(context);
            Spanned spanned = emoticonManager.getSpannedFromEmoticonCache(content);
            if (spanned != null) {
                Log.i(TAG, "load from cache: " + content);
                setMovementMethod(SmartLinkMovementMethod.getInstance());
                setTextSpanned(spanned);
//                setText(spanned, BufferType.SPANNABLE);
            } else {
                Log.i(TAG, "execute TagEmoticonWorkerTask: " + content);
                setText(content);
                setTextId(textId);
                Html.ImageGetter imageGetter = TagEmoticonManager.getInstance(context).getImageGetter();
                TagEmoticonWorkerTask task = new TagEmoticonWorkerTask(this, emoticonManager, imageGetter,
                        onClickTagListener, listTag, context);
                task.setTypeText(TagEmoticonWorkerTask.TEXT_FROM_MESSAGE);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, objectParent);
            }
        }
    }

}
