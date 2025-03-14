package com.example.livestream_update.Ringme.Utils;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.livestream_update.R;
import com.example.livestream_update.ringme.ApplicationController;
import com.vtm.ringme.BuildConfig;
import com.vtm.ringme.activities.WebViewNewActivity;
import com.vtm.ringme.api.GsonRequest;
import com.vtm.ringme.api.request.StringRequest;
import com.vtm.ringme.common.utils.SharedPrefs;
import com.vtm.ringme.helper.Config;
import com.vtm.ringme.values.ConstantDefault;
import com.vtm.ringme.values.Constants;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

@SuppressLint("DefaultLocale")
public class Utilities {



    public static void addDefaultParamsRequestVolley(Request request) {
        try {
            Map<String, String> body = null;
            Map<String, String> header = null;
            if (request instanceof StringRequest) {
                try {
                    StringRequest stringRequest = (StringRequest) request;
                    body = stringRequest.getParams();
                    header = stringRequest.getHeaders();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (request instanceof GsonRequest) {
                try {
                    GsonRequest gsonRequest = (GsonRequest) request;
                    body = gsonRequest.getParams();
                    header = gsonRequest.getHeaders();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            addDefaultParamsRequest(body);
            addDefaultHeadersRequest(header);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String fixPhoneNumbTo0(String str) {
        String msisdn = fixPhoneNumb(str);
        if (!TextUtils.isEmpty(msisdn))
            return "0" + msisdn;
        return "";
    }


    public static int dpToPx(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = Math.round(dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }


    private static final String TAG = Utilities.class.getSimpleName();
    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    private static final String RESULT_REGEX = "[-|+|:|.|_|+|;|'|$|@||/]";
    private static final String PARAM_APPEND_UUID = "uuid=";
    private static final String PARAM_APPEND_MCUID = "mcuid=";
    private static final String PARAM_APPEND_MCAPP = "mcapp=";
    private static final String PARAM_APPEND_REVISION = "revision=";

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static int dpToPixels(int dp, Resources res) {
        return (int) (res.getDisplayMetrics().density * dp + 0.5f);
    }

    /*
     * Convert string
     */
    public static String convert(String org) {
        // convert to VNese no sign. @haidh 2008
        char[] arrChar = org.toCharArray();
        char[] result = new char[arrChar.length];
        for (int i = 0; i < arrChar.length; i++) {
            switch (arrChar[i]) {
                case '\u00E1':
                case '\u00E0':
                case '\u1EA3':
                case '\u00E3':
                case '\u1EA1':
                case '\u0103':
                case '\u1EAF':
                case '\u1EB1':
                case '\u1EB3':
                case '\u1EB5':
                case '\u1EB7':
                case '\u00E2':
                case '\u1EA5':
                case '\u1EA7':
                case '\u1EA9':
                case '\u1EAB':
                case '\u1EAD':
                case '\u0203':
                case '\u01CE': {
                    result[i] = 'a';
                    break;
                }
                case '\u00E9':
                case '\u00E8':
                case '\u1EBB':
                case '\u1EBD':
                case '\u1EB9':
                case '\u00EA':
                case '\u1EBF':
                case '\u1EC1':
                case '\u1EC3':
                case '\u1EC5':
                case '\u1EC7':
                case '\u0207': {
                    result[i] = 'e';
                    break;
                }
                case '\u00ED':
                case '\u00EC':
                case '\u1EC9':
                case '\u0129':
                case '\u1ECB': {
                    result[i] = 'i';
                    break;
                }
                case '\u00F3':
                case '\u00F2':
                case '\u1ECF':
                case '\u00F5':
                case '\u1ECD':
                case '\u00F4':
                case '\u1ED1':
                case '\u1ED3':
                case '\u1ED5':
                case '\u1ED7':
                case '\u1ED9':
                case '\u01A1':
                case '\u1EDB':
                case '\u1EDD':
                case '\u1EDF':
                case '\u1EE1':
                case '\u1EE3':
                case '\u020F': {
                    result[i] = 'o';
                    break;
                }
                case '\u00FA':
                case '\u00F9':
                case '\u1EE7':
                case '\u0169':
                case '\u1EE5':
                case '\u01B0':
                case '\u1EE9':
                case '\u1EEB':
                case '\u1EED':
                case '\u1EEF':
                case '\u1EF1': {
                    result[i] = 'u';
                    break;
                }
                case '\u00FD':
                case '\u1EF3':
                case '\u1EF7':
                case '\u1EF9':
                case '\u1EF5': {
                    result[i] = 'y';
                    break;
                }
                case '\u0111': {
                    result[i] = 'd';
                    break;
                }
                case '\u00C1':
                case '\u00C0':
                case '\u1EA2':
                case '\u00C3':
                case '\u1EA0':
                case '\u0102':
                case '\u1EAE':
                case '\u1EB0':
                case '\u1EB2':
                case '\u1EB4':
                case '\u1EB6':
                case '\u00C2':
                case '\u1EA4':
                case '\u1EA6':
                case '\u1EA8':
                case '\u1EAA':
                case '\u1EAC':
                case '\u0202':
                case '\u01CD': {
                    result[i] = 'A';
                    break;
                }
                case '\u00C9':
                case '\u00C8':
                case '\u1EBA':
                case '\u1EBC':
                case '\u1EB8':
                case '\u00CA':
                case '\u1EBE':
                case '\u1EC0':
                case '\u1EC2':
                case '\u1EC4':
                case '\u1EC6':
                case '\u0206': {
                    result[i] = 'E';
                    break;
                }
                case '\u00CD':
                case '\u00CC':
                case '\u1EC8':
                case '\u0128':
                case '\u1ECA': {
                    result[i] = 'I';
                    break;
                }
                case '\u00D3':
                case '\u00D2':
                case '\u1ECE':
                case '\u00D5':
                case '\u1ECC':
                case '\u00D4':
                case '\u1ED0':
                case '\u1ED2':
                case '\u1ED4':
                case '\u1ED6':
                case '\u1ED8':
                case '\u01A0':
                case '\u1EDA':
                case '\u1EDC':
                case '\u1EDE':
                case '\u1EE0':
                case '\u1EE2':
                case '\u020E': {
                    result[i] = 'O';
                    break;
                }
                case '\u00DA':
                case '\u00D9':
                case '\u1EE6':
                case '\u0168':
                case '\u1EE4':
                case '\u01AF':
                case '\u1EE8':
                case '\u1EEA':
                case '\u1EEC':
                case '\u1EEE':
                case '\u1EF0': {
                    result[i] = 'U';
                    break;
                }

                case '\u00DD':
                case '\u1EF2':
                case '\u1EF6':
                case '\u1EF8':
                case '\u1EF4': {
                    result[i] = 'Y';
                    break;
                }
                case '\u0110':
                case '\u00D0':
                case '\u0089': {
                    result[i] = 'D';
                    break;
                }
                default:
                    result[i] = arrChar[i];
            }
        }
        return new String(result);
    }

    public static String fixPhoneNumbTo84(String str) {
        if (str == null || "".equals(str) || str.length() < 3)
            return "";
        /*String x = "0123456789";
        for (int i = 0; i < str.length(); i++) {
            if (x.indexOf("" + str.charAt(i)) < 0) {
                str = str.replace("" + str.charAt(i), "");
                i--;
            }
        }*/
        int i = 0;
        while (i < str.length())
            if (str.startsWith("084")) {
                str = str.substring(1);
            } else if (str.startsWith("0")) {
                str = "84" + str.substring(1);
            } else if (!str.startsWith("84")) {
                str = "84" + str;
            }

        return str.trim();
    }

    public static String fixPhoneNumb(String str) {
        String fixPhoneNumbTo84 = fixPhoneNumbTo84(str);
        if (fixPhoneNumbTo84.length() < 3) {
            return "";
        }
        return fixPhoneNumbTo84.substring(2);
    }


    public static String milliSecondsToTimer(long milliseconds) {
        // Convert total duration into time
        /*int hours = (int) (milliseconds / (Constants.ONMEDIA.ONE_SECOND * 60 * 60));
        int minutes = (int) (milliseconds % (Constants.ONMEDIA.ONE_SECOND * 60 * 60))
                / (Constants.ONMEDIA.ONE_SECOND * 60);
        int seconds = (int) ((milliseconds % (Constants.ONMEDIA.ONE_SECOND * 60 * 60))
                % (Constants.ONMEDIA.ONE_SECOND * 60) / Constants.ONMEDIA.ONE_SECOND);
        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(twoDigit(hours)).append(':');
        }
        sb.append(twoDigit(minutes)).append(':');
        sb.append(twoDigit(seconds));
        return sb.toString();*/
        return secondsToTimer((int) milliseconds / Constants.ONMEDIA.ONE_SECOND);
    }

    public static String secondsToTimer(int allSeconds) {
        // Convert total duration into time
        int hours = (allSeconds / (60 * 60));
        int minutes = (allSeconds % (60 * 60)) / (60);
        int seconds = allSeconds - (hours * 3600 + minutes * 60);
        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(twoDigit(hours)).append(':');
        }
        sb.append(twoDigit(minutes)).append(':');
        sb.append(twoDigit(seconds));
        return sb.toString();
    }



    static public String twoDigit(int d) {
        NumberFormat formatter = new DecimalFormat("#00");
        return formatter.format(d);
    }






    public static String hidenPhoneNumber(String sdt) {
        if (TextUtils.isEmpty(sdt)) {
            return "";
        }
        int leng = sdt.length();
        StringBuilder mPhone = new StringBuilder(sdt);
        if (leng >= 6) {
//            mPhone.replace(leng - 6, leng - 4, "***");  //ma hoa thanh dang 0984***398
            mPhone.replace(leng - 6, leng, "******");  //ma hoa thanh dang 0984******
        } else if (leng < 6 && leng >= 3) {
            mPhone.replace(leng - 3, leng, "***");
        } else {
            mPhone = new StringBuilder("***");
        }
        return mPhone.toString();
    }

    public static String shortenLongNumber(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here  //cai nay ko can
        if (value == Long.MIN_VALUE) return shortenLongNumber(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + shortenLongNumber(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;


        /*if (value <= 0) return "0";
        if (value < 1000) return Long.toString(value);

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / divideBy;
        double withDecimals = (value * 1.0) / divideBy;
        boolean hasDecimal = (Double.compare(withDecimals, Math.floor(withDecimals)) != 0);
        return !hasDecimal ? truncated + suffix : String.format("%.1f", withDecimals) + suffix;*/
    }



    public static int dpToPx(float dp) {
        DisplayMetrics displayMetrics = ApplicationController.self().getResources().getDisplayMetrics();
        float px = dp * (displayMetrics.densityDpi) / 160.0F;
        return Math.round(px);
//        return 30;
    }





    public static void openApp(AppCompatActivity activity, String packageStr) {
        try {
            Intent intent = activity.getPackageManager().getLaunchIntentForPackage(packageStr);
            if (intent == null) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + packageStr));
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageStr)));
        }
    }

    public static boolean equals(Object a, Object b) {
        return Objects.equals(a, b);
    }

    public static <T> T getFistItem(List<T> list) {
        return list.get(0);
    }

    public static <T> T getLastItem(List<T> list) {
        return list.get(list.size() - 1);
    }

    public static <T> boolean notEmpty(List<T> list) {
        return !isEmpty(list);
    }

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    public static boolean notEmpty(String text) {
        return !isEmpty(text);
    }

    public static boolean isEmpty(String text) {
        return TextUtils.isEmpty(text);
    }

    public static <T> boolean notNull(WeakReference<T> reference) {
        return reference != null && reference.get() != null;
    }

    public static void shareLink(AppCompatActivity activity, String link) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_TEXT, link);
        activity.startActivity(Intent.createChooser(share, activity.getResources().getString(R.string.title_share_video_other)));
    }



    public static String getTotalView(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here  //cai nay ko can
        if (value == Long.MIN_VALUE) return shortenLongNumber(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + shortenLongNumber(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();
        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }


    public static void addDefaultParamsRequest(Map<String, String> body) {
        try {
            if (body != null) {
                if (body.get("clientType") == null) {
                    Log.i(TAG, "body không có clientType nên bổ sung clientType: " + Constants.HTTP.CLIENT_TYPE_STRING);
                    body.put("clientType", Constants.HTTP.CLIENT_TYPE_STRING);
                } else {
                    Log.i(TAG, "body đã có clientType");
                }
                if (body.get("revision") == null) {
                    Log.i(TAG, "body không có revision nên bổ sung revision: " + Config.REVISION);
                    body.put("revision", Config.REVISION);
                } else {
                    Log.i(TAG, "body đã có revision");
                }
                if (body.get("Platform") == null) {
                    Log.i(TAG, "body không có Platform nên bổ sung Platform: " + Constants.HTTP.PLATFORM);
                    body.put("Platform", Constants.HTTP.PLATFORM);
                } else {
                    Log.i(TAG, "body đã có Platform");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addDefaultHeadersRequest(Map<String, String> header) {
        try {
            if (header != null) {
                if (header.get("Media-IP") == null) {
                    String ipAddress = SharedPrefs.getInstance().get(Constants.PREFERENCE.PREF_LOCATION_CLIENT_IP, String.class);
                    Log.i(TAG, "header không có Media-IP nên bổ sung ipAddress: " + ipAddress);
                    if (notEmpty(ipAddress))
                        header.put("Media-IP", ipAddress);
                } else {
                    Log.i(TAG, "header đã có Media-IP");
                }

                if (header.get("Media-CC") == null) {
                    String countryCode = SharedPrefs.getInstance().get(Constants.PREFERENCE.PREF_LOCATION_CLIENT_COUNTRY_CODE, String.class);
                    Log.i(TAG, "header không có Media-CC nên bổ sung countryCode: " + countryCode);
                    if (notEmpty(countryCode))
                        header.put("Media-CC", countryCode);
                } else {
                    Log.i(TAG, "header đã có Media-CC");
                }
                if (header.get("uuid") == null) {
                    String uuid = getUuidApp();
                    Log.i(TAG, "header không có uuid nên bổ sung uuid: " + uuid);
                    if (notEmpty(uuid))
                        header.put("uuid", uuid);
                } else {
                    Log.i(TAG, "header đã có uuid");
                }
                if (header.get("APPNAME") == null) {
                    header.put("APPNAME", ConstantDefault.KEY_APP_VERION);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @return : true nếu có xử lý
     * false nếu không xử lý gì
     **/
    public static boolean processOpenLink(final ApplicationController application
            , final AppCompatActivity activity, String link) {
        Log.i(TAG, "processOpenLink: " + link);

        return false;
    }




    public static String getUuidConfig() {
        String uuid = SharedPrefs.getInstance().get(Constants.PREFERENCE.PREF_UUID_CONFIG, String.class);
        Log.i(TAG, "getUuidConfig uuid: " + uuid);
        if (uuid == null) uuid = "";
        return uuid;
    }

    public static String getUuidApp() {
        String uuid = null;
        try {
            uuid = Settings.Secure.getString(ApplicationController.self().getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (SecurityException e) {
            if (BuildConfig.DEBUG) e.printStackTrace();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) e.printStackTrace();
        }
        //Log.i(TAG, "getUuidApp uuid: " + uuid);
        if (uuid == null) uuid = "";
        return uuid;
    }



    public static void gotoWebView(ApplicationController application, AppCompatActivity activity
            , String url, String title, String orientation, String fullscreen, String onmedia
            , String close, String expand) {
        if (application == null || activity == null || activity.isFinishing()) return;
        if (TextUtils.isEmpty(url)) {
            ToastUtils.showToast(activity.getApplicationContext(), activity.getString(R.string.e601_error_but_undefined));
        } else {
            Intent intent = new Intent(application, WebViewNewActivity.class);
            intent.putExtra(Constants.ONMEDIA.EXTRAS_DATA, url);
            intent.putExtra(Constants.ONMEDIA.EXTRAS_WEBVIEW_TITLE, title);
            intent.putExtra(Constants.ONMEDIA.EXTRAS_WEBVIEW_ORIENTATION, orientation);
            intent.putExtra(Constants.ONMEDIA.EXTRAS_WEBVIEW_FULLSCREEN, "1".equals(fullscreen));
            intent.putExtra(Constants.ONMEDIA.EXTRAS_WEBVIEW_ON_MEDIA, "1".equals(onmedia));
            intent.putExtra(Constants.ONMEDIA.EXTRAS_WEBVIEW_CLOSE, close);
            intent.putExtra(Constants.ONMEDIA.EXTRAS_WEBVIEW_EXPAND, expand);
            activity.startActivity(intent);

        }
    }
}