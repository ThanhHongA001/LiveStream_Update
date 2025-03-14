package com.example.livestream_update.Ringme.Helper;


import android.app.Activity;
import android.content.Intent;


public class NavigateActivityHelper {
    private static final String TAG = NavigateActivityHelper.class.getSimpleName();

    public static void navigateToSendEmail(Activity activity, String email) {
        if (activity == null || email == null) return;
        Intent sendEmailIntent = new Intent(Intent.ACTION_SEND);
        sendEmailIntent.setType("message/rfc822");
        sendEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        activity.startActivity(sendEmailIntent);
    }


}
