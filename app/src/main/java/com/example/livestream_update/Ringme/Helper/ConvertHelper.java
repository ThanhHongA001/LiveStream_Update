package com.example.livestream_update.Ringme.Helper;


import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.vtm.ringme.utils.Log;

/**
 * Created by toanvk2 on 03/09/2015.
 */
public class ConvertHelper {
    private static final String TAG = ConvertHelper.class.getSimpleName();

    public static boolean parserBoolenFromString(String input, boolean defaultValue) {
        if (TextUtils.isEmpty(input)) {
            return defaultValue;
        }
        boolean value = defaultValue;
        try {
            value = Boolean.valueOf(input);
        } catch (NumberFormatException e) {
            Log.f(TAG, "NumberFormatException", e);
        }
        return value;
    }

    public static int parserIntFromString(String input, int defaultValue) {
        if (TextUtils.isEmpty(input)) {
            return defaultValue;
        }
        int value = defaultValue;
        try {
            value = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            Log.f(TAG, "NumberFormatException", e);
        }
        return value;
    }

    public static long parserLongFromString(String input, long defaultValue) {
        if (TextUtils.isEmpty(input)) {
            return defaultValue;
        }
        long value = defaultValue;
        try {
            value = Long.parseLong(input);
        } catch (NumberFormatException e) {
            Log.f(TAG, "NumberFormatException", e);
        }
        return value;
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}
