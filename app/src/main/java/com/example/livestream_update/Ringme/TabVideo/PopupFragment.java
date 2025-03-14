package com.example.livestream_update.Ringme.TabVideo;


/**
 * Created by toanvk2 on 10/22/14.
 */

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.vtm.R;
import com.vtm.ringme.livestream.listener.ClickListener;
import com.vtm.ringme.utils.Log;


public class PopupFragment extends Dialog {
    private static final String TAG = "PopupFragment";
    private Button mBtnYes, mBtnNo;
    private TextView mTvwTitle, mTvwContent;
    private CheckBox mCbNotShowAgain;
    private LinearLayout mLlTitle;
    protected ClickListener.IconListener clickHandler;
    protected ClickListener.CheckboxListener cbHandler;
    protected Object entry;
    protected int listenerId, listenerNoId = -1;
    protected String titleString, contentString, yesLabel, noLabel;
    private boolean visibleNotShowAgain = false;

    public PopupFragment(AppCompatActivity activity, String title, String msg, String yesLabel,
                         String noLabel, ClickListener.IconListener clickHandler,
                         Object entry, int listenerId, boolean isCancelable, ClickListener.CheckboxListener cbHandler, boolean visibleNotShowAgain) {
        super(activity, R.style.DialogFullscreen);
        this.titleString = title;
        this.contentString = msg;
        this.yesLabel = yesLabel;
        this.noLabel = noLabel;
        this.clickHandler = clickHandler;
        this.entry = entry;
        this.listenerId = listenerId;
        this.visibleNotShowAgain = visibleNotShowAgain;
        this.cbHandler = cbHandler;
        setCancelable(isCancelable);
    }

    public PopupFragment(AppCompatActivity activity, String title, String msg, String yesLabel,
                         String noLabel, ClickListener.IconListener clickHandler,
                         Object entry, int listenerId, boolean isCancelable) {
        super(activity, R.style.DialogFullscreen);
        this.titleString = title;
        this.contentString = msg;
        this.yesLabel = yesLabel;
        this.noLabel = noLabel;
        this.clickHandler = clickHandler;
        this.entry = entry;
        this.listenerId = listenerId;
        setCancelable(isCancelable);
    }

    public PopupFragment(AppCompatActivity activity, String title, String msg, String yesLabel,
                         String noLabel, ClickListener.IconListener clickHandler,
                         Object entry, int listenerYesId, int listenerNoId, boolean isCancelable) {
        super(activity, R.style.DialogFullscreen);
        this.titleString = title;
        this.contentString = msg;
        this.yesLabel = yesLabel;
        this.noLabel = noLabel;
        this.clickHandler = clickHandler;
        this.entry = entry;
        this.listenerId = listenerYesId;
        this.listenerNoId = listenerNoId;
        setCancelable(isCancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initControl();
        initEvent();
    }

    /*

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View v = initControl(inflater, container);
        initEvent();
        if (getDialog() != null) {
            getDialog().setCanceledOnTouchOutside(true);
        }
        return v;
    }*/

    /**
     * init controller
     *
     * @param: n/a
     * @return: n/a
     * @throws: n/a
     */
    private void initControl() {
        if (yesLabel == null && noLabel == null) {// no button
            setContentView(R.layout.rm_popup_fragment_no_button);
        } else if (yesLabel != null && noLabel != null) { // popup two button
            setContentView(R.layout.rm_popup_fragment_two_button);
            mBtnYes = (Button) findViewById(R.id.popup_yes_btn);
            mBtnNo = (Button) findViewById(R.id.popup_no_btn);
            mCbNotShowAgain = (CheckBox) findViewById(R.id.cb_not_show_again);
            mCbNotShowAgain.setVisibility(visibleNotShowAgain ? View.VISIBLE : View.GONE);
        } else { // popup one button
            setContentView(R.layout.rm_popup_fragment_one_button);
            if (yesLabel == null)
                mBtnNo = (Button) findViewById(R.id.popup_no_btn);
            else if (noLabel == null)
                mBtnYes = (Button) findViewById(R.id.popup_no_btn);
        }
        mLlTitle = (LinearLayout) findViewById(R.id.popup_title_layout);
        mTvwTitle = (TextView) findViewById(R.id.popup_fragment_title);
        mTvwContent = (TextView) findViewById(R.id.popup_content_txt);
    }

    /**
     * init event for controller
     *
     * @param: n/a
     * @return: n/a
     * @throws: n/a
     */
    private void initEvent() {
        try {
            if (!TextUtils.isEmpty(titleString)) {
                mLlTitle.setVisibility(View.VISIBLE);
                mTvwTitle.setText(titleString);
            } else {
                mLlTitle.setVisibility(View.GONE);
            }
            if (mTvwContent != null) {
                if (contentString != null) mTvwContent.setText(Html.fromHtml(contentString));
                else mTvwContent.setText("");
            }
            if (mBtnYes != null) {
                mBtnYes.setText(yesLabel);
                mBtnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cbHandler != null) {
                            boolean checked = mCbNotShowAgain != null && mCbNotShowAgain.isChecked();
                            cbHandler.onChecked(checked);
                        }
                        if (clickHandler != null) {
                            clickHandler.onIconClickListener(view, entry, listenerId);
                        }
                        dismiss();
                    }
                });
            }
            if (mBtnNo != null) {
                mBtnNo.setText(noLabel);
                mBtnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cbHandler != null) {
                            boolean checked = mCbNotShowAgain != null && mCbNotShowAgain.isChecked();
                            cbHandler.onChecked(checked);
                        }
                        if (clickHandler != null && listenerNoId != -1) {
                            clickHandler.onIconClickListener(view, entry, listenerNoId);
                        }
                        dismiss();
                    }
                });
            }

            if (mCbNotShowAgain != null) {
                mCbNotShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            mCbNotShowAgain.setTextColor(ContextCompat.getColor(getContext(), R.color.onmedia_bg_button));
                        } else {
                            mCbNotShowAgain.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray_light));
                        }
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "initEvent", e);
        }
    }

    @Override
    public void dismiss() {
        cbHandler = null;
        clickHandler = null;
        super.dismiss();
    }

    public interface IconListener {
        void onIconClickListener(View view, Object entry, int menuId);
    }

    // checkbox
    public interface CheckboxListener {
        void onChecked(boolean isChecked);
    }
}
