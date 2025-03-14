package com.example.livestream_update.Ringme.Helper.encrypt;

import android.util.Base64;

import com.vtm.ringme.helper.UrlConfigHelper;
import com.vtm.ringme.utils.Log;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Encrypt and decrypt messages using AES 256 bit encryption that are compatible with AESCrypt-ObjC and AESCrypt Ruby.
 * <p/>
 * Created by scottab on 04/10/2014.
 */
public final class AESCrypt {
    private static final String TAG = AESCrypt.class.getSimpleName();
    //AESCrypt-ObjC uses CBC and PKCS7Padding
    private static final String AES_MODE = "AES/CBC/PKCS5Padding";
    private static final String CHARSET = "UTF-8";
    //AESCrypt-ObjC uses SHA-256 (and so a 256-bit key)
    private static final String HASH_ALGORITHM = "SHA-256";
    //AESCrypt-ObjC uses blank IV (not the best security, but the aim here is compatibility)
    private static final byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    private static AESCrypt mInstance;
    private SecretKeySpec mSecretKeySpec;

    private AESCrypt() {
        try {
            this.mSecretKeySpec = generateKey(UrlConfigHelper.KEY);
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
    }

    public static synchronized AESCrypt getInStance() {
        if (mInstance == null) {
            mInstance = new AESCrypt();
        }
        return mInstance;
    }

    /**
     * Generates SHA256 hash of the password which is used as key
     *
     * @param password used to generated key
     * @return SHA256 of the password
     */
    private SecretKeySpec generateKey(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Log.d(TAG, "generateKey: " + password);
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] bytes = UrlConfigHelper.KEY.getBytes(StandardCharsets.UTF_8);
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        return new SecretKeySpec(key, "AES");
    }

    /**
     * Encrypt and encode message using 256-bit AES with key generated from password.
     *
     * @param message the thing you want to encrypt assumed String UTF-8
     * @return Base64 encoded CipherText
     * @throws GeneralSecurityException if problems occur during encryption
     */
    public String encrypt(String message) {
        try {
            byte[] cipherText = encrypt(ivBytes, message.getBytes(CHARSET));
            //NO_WRAP is important as was getting \n at the end
            return Base64.encodeToString(cipherText, Base64.NO_WRAP);
        } catch (GeneralSecurityException e) {
            Log.e(TAG, "GeneralSecurityException ", e);
            return null;
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "UnsupportedEncodingException", e);
            return null;
        }
    }

    /**
     * More flexible AES encrypt that doesn't encode
     *
     * @param iv      Initiation Vector
     * @param message in bytes (assumed it's already been decoded)
     * @return Encrypted cipher text (not encoded)
     * @throws GeneralSecurityException if something goes wrong during encryption
     */
    public byte[] encrypt(final byte[] iv, final byte[] message)
            throws GeneralSecurityException {
        final Cipher cipher = Cipher.getInstance(AES_MODE);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, mSecretKeySpec, ivSpec);
        return cipher.doFinal(message);
    }

    /**
     * Decrypt and decode ciphertext using 256-bit AES with key generated from password
     *
     * @param base64EncodedCipherText the encrpyted message encoded with base64
     * @return message in Plain text (String UTF-8)
     * @throws GeneralSecurityException if there's an issue decrypting
     */
    public String decrypt(String base64EncodedCipherText)
            throws GeneralSecurityException {
        try {
            byte[] decodedCipherText = Base64.decode(base64EncodedCipherText, Base64.NO_WRAP);
            byte[] decryptedBytes = decrypt(ivBytes, decodedCipherText);
            return new String(decryptedBytes, CHARSET);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "UnsupportedEncodingException ", e);
            throw new GeneralSecurityException(e);
        }
    }


    /**
     * More flexible AES decrypt that doesn't encode
     *
     * @param iv                Initiation Vector
     * @param decodedCipherText in bytes (assumed it's already been decoded)
     * @return Decrypted message cipher text (not encoded)
     * @throws GeneralSecurityException if something goes wrong during encryption
     */
    public byte[] decrypt(final byte[] iv, final byte[] decodedCipherText)
            throws GeneralSecurityException {
        final Cipher cipher = Cipher.getInstance(AES_MODE);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, mSecretKeySpec, ivSpec);
        return cipher.doFinal(decodedCipherText);
    }

    /**
     * Converts byte array to hexidecimal useful for logging and fault finding
     *
     * @param bytes
     * @return
     */
   /* private String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }*/
}