package com.example.livestream_update.Ringme.Utils;

import android.content.Context;

public class ToastUtilsV2 {
    public static void makeText(Context context, String text) {
        ToastUtils.showToast(context, text);
    }

    public static void makeText(Context context, int text) {
        String msg = "";
        if (context != null) msg = context.getString(text);
        ToastUtils.showToast(context, msg);
    }

    public static void makeText(Context context, String text, int dur) {
        ToastUtils.showToast(context, text);
    }
}
