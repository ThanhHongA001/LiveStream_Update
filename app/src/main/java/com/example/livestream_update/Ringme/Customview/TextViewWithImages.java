package com.example.livestream_update.Ringme.Customview;


import android.content.Context;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;

import com.vtm.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TextViewWithImages extends BaseTextView {
    boolean isHaveIcon = false;
    static String textHot = "ĐỪNG LỠ";

    public TextViewWithImages(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TextViewWithImages(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewWithImages(Context context) {
        super(context);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (isHaveIcon || text != null && text.toString() != null && text.toString().toUpperCase().startsWith(textHot)) {
            Spannable s = getTextWithImages(getContext(), text);
            super.setText(s, BufferType.SPANNABLE);
        } else
            super.setText(text, type);
    }

    private static final Spannable.Factory spannableFactory = Spannable.Factory.getInstance();

    private static boolean addImages(Context context, Spannable spannable) {
        boolean hasChanges = false;
        try {
            Pattern refImg = Pattern.compile("\\Q[img src=\\E([a-zA-Z0-9_]+?)\\Q/]\\E");

//        BackgroundColorSpan[] spans=spannable.getSpans(0,
//                spannable.length(),
//                BackgroundColorSpan.class);
//        for (BackgroundColorSpan span : spans) {
//            spannable.removeSpan(span);
//        }
//
            try {
                ImageSpan[] spans = spannable.getSpans(0,
                        spannable.length(),
                        ImageSpan.class);
                for (ImageSpan span : spans) {
                    spannable.removeSpan(span);
                }

                int index = TextUtils.indexOf(spannable, textHot);
                while (index >= 0) {
//            spannable.setSpan(new BackgroundColorSpan(Color.RED), index, index
//                    + textHot.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(new VerticalImageSpan(context, R.drawable.rm_ic_dunglo),
                            index,
                            index + textHot.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                    index = TextUtils.indexOf(spannable, textHot, index + textHot.length());
                }
            } catch (IndexOutOfBoundsException ex) {

            } catch (Exception ex) {

            }

            Matcher matcher = refImg.matcher(spannable);
            while (matcher.find()) {
                boolean set = true;
                for (ImageSpan span : spannable.getSpans(matcher.start(), matcher.end(), ImageSpan.class)) {
                    if (spannable.getSpanStart(span) >= matcher.start()
                            && spannable.getSpanEnd(span) <= matcher.end()) {
                        spannable.removeSpan(span);
                    } else {
                        set = false;
                        break;
                    }
                }
                final String resname = spannable.subSequence(matcher.start(1), matcher.end(1)).toString().trim();
                int id = context.getResources().getIdentifier(resname, "drawable", context.getPackageName());
                if (set) {
                    hasChanges = true;
                    spannable.setSpan(new VerticalImageSpan(context, id),
                            matcher.start(),
                            matcher.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                }
            }
        } catch (Exception ex) {

        }
        return hasChanges;
    }

    private static Spannable getTextWithImages(Context context, CharSequence text) {
        if (text == null) return null;
        Spannable spannable = spannableFactory.newSpannable(text);
        addImages(context, spannable);
        return spannable;
    }

    public void setHaveIcon(boolean flag) {
        isHaveIcon = flag;
    }
}