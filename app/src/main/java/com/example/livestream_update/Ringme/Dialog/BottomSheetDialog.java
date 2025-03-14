package com.example.livestream_update.Ringme.Dialog;


import static com.google.android.material.bottomsheet.BottomSheetBehavior.from;

import android.app.Activity;
import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.vtm.R;
import com.vtm.ringme.livestream.listener.KeyboardVisibilityEventListener;

public class BottomSheetDialog extends com.google.android.material.bottomsheet.BottomSheetDialog {
    protected AppCompatActivity activity;
    private KeyboardVisibilityEventListener keyboardVisibilityListener;

    public BottomSheetDialog(@NonNull Context context) {
        super(context, R.style.style_bottom_sheet_dialog_v2);
        if (context instanceof Activity) {
            activity = (AppCompatActivity) context;
        }
    }

    public BottomSheetDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected BottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setKeyboardVisibilityListener(KeyboardVisibilityEventListener listener) {
        this.keyboardVisibilityListener = listener;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {

        super.dismiss();
    }

    @Nullable
    public BottomSheetBehavior getBottomSheetBehavior() {
        BottomSheetBehavior behavior = null;
        try {
            FrameLayout bottomSheet = findViewById(R.id.design_bottom_sheet);
            if (bottomSheet != null) behavior = from(bottomSheet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return behavior;
    }

}
