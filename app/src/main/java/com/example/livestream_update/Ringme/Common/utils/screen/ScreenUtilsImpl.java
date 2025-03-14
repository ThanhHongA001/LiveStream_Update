package com.example.livestream_update.Ringme.Common.utils.screen;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.vtm.ringme.ApplicationController;

/**
 * Created by tuanha00 on 3/16/2018.
 */

@Deprecated
public class ScreenUtilsImpl implements ScreenUtils {

    private static int screenHeight = 0;
    private static int screenWidth = 0;

    private final ApplicationController application;

    public ScreenUtilsImpl(ApplicationController application) {
        this.application = application;
    }

    @Override
    public int getScreenHeight() {
        return getScreenHeight(application);
    }

    @Override
    public int getScreenWidth() {
        return getScreenWidth(application);
    }

    public static int getScreenHeight(Context c) {
        if (c != null && screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (c != null && screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }

}
