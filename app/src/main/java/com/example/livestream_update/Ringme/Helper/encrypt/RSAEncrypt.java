package com.example.livestream_update.Ringme.Helper.encrypt;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.helper.UrlConfigHelper;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.values.Constants;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * Created by toanvk2 on 01/07/2015.
 */
public class RSAEncrypt {
    protected static final String ALGORITHM = "RSA";
    private static final String TAG = AESCrypt.class.getSimpleName();
    private static RSAEncrypt mInstance;
    private SharedPreferences mPref;
    private PublicKey mPublicKey;

    private RSAEncrypt(ApplicationController app) {
        try {
            mPref = app.getSharedPreferences(Constants.PREFERENCE.PREF_DIR_NAME, Context.MODE_PRIVATE);
            String key = mPref.getString(Constants.PREFERENCE.PREF_PUBLIC_RSA_KEY, UrlConfigHelper.RSA_KEY);
            this.mPublicKey = getPublicKeyFromString(key);
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
    }

    public static synchronized RSAEncrypt getInStance(ApplicationController app) {
        if (mInstance == null) {
            mInstance = new RSAEncrypt(app);
        }
        return mInstance;
    }

    private static String base64Encode(byte[] input) {
        //Log.d(getClass().getSimpleName(), "Base64-encode: " + encoded);
        return Base64.encodeToString(input, Base64.NO_WRAP);
    }

    private static String base64EncodeRFC2045(byte[] input) {
        //Log.d(getClass().getSimpleName(), "Base64-encode: " + encoded);
        return Base64.encodeToString(input, Base64.DEFAULT);
    }

    private static byte[] base64Decode(String input, String coding) throws UnsupportedEncodingException {
        //Log.d(getClass().getSimpleName(), "Base64-decode: " + input);
        return Base64.decode(input.getBytes(coding), Base64.NO_WRAP);
    }

    private static byte[] base64Decode(String input) {
        //Log.d(getClass().getSimpleName(), "Base64-decode: " + input);
        return Base64.decode(input.getBytes(), Base64.NO_WRAP);
    }

    private static synchronized byte[] encrypt(byte[] text, PublicKey publicKey) throws Exception {
        byte[] cipherText = new byte[0];
        if (publicKey != null) {
            // get an RSA cipher object and print the provider
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            // encrypt the plaintext using the public key
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            cipherText = cipher.doFinal(text);
        }
        return cipherText;
    }

    public static PublicKey getPublicKeyFromString(String key) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(base64Decode(key));
        return keyFactory.generatePublic(publicKeySpec);
    }

    /**
     * Encrypt a text using public key. The result is enctypted BASE64 encoded
     * text
     *
     * @param text The original unencrypted text
     * @return Encrypted text encoded as BASE64
     * @throws Exception
     */
    public static String encrypt(String text, PublicKey publicKey) {
        if (TextUtils.isEmpty(text)) return null;
        try {
            String encryptedText;
            Log.d("OkHttp", text.getBytes(StandardCharsets.UTF_8).length+"");
            byte[] cipherText = encrypt(text.getBytes(StandardCharsets.UTF_8), publicKey);
            encryptedText = base64Encode(cipherText);
            return encryptedText;
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return null;
        }
    }

    public static String encryptRFC2045(String text, PublicKey publicKey) {
        try {
            String encryptedText;
            byte[] cipherText = encrypt(text.getBytes(StandardCharsets.UTF_8), publicKey);
            encryptedText = base64EncodeRFC2045(cipherText);
            return encryptedText;
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return null;
        }
    }

    /**
     * Encrypt a text using public key. The result is enctypted BASE64 encoded
     * text
     *
     * @param text The original unencrypted text
     * @return Encrypted text encoded as BASE64
     * @throws Exception
     */
    public String encrypt(ApplicationController app, String text) {
        if (TextUtils.isEmpty(text)) return null;
        try {
            String encryptedText;
            byte[] cipherText = encrypt(text.getBytes(StandardCharsets.UTF_8), mPublicKey);
            encryptedText = base64Encode(cipherText);
            return encryptedText;
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return null;
        }
    }
}