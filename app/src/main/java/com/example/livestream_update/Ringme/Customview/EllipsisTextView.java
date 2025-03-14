package com.example.livestream_update.Ringme.Customview;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.vtm.ringme.helper.TextHelper;
import com.vtm.ringme.helper.emoticon.EmoticonManager;
import com.vtm.ringme.helper.emoticon.EmoticonStatusManager;
import com.vtm.ringme.helper.emoticon.EmoticonWorkerTask;
import com.vtm.ringme.helper.emoticon.TagEmoticonManager;
import com.vtm.ringme.helper.emoticon.TagEmoticonWorkerTask;
import com.vtm.ringme.model.TagRingMe;
import com.vtm.ringme.utils.Log;

import java.util.ArrayList;

/**
 * Created by toanvk2 on 6/27/14.
 */
public class EllipsisTextView extends TextView {

    private static final String TAG = EllipsisTextView.class.getSimpleName();

    private int textId = -1;

    public EllipsisTextView(Context context) {
        super(context);
    }

    public EllipsisTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EllipsisTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public int getTextId() {
        return textId;
    }

    public void setTextId(int tag) {
        this.textId = tag;
    }

//    @Override
//    public void setOnLongClickListener(OnLongClickListener listener) {
//        this.mLongClickListener = listener;
//        super.setOnLongClickListener(this);
//    }

//    @Override
//    public boolean onLongClick(View view) {
//        isLongClick = true;
//        if (mLongClickListener != null) {
//            mLongClickListener.onLongClick(view);
//        }
//        return false;
//    }

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
            Log.e("EllipsisTextView", "setText", e);
        }
    }

    public void setTextSpanned(Spanned spanned) {
        try {
            if (spanned != null) {
                setText(spanned);
                // test ellipsize
                resetEllipsize(this, spanned);
            } else {
                super.setText("");
            }
        } catch (Exception e) {
            Log.e("EllipsisTextView", "setTextSpanned", e);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    /**
     * gen emoticon and add cache
     *
     * @param context
     * @param content
     * @param textId
     * @param objectParent // khong de truong nay null
     */
    public void setEmoticon(Context context, String content, int textId, Object objectParent) {
        if (objectParent == null) {//loi
            return;
        }
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
    }

    public void setEmoticonStatus(Context context, String content, int textId, Object objectParent) {
        if (objectParent == null) {//loi
            return;
        }
        EmoticonStatusManager emoticonStatusManager = EmoticonStatusManager.getInstance(context);
        Spanned spanned = emoticonStatusManager.getSpannedFromEmoticonCache(content);
        if (spanned != null) {
            setTextSpanned(spanned);
        } else {
            setText(content);
            setTextId(textId);
            Html.ImageGetter imageGetter = EmoticonStatusManager.getInstance(context).getImageGetter();
            EmoticonWorkerTask task = new EmoticonWorkerTask(this, emoticonStatusManager, imageGetter, false, true);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, objectParent);
        }
    }

    public void setEmoticonWithTag(Context context, String content, int textId, Object objectParent, ArrayList<TagRingMe> listTag) {
        if (objectParent == null) {//loi
            return;
        }

        if(listTag == null || listTag.isEmpty()){
            EmoticonManager emoticonManager = EmoticonManager.getInstance(context);
            Spanned spanned = emoticonManager.getSpannedFromEmoticonCache(content);
            if (spanned != null) {
                setTextSpanned(spanned);
            } else {
                try {
                    setText(content);
                    setTextId(textId);
                    Html.ImageGetter imageGetter = EmoticonManager.getInstance(context).getImageGetter();
                    EmoticonWorkerTask task = new EmoticonWorkerTask(this, emoticonManager, imageGetter, false);
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, objectParent);
                }catch (Exception e){
                    setText(content);
                    setTextId(textId);
                    e.printStackTrace();
                }
            }
        } else {
            TagEmoticonManager emoticonManager = TagEmoticonManager.getInstance(context);
            Spanned spanned = emoticonManager.getSpannedFromEmoticonCache(String.valueOf(content.hashCode()));
            Log.i(TAG, "content: " + content);
            if (spanned != null) {
                Log.i(TAG, "load from cache: " );
                setTextSpanned(spanned);
            } else {
                setText(content);
                setTextId(textId);
                Log.i(TAG, "start TagEmoticonWorkerTask" );
                Html.ImageGetter imageGetter = TagEmoticonManager.getInstance(context).getImageGetter();
                TagEmoticonWorkerTask task = new TagEmoticonWorkerTask(this, emoticonManager, imageGetter,
                        null, listTag, context);
                task.setTypeText(TagEmoticonWorkerTask.TEXT_FROM_THREAD_MESSAGE);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, objectParent);
            }
        }


    }

    private void resetEllipsize(EllipsisTextView textView, Spanned spanned) {
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
