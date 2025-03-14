package com.example.livestream_update.Ringme.Base;


import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewbinding.ViewBinding;

import com.vtm.R;
import com.vtm.ringme.ApplicationController;


public abstract class BaseActivityNew extends AppCompatActivity {
    public abstract ViewBinding getViewBinding();
    private final String TAG = getClass().getSimpleName();
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Toolbar mToolBar;
    private ApplicationController applicationController;
    private Handler mHandler;

    private Dialog currentPrefixDialog;
    private long lastClick;
    protected Dialog dialogConfirm;
    Window window;
    protected ApplicationController mApp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getViewBinding().getRoot());
        window = getWindow();
        mApp = ApplicationController.self();
        if (Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
//        int visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
//        window.getDecorView().setSystemUiVisibility(visibility);
        if (Build.VERSION.SDK_INT >= 21) {
            int windowManager = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            setWindowFlag(windowManager, false);
            window.setStatusBarColor(getResources().getColor(R.color.main_color_new));
        }
    }

    private void setWindowFlag(final int bits, boolean on) {
        WindowManager.LayoutParams winParams = window.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        window.setAttributes(winParams);
    }

    public void showConfirmDialog(String title, String message, View.OnClickListener listenerAccept) {
        dialogConfirm = new Dialog(BaseActivityNew.this);

        if (dialogConfirm.getWindow() != null) {
            dialogConfirm.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
            dialogConfirm.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            WindowManager.LayoutParams wlp = dialogConfirm.getWindow().getAttributes();
            wlp.gravity = Gravity.BOTTOM; //Gravity.BOTTOM
            dialogConfirm.getWindow().setAttributes(wlp);
        }

        //dialogConfirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogConfirm.setContentView(R.layout.rm_dialog_confirm_delete_status);
        dialogConfirm.setCanceledOnTouchOutside(false);

        ((TextView) dialogConfirm.findViewById(R.id.txt_title_dialog)).setText(title);
        ((TextView) dialogConfirm.findViewById(R.id.txt_dialog)).setText(message);
        ((TextView) dialogConfirm.findViewById(R.id.btn_ok_dialog)).setText(R.string.accept);
        ((TextView) dialogConfirm.findViewById(R.id.btn_cancel_dialog)).setText(R.string.cancel);
        dialogConfirm.findViewById(R.id.btn_ok_dialog).setOnClickListener(v -> {
            dialogConfirm.dismiss();
            listenerAccept.onClick(v);
        });

        dialogConfirm.findViewById(R.id.btn_cancel_dialog).setOnClickListener(v -> dialogConfirm.dismiss());

        dialogConfirm.show();
    }
}
