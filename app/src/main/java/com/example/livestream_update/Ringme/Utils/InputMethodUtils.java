package com.example.livestream_update.Ringme.Utils;


import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


/**
 * Created by thaodv on 7/9/2014.
 */
public class InputMethodUtils {
    private static final String TAG = InputMethodUtils.class.getSimpleName();

    public static void hideKeyboardWhenTouch(View view, final Activity activity) {
        if (view == null || activity == null) {
            return;
        }
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                hideKeyboardWhenTouch(innerView, activity);
            }
        }
    }

    public static void hideSoftKeyboard(EditText editText, Context context) {
        if (editText == null || context == null) return;
        //Log.i(TAG, "hideSoftKeyboard(EditText: " + editText.getText() + ", Context: " + context.getClass().getName() + ")");
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


    public static void showSoftKeyboard(Context context, EditText editText) {
        if (context == null || editText == null) return;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        // imm.showSoftInput(this.editText,InputMethodManager.SHOW_FORCED);
        editText.requestFocus();
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity
                        .getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
    }

}