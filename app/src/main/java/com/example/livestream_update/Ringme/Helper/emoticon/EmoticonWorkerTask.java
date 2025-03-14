package com.example.livestream_update.Ringme.Helper.emoticon;

import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.TextView;

import com.vtm.ringme.customview.EllipsisTextView;
import com.vtm.ringme.customview.EmoTextViewListChat;
import com.vtm.ringme.helper.TextHelper;
import com.vtm.ringme.model.PhoneNumber;
import com.vtm.ringme.model.ReengAccount;
import com.vtm.ringme.model.ReengMessage;

import java.lang.ref.WeakReference;

/**
 * Created by toanvk2 on 1/9/15.
 */
public class EmoticonWorkerTask extends AsyncTask<Object, Void, Spanned> {
    private WeakReference<TextView> textViewReference;
    private Object object;
    private int textViewId;
    private boolean isSmartText = false;
    private EmoticonManager mEmoticonManager;
    private EmoticonStatusManager mEmoticonStatusManager;
    private Html.ImageGetter mImageGetter;
    private boolean isTextStatusProfile;

    public EmoticonWorkerTask(TextView textView, EmoticonManager emoticonManager,
                              Html.ImageGetter imageGetter, boolean isSmartText) {
        textViewReference = new WeakReference<>(textView);
        this.mEmoticonManager = emoticonManager;
        this.mImageGetter = imageGetter;
        this.isSmartText = isSmartText;
        this.isTextStatusProfile = false;
    }

    public EmoticonWorkerTask(TextView textView, EmoticonStatusManager emoticonManager,
                              Html.ImageGetter imageGetter, boolean isSmartText, boolean textStatusProfile) {
        textViewReference = new WeakReference<>(textView);
        this.mEmoticonStatusManager = emoticonManager;
        this.mImageGetter = imageGetter;
        this.isSmartText = isSmartText;
        this.isTextStatusProfile = textStatusProfile;
    }

    @Override
    protected Spanned doInBackground(Object... objects) {
        object = objects[0];
        if (object == null) {
            return null;
        }
        String content;
        String contentToTag;
        if (object instanceof PhoneNumber) {
            content = ((PhoneNumber) object).getStatus();
            textViewId = ((PhoneNumber) object).getIdInt();
        } else if (object instanceof ReengAccount) {
            content = ((ReengAccount) object).getStatus();
            textViewId = ((ReengAccount) object).getIdInt();
        } else if (object instanceof ReengMessage) {
            ReengMessage msg = (ReengMessage) object;
            if (msg.isShowTranslate() && !TextUtils.isEmpty(msg.getTextTranslated())) {
                content = msg.getTextTranslated();
            } else {
                content = msg.getContent();
            }
            textViewId = msg.getId();
        } else if (object instanceof String) {
            content = (String) object;
            textViewId = content.hashCode();
        } else {
            return null;
        }
        if (content == null || content.isEmpty()) {
            return null;
        }

        Spanned spanned;
        if (isTextStatusProfile) {
            spanned = mEmoticonStatusManager.getSpannedFromEmoticonCache(content);
        } else {
            spanned = mEmoticonManager.getSpannedFromEmoticonCache(content);
        }
        if (spanned != null) {
            return spanned;
        }
        contentToTag = TextHelper.getInstant().escapeXml(content);
        contentToTag = EmoticonUtils.emoTextToTag(contentToTag);
        if (isTextStatusProfile) {
            contentToTag = "&ldquo;" + contentToTag + "&rdquo;";
            spanned = TextHelper.fromHtml(contentToTag, mImageGetter, null);
            mEmoticonStatusManager.addSpannedToEmoticonCache(content, spanned);
        } else {
            spanned = TextHelper.fromHtml(contentToTag, mImageGetter, null);
            mEmoticonManager.addSpannedToEmoticonCache(content, spanned);
        }
        return spanned;
    }

    @Override
    protected void onPostExecute(Spanned spanned) {
        if (textViewReference != null && spanned != null) {
            if (isSmartText) {
                EmoTextViewListChat textView = (EmoTextViewListChat) textViewReference.get();
                if (textView != null && textViewId == textView.getTextId()) {
                    textView.setTextSpanned(spanned);
                }
            } else {
                EllipsisTextView textView = (EllipsisTextView) textViewReference.get();
                if (textView != null && textViewId == textView.getTextId()) {
                    textView.setTextSpanned(spanned);
                }
                /*ko hieu sao phai check cai textviewid kia????*/
                if (textView != null && isTextStatusProfile) {
                    textView.setTextSpanned(spanned);
                }
            }
        }
    }
}