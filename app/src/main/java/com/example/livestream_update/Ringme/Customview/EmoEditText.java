package com.example.livestream_update.Ringme.Customview;


import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.vtm.ringme.helper.TextHelper;
import com.vtm.ringme.helper.emoticon.EmoticonManager;
import com.vtm.ringme.helper.emoticon.EmoticonUtils;
import com.vtm.ringme.utils.Log;

public class EmoEditText extends AppCompatEditText implements TextWatcher {
    private static final String TAG = EmoEditText.class.getSimpleName();
    private static final int maxLengthEmoTag = 15;
    private final Context mContext;
    private boolean isEmotifySpannable = false;
    private int lastSelection = 0;
    private StringBuilder buffer;
    /*private boolean isPaste = false;
    private int posCharDelete = -1;*/

    public EmoEditText(Context context) {
        super(context);
        this.mContext = context;
        addTextChangedListener(this);
    }

    public EmoEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        addTextChangedListener(this);
    }

    public EmoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        addTextChangedListener(this);
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        // Log.d(TAG, "onTextChanged: " + text + " start: " + start + " lengthBefore: " + lengthBefore + " lengthAfter: " + lengthAfter);
        /*Editable buffer = getText();
        int start1 = Selection.getSelectionStart(buffer);
        int end1 = Selection.getSelectionEnd(buffer);
        Log.d(TAG, "onTextChanged: " + text + " start1: " + start1 + " end1: " + end1);*/
       /* if (!isPaste) {
            if (lengthBefore == 0 && lengthAfter == 2) {
                posCharDelete = start;
            }
        } else {
            isPaste = false;
        }*/
        //Editable buffer = editText.getText();
        // If the cursor is at the end of a RecipientSpan then remove the whole span
                /*int start = Selection.getSelectionStart(buffer);
                int end = Selection.getSelectionEnd(buffer);
                if (start == end) {
                    ImageSpan[] link = buffer.getSpans(start, end, ImageSpan.class);
                    if (link.length > 0) {
                        buffer.replace(
                                buffer.getSpanStart(link[0]),
                                buffer.getSpanEnd(link[0]),
                                ""
                        );
                        buffer.removeSpan(link[0]);
                    }
                }*/
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        //Log.d(TAG, "beforeTextChanged: " + charSequence + " start: " + start + " count: " + count + " lengthAfter: " + after);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // Log.d(TAG, "afterTextChanged: " + editable);
        // removeTextChangedListener(this);
        if (!isEmotifySpannable) {
            emotifySpannable(editable);
        }
        //addTextChangedListener(this);
    }

    /**
     * Work through the contents of the string, and replace any occurrences of [icon] with the imageSpan
     *
     * @param editable
     */
    private void test(Editable editable) {
        // long begin = System.currentTimeMillis();
        isEmotifySpannable = true;
        EmoticonManager mEmoticonManager = EmoticonManager.getInstance(mContext);
        Html.ImageGetter imageGetter = mEmoticonManager.getImageGetter();
        String rawText = EmoticonUtils.getRawTextFromSpan(editable);
        Spanned spanned = mEmoticonManager.getSpannedFromEmoticonCache(rawText);
        if (spanned != null) {
            setText(spanned);
        } else {
            String contentToTag = TextHelper.getInstant().escapeXml(rawText);
            contentToTag = EmoticonUtils.emoTextToTag(contentToTag);
            spanned = TextHelper.fromHtml(contentToTag, imageGetter, null);
            editable.replace(0, editable.length() - 1, spanned);
        }
        //Log.d(TAG, "take : " + (System.currentTimeMillis() - begin));
        isEmotifySpannable = false;
    }

    private void emotifySpannable(Editable editable) {
        isEmotifySpannable = true;
        int length = editable.length();
        if (length <= 0) {
            isEmotifySpannable = false;
            return;
        }
        int position = 0;
        int tagStartPosition = 0;
        int tagLength = 0;
        int currentSelection = getSelectionStart();
        boolean inTagNormal = false;
        boolean inTagSpecial = false;
        Spanned spanned;
        Character cr;
        // SpannableStringBuilder spannableSb = new SpannableStringBuilder();
        long beginTime = System.currentTimeMillis();
        if (lastSelection > maxLengthEmoTag && lastSelection <= currentSelection) {
            position = lastSelection - maxLengthEmoTag;
        } else if (currentSelection < lastSelection && currentSelection > maxLengthEmoTag) {
            position = currentSelection - maxLengthEmoTag;
        }
        if (editable.length() <= position) position = 0;
        do {
            //c = editable.subSequence(position, position + 1).toString();
            cr = editable.charAt(position);
            if (cr == '[') {
                buffer = new StringBuilder();
                tagStartPosition = position;
                inTagNormal = true;
                tagLength = 0;
            } else if (cr == ':' || cr == ';' || cr == '<') {
                buffer = new StringBuilder();
                tagStartPosition = position;
                inTagSpecial = true;
                tagLength = 0;
            }
            if (inTagNormal || inTagSpecial) {
                if ((int) cr == 65532) {
                    inTagNormal = false;
                    inTagSpecial = false;
                    buffer = new StringBuilder();
                } else {
                    buffer.append(cr);
                    tagLength++;
                    if ((inTagSpecial && tagLength >= 2) || (inTagNormal && cr == ']')) {
                        String input = buffer.toString();
                        long time1 = System.currentTimeMillis();
                        String tag = EmoticonUtils.getExistEmoTagFromText(input);
                        Log.d(TAG, "getExistEmoTagFromText:take -> " + (System.currentTimeMillis() - time1));
                        if (!TextUtils.isEmpty(tag)) {
                            int tagEnd = tagStartPosition + tagLength;
                            spanned = EmoticonUtils.getSpannedFromTag(getContext(), tag, getTextSize());
                            time1 = System.currentTimeMillis();
                            editable.replace(tagStartPosition, tagEnd, spanned);
                            //editable.setSpan(spanned, tagStartPosition, tagEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                           /* editable.delete(tagStartPosition, tagEnd);
                            editable.insert(tagStartPosition, spanned);*/
                            Log.d(TAG, "edittable replace :take -> " + (System.currentTimeMillis() - time1));
                            position = tagStartPosition;
                            length = editable.length();
                            inTagNormal = false;
                            inTagSpecial = false;
                        } else {
                            if (inTagSpecial && tagLength > 3) {// emo dac biet <=3 kt
                                inTagSpecial = false;
                            } else if (inTagNormal && (tagLength > maxLengthEmoTag || cr == ']')) {
                                inTagNormal = false;
                            }
                        }
                    }
                }
            }
            position++;
        } while (position < length);
       /* if ((int) editable.charAt(length - 1) == 65532 && (cr >= 'A' && cr <= 'Z' || cr >= 'a' && cr <= 'z')) {
            posCharDelete = length;
        }*/
        lastSelection = getSelectionStart();
        isEmotifySpannable = false;
        Log.d(TAG, "end--emotifySpannable:-> " + (System.currentTimeMillis() - beginTime));
    }

    /**
     * context menu
     */
    @Override
    public boolean onTextContextMenuItem(int id) {
        boolean returnValue;
        switch (id) {
            case android.R.id.cut:
                String contentCut = getRawContentFromSpannable();
                returnValue = super.onTextContextMenuItem(id);
                TextHelper.copyToClipboard(mContext, contentCut);
                //copyContentToClipboard(true);
                break;
            case android.R.id.copy:
                String contentCopy = getRawContentFromSpannable();
                returnValue = super.onTextContextMenuItem(id);
                TextHelper.copyToClipboard(mContext, contentCopy);
                //copyContentToClipboard(false);
                break;
            case android.R.id.paste:
                returnValue = super.onTextContextMenuItem(id);
                break;
            default:
                returnValue = super.onTextContextMenuItem(id);
                break;
        }
        return returnValue;
    }

    /**
     * copy text
     *
     * @param isCut
     */
    private void copyContentToClipboard(boolean isCut) {
        if (isFocused()) {
            int selStart = getSelectionStart();
            int selEnd = getSelectionEnd();
            int min = Math.max(0, Math.min(selStart, selEnd));
            int max = Math.max(0, Math.max(selStart, selEnd));
            Spannable spannable = (Spannable) this.getText().subSequence(min, max);
            if (spannable != null) {
                TextHelper.copyToClipboard(mContext, EmoticonUtils.getRawTextFromSpan(spannable));
            }
            if (isCut) {
                this.getText().delete(min, max);
            }
            // Log.d(TAG, "selected: " + spannable + " min: " + min + " max: " + max);
        }
        Selection.setSelection(this.getText(), getSelectionEnd());
        //stopTextSelectionMode();
    }

    private String getRawContentFromSpannable() {
        int min = 0;
        int max = this.length();
        if (isFocused()) {
            int selStart = getSelectionStart();
            int selEnd = getSelectionEnd();
            min = Math.max(0, Math.min(selStart, selEnd));
            max = Math.max(0, Math.max(selStart, selEnd));
        }
        Spannable spannable = (Spannable) this.getText().subSequence(min, max);
        if (spannable != null) {
            return EmoticonUtils.getRawTextFromSpan(spannable);
        } else {
            return "";
        }
    }
}