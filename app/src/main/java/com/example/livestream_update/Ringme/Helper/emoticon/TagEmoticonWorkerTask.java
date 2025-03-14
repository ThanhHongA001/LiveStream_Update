package com.example.livestream_update.Ringme.Helper.emoticon;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.vtm.R;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.customview.EmoTagTextViewListChat;
import com.vtm.ringme.customview.TagTextView;
import com.vtm.ringme.helper.TextHelper;
import com.vtm.ringme.model.PhoneNumber;
import com.vtm.ringme.model.ReengMessage;
import com.vtm.ringme.model.TagRingMe;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.utils.Utilities;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by thanhnt72 on 4/6/2016.
 */
public class TagEmoticonWorkerTask extends AsyncTask<Object, Void, Spanned> {

    public static int TEXT_FROM_ONMEDIA = 0;
    public static int TEXT_FROM_MESSAGE = 1;
    public static int TEXT_FROM_THREAD_MESSAGE = 2;

    private static final String TAG = TagEmoticonWorkerTask.class.getSimpleName();
    private WeakReference<TextView> textViewReference;
    private Object object;
    private int textViewId;
    private TagEmoticonManager mEmoticonManager;
    private Html.ImageGetter mImageGetter;
    private TagRingMe.OnClickTag onClickTagListener;
    private ArrayList<TagRingMe> tagRingMes;
    private ApplicationController mContext;
    private String textBold;   //dung de hien thi ten o comment tren feed
    private String myPhone;
    private int typeText = 0;

    public TagEmoticonWorkerTask(TextView textView, TagEmoticonManager emoticonManager,
                                 Html.ImageGetter imageGetter) {
        textViewReference = new WeakReference<>(textView);
        this.mEmoticonManager = emoticonManager;
        this.mImageGetter = imageGetter;
    }

    public TagEmoticonWorkerTask(TextView textView, TagEmoticonManager mEmoticonManager,
                                 Html.ImageGetter mImageGetter, TagRingMe.OnClickTag onClickTagListener,
                                 ArrayList<TagRingMe> tagRingMes, Context context) {
        textViewReference = new WeakReference<>(textView);
        this.mEmoticonManager = mEmoticonManager;
        this.mImageGetter = mImageGetter;
        this.onClickTagListener = onClickTagListener;
        this.tagRingMes = tagRingMes;
        this.mContext = (ApplicationController) context;
        myPhone = mContext.getJidNumber();
    }

    public void setTypeText(int typeText) {
        this.typeText = typeText;
    }

    @Override
    protected Spanned doInBackground(Object... objects) {
        object = objects[0];
        if (object == null) {
            return null;
        }
        String content;
        String contentToTag;
        if (object instanceof String) {
            content = (String) object;
            textViewId = content.hashCode();
        } else if (object instanceof ReengMessage) {
            ReengMessage msg = (ReengMessage) object;
            if (msg.isShowTranslate() && !TextUtils.isEmpty(msg.getTextTranslated())) {
                content = msg.getTextTranslated();
            } else {
                content = msg.getContent();
            }
            textViewId = msg.getId();
        } else {
            return null;
        }
        if (content.isEmpty()) {
            return null;
        }

        Spanned spanned;
        String contentTmp = TextHelper.getSubLongText(textBold, TextHelper.MAX_LENGTH_DEFAULT) + " " + content;
        if (!TextUtils.isEmpty(textBold)) {
            spanned = mEmoticonManager.getSpannedFromEmoticonCache(contentTmp);
        } else {
            if (typeText == TEXT_FROM_THREAD_MESSAGE) {
                spanned = mEmoticonManager.getSpannedFromEmoticonCache(String.valueOf(content.hashCode()));
            } else {
                spanned = mEmoticonManager.getSpannedFromEmoticonCache(content);
            }
        }
        if (spanned != null) {
            return spanned;
        }


        contentToTag = TextHelper.getInstant().escapeXml(content);
        Log.i(TAG, "context tag without emotion escapeXML: " + contentToTag);
        if (tagRingMes != null && !tagRingMes.isEmpty()) {
            contentToTag = paserTag(content);// de ve dang binh thuong de tu no escapexml
            Log.i(TAG, "context tag without emotion: " + contentToTag);
        }
        contentToTag = EmoticonUtils.emoTextToTag(contentToTag);
        if (!TextUtils.isEmpty(textBold)) {
            contentToTag = TextHelper.textBoldWithHTMLWithMaxLength(
                    textBold, TextHelper.MAX_LENGTH_DEFAULT) + " " + contentToTag;
        }
        Log.i(TAG, "context tag emotion escapeXML: " + contentToTag);
        spanned = TextHelper.fromHtml(contentToTag, mImageGetter, null);
        if (tagRingMes != null && !tagRingMes.isEmpty()) {
            Spannable strBuilder = new Spannable.Factory().newSpannable(spanned);
            URLSpan[] urls = strBuilder.getSpans(0, spanned.length(),
                    URLSpan.class);
            for (URLSpan span : urls) {
                makeLinkClickable(strBuilder, span);
            }
            if (!TextUtils.isEmpty(textBold)) {
                Log.i(TAG, "add cache textbold: " + contentTmp);
                mEmoticonManager.addSpannedToEmoticonCache(contentTmp, strBuilder);
            } else {
                if (typeText == TEXT_FROM_THREAD_MESSAGE) {
                    mEmoticonManager.addSpannedToEmoticonCache(String.valueOf(content.hashCode()), strBuilder);
                } else {
                    mEmoticonManager.addSpannedToEmoticonCache(content, strBuilder);
                }
            }
            return strBuilder;
        } else {
            if (!TextUtils.isEmpty(textBold)) {
                Log.i(TAG, "add cache textbold: " + contentTmp);
                mEmoticonManager.addSpannedToEmoticonCache(contentTmp, spanned);
            } else {
                mEmoticonManager.addSpannedToEmoticonCache(content, spanned);
            }
            return spanned;
        }
    }

