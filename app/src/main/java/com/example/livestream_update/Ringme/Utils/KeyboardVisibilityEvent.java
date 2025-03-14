package com.example.livestream_update.Ringme.Utils;


import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.vtm.ringme.livestream.listener.KeyboardVisibilityEventListener;
import com.vtm.ringme.livestream.listener.Unregistrar;


/**
 * Created by yshrsmz on 15/03/17.
 */
public class KeyboardVisibilityEvent {
    private final static double KEYBOARD_MIN_HEIGHT_RATIO = 0.15;

    public static Unregistrar registerEventListener(final Activity activity,
                                                    final KeyboardVisibilityEventListener listener) {

        if (activity == null) {
            throw new NullPointerException("Parameter:activity must not be null");
        }

        int softInputAdjust = activity.getWindow().getAttributes().softInputMode
                & WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST;

        // fix for #37 and #38.
        // The window will not be resized in case of SOFT_INPUT_ADJUST_NOTHING
        if ((softInputAdjust & WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
                == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING) {
            throw new IllegalArgumentException("Parameter:activity window SoftInputMethod is SOFT_INPUT_ADJUST_NOTHING. In this case window will not be resized");
        }

        if (listener == null) {
            throw new NullPointerException("Parameter:listener must not be null");
        }

        final View activityRoot = getActivityRoot(activity);

        final ViewTreeObserver.OnGlobalLayoutListener layoutListener =
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    private final Rect r = new Rect();

                    private boolean wasOpened = false;

                    @Override
                    public void onGlobalLayout() {
                        activityRoot.getWindowVisibleDisplayFrame(r);

                        int screenHeight = activityRoot.getRootView().getHeight();
                        int heightDiff = screenHeight - r.height();

                        boolean isOpen = heightDiff > screenHeight * KEYBOARD_MIN_HEIGHT_RATIO;

                        if (isOpen == wasOpened) {
                            // keyboard state has not changed
                            return;
                        }

                        wasOpened = isOpen;

                        listener.onVisibilityChanged(isOpen);
                    }
                };
        activityRoot.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);

        return new SimpleUnregistrar(activity, layoutListener);
    }

    static View getActivityRoot(Activity activity) {
        return ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
    }

}
