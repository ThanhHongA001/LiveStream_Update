package com.example.livestream_update.Ringme.Utils;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.vtm.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

public final class CommonUtils {

    private static final String TAG = "CommonUtils";

    public static long SECOND = 1000;

    public static long MINUTE = 60 * SECOND;

    public static long HOUR = 60 * MINUTE;

    public static final int NUM_SIZE = 20;
    public static final int NUM_BIG_SIZE = 100;

    private CommonUtils() {
        // This utility class is not publicly instantiable
    }

    public static void showNetworkDisconnect(Activity activity) {
        if (activity == null || !(activity instanceof Activity)) {
            return;
        }

        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), "Error connect", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(activity, android.R.color.white));
        snackbar.show();
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.rm_news_detail_progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }


    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "B");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    private static NetworkInfo getNetworkInfo(Context context) {
        if (context == null) return null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }


    public static String AssetJSONFile(String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }

    public static String getUIID(Context context) {
        String uiid = "";
        try {
            uiid = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        } catch (SecurityException e) {
            //Log.e(TAG, "getUIID SecurityException", e);
        } catch (Exception e) {
            Log.e(TAG, "getUIID Exception", e);
        }
        return uiid;
    }

    public static String getMacAddress(Context context) {
        try {
            String address = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
                    for (NetworkInterface nif : all) {
                        if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                        byte[] macBytes = nif.getHardwareAddress();
                        if (macBytes == null) {
                            address = "";
                            break;
                        }
                        StringBuilder res1 = new StringBuilder();
                        for (byte b : macBytes) {
                            res1.append(String.format("%02X:", b));
                        }
                        if (res1.length() > 0) {
                            res1.deleteCharAt(res1.length() - 1);
                        }
                        address = res1.toString();
                        break;
                    }
                } catch (Exception ex) {
                    address = "";
                }
            } else {
                WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (manager != null) {
                    WifiInfo info = manager.getConnectionInfo();
                    address = info.getMacAddress();
                } else {
                    address = "";
                }
            }
            return address;
        } catch (Exception ex) {
            return "";
        }
    }


    public static void share(Context context, String link) {
        if (context == null || !(context instanceof Activity) || TextUtils.isEmpty(link))
            return;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, link);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, context.getString(R.string.share)));
    }

    public static void shareFB(Context context, String link) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, link);
            List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
            for (ResolveInfo info : matches) {
                if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                    intent.setPackage(info.activityInfo.packageName);
                    break;
                }
            }
            context.startActivity(intent);

        } catch (Exception e) {
            // If we failed (not native FB app installed), try share through SEND
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + link;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
            context.startActivity(intent);

            Log.e(TAG, e.toString());

        }
    }



    public static String milliSecondsToTimer(long milliseconds) {
        try {
            int hours = (int) (milliseconds / HOUR);
            int minutes = (int) ((milliseconds % HOUR) / MINUTE);
            int seconds = (int) ((milliseconds % HOUR % MINUTE) / SECOND);

            StringBuilder sb = new StringBuilder();
            if (hours > 0) {
                sb.append(twoDigit(hours)).append(':');
            }
            sb.append(twoDigit(minutes)).append(':');
            sb.append(twoDigit(seconds));
            return sb.toString();
        } catch (Exception e) {
            android.util.Log.e(TAG, e.getMessage());
        }
        return "";
    }

    private static String twoDigit(int d) {
        NumberFormat formatter = new DecimalFormat("#00");
        return formatter.format(d);
    }


    public static final int TYPE_NONE = 0;
    public static final int TYPE_NEWS = 1;
    public static final int TYPE_VIDEO = 2;
    public static final int TYPE_RADIO = 3;

    public static final int TYPE_NEWS_DETAIL_FROM_DEFALT = 0;
    public static final int TYPE_NEWS_DETAIL_FROM_HOME = 1;
    public static final int TYPE_NEWS_DETAIL_FROM_RELATE = 2;
    public static final int TYPE_NEWS_DETAIL_FROM_CATEGORY = 3;
    public static final int TYPE_NEWS_DETAIL_FROM_EVENT = 4;

    public static final String KEY_PLAY_MODE = "playMode";
    public static final String KEY_CATEGORY_ID = "CATEGORY_ID";
    public static final String KEY_CATEGORY_NAME = "CATEGORY_NAME";
    public static final String KEY_CID = "KEY_CID";
    public static final String KEY_PID = "KEY_PID";
    public static final String KEY_NEWS_ITEM_SELECT = "KEY_NEWS_ITEM_SELECT";
    public static final String KEY_SONG = "KEY_SONG";
    public static final String KEY_NEWS_DETAIL_TITLE = "KEY_NEWS_DETAIL_TITLE";
    public static final String KEY_NEWS_DETAIL_BASE_URL = "KEY_NEWS_DETAIL_BASE_URL";
    public static final String KEY_SORT_CATEGORY = "KEY_SORT_CATEGORY";
    public static final String KEY_SORT_CATEGORY_TYPE = "KEY_SORT_CATEGORY_TYPE";
    public static final String KEY_TAB = "KEY_TAB";
    public static final String KEY_EVENT_DATA = "KEY_EVENT_DATA";
    public static final String KEY_LOG_READ_NEWS = "MC_NEWS";

    public static final int CATE_RADIO_STORY = 444;

    public static final int TAB_MAIN = 1;
    public static final int TAB_NEWS_DETAIL = 2;
    public static final int TAB_MORE_VIDEO = 3;
    public static final int TAB_NEWS_DETAIL_NATIVE = 4;
    public static final int TAB_SOURCE_TOP_NOW = 5;
    public static final int TAB_SETTING_CATEGORY = 6;
    public static final int TAB_CONTENT_DATA = 7;
    public static final int TAB_OFFLINE = 8;
    public static final int TAB_EVENT = 9;
    public static final int TAB_NEWS_BY_EVENT = 10;
    public static final int TAB_CATEGORY_NEWS = 11;
    public static final int TAB_SETTING_TOPNOW = 12;
    public static final int TAB_SEARCH = 13;
    public static final int TAB_RADIO_STORY = 14;
    public static final int TAB_SELECT_CATEGORY = 15;
    public static final int TAB_ABOUT_APP = 16;
    public static final int TAB_USER_FEEDBACK = 17;
    public static final int TAB_NOTI_DATA = 18;

    public static final String DOMAIN = "http://netnews.vn/";
    public static final String USER = "tinngan";
    public static final String PASS = "191f1f632d69180e6228d26849d34d081a3b8d8aa9197eba0f70530ffe698ba80108bfb075c43e82081e245ccb63f6a39107327b2c1d053469bdf4f09bc1e820";
    public static final String DEVICE = "0";//Android
    public static final String API_KEY = "abc";//Android

    public static final int BASE_DEFAULT_ID = 3000;
    public static final int DEFAULT_HOT_NEWS_ID = BASE_DEFAULT_ID;

    public static final int START_NEWS_DETAIL = 1;
    public static final int START_NEWS_EVENT = 2;
    public static final int START_NEWS_CATEGORY = 3;
    public static final int START_NEWS_SEARCH = 4;

    public static final int FONT_SIZE_SMALL = -1;
    public static final int FONT_SIZE_NORMAL = 0;
    public static final int FONT_SIZE_LARGE = 1;

    public static final boolean FLAG_SUPPORT_RADIO = false;

    public static final String FULL_DATA_DETAIL = "full_data";



    public static String getIdentifyFromLink(String link) {
        String identify = "";
        try {
            if (!TextUtils.isEmpty(link)) {
                int index = link.indexOf(".html");
                for (int i = index; i >= 0; i--) {
                    if ('/' == link.charAt(i)) {
                        identify = link.substring(i + 1, index);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
        Log.i(TAG, "getIdentifyFromLink link: " + link + "\n identify: " + identify);
        return identify;
    }


}