    @Override
    protected void onPostExecute(Spanned spanned) {
        Log.i(TAG, "textViewReference; " + textViewReference + " spanned: " + spanned);
        if (textViewReference != null && spanned != null) {
            if (textViewReference.get() != null) {
                if (textViewReference.get() instanceof TagTextView) {
                    TagTextView tagTvw = (TagTextView) textViewReference.get();
                    if (tagTvw != null && textViewId == tagTvw.getTextId()) {
                        tagTvw.setMovementMethod(LinkMovementMethod.getInstance());
                        tagTvw.setTextSpanned(spanned);
                    }
                } else if (textViewReference.get() instanceof EmoTagTextViewListChat) {
                    EmoTagTextViewListChat emoTagTvw = (EmoTagTextViewListChat) textViewReference.get();
                    if (emoTagTvw != null && textViewId == emoTagTvw.getTextId()) {
                        emoTagTvw.setTextSpanned(spanned);
                    }
                }
            }
        }
    }

    public String paserTag(String comment) {
        String commentTmp = comment;
        try {
            if (TextUtils.isEmpty(comment))
                return comment;
            for (TagRingMe tag : tagRingMes) {
                Log.i(TAG, "-----tag: " + tag.toString());
                String tagName = tag.getTagName();
                String msisdn = tag.getMsisdn();
                if (!TextUtils.isEmpty(tagName) && !TextUtils.isEmpty(msisdn)) {
                    if (TextUtils.isEmpty(tag.getContactName())) {
                        if (msisdn.equals(myPhone)) {
                            tag.setContactName(mContext.getUserName());
                        } else {
                            PhoneNumber phoneNumber = mContext.getPhoneNumberFromNumber(msisdn);
                            if (phoneNumber != null) {
                                tag.setContactName(phoneNumber.getName());
                            } else {
                                if (!TextUtils.isEmpty(tag.getName())) {
                                    tag.setContactName(tag.getName());
                                } else {
                                    if (!TextUtils.isEmpty(tag.getMsisdn())) {
                                        if (typeText == TEXT_FROM_ONMEDIA) {
                                            tag.setContactName(Utilities.hidenPhoneNumber(tag.getMsisdn()));
                                        } else {
                                            tag.setContactName(tag.getMsisdn());
                                        }
                                    } else {
                                        tag.setContactName("***");
                                    }
                                }
                            }
                        }
                    }

                    int start = comment.indexOf(tagName);
                    if (start != -1) {
                        if (!TextUtils.isEmpty(textBold)) {
                            comment = comment.replaceFirst(Pattern.quote(tagName),
                                    Matcher.quoteReplacement(tag.getContactName()));
                        } else {
                            String href = msisdn + "'>";

                            //<font color=\"#23ca47\">" + aaaa+ "</font>"
                            String linkpaser = "<font color='#FF23ca47'><a href='" + href
                                    + Html.escapeHtml(Matcher.quoteReplacement(tag.getContactName()))
                                    //Ten hien thi
                                    + "</a></font>";

                            comment = comment.replaceFirst(Pattern.quote(tagName), linkpaser);
                        }
                    }
                }
            }
            Log.i(TAG, "-----comment: " + comment);
            return comment;
        } catch (Exception ex) {
            Log.e(TAG, "pasertag er: " + ex);
            return commentTmp;
        }

    }

    protected void makeLinkClickable(final Spannable strBuilder, final URLSpan span) {
        final int start = strBuilder.getSpanStart(span);
        final int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);

        ClickableSpan clickable = new ClickableSpan() {

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ContextCompat.getColor(mContext, R.color.bg_kakoak));
                // ds.bgColor = Color.CYAN;
                // ds.setARGB(255, 255, 255, 255);

                ds.setUnderlineText(false); // set to false to remove underline
            }

            public void onClick(View view) {
                // Do something with span.getURL() to handle the link click...
                String url = span.getURL();
                String name = strBuilder.toString();
                name = name.substring(start, end);
                Log.i(TAG, "url: " + url + " name: " + name);
                if (onClickTagListener != null) {
                    onClickTagListener.OnClickUser(url, name);
                } else {
                    Log.e(TAG, "onClickTagListener null");
                }
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    public void setTextBold(String textBold) {
        this.textBold = textBold;
    }
}
