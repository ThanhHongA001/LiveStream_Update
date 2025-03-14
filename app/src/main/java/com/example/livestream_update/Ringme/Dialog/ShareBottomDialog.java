package com.example.livestream_update.Ringme.Dialog;

/*
 * Copyright (c) https://bigzun.blogspot.com/
 * Email: bigzun.com@gmail.com
 * Created by namnh40 on 2019/8/20
 *
 */

import static com.google.android.material.bottomsheet.BottomSheetBehavior.from;

import android.app.Activity;
import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vtm.R;
import com.vtm.ringme.livestream.listener.KeyboardVisibilityEventListener;
import com.vtm.ringme.livestream.listener.Unregistrar;
import com.vtm.ringme.utils.KeyboardVisibilityEvent;
import com.vtm.ringme.utils.Log;

public class ShareBottomDialog extends BottomSheetDialog {
    private KeyboardVisibilityEventListener keyboardVisibilityListener;
    private Unregistrar unregistrar;
    protected Activity activity;

    public ShareBottomDialog(@NonNull Context context) {
        super(context, R.style.style_share_content_dialog);
        if (context instanceof AppCompatActivity) {
            activity = (AppCompatActivity) context;
        }
    }

    public ShareBottomDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected ShareBottomDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setKeyboardVisibilityListener(KeyboardVisibilityEventListener listener) {
        this.keyboardVisibilityListener = listener;
    }

    @Override
    public void show() {
        super.show();
        if (activity != null && keyboardVisibilityListener != null) {
            unregistrar = KeyboardVisibilityEvent.registerEventListener(activity, keyboardVisibilityListener);
        }
    }

    @Override
    public void dismiss() {
        Log.d("ShareBottomDialog", "dismiss");
        if (unregistrar != null) unregistrar.unregister();
        super.dismiss();
    }

    @Nullable
    public BottomSheetBehavior getBottomSheetBehavior() {
        BottomSheetBehavior behavior = null;
        try {
            FrameLayout bottomSheet = findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) behavior = from(bottomSheet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return behavior;
    }

}
