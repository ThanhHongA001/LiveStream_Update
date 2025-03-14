package com.example.livestream_update.Ringme.Common.utils;


import android.content.Intent;
import android.text.TextUtils;
import android.webkit.URLUtil;

import androidx.appcompat.app.AppCompatActivity;

import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.model.ItemContextMenu;
import com.vtm.ringme.model.tab_video.Video;

public class ShareUtils {

    private static ItemContextMenu providerContextMenu(AppCompatActivity activity
            , int mStringRes, int mDrawableRes, int itemId) {
        return new ItemContextMenu(activity.getString(mStringRes), mDrawableRes, null, itemId);
    }

    public static void openShareMenu(final AppCompatActivity activity, final Object object) {
        if (activity == null || activity.isFinishing() || object == null) return;
//        ShareContentBusiness business = new ShareContentBusiness(activity, object);
//        business.setTypeSharing(ShareContentBusiness.TYPE_SHARE_CONTENT);
//        business.showPopupShareContent();

        if (object instanceof Video) {
            shareWithIntent(activity, ((Video) object).getLink(), "Share");
            ApplicationController app = (ApplicationController) activity.getApplication();
        }
    }



    public static void shareWithIntent(AppCompatActivity activity, String content, String title) {
        if (activity == null || TextUtils.isEmpty(content)) return;
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (URLUtil.isNetworkUrl(content))
            intent.setType("text/plain");
        else
            intent.setType("text/html");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        activity.startActivity(Intent.createChooser(intent, title));
    }


    public static void shareLivestream(AppCompatActivity activity, String content, String title) {
        if (activity == null || TextUtils.isEmpty(content)) return;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        activity.startActivity(Intent.createChooser(intent, title));
    }
}
