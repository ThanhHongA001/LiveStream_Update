package com.example.livestream_update.Ringme.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.helper.encrypt.AESCrypt;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.utils.Utilities;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by toanvk  on 11/19/2014.
 */
public class UrlConfigHelper {
    private static final String TAG = UrlConfigHelper.class.getSimpleName();
    private static UrlConfigHelper mInstance;
    private final SharedPreferences mPref;
    private final Context mContext;

    private String fullDomainGenOtp;

    private String domainFileV1;
    private String domainImageV1;
    private String domainOnMediaV1;
    private String domainRingMeVideo;


    public static final String KEY = "CQPkng4R1wL@CZT29YDE94A$*";
    public static final String RSA_KEY = "MIGeMA0GCSqGSIb3DQEBAQUAA4GMADCBiAKBgF3g82nB1ImzAwSN7JXeOC7wChDA4Nbzun" +
            "/2B60sB04LCxBt88yRQTK734ugqAJ9cnYYNjwYfzcoTmubiMygsdtoNf1HTmezAL+ppsJxZ" +
            "/TlfomXz6zUS2HxNUdNcgX0NdHpq5OR9713p6tiq5Z4TdYjja9P7FEG8p4xf8snDEjhAgMBAAE=";
    public static final String RSA_LIVESTREAM_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCPqLrEtOnUkWh1TMSAWrlhrUzdeOgBJn4QOai7kWr2OJlLYyRjIN5eLCoME6nnvdDsvWWKZtnvkIU4zjNwu0XpfYsomW2LF1NI3JQzNHPbZdCOyVkyAbyiP0UeoI1iN1DOCQ+ly4qTZso1LT13qlcOMUrsdsboLwdRbXexQU0ChQIDAQAB";
    private AESCrypt mAESCrypt;
    private HashMap<String, String> hashMapUrl = new HashMap<>();
    private final SharedPreferences.Editor editor;

    public static synchronized UrlConfigHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UrlConfigHelper(context);
        }
        return mInstance;
    }

    private UrlConfigHelper(Context context) {
        mContext = context;
        mPref = ApplicationController.self().getPref();
        new Thread(this::initHashMapUrl);
        editor = mPref.edit();
        initDomain();
    }



    public String getUrlConfigOfFile(Config.UrlEnum urlEnum) {
        String urlApi = getUrlByKey(urlEnum);
        return getDomainFile() + urlApi;
    }

    public String getUrlByKey(Config.UrlEnum urlEnum) {
        if (hashMapUrl.isEmpty()) {
            initHashMapUrl();
        }
        return hashMapUrl.get(urlEnum.name());
    }

    private void initHashMapUrl() {
        long t = System.currentTimeMillis();
        mAESCrypt = AESCrypt.getInStance();
        int size = HttpHelper.getPlmTye().length;
        hashMapUrl = new HashMap<>();
        for (int i = 0; i < size; i++) {
            try {
                String decrypt = mAESCrypt.decrypt(HttpHelper.getPlmTye()[i]);
                JSONObject object = new JSONObject(decrypt);
                Iterator iter = object.keys();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    String value = null;
                    try {
                        value = object.getString(key);
                    } catch (Exception e) {
                        Log.e(TAG, "Exception", e);
                    } finally {
                        Log.i(TAG, key + " - " + value);
                        if (value.contains("ReengBackendBiz"))
                            value = value.replace("ReengBackendBiz", "RingMeAPI");
                        hashMapUrl.put(key, value);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception", e);
            }
        }
        Log.d(TAG, "[perform] initHashMapUrl take: " + (System.currentTimeMillis() - t));
    }




    public static void gotoWebViewOnMedia(ApplicationController application, AppCompatActivity activity, String url) {
        Utilities.gotoWebView(application, activity, url, "", "", "", "1", "", "");
    }






    private void initDomain() {
        boolean isDev = false;
        if (ApplicationController.self() != null )
            isDev = ApplicationController.self().isDev();
        // them protocol
        this.fullDomainGenOtp = getDomainDefault("OTP", isDev);

        this.domainFileV1 = getDomainDefault("FILE", isDev);
        this.domainImageV1 = getDomainDefault("IMAGE", isDev);

        this.domainRingMeVideo = getDomainDefault("MC_VIDEO", isDev);

        Log.d(TAG, "initDomain"


                + "\n--------------------"
                + "\nfullDomainGenOtp: " + fullDomainGenOtp
                + "\ndomainImageV1: " + domainImageV1
                + "\ndomainOnMediaV1: " + domainOnMediaV1
                + "\ndomainRingMeVideo: " + domainRingMeVideo
        );
    }




    public String getDomainFile() {
        if (TextUtils.isEmpty(domainFileV1)) {
            initDomain();
        }
        return domainFileV1;
    }


    public String getDomainImage() {
        if (TextUtils.isEmpty(domainImageV1)) {
            initDomain();
        }
        return domainImageV1;
    }


    public String getDomainDefault(String key, boolean isDev) {
        if (key != null) {
            switch (key) {
                case "FILE":
                    return isDev ? ConfigLocalized.DOMAIN_FILE_V1_TEST : ConfigLocalized.DOMAIN_FILE_V1;
                case "IMAGE":
                    return isDev ? ConfigLocalized.DOMAIN_IMAGE_V1_TEST : ConfigLocalized.DOMAIN_IMAGE_V1;
                case "OTP":
                    return isDev ? ConfigLocalized.DOMAIN_GEN_OTP_TEST : ConfigLocalized.DOMAIN_GEN_OTP;
                case "MC_VIDEO":
                    return isDev ? ConfigLocalized.DOMAIN_MC_VIDEO_TEST : ConfigLocalized.DOMAIN_MC_VIDEO;
            }
        }
        return "";
    }


    //todo ----------------test ------------------
    public String getUrlConfigOfFileGameWheel(Config.UrlEnum urlEnum) {
        String urlApi = getUrlByKeyGame(urlEnum);
        return getDomainFile() + urlApi;
    }

    public String getUrlByKeyGame(Config.UrlEnum urlEnum) {
        if (hashMapUrl.isEmpty()) {
            initHashMapUrl2();
        }
        return hashMapUrl.get(urlEnum.name());
    }

    private void initHashMapUrl2() {
        long t = System.currentTimeMillis();
        mAESCrypt = AESCrypt.getInStance();
        int size = HttpHelper.getPlmTye().length;
        hashMapUrl = new HashMap<>();
        for (int i = 0; i < size; i++) {
            try {
                String decrypt = mAESCrypt.decrypt(HttpHelper.getPlmTye()[i]);
                JSONObject object = new JSONObject(decrypt);
                Iterator iter = object.keys();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    String value = null;
                    try {
                        value = object.getString(key);
                    } catch (Exception e) {
                        Log.e(TAG, "Exception", e);
                    } finally {
                        Log.i(TAG, key + " - " + value);
                        hashMapUrl.put(key, value);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception", e);
            }
        }
        Log.d(TAG, "[perform] initHashMapUrl take: " + (System.currentTimeMillis() - t));
    }
}