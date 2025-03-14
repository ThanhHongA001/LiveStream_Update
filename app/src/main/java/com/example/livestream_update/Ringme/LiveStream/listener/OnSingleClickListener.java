package com.example.livestream_update.Ringme.LiveStream.listener;

/*
 * Copyright (c) https://bigzun.blogspot.com/
 * Email: bigzun.com@gmail.com
 * Created by namnh40 on 2019/8/21
 *
 */


import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.view.View;

import com.vtm.R;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.helper.NetworkHelper;
import com.vtm.ringme.utils.ToastUtils;


public abstract class OnSingleClickListener implements View.OnClickListener {
    private static final long MIN_CLICK_INTERVAL = 800;
    private long lastClickTime;

    public boolean isCheckLogin() {
        return false;
    }

    public boolean isCheckNetwork() {
        return true;
    }

    public boolean isLockTime() {
        return true;
    }

    public abstract void onSingleClick(View view);

    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View view) {
        if (isCheckNetwork()) {
            //check network
            if (!NetworkHelper.isConnectInternet(ApplicationController.self())) {
                ToastUtils.showToast(ApplicationController.self(),ApplicationController.self().getString(R.string.error_internet_disconnect));
                return;
            }
        }
        if (isLockTime()) {
            //check time
            long currentClickTime = SystemClock.uptimeMillis();
            long elapsedTime = currentClickTime - lastClickTime;
            lastClickTime = currentClickTime;
            if (elapsedTime < MIN_CLICK_INTERVAL) return;
        }
        onSingleClick(view);
    }
}