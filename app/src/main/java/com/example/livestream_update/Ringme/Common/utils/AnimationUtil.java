package com.example.livestream_update.Ringme.Common.utils;

import android.view.View;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;

public class AnimationUtil {

    public static int ANIMATION_DURATION_SHORT = 150;

    public static void crossFadeViews(View showView, View hideView) {
        crossFadeViews(showView, hideView, ANIMATION_DURATION_SHORT);
    }

    public static void crossFadeViews(View showView, View hideView, int duration) {
        fadeInView(showView, duration);
        fadeOutView(hideView, duration);
    }

    public static void fadeInView(View view) {
        fadeInView(view, ANIMATION_DURATION_SHORT);
    }

    public static void fadeInView(View view, int duration) {
        fadeInView(view, duration, null);
    }

    public static void fadeInView(View view, AnimationListener listener) {
        fadeInView(view, ANIMATION_DURATION_SHORT, listener);
    }

    public static void fadeInView(View view, int duration, final AnimationListener listener) {
        if (view == null || view.getVisibility() == View.VISIBLE) return;
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.0F);
        ViewPropertyAnimatorListener vpListener = null;
        vpListener = new ViewPropertyAnimatorListener() {
            public void onAnimationStart(View view) {
            }

            public void onAnimationEnd(View view) {
                if (listener != null)
                    listener.onAnimationEnd(view);
            }

            public void onAnimationCancel(View view) {
            }
        };
        ViewCompat.animate(view).alpha(1.0F).setDuration(duration).setListener(vpListener);
    }

    public static void fadeOutView(View view) {
        fadeOutView(view, ANIMATION_DURATION_SHORT);
    }

    public static void fadeOutView(View view, int duration) {
        fadeOutView(view, duration, null);
    }

    public static void fadeOutView(View view, AnimationListener listener) {
        fadeOutView(view, ANIMATION_DURATION_SHORT, listener);
    }

    public static void fadeOutView(View view, int duration, final AnimationListener listener) {
        if (view == null || view.getVisibility() != View.VISIBLE) return;
        view.setVisibility(View.VISIBLE);
        view.setAlpha(1.0F);
        ViewPropertyAnimatorListener vpListener = null;
        if (listener != null) {
            vpListener = new ViewPropertyAnimatorListener() {
                public void onAnimationStart(View view) {
                    view.setDrawingCacheEnabled(true);
                }

                public void onAnimationEnd(View view) {
                    view.setVisibility(View.GONE);
                    listener.onAnimationEnd(view);
                }

                public void onAnimationCancel(View view) {
                }
            };
        }
        ViewCompat.animate(view).alpha(0.0F).setDuration(duration).setListener(vpListener);
    }

    public interface AnimationListener {
        void onAnimationEnd(View view);
    }
}
