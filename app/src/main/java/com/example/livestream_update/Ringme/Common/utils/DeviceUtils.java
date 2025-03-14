package com.example.livestream_update.Ringme.Common.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Device Utils
 * Created by neo on 2/16/2016.
 */
public class DeviceUtils {

    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static Point getDeviceSize(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Point getDeviceSizePortrait(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int x = Math.min(size.x, size.y);
        int y = Math.max(size.x, size.y);
        return new Point(x, y);
    }

    public static int getDpi(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (metrics.density * 160f);
    }

    @Deprecated
    public static boolean isLandscape(Activity activity) {
        //todo hàm này bị lỗi api 22, 23
        return activity != null && activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * Force set the orientation of activity
     *
     * @param activity    target activity
     * @param orientation 1 of those values
     *                    Configuration.ORIENTATION_LANDSCAPE
     *                    or Configuration.ORIENTATION_PORTRAIT
     *                    or Configuration.ORIENTATION_UNDEFINED
     */
    @SuppressLint("SourceLockedOrientationActivity")
    public static void forceRotateScreen(Activity activity, int orientation) {
        switch (orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case Configuration.ORIENTATION_UNDEFINED:
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                break;
        }
    }

    public static boolean isDeviceLockRotate(Context context) {
        try {
            final int rotationState = Settings.System.getInt(
                    context.getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATION, 0
            );
            return rotationState == 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @SuppressLint("MissingPermission")
    public static String getPhoneNumber(Context context) {
        TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tMgr.getLine1Number();
    }

    public static void openAppInStore(Context context, String appPackageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    //TODO kiem tra xem 1 ung dung da duoc cai dat hay chua
    public static boolean checkAppEnabled(Context context, String packageId) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageId, 0);
            if (applicationInfo != null) return applicationInfo.enabled;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static int getScreenHeight(Context c) {
        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        assert wm != null : 0;
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static int getScreenWidth(Context c) {
        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        assert wm != null : 0;
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

}