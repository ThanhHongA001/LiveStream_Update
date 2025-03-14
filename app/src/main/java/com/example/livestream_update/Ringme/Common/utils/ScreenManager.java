package com.example.livestream_update.Ringme.Common.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.utils.Log;

public class ScreenManager {

    public static int getHeight(Activity activity) {
        if (activity != null) {
            WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
            Display display = null;
            if (wm != null) {
                display = wm.getDefaultDisplay();
            }
            Point size = new Point();
            if (display != null) {
                display.getSize(size);
            }
            return size.y;
        }
        return 0;
    }

    public static int getWidth(Activity activity) {
        if (activity != null) {
            WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
            Display display = null;
            if (wm != null) {
                display = wm.getDefaultDisplay();
            }
            Point size = new Point();
            if (display != null) {
                display.getSize(size);
            }
            return size.x;
        }
        return 0;
    }

    public static int getHeight(Context context) {
        if (context != null) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = null;
            if (wm != null) {
                display = wm.getDefaultDisplay();
            }
            Point size = new Point();
            if (display != null) {
                display.getSize(size);
            }
            return size.y;
        }
        return 0;
    }

    public static int getWidth(Context context) {
        if (context != null) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = null;
            if (wm != null) {
                display = wm.getDefaultDisplay();
            }
            Point size = new Point();
            if (display != null) {
                display.getSize(size);
            }
            return size.x;
        }
        return 0;
    }

    public static int getHeight() {
        return getHeight(ApplicationController.self());
    }

    public static int getWidth() {
        return getWidth(ApplicationController.self());
    }

    public static boolean isLandscape(Activity activity) {
        boolean isLandscape = false;
        int width = 0;
        int height = 0;
        if (activity != null) {
            try {
                WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
                Display display = null;
                if (wm != null) {
                    display = wm.getDefaultDisplay();
                }
                Point size = new Point();
                if (display != null) {
                    display.getSize(size);
                }
                width = size.x;
                height = size.y;
                isLandscape = width > height;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.i("ScreenManager", "isLandscape width: " + width + ", height: " + height + ", landscape: " + isLandscape);
        return isLandscape;
    }

}
