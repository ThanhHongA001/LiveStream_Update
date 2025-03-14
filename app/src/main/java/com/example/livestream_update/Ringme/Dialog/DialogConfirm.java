package com.example.livestream_update.Ringme.Dialog;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.KeyboardUtils;
import com.vtm.R;
import com.vtm.ringme.base.BaseDialogFragment;
import com.vtm.ringme.base.RoundTextView;
import com.vtm.ringme.customview.PinEntryEditText;
import com.vtm.ringme.utils.ToastUtils;


public class DialogConfirm extends BaseDialogFragment {

    public static final int CONFIRM_TYPE = 0; // have two button
    public static final int INTRO_TYPE = 1; // have one button

    public static final int SELECT_TYPE = 2;//todo 2 button, 2 radio button
    public static final int PIN_TYPE = 3;
    public static final int OTP_TYPE = 4;

    private static final String TYPE_KEY = "type_key";
    private static final String TITLE_KEY = "title_key";
    private static final String MESSAGE_KEY = "message_key";
    private static final String RIGHT_BUTTON_KEY = "right_button_key";
    private static final String LEFT_BUTTON_KEY = "left_button_key";
    private static final String IC_SHORT_CUT_KEY = "ic_short_cut_key";
    private static final String ID_TITLE_SHORT_CUT_KEY = "title_short_cut_key";

    private PinEntryEditText edtPin;
    private LinearLayout layoutOtp;
    private AppCompatEditText edtOtp;
    private RoundTextView tvResend;
    private CountDownTimer countDownTimer;

    public static DialogConfirm newInstance(int idTitle, int idMessage, int type) {
        Bundle args = new Bundle();
        DialogConfirm fragment = new DialogConfirm();
        fragment.setArguments(args);
        return fragment;
    }

    public static DialogConfirm newInstance(String title, String message, int type,
                                            int idTitleLeftButton, int idTitleRightButton) {
        Bundle args = new Bundle();
        args.putString(TITLE_KEY, title);
        args.putString(MESSAGE_KEY, message);
        args.putInt(TYPE_KEY, type);
        args.putInt(LEFT_BUTTON_KEY, idTitleLeftButton);
        args.putInt(RIGHT_BUTTON_KEY, idTitleRightButton);
        DialogConfirm fragment = new DialogConfirm();
        fragment.setArguments(args);
        return fragment;
    }

    public static DialogConfirm newInstance(String title, int type,
                                            int idTitleLeftButton, int idTitleRightButton) {
        Bundle args = new Bundle();
        args.putString(TITLE_KEY, title);
        args.putInt(TYPE_KEY, type);
        args.putInt(LEFT_BUTTON_KEY, idTitleLeftButton);
        args.putInt(RIGHT_BUTTON_KEY, idTitleRightButton);
        DialogConfirm fragment = new DialogConfirm();
        fragment.setArguments(args);
        return fragment;
    }

    public static DialogConfirm newInstance(String title, String message, int type,
                                            int idTitleLeftButton, int idTitleRightButton
            , int idShortcut
            , int titleShortcut) {
        Bundle args = new Bundle();
        args.putString(TITLE_KEY, title);
        args.putString(MESSAGE_KEY, message);
        args.putInt(TYPE_KEY, type);
        args.putInt(LEFT_BUTTON_KEY, idTitleLeftButton);
        args.putInt(RIGHT_BUTTON_KEY, idTitleRightButton);
        args.putInt(IC_SHORT_CUT_KEY, idShortcut);
        args.putInt(ID_TITLE_SHORT_CUT_KEY, titleShortcut);
        DialogConfirm fragment = new DialogConfirm();
        fragment.setArguments(args);
        return fragment;
    }

