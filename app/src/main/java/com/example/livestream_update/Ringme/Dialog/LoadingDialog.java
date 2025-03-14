package com.example.livestream_update.Ringme.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.vtm.R;
import com.vtm.ringme.livestream.listener.DismissListener;

public class LoadingDialog extends Dialog {
    private String label;
    private String msg;
    private DismissListener dismissListener;
    private View mViewLabel;
    private TextView mTvwTitle, mTvwMessage;

    public LoadingDialog(Activity activity, boolean isCancelable) {
        super(activity, R.style.DialogFullscreen);
        setCancelable(isCancelable);
    }

    public LoadingDialog setLabel(String label) {
        this.label = label;
        return this;
    }

    public LoadingDialog setMessage(String message) {
        this.msg = message;
        return this;
    }

    public LoadingDialog setDismissListener(DismissListener listener) {
        this.dismissListener = listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rm_dialog_loading);
        findComponentViews();
        drawDetail();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (dismissListener != null) {
            dismissListener.onDismiss();
        }
    }

    private void findComponentViews() {
        mViewLabel = findViewById(R.id.dialog_title_layout);
        mTvwTitle = findViewById(R.id.dialog_title);
        mTvwMessage = findViewById(R.id.dialog_message);
    }

    private void drawDetail() {
        if (TextUtils.isEmpty(label)) {
            mViewLabel.setVisibility(View.GONE);
        } else {
            mViewLabel.setVisibility(View.VISIBLE);
            mTvwTitle.setText(label);
        }
        if (TextUtils.isEmpty(msg)) {
            mTvwMessage.setVisibility(View.GONE);
        } else {
            mTvwMessage.setVisibility(View.VISIBLE);
            mTvwMessage.setText(msg);
        }
    }
}
