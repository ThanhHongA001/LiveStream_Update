package com.example.livestream_update.Ringme.TabVideo;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vtm.R;
import com.vtm.ringme.dialog.PositiveListener;
import com.vtm.ringme.listener.HyperlinkListener;
import com.vtm.ringme.listener.NegativeListener;
import com.vtm.ringme.livestream.listener.DismissListener;
import com.vtm.ringme.utils.Log;


/**
 * Created by toanvk2 on 7/13/2017.
 */

public class DialogConfirm extends Dialog implements View.OnClickListener {
    private AppCompatActivity activity;
    private Object mEntry;
    private String label;
    private String msg;
    private String negativeLabel;
    private String positiveLabel;
    private String checkbox;
    private boolean useHtml = false;
    private NegativeListener<Object> negativeListener;
    private PositiveListener<Object> positiveListener;
    private DismissListener dismissListener;
    private HyperlinkListener hyperLinkListener;

    private Button mBtnNegative, mBtnPositive;
    private TextView mTvwTitle, mTvwMessage;
    private View mViewDivider, mViewVerticalDivider;
    private CheckBox mCheckBox;
    private int buttonColor;
    private boolean forceUpdate = false;

    public DialogConfirm(Context context, boolean isCancelable) {
        super(context, R.style.DialogFullscreen);
        setCancelable(isCancelable);
    }

    public DialogConfirm(AppCompatActivity activity, boolean isCancelable) {
        super(activity, R.style.DialogFullscreen);
        this.activity = activity;
        setCancelable(isCancelable);
    }

    public DialogConfirm setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
        setCancelable(!forceUpdate);
        return this;
    }

    public DialogConfirm setEntry(Object entry) {
        this.mEntry = entry;
        return this;
    }

    public DialogConfirm setLabel(String label) {
        this.label = label;
        return this;
    }

    public DialogConfirm setMessage(String message) {
        this.msg = message;
        return this;
    }

    public DialogConfirm setUseHtml(boolean useHtml) {
        this.useHtml = useHtml;
        return this;
    }

    public DialogConfirm setNegativeLabel(String label) {
        this.negativeLabel = label;
        return this;
    }

    public DialogConfirm setPositiveLabel(String label) {
        this.positiveLabel = label;
        return this;
    }

    public DialogConfirm setCheckBox(String label) {
        this.checkbox = label;
        return this;
    }

    public DialogConfirm setNegativeListener(NegativeListener listener) {
        this.negativeListener = listener;
        return this;
    }

    public DialogConfirm setPositiveListener(PositiveListener<Object> listener) {
        this.positiveListener = listener;
        return this;
    }

    public DialogConfirm setDismissListener(DismissListener listener) {
        this.dismissListener = listener;
        return this;
    }

    public DialogConfirm setHyperLinkListener(HyperlinkListener hyperLinkListener) {
        this.hyperLinkListener = hyperLinkListener;
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rm_dialog_confirm);
        findComponentViews();
        drawDetail();
        setListener();
    }

    @Override
    public void dismiss() {
        Log.d("DialogConfirm", "dismiss");
        super.dismiss();
        if (dismissListener != null) {
            dismissListener.onDismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_confirm_button_negative:
                if (negativeListener != null) {
                    negativeListener.onNegative(mEntry);
                }
                break;
            case R.id.dialog_confirm_button_positive:
                if (positiveListener != null) {
                    positiveListener.onPositive(mEntry);
                }
                break;
        }
        dismiss();
    }

    private void findComponentViews() {
        mTvwTitle = findViewById(R.id.dialog_confirm_label);
        mTvwMessage = findViewById(R.id.dialog_confirm_message);
        mTvwMessage.setMovementMethod(new ScrollingMovementMethod());
        mCheckBox = findViewById(R.id.dialog_confirm_check_box);
        mViewDivider = findViewById(R.id.dialog_confirm_divider);
        mViewVerticalDivider = findViewById(R.id.dialog_confirm_vertical_divider);
        mBtnNegative = findViewById(R.id.dialog_confirm_button_negative);
        mBtnPositive = findViewById(R.id.dialog_confirm_button_positive);
    }

    public boolean isChecked() {
        return mCheckBox != null && mCheckBox.isChecked();
    }

    private void setListener() {
        mBtnPositive.setOnClickListener(this);
        mBtnNegative.setOnClickListener(this);
    }

    private void drawDetail() {
        if (TextUtils.isEmpty(label)) {
            mTvwTitle.setVisibility(View.GONE);
        } else {
            mTvwTitle.setVisibility(View.VISIBLE);
            mTvwTitle.setText(label);
        }
        if (TextUtils.isEmpty(msg)) {
            mTvwMessage.setVisibility(View.GONE);
        } else {
            mTvwMessage.setVisibility(View.VISIBLE);
            if (useHtml)
                mTvwMessage.setText(Html.fromHtml(msg));
            else
                mTvwMessage.setText(msg);
        }
        if (TextUtils.isEmpty(checkbox)) {
            mCheckBox.setVisibility(View.GONE);
        } else {
            mCheckBox.setVisibility(View.VISIBLE);
            if (useHtml)
                mCheckBox.setText(Html.fromHtml(checkbox));
            else
                mCheckBox.setText(checkbox);
        }
        if (TextUtils.isEmpty(negativeLabel) && TextUtils.isEmpty(positiveLabel)) {
            mViewDivider.setVisibility(View.GONE);
            mViewVerticalDivider.setVisibility(View.GONE);
            mBtnNegative.setVisibility(View.GONE);
            mBtnPositive.setVisibility(View.GONE);
        } else {
            mViewDivider.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(negativeLabel) && !TextUtils.isEmpty(positiveLabel)) {
                mViewVerticalDivider.setVisibility(View.VISIBLE);
            } else {
                mViewVerticalDivider.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(negativeLabel)) {
                mBtnNegative.setVisibility(View.GONE);
                mBtnPositive.setVisibility(View.VISIBLE);
                mBtnPositive.setText(positiveLabel);
            } else if (TextUtils.isEmpty(positiveLabel)) {
                mBtnNegative.setVisibility(View.VISIBLE);
                mBtnPositive.setVisibility(View.GONE);
                mBtnNegative.setText(negativeLabel);
            } else {
                mBtnNegative.setVisibility(View.VISIBLE);
                mBtnPositive.setVisibility(View.VISIBLE);
                mBtnNegative.setText(negativeLabel);
                mBtnPositive.setText(positiveLabel);
            }
        }

        if(buttonColor != 0)
        {
            mBtnPositive.setTextColor(buttonColor);
        }

        if(forceUpdate)
            mBtnNegative.setVisibility(View.GONE);
    }

    public void setButtonTextColor(int color)
    {
        buttonColor = color;
    }

    public void setTextViewHTML(TextView text, String html)
    {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for(URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span)
    {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                if (hyperLinkListener != null) {
                    hyperLinkListener.onClickHyperLink(span.getURL());
                }
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    public void setOneButton(){
        if(mBtnNegative != null) mBtnNegative.setVisibility(View.GONE);
    }
}
