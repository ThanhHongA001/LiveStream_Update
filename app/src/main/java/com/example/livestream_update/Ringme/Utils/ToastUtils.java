package com.example.livestream_update.Ringme.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;

import com.vtm.R;
import com.vtm.ringme.ApplicationController;

/**
 * Created by thanhnt72 on 11/6/2019.
 */

public class ToastUtils {

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

    private static final int MAX_LENGTH_SHORT_TOAST = 50;

    public static void showToast(int string){
        showToast(ApplicationController.self(), ApplicationController.self().getString(string));
    }

    public static void showToast(String string){
        showToast(ApplicationController.self(), string);
    }

    public static void showToast(Context context, String message, int duration, int resIdCustomImg, ToastType type) {
        if (TextUtils.isEmpty(message)) return;
        ApplicationController app = ApplicationController.self();
        if (app == null) return;
        Toast toast = new Toast(app.getApplicationContext());
        View layout = LayoutInflater.from(app.getApplicationContext()).inflate(R.layout.rm_custom_toast_v2, null, false);
        TextView l1 = layout.findViewById(R.id.text);
        AppCompatImageView img = layout.findViewById(R.id.image);
        l1.setText(message);
        if (type == null)
            img.setVisibility(View.GONE);
        else
            switch (type) {
                case customImage:
                    img.setImageResource(resIdCustomImg);
                    break;
                case done:
                    img.setImageResource(R.drawable.rm_ic_v5_done);
                    break;
                case error:
                    break;

            }
        toast.setView(layout);
        toast.setDuration(duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToastSuccess(Context context, String message) {
        int duration;
        if (message != null && message.length() > MAX_LENGTH_SHORT_TOAST)
            duration = Toast.LENGTH_LONG;
        else
            duration = Toast.LENGTH_SHORT;
        showToast(context, message, duration, R.drawable.rm_ic_tick_v5, ToastType.customImage);

    }


    public static void showToast(Context context, String message) {
        int duration;
        if (message != null && message.length() > MAX_LENGTH_SHORT_TOAST)
            duration = Toast.LENGTH_LONG;
        else
            duration = Toast.LENGTH_SHORT;
        showToast(context, message, duration, 0, null);

    }

    public enum ToastType {
        done, error, customImage
    }
}
