package com.example.livestream_update.Ringme.Helper;


import android.text.TextUtils;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.vtm.ringme.ApplicationController;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Toanvk2 on 6/13/14.
 */
public class PhoneNumberHelper {
    private final String TAG = PhoneNumberHelper.class.getSimpleName();
    private static PhoneNumberHelper mInstant;
    private Pattern mViettelPattern;
    //private Pattern mPhonePattern;
    private Pattern mNumberPattern;
    private Pattern mMorePhone;
    private Pattern mPatternLixi;
    private ArrayList<String> smsOutPrefixes;
    private String regionCode;

    public static synchronized PhoneNumberHelper getInstant() {
        if (mInstant == null) {
            mInstant = new PhoneNumberHelper();
        }
        return mInstant;
    }

    private PhoneNumberHelper() {
        initPattern();
        smsOutPrefixes = new ArrayList<>();
    }

    private void initPattern() {
        mViettelPattern = Pattern.compile(Config.Pattern.VIETTEL);
        //mPhonePattern = Pattern.compile(Config.Pattern.PHONE);
        mNumberPattern = Pattern.compile("[^0-9]");
        mMorePhone = Pattern.compile(Config.Pattern.MORE_PHONE);
        mPatternLixi = Pattern.compile("\\d+");
    }

    public Pattern getPatternLixi() {
        if (mPatternLixi == null) {
            initPattern();
        }
        return mPatternLixi;
    }

    /**
     * @param number
     * @return boolean
     */
    public boolean isValidNumberNotRemoveChar(String number) {
        if (number == null || number.length() <= 0) {
            return false;
        }
        return mMorePhone.matcher(number).find();
    }

    /**
     * check so viettel
     * th so vietnam chua lay duoc config thi check nhu ham cu
     *
     * @param jidNumber
     * @return
     */
    public boolean isViettelNumber(String jidNumber) {
        synchronized (TAG) {
            if (TextUtils.isEmpty(jidNumber)) {
                return false;
            } else if (smsOutPrefixes.isEmpty()) {
                if ("VN".equals(regionCode)) {
                    return mViettelPattern.matcher(jidNumber).find();
                }
            } else {
                for (String prefix : smsOutPrefixes) {
                    if (jidNumber.startsWith(prefix)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }


    public String getAvatarNameFromName(String cname) {
        if (TextUtils.isEmpty(cname)) {
            return "#";
        }
        //Log.d(TAG, "cname: " + cname);
        String avatarName = "";
        String[] splitNames = cname.split("\\s+");
        int size = splitNames.length;
        if (size > 0) {
            if (TextUtils.isEmpty(splitNames[0])) {
                avatarName = "#";
            } else {
                avatarName = avatarName + splitNames[0].charAt(0);
                if (size > 1 && !TextUtils.isEmpty(splitNames[size - 1])) {
                    avatarName += splitNames[size - 1].charAt(0);
                }
            }
        } else {
            avatarName = "#";
        }
        return avatarName.toUpperCase();
    }


    public String getNumberJidFromNumberE164(String numberE164) {
        // so vietnam thi bo +84 thay bang 0
        if (!TextUtils.isEmpty(numberE164) && numberE164.startsWith("+84")) {
            numberE164 = "0" + numberE164.substring(3);
        }
        return numberE164;
    }






    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public boolean isValidPhoneNumber(ApplicationController application, String number) {
        return true;
    }

    public boolean isValidPhoneNumber(PhoneNumberUtil phoneUtil, Phonenumber.PhoneNumber numberProtocol) {
        if (numberProtocol == null) {
            return false;
        } else {
            return phoneUtil.isValidNumber(numberProtocol);
        }
    }

    public Phonenumber.PhoneNumber getPhoneNumberProtocol(PhoneNumberUtil phoneUtil,
                                                          String number, String regionCode) {
        if (TextUtils.isEmpty(number)) {
            return null;
        }
        try {
            return phoneUtil.parse(number, regionCode);
        } catch (Exception e) {
            //Log.e(TAG, "Exception", e);
            return null;
        }
    }


}