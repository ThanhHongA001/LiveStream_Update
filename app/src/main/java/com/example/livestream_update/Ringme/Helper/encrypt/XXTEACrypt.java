package com.example.livestream_update.Ringme.Helper.encrypt;

import android.text.TextUtils;
import android.util.Base64;

import com.vtm.ringme.utils.Log;

import java.nio.charset.StandardCharsets;

/**
 * Created by toanvk2 on 17/11/2015.
 */
public class XXTEACrypt {
    private static final String TAG = XXTEACrypt.class.getSimpleName();
    private static final int DELTA = 0x9E3779B9;

    private static int MX(int sum, int y, int z, int p, int e, int[] k) {
        return (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
    }

    private XXTEACrypt() {
    }

    public static final byte[] encrypt(byte[] data, byte[] key) {
        if (data.length == 0) {
            return data;
        }
        return toByteArray(
                encrypt(toIntArray(data, true), toIntArray(fixKey(key), false)), false);
    }

    public static final byte[] encrypt(String data, byte[] key) {
        try {
            return encrypt(data.getBytes(StandardCharsets.UTF_8), key);
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return null;
        }
    }

    public static final byte[] encrypt(byte[] data, String key) {
        try {
            return encrypt(data, key.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return null;
        }
    }

    public static final byte[] encrypt(String data, String key) {
        try {
            return encrypt(data.getBytes(StandardCharsets.UTF_8), key.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return null;
        }
    }

    /*public static final String encryptToBase64String(byte[] data, byte[] key) {
        byte[] bytes = encrypt(data, key);
        if (bytes == null) return null;
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }*/

    public static final String encryptToBase64String(String data, byte[] key) {
        byte[] bytes = encrypt(data, key);
        if (bytes == null) return null;
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static final String encryptToBase64String(byte[] data, String key) {
        byte[] bytes = encrypt(data, key);
        if (bytes == null) return null;
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static final String encryptToBase64String(String data, String key) {
        byte[] bytes = encrypt(data, key);
        if (bytes == null) return null;
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static final byte[] decrypt(byte[] data, byte[] key) {
        if (data.length == 0) {
            return data;
        }
        return toByteArray(
                decrypt(toIntArray(data, false), toIntArray(fixKey(key), false)), true);
    }

    public static final byte[] decrypt(byte[] data, String key) {
        try {
            return decrypt(data, key.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return null;
        }
    }

    public static final byte[] decryptBase64String(String data, byte[] key) {
        return decrypt(Base64.decode(data, Base64.DEFAULT), key);
    }

    public static final byte[] decryptBase64String(String data, String key) {
        return decrypt(Base64.decode(data, Base64.DEFAULT), key);
    }

    public static final String decryptToString(byte[] data, byte[] key) {
        try {
            byte[] bytes = decrypt(data, key);
            if (bytes == null) return null;
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return null;
        }
    }

    public static final String decryptToString(byte[] data, String key) {
        try {
            byte[] bytes = decrypt(data, key);
            if (bytes == null) return null;
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return null;
        }
    }

    public static final String decryptBase64StringToString(String data, byte[] key) {
        try {
            byte[] bytes = decrypt(Base64.decode(data, Base64.DEFAULT), key);
            if (bytes == null) return null;
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return null;
        }
    }

    public static final String decryptBase64StringToString(String data, String key) {
        if (TextUtils.isEmpty(data)) {
            return null;
        }
        try {
            byte[] bytes = decrypt(Base64.decode(data, Base64.DEFAULT), key);
            if (bytes == null) return null;
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return null;
        }
    }

    private static int[] encrypt(int[] v, int[] k) {
        int n = v.length - 1;

        if (n < 1) {
            return v;
        }
        int p, q = 6 + 52 / (n + 1);
        int z = v[n], y, sum = 0, e;

        while (q-- > 0) {
            sum = sum + DELTA;
            e = sum >>> 2 & 3;
            for (p = 0; p < n; p++) {
                y = v[p + 1];
                z = v[p] += MX(sum, y, z, p, e, k);
            }
            y = v[0];
            z = v[n] += MX(sum, y, z, p, e, k);
        }
        return v;
    }

    private static int[] decrypt(int[] v, int[] k) {
        int n = v.length - 1;

        if (n < 1) {
            return v;
        }
        int p, q = 6 + 52 / (n + 1);
        int z, y = v[0], sum = q * DELTA, e;

        while (sum != 0) {
            e = sum >>> 2 & 3;
            for (p = n; p > 0; p--) {
                z = v[p - 1];
                y = v[p] -= MX(sum, y, z, p, e, k);
            }
            z = v[n];
            y = v[0] -= MX(sum, y, z, p, e, k);
            sum = sum - DELTA;
        }
        return v;
    }

    private static byte[] fixKey(byte[] key) {
        if (key.length == 16) return key;
        byte[] fixedkey = new byte[16];
        if (key.length < 16) {
            System.arraycopy(key, 0, fixedkey, 0, key.length);
        } else {
            System.arraycopy(key, 0, fixedkey, 0, 16);
        }
        return fixedkey;
    }

    private static int[] toIntArray(byte[] data, boolean includeLength) {
        int n = (((data.length & 3) == 0)
                ? (data.length >>> 2)
                : ((data.length >>> 2) + 1));
        int[] result;

        if (includeLength) {
            result = new int[n + 1];
            result[n] = data.length;
        } else {
            result = new int[n];
        }
        n = data.length;
        for (int i = 0; i < n; ++i) {
            result[i >>> 2] |= (0x000000ff & data[i]) << ((i & 3) << 3);
        }
        return result;
    }

    private static byte[] toByteArray(int[] data, boolean includeLength) {
        int n = data.length << 2;

        if (includeLength) {
            int m = data[data.length - 1];
            n -= 4;
            if ((m < n - 3) || (m > n)) {
                return null;
            }
            n = m;
        }
        byte[] result = new byte[n];

        for (int i = 0; i < n; ++i) {
            result[i] = (byte) (data[i >>> 2] >>> ((i & 3) << 3));
        }
        return result;
    }

    /*test
    String str = "Hello World! 你好，中国！";
    String key = "1234567890";
    String encrypt_data = XXTEA.encryptToBase64String(str, key);
    System.out.println(encrypt_data);
    assert("QncB1C0rHQoZ1eRiPM4dsZtRi9pNrp7sqvX76cFXvrrIHXL6".equals(encrypt_data));
    String decrypt_data = XXTEA.decryptBase64StringToString(encrypt_data, key);
    assert(str.equals(decrypt_data));*/
}
