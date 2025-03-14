package com.example.livestream_update.Ringme.Base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

import com.vtm.R;


public abstract class BaseDialogFragment extends DialogFragment {

    protected AppCompatButton btnRight;
    protected AppCompatButton btnLeft;
    protected AppCompatTextView tvTitle;
    protected RadioGroup rgPackage;
    protected DialogListener selectListener;
    protected AppCompatImageView ivClose;

    protected abstract int getResId();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.V5DialogRadioButton);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getResId(), container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        getDialog().getWindow()
//                .getAttributes().windowAnimations = R.style.V5DialogWindowAnimation;
        initView();
    }

    public void setSelectListener(DialogListener selectListener) {
        this.selectListener = selectListener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        if( selectListener != null){
            selectListener.onDismiss();
        }
        super.onDismiss(dialog);
    }

    protected void initView(){
        btnLeft = getView().findViewById(R.id.btnLeft);
        btnRight = getView().findViewById(R.id.btnRight);
        tvTitle = getView().findViewById(R.id.txtTitle);
        rgPackage = getView().findViewById(R.id.rg_package);
        ivClose = getView().findViewById(R.id.iv_close);
        if(btnLeft != null){
            btnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDialog().cancel();
                    if( selectListener != null){
                        selectListener.dialogLeftClick();
                    }
                }
            });
        }
        if(ivClose != null) {
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().cancel();
                    if( selectListener != null){
                        selectListener.onClickClose();
                    }
                }
            });
        }
    }


    public interface DialogListener {
        void dialogRightClick(int value);
        void dialogLeftClick();

        default void onClickClose(){}
        default void onDismiss(){}
        default void onResend(){}
    }


}
