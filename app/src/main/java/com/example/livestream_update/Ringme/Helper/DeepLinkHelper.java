package com.example.livestream_update.Ringme.Helper;

import android.net.Uri;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.vtm.R;
import com.vtm.ringme.utils.ToastUtils;

/**
 * Created by toanvk2 on 12/6/2016.
 */
public class DeepLinkHelper {
    private static final String TAG = DeepLinkHelper.class.getSimpleName();
    private static DeepLinkHelper mInstance;
    String sectionIdOtp = "";
    boolean isResend = false;
    //Dinh nghia 4 param cho game link
    private final String link = "";
    private final String title = "";
    private final String font = "";
    private final String id = "";
    private long mLastClickTime = 0;

    public DeepLinkHelper() {

    }

    public static synchronized DeepLinkHelper getInstance() {
        if (mInstance == null) {
            mInstance = new DeepLinkHelper();
        }
        return mInstance;
    }

    public boolean openSchemaLink(AppCompatActivity activity, String schemaLink) {
        return openSchemaLink(activity, schemaLink, true);
    }

    public boolean openSchemaLink(AppCompatActivity activity, String schemaLink, boolean showToast) {
        if (System.currentTimeMillis() - mLastClickTime < 1000) {
            return false;
        }
        mLastClickTime = System.currentTimeMillis();

        if (TextUtils.isEmpty(schemaLink)) {
            if (showToast)
                ToastUtils.showToast(activity, activity.getString(R.string.e601_error_but_undefined));
            return false;
        }
        Uri uri = Uri.parse(schemaLink);
        String scheme = uri == null ? "" : uri.getScheme();
        if (DEEP_LINK.SCHEME.equals(scheme)) {
            return true;
        } else {

        }
        return false;
    }

    public static final class DEEP_LINK {
        public static final String SCHEME = "kakoak";
    }
}


