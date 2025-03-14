package com.example.livestream_update.Ringme.Customview;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vtm.R;


public class CustomSuccessToast extends Toast {
    public CustomSuccessToast(Context context) {
        super(context);
    }
    public static int SHORT = 2000;
    public static int LONG = 4000;

    public static Toast makeText(Context context, int duration) {
        Toast toast = new Toast(context);
        View layout = LayoutInflater.from(context).inflate(R.layout.rm_layout_toast_donate_success, null, false);
        toast.setView(layout);
        toast.setDuration(duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }

    public static Toast makeText(Context context, String message, int duration) {
        Toast toast = new Toast(context);
        View layout = LayoutInflater.from(context).inflate(R.layout.rm_layout_toast_donate_success, null, false);
        toast.setView(layout);
        toast.setDuration(duration);
        ((TextView) layout.findViewById(R.id.text)).setText(message);
        toast.setGravity(Gravity.CENTER, 0, 0);
        return toast;
    }
}
