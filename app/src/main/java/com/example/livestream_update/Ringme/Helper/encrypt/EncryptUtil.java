package com.example.livestream_update.Ringme.Helper.encrypt;

import com.vtm.ringme.helper.Config;
import com.vtm.ringme.utils.Log;

import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by toanvk2 on 3/10/2016.
 */
public class EncryptUtil {
    private static final String TAG = EncryptUtil.class.getSimpleName();

    public static String getVerify(String content) {
        try {
            content = URLDecoder.decode(content, Config.Extras.DEFAULT_ENCODING);
        } catch (Exception e) {
            Log.e(TAG, "URLDecoder.decode", e);
        }
        content = Config.Extras.ENCODE_MD5 + content;
        //return Utilities.md5(number);
        return encryptMD5(content);
    }

    public static String encryptMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] byteData = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByteData : byteData) {
                sb.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (EnumConstantNotPresentException e) {
            Log.e(TAG, "EnumConstantNotPresentException", e);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "NoSuchAlgorithmException", e);
        }
        return "";
    }

    public static String encryptSHA256(String input) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA256");
            byte[] result = mDigest.digest(input.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte aResult : result) {
                sb.append(Integer.toString((aResult & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Exception", e);
            return null;
        }
    }

    public static String md5(String input) {
        if (input != null) {
            try {
                MessageDigest msgDigest = MessageDigest.getInstance("MD5");
                char[] hexArray = "0123456789abcdef".toCharArray();
                byte[] bytes = msgDigest.digest(input.getBytes());
                char[] hexChars = new char[bytes.length * 2];
                int v;
                for (int j = 0; j < bytes.length; j++) {
                    v = bytes[j] & 0xFF;
                    hexChars[j * 2] = hexArray[v >>> 4];
                    hexChars[j * 2 + 1] = hexArray[v & 0x0F];
                }
                return new String(hexChars);
            } catch (NoSuchAlgorithmException e) {
                Log.e(TAG, e);
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }
        return null;
    }

}