    public static DialogConfirm newInstance(String idTitle, String idMessage, int idTitleButton) { // type = 1
        Bundle args = new Bundle();
        DialogConfirm fragment = new DialogConfirm();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getResId() {
        return R.layout.rm_dialog_confirm_v5;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected void initView() {
        super.initView();
        edtPin = getView().findViewById(R.id.edt_pin);
        edtOtp = getView().findViewById(R.id.edt_otp);
        layoutOtp = getView().findViewById(R.id.layout_otp);
        tvResend = getView().findViewById(R.id.tv_resend);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getArguments().getInt(TYPE_KEY) == SELECT_TYPE) {
                    if (selectListener != null) {
//                        if(rgPackage.getCheckedRadioButtonId() == R.id.rb_mosan){
//                            ToastUtils.showToast(ApplicationController.self(), getString(R.string.title_not_suppport_mosan));
//                        } else {
                        selectListener.dialogRightClick(rgPackage.getCheckedRadioButtonId());
//                        }
                    }
                } else if (getArguments().getInt(TYPE_KEY) == PIN_TYPE) {
                    if (edtPin.getText().length() > 5) {
                        int pin = Integer.parseInt(edtPin.getText().toString());
                        if (selectListener != null) {
                            KeyboardUtils.hideSoftInput(edtPin);
                            selectListener.dialogRightClick(pin);
                        }
                    } else {
                        ToastUtils.showToast(getContext(), getString(R.string.you_need_enter_pin));
                    }

                } else if (getArguments().getInt(TYPE_KEY) == OTP_TYPE) {
                    int pin = Integer.parseInt(edtOtp.getText().toString());
                    if (selectListener != null) {
                        KeyboardUtils.hideSoftInput(edtOtp);
                        selectListener.dialogRightClick(pin);
                    }
                }
                else {
                    if (selectListener != null) {
                        selectListener.dialogRightClick(0);
                    }
                    getDialog().cancel();
                }

            }
        });
        if (getArguments() != null) {
            if (!TextUtils.isEmpty(getArguments().getString(TITLE_KEY))) {
                tvTitle.setText(getArguments().getString(TITLE_KEY));
            } else {
                tvTitle.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(getArguments().getString(MESSAGE_KEY))) {
                ((AppCompatTextView) getView().findViewById(R.id.tvMessage)).setText(getArguments().getString(MESSAGE_KEY));

            } else {
                getView().findViewById(R.id.tvMessage).setVisibility(View.GONE);
            }
            btnRight.setText(getArguments().getInt(RIGHT_BUTTON_KEY));
            if (getArguments().getInt(TYPE_KEY) == CONFIRM_TYPE) {
                btnLeft.setText(getArguments().getInt(LEFT_BUTTON_KEY));
                // has short cut icon
                if (getArguments().getInt(ID_TITLE_SHORT_CUT_KEY) != 0) {
                    AppCompatImageView icShortcut = getView().findViewById(R.id.icShortcut);
                    icShortcut.setImageResource(getArguments().getInt(IC_SHORT_CUT_KEY));
                    icShortcut.setVisibility(View.VISIBLE);
                    icShortcut.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            if(selectListener != null){
                                selectListener.dialogRightClick(0);
                            }
                            getDialog().cancel();
                            return true;
                        }
                    });
                    AppCompatTextView tvTitleShortCut = getView().findViewById(R.id.tvShortCut);
                    tvTitleShortCut.setVisibility(View.VISIBLE);
                    tvTitleShortCut.setText(getArguments().getInt(ID_TITLE_SHORT_CUT_KEY));
                }
            } else if (getArguments().getInt(TYPE_KEY) == SELECT_TYPE) {
                btnLeft.setText(getArguments().getInt(LEFT_BUTTON_KEY));
                rgPackage.setVisibility(View.VISIBLE);
                btnRight.setTextColor(getResources().getColor(R.color.v5_error));
            } else if (getArguments().getInt(TYPE_KEY) == PIN_TYPE) {
                btnLeft.setText(getArguments().getInt(LEFT_BUTTON_KEY));
                edtPin.setVisibility(View.VISIBLE);
                btnRight.setTextColor(getResources().getColor(R.color.v5_error));
            } else if (getArguments().getInt(TYPE_KEY) == OTP_TYPE) {
                ivClose.setVisibility(View.GONE);
                btnLeft.setText(getArguments().getInt(LEFT_BUTTON_KEY));
                layoutOtp.setVisibility(View.VISIBLE);
                btnRight.setTextColor(getResources().getColor(R.color.v5_error));
                countDownTimer = new CountDownTimer(120000,1000) {
                    @Override
                    public void onTick(long l) {
                        tvResend.setText((l / 1000) + "");
                    }

                    @Override
                    public void onFinish() {
                        tvResend.setText("Resend");
                    }
                };
                countDownTimer.start();
                tvResend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(selectListener != null){
                            selectListener.onResend();
                        }
                        countDownTimer.start();
                    }
                });
            }
            else {
                btnLeft.setVisibility(View.GONE);
                getView().findViewById(R.id.lineVertical).setVisibility(View.GONE);
                btnRight.getLayoutParams().width = ConstraintLayout.LayoutParams.MATCH_PARENT;
            }
        }
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        try {
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.add(this, tag);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        if(countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        super.dismiss();
    }

    public final static class Builder {
        private String title;
        private String message;
        private int type;
        private int titleLeftButton;
        private int titleRightButton;
        private int idIconShortCut;
        private int titleShortcut;

        public Builder(int type) {
            this.type = type;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setType(int type) {
            this.type = type;
            return this;
        }

        public Builder setTitleLeftButton(int titleLeftButton) {
            this.titleLeftButton = titleLeftButton;
            return this;
        }

        public Builder setTitleRightButton(int idTitleRightButton) {
            this.titleRightButton = idTitleRightButton;
            return this;
        }

        public Builder setIdIconShortCut(int idIconShortCut) {
            this.idIconShortCut = idIconShortCut;
            return this;
        }

        public Builder setTitleShortcut(int titleShortcut) {
            this.titleShortcut = titleShortcut;
            return this;
        }

        public DialogConfirm create() {
            return DialogConfirm.newInstance(title, message
                    , type, titleLeftButton, titleRightButton
                    , idIconShortCut, titleShortcut);
        }
    }

}
