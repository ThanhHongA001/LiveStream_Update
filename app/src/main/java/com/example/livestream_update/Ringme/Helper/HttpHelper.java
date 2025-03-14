package com.example.livestream_update.Ringme.Helper;

import android.text.TextUtils;

import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.helper.encrypt.EncryptUtil;
import com.vtm.ringme.helper.encrypt.RSAEncrypt;
import com.vtm.ringme.helper.encrypt.XXTEACrypt;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.values.Constants;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by toanvk2 on 9/16/14.
 */
public class HttpHelper {
    private static final String TAG = HttpHelper.class.getSimpleName();
    private static final String crlf = "\r\n";
    private static final String twoHyphens = "--";
    private static final String boundaryOpen = twoHyphens + Constants.HTTP.BOUNDARY + crlf;
    private static final String boundaryClose = twoHyphens + Constants.HTTP.BOUNDARY + twoHyphens + crlf;
    private static final String contentString = "Content-Disposition: form-data; name=\"%s\"" + crlf;
    private static final String contentFile = "Content-Disposition: form-data; name=\"%1$s\";filename=\"%2$s\"" + crlf;


    public static void addParams(OutputStream outputStream, String key, String value) throws IOException {
        outputStream.write(boundaryOpen.getBytes());
        outputStream.write(String.format(contentString, key).getBytes());
        outputStream.write(crlf.getBytes());
        outputStream.write(value.getBytes());
        outputStream.write(crlf.getBytes());
    }

    public static void addOpenFileParams(OutputStream outputStream, String key, String fileName) throws IOException {
        outputStream.write(boundaryOpen.getBytes());
        outputStream.write(String.format(contentFile, key, fileName).getBytes());
        outputStream.write(crlf.getBytes());
    }

    public static void addCloseParams(OutputStream outputStream) throws IOException {
        outputStream.write(crlf.getBytes());
        outputStream.write(boundaryClose.getBytes());
    }

    public static String EncoderUrl(String content) {
        if (TextUtils.isEmpty(content)) return content;
        try {
            return URLEncoder.encode(content, Config.Extras.DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "UnsupportedEncodingException", e);
        }
        return content;
    }

    public static String DecoderUrl(String content) {
        if (TextUtils.isEmpty(content)) return content;
        try {
            return URLDecoder.decode(content, Config.Extras.DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "UnsupportedEncodingException", e);
        }
        return content;
    }

    /**
     * @param application
     * @param textToEncrypt tong params truyen vao
     * @param token
     * @return dataEncrypt
     */
    public static String encryptDataV2(ApplicationController application, String textToEncrypt, String token) {
        String md5Encrypt = EncryptUtil.encryptMD5(textToEncrypt);
        JSONObject data = new JSONObject();
        try {
            data.put(Constants.HTTP.REST_TOKEN, token);
            data.put(Constants.HTTP.REST_MD5, md5Encrypt);
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return null;
        }
        return RSAEncrypt.getInStance(application).encrypt(application, data.toString());
    }

    public static String encryptData(ApplicationController application, String jidNumber, String token, long
            currentTime) {
        String textToEncrypt = jidNumber + token + currentTime;
        String md5Encrypt = EncryptUtil.encryptMD5(textToEncrypt);
        JSONObject data = new JSONObject();
        try {
            data.put(Constants.HTTP.REST_TOKEN, token);
            data.put(Constants.HTTP.MESSGE.MD5_STR, md5Encrypt);
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return null;
        }
        return RSAEncrypt.getInStance(application).encrypt(application, data.toString());
    }

    public static String encryptDataProfile(ApplicationController application, String jidNumber, String token, long
            currentTime, String contact) {
        String textToEncrypt = jidNumber + token + contact + currentTime;
        String md5Encrypt = EncryptUtil.encryptMD5(textToEncrypt);
        JSONObject data = new JSONObject();
        try {
            data.put(Constants.HTTP.REST_TOKEN, token);
            data.put(Constants.HTTP.MESSGE.MD5_STR, md5Encrypt);
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return null;
        }
        return RSAEncrypt.getInStance(application).encrypt(application, data.toString());
    }

    public static String encryptDataObject(ApplicationController application, String jidNumber, String token, long
            currentTime, String objectId) {
        String textToEncrypt = jidNumber + token + objectId + currentTime;
        String md5Encrypt = EncryptUtil.encryptMD5(textToEncrypt);
        JSONObject data = new JSONObject();
        try {
            data.put(Constants.HTTP.REST_TOKEN, token);
            data.put(Constants.HTTP.MESSGE.MD5_STR, md5Encrypt);
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return null;
        }
        return RSAEncrypt.getInStance(application).encrypt(application, data.toString());
    }

    public static String decryptTest(String input, String key) {
        if (TextUtils.isEmpty(input) || TextUtils.isEmpty(key)) {
            return null;
        }
        return XXTEACrypt.decryptBase64StringToString(input, key);
    }

    public static String decryptResponse(String input, String key) {
        //Log.d(TAG, "decryptResponse-- key: " + key);
        if (TextUtils.isEmpty(input)) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(input);
            if (jsonObject.optInt("code", -1) == 200) {
                return XXTEACrypt.decryptBase64StringToString(jsonObject.optString("data"), key);
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
        return null;
    }

    protected static final String[] PLM_TYE = {
            //LIXI_GENOTP
            "9q2u8Wb8CrzhH/Cq5jrXtVWQjFqFSYMMXttLApDbtkdkCZhKTYBMpaqgISBy0hxFpoS+XnwTKu6Gv0J+5oFzlg==",
            //LIXI_TRANSFER_MONEY
            "p2MIzAGWuS+ZGPigb7hDvgUbQxy1SZ3R4/GzKc+RumovkIlIJmkTfFL3htGccd/Ia9oVL8sVY9d4/lJk1oFwlg==",
            //LIXI_CLICK
            "CpR22OvuF4wADTFxDcZ8ggY2I35TFw1PycdS2ILSUD3ZuuC0x/998KJqKVcp+BGxMcbzlkzjSREwPVKXDdnm7w==",
            //FILE_DOWNLOAD_URL
            "G7Y/K+i9H/MfijX8YOtVgBxYLyn7yqDbnkWA+EtT0ryXTY+ZvBmCptrnFqOhhS+WZ4Ayz/1Pm9OOt2byUG5gk4" +
                    "/RHRkuJ4jSwBW2QzCHk1E=",
            //FILE_UPLOAD_URL
            "2oJ+SRVXMx9n+9mSqnYhcgeObf2LMnFnGQWqLNHHdD8h5ve7BvGZFtvoAVft6mf+",
            //SET_DEVICE_ID
            "4tkw9MPsAgNbhMTiNvxBL/MZs3js9YzkbirMUY8th+N02Ably9jrD4R9bNfIliZtT+KzLWbLi9KKckwlthTO7w==",
            //REPORT_ERROR
            "DFoy8Sm7CljfBidfF5UhggcwXnbaFV/a+X2w1tvg2VWyDAomLnT2WFI2v9cctRSY",
            //GET_CONTACT
            "WI4He+5aN/aTUOCFHSe98XLusrQrqBcjSkB9Cw5Z0GWYdOZCwGUFTTsmn4dXBOaWzY36iX/PU4gkK4Cnt6rhEA==",
            //SET_CONTACT
            "SaVG5PbYgkAm+jkmwllYMFr6H8vceSp0oA2TfVarJiibfkPFLNqeE6umAaq+Wt/75klCgbIhgRWY0qsyrdehjQ==",
            //ADD_CONTACT
            "LtYGx22g9AaRzPt1D3DYfrHHlY9uuSmi6bk6014PzuBYYdCms7TW+sGAQnLuhRm00m/mkmjer2aQTYyBC/QK0A==",
            //REMOVE_CONTACT
            "A5rnUTAOBL794Xuo+oS0KvE2xBITCohtiewkKjCQI793KKMIoaY4+MIbXzkiLFk/li38rwq/xrUPJnjTlplrLg==",
            //INVITE_FRIENDS
            "cQphemRv7jw9kmUpS8uMKn73GyZtRmkWGo3di3+lmw2G4fzTQsLdpEFWnSvUSPJSbHVZP7KwBuCZcjOWoLTN3g==",
            //GET_CONTENT_CONFIG
            "7eZQESTJ04NclkJmOz6Ck59A1BVwP5QC6m4/pf1soAbSeYrIeK0YDuOhR/SKElh0/uE2mBcHSWDPoPnoJrkH1qlTtLg1B/WR6u+k" +
                    "/kCNIgKxbo+D7dJlQHqj59KHJYXVAleYnKqcuv4uxRsLWsv316" +
                    "/4RCjBFM0nUnl0hCEBGTIozueI9lZziBYjRtTaVc9Hu1YyXRiL4u/A+zvCnRAgyA==",
            //GET_ALL_STICKER
            "6JOw7A0rbDa/1c6s+thRkQqfb4vzOrBXsWlh0B8k6YFl4E1aiMOUJysUO2CQ8ZRMdL9SRWk" +
                    "+CJds5DdiO27miSbmKYvqUQogqspziKPACwC7d1mzIV1tuOs6Mhs3oJ0kiWXGq" +
                    "+qC0M1VC4MRraLYJafbNNUTfIEF7fj5nFgwa7c=", //new
            //DOWNLOAD_STICKER_COLLECTION
            "JQ/MWuMJIsLcBFg0c8eWg6Rrg6jTRQGjuA662cVW9amnF5lI3Vj7MNpl/pX8iPV95lqcIwaGDYXfPMjwVk9D5AUUES9" +
                    "+qaHyM8D05KQIvz/YVYUWFdFSXHJeTyuUSWNDWhIBX+NvOqeThMCIYbl9Z7Yvxu6f/TsYEmvEXCSz3nU=",
            //RESTORE_MESSAGE_V2
            "HtSk8BAe+kUZl82jaxjfkwFZqcP7Y+zoQWg10cvoWF28k4+Cx4YA2Yu7t6/JGIWtpAUlJAGXe0IGv8sfQ8DBnw==",
            //GET_STRANGER_MUSIC
            "BWZYkuKqw4FjS89XZwCYQmplt5nhKP6pXZZU7J1EjHwVK7sBPtIaQRiIu9hIigz70sD1kT5Ae/D/qnzi6U79dWYIQyHg/oCkpGXZB2ScFOdzjZJZuhI3t2zaHRh9kbGM1v/yp2pT1cQglKGuUWF3y4FUfCygLMKtU+IH6O4Jbud0j+k5CtZal+Dqp71UIzWmjin1np8+Yzw6qj/KQ5JlQ7kljMJkl8lN+OwGXVOfq3GfXCCwfMmNYJPyvKpjytIAxBO+TWD9X0JALyfX9/Er59sze4i7b3dhgB/YLDoeXyrbyAgQuiw8jwGJpyW7LlQtujLpKCANWAKosPoZPNODnLb8aAAU54TirQAKjm2uJko=",
            //POST_STRANGER_MUSIC
            "bKne8TvidbL0+tIKx+SkvaGV111Ms4herP5yFMgX5r92mjkJ8XEAsufv5V6IIql0sfJkBrlDVHHbvLcqKu7U+Q==",
            //CANCEL_STRANGER_MUSIC
            "ZjEbxaEpvHONrYj6ZXbwbY82rz/KGAfXlT+fFDXuh" +
                    "/FAMiHP9NJqif2xGCXgRyK0OnPalrXnt2VX85VXADFt4JtGRZTemA4WxNNQESoTnd0=",
            //ACCEPT_STRANGER_MUSIC
            "Dt9yy07WgVFD49raQu7f++qdq4mflzetLwan/5x4LRJkMgiFLtT09skEBY4SqkU+OctodljjT0GXcljhkiTlHUNv+m59MY" +
                    "/5D39mllvYKVo=",
            //GET_OFFICER_LIST
            "iJIPGGTyWBjN7MFkF" +
                    "+TNstiTnlsyxvZN8omslBXOgKzS4yrt6s1x9AaUn1Djg06FVCVg4gjQXuYHl5kGf8nwZgDVgRZ9d5uZjMSxbU02iAD" +
                    "/CGb5UgRHLv0uiqqRsky/",
            //ACCEPT_STRANGER_KEENG
            "Dt9yy07WgVFD49raQu7f+xeyPKUgVRtP2N66iD99QPaBE+cwaEc6JqVSHaG0R28+ZFusL0Ydg1zH7zcxlijFLQ==",
            //MOCHA_2_SMS
            "OF+7/LFUbH7Bvq60rgyw4cKAtjz5OHr+tJJzXJ1T/x9JyHm2vd4FR9n+cGyVpGkI",
            //AUTO_DETECT_URL_V23
            "qWP4ddUipI2fVQeDlCwPJXZQ4zM4BctX2lYHx+VuJfAjujUJuo8lz/zey7W4Bf57mAylWoEwGR1gFWKHEXtjGg==",
            //GEN_OTP_INTERNATIONAL
            "+KY3R6MvTuRQih7oIpyTM69RrqxHUx9e8pcEw5WgnNj06UmkbIDoXNx6s8jsSZPtMsuzYVpN8shwFuq3/xqH4A==",
            //GET_USER_INFO
            "pnjWyVm+o5urYHpYsj0jGkxBhUoXMPGL09nH88j3SD9r56tAICGw0HPfpa5hmT55nYcDXaRED6CNFJuSaYAxD/4" +
                    "/eBFXQtpGos7Zf4Fj2VeojQpcEiJDDoqjEv9g0nWkzsdDs/bkMergbxzDpixLNA==",
            //SET_USER_INFO
            "U5otHmSrOfPF6moRgvBv3degVoGKT40SsrvEnFCzW9cR7yrqc346viOR/VK2XIDUTkSP+QSkz4m77RFZssRl+Q==",
            //AUTO_DETECT_FREE_URL_V23
            "gFqstG2xy4nWmUiIVUWg/mSQaW5Pbh6UbsO5pC8kQOM2w2iFDgUZ4/CPFD2F3dfh/nvI/j67PHv0mV41rHb/vQ==",
            //GEN_OTP_FREE
            "aISZSWMu+0lsSNn2qugbwFd00/Ac35ZP44YzVv9l+2Vthb/aWPcUs2tdbez8ddnmeafLfGKhl3iWnegUmhxjag==",
            //AVATAR_UPLOAD
            "OUEuyM2olNUalYacrEQ7YTpsv/ACF+3QVWH4gmsCZtDrzGLXjGjNdxobtk1/wvA4OVVJlF2VPNXc46aC7umcXw==",
            //INVITE_NOP_HOSO
            "R59XpqwV9wn3zbo/YT9STZc2rYn66cTMef5QI9DEaSRno96du6OCNW7l0pKkmSXVGv5Ou6rXr8jGmHJYKq4eRQ==",
            //CHANGE_STATUS
            "3ER0XMxldKkjcNBRxZNItu2Ce93jyA9Hia3xG0YwJpJ/IDnP3qNgQn/n5mhDzcNCX6CLhoHbfpBZH6LEG6FoWw==",
            //ROOM_CHAT_FOLLOW
            "E3JwyO9uwCkaXlNKGr0o+B9BhanZLQi3kQsz999elZJBBpHl2AhMNXfIXppDhjwAJuz5iSWD2aidRuLXIB1DFg==",
            //GG_TRANSLATE
            "ZWHaJ7ZqZYgJUWz/Uu455gPv4d8/H2GEDuHvmAALbsFgjA9kXsWy8l9C/ZVbSbV6wJTYUAtK2hPwWJxoiAMFMw==",
            //ANONYMOUS_DETAIL
            "RBS+MtfQfiXWvdlRfC9zj+sZUPwavATQkK1YCm5XNfIWLdJfssWUJ957UMCj5Eis0sx26f6B6Wg8yRb8xvOpRtkT" +
                    "+4snAYz8Uq9r65HhMzlwZdUdcTp5EHZd5j9kEXoTvzbMbyowRNxnFuMckICIAIEqicwGjaxv4YxVLOJkIMykbfdK6vx" +
                    "+6KX0utLjkkXU",
            //GET_PROFILE_URL
            "73J/RreRA7n0hr2uTNUXLEbI5OLSKdf1wzQLmhUKsNBUq0O7r5eMx4lL5gbOoIwlXQ1XxgVyP8dcoUoE8zBblRmNL5ejGwYH/cq3HH2NsNmoLjik3o1Kg/HAnMQ4Zbqs0r/sg5lZR2XPSCuhS8fIpw==",
            //IMAGE_PROFILE_UPLOAD_URL
            "Bm0NutG0OvjYElzFPjqyQMCXm1OcgRVgrB8HWwgSDWcablWV7DejLwXCHaf+8gYPQeZli9NEuUsoS2cXc/tF4g==",
            //IMAGE_COVER_UPLOAD_URL
            "Erzt6MgwEjw8HZMkE2WMcl5H3YzeOMQavPJxnsTmN3X/3ZJ6tJ4T07a0mZivFzR4",
            //REMOVE_IMAGE_PROFILE
            "FTqgACK1nTtGWf0NS00CCQR5OkcgZM/9Tb/qUcy8I1PeI0FZHFvQvYHLfYb77K1MUY/AI0TsiyNoRcaX" +
                    "+dW2iCpsuyRMKyrkWDtjf3yi2WrWfy1P1n3Oe06fna52qwNFDEqc5s0wxUhVTzCux7nIynD1f7vEb9VEYyRsoi+gFmU=",
            //SET_PERMISSION
            "tpRfHCglU/0X8/7nAZNiTUYxzqiQtDlOJkvDuGHAZci8dvQv96LpLHrL0M03dJKtBMQq3q0KfPkbhUwFL/bE31DAyyZe/Mfh4ztnYp8" +
                    "+oGUWDo+glej6G/tcyrhILkBZKHUCnO/c04ePgybYV2U/QLB6f+92cKSEpcUYplAED1s=",
            //ONMEDIA_GET_HOME_TIMELINE
            "23pTBiHBc/1V0iDblEpn1HWHUJyVXHMdhGrjTkN3V2kj7qJClsP81X86kHE4wPlc3opMANZkZh5/6Fq6Lm5QoSAQU1Y3Y0c3kFl8U3" +
                    "/hDKdEb/1ZxbkLeqI2hVEOHNWA",
            //ONMEDIA_GET_COMMENT_V2
            "QxAU08MWBgOqg2LU5GkjfdGdg1OtsbTZ1MukM3hZcu6ZYCDrMYr3/zPFYMUMf03JL1Tv4b0+2q+fxhoGQo+OiWGuRCt6EqNWHhDJlWheLMU=",
            //ONMEDIA_GET_LIKE_V2
            "4ICfEmeydffoSXp/k95zFPJwXa7RAnegVqONpimYk2N1xrkakk6u7p6gRrqB+PAtSr27TDyiQRin/v1dQxSjpeGvtwtN6z9Yy8sNlk+uAfU=",
            //ONMEDIA_GET_SHARE_V2
            "c9Fypabs4GpK0twoUIqD7NNEsGXANcOzX0/5/XDryUMq5ZxoM1JwlzD2VW7q8VUfrKvTCI2ZXPYvcY1Zyn+oB6+euxutYyb1AFh+wRKA8bs=",
            //ONMEDIA_GET_METADATA
            "yqsumd1dO19pIRtHAkB0EQz2smgGVGeGDxa+eDWj1LLysf+iUV0naoLfgmaEd5PmrcHU7EKm6R1cTxO4H6tigOfS8sMMnnDhic" +
                    "+LCJC4mxk=",
            //ONMEDIA_REPORT_VIOLATION
            "DEvSnsqD+/baAWKKdV8OWDktS8MLHpvx+RM9z6W9CVazj2EU3GFJxRNG0W1D+p/GbIZYvlvY7JyuKf2zeu2OYkNyKAynOSZXJHhm3" +
                    "/KDc/I=",
            //ONMEDIA_UNFOLLOW
            "cAnVmEsUBaUvOT63CNNs0/clGPfWniWNccsCqBBmjGzADENbghBIZtKqnJaRBht5cRLwQQVQVAO3zYtUkfbBIg==",
            //ONMEDIA_GET_NOTIFY_V1
            "odDnTgMBiH4eW8FJnAWD9S3Z5HLeCzfRr2N7Tjdc8NqdhlB3Z4lNM31ycVKYWz0SnUu3PXXn59UWImqI812RGpcM64CwVXrsQ9jfvLjWslo=",
            //ONMEDIA_RESET_NOTIFY
            "5NA2zWZmjWf8i8VK7HgzKci1eFKJbWccjVenvNqCCKJkvILeT2DqEAG96wsLSBjuGuWe94coBtpj4Ytidxa9Se/rsUj0XB839vLK1bYU1bk=",
            //ONMEDIA_GET_NUMBER_NOTIFY
            "+UTlKEc6sbJ0PO5JhEZ4kSg107bog/5mH8SBhqyAPqR3mkhJweVHU/gpH3MMooHxQ1nWRbowpXT2BeUfzMPoH+IBzThmlpk48b" +
                    "/4jQB7vso=",
            //ONMEDIA_GET_USER_TIMELINE_V3
            "bL7nClaYN5GmSWuq89xbMjoHht/M98Cp0Q3njqMSnpsQh/YqN54IX9GBGQtjaCL1Od6SCm1AwmjWPsnZmdMRkDS7rmjJmLBIFQ65vZ1ijrx09d4yCsztUA4HhZdnHKjI",
            //ONMEDIA_LOG_CLICK_LINK
            "ufKmvQLMxffq7dUbAMvydPwpuq/tGYEAnszBu8d7i5yo/okk7V1E68uC7Ay7MYkGgKolUfC+4CIYYhyrX4OKtKICbQMnW+BxDbGdy" +
                    "//zMdg=",
            //REPORT_ROOM
            "cTLbgW+uLfrpzLZrJpjyed5+dfP4rSg6xXHZ3pDH4NeEMVBeGeluKg4IXvoDPQ1QcHNLVcYsejsIuO6HzeAtcw==",
            //LOG_LISTEN_TOGETHER
            "9PLjHdmCayiHR2uuRiD+Cqj60DO0n2yDCRVf5ywU4ieT+5ypgAgq42KYpHhN99rFTRbbQ4GSRbiv71Lry0L1Eg==",
            //GET_PROFILE_V2
            "xLNjbo64sl9B6zDuWwKk1CAQ0uY0Fp4f9nS0ShFVNBiu6rVVfHBG8ghzkWDndqpzTUfOa04rQOMtGvUdPZuzoPTJWcR3fqINA05IeZ0Ky9iAUe2XtpX4maf8xfjJvAkcZ80p4am1VUMszpmOUAFreQ==",
            //GET_FRIEND_PROFILE_V2
            "ubml8+71xQQyRMghc2NMP1hthilgdUDsDhOcCDAXmb/sf6E1ZrPGjzs777uuj/eRFGntthP7pKTGsNymwc4bXa5/z9sS3pUZG5ls8" +
                    "/rHEacvDzby9BPv4OEVEkVOaXDmGWgoPVHDxe/firBMWFCpbv6FeYgoJQoO7QSd6MuaXPQ=",
            //GET_STRANGER_STAR_LIST
            "83yQlcoriDPRYb8fappT/nlGxVuRBHzIVkYSZrB4t6/kpBfCrULAY/Cu7H+EzNJdrcpOTuqIM4l" +
                    "+GFuaMxvIDo5WepsfVdQtss9N0vVCsmpOKzrUcT51QjITfbVjibo8Y23NChnJFgjuYsYFBCsQAz353qQYtvsqfTZEZSbEIzq" +
                    "/jf3eTG2BLYcoZijsv3CmMjZKwRiCAL1frccr6he24vbpiZu+S4T8ISJF2dji5E4=",
            //GET_FRIEND_KEENG_PROFILE
            "heLR8wa8uoBwsg9ZWLc0pl5snK+NDyOvELCNkx7AsWl+xayZltKqzX6mtZlalJmFcpM2SCPqevzysR7dl5VdlClM+1WzhmVQ" +
                    "/FcYvRGgJV14UX7hi5U0CbTVQH+bk0HTfkSw9to5/gi+JV7H0eyjjA==",
            //CRBT_GIFT
            "J8OJKddhrMmy8r3vhVEgVlSKvgcDwSJzlO3KKY/oZh0jTGejrLhDcCDVyZRtIKRCvawMoi54u3NDQ4IjLat7sw==",
            //CRBT_ACCEPT
            "HDZ3n+vrV0KAf4eZ7xLW+foiqiRnWFlqdQCeZFWkiSWu2cV0wQlSUPHCT554qNH6Tlq5A5Inehl4INX9I+5a/g==",
            //ONMEDIA_GET_FEED_NOTIFY
            "jZlD3NJEbbDZjBvajIU49KAsV9qiVSShS9IOPQd4sNNQC7w9EQ45liks/HF1B+0a9hfbKlnMrvy2FMpy6MoDUer" +
                    "/bBzGaVHGkUHKnj10Hnw=",
            //GET_STAR_ROOM_TAB
            "6IgbYryAqA+W3XvtvMMuaRbpGE3Nk7XP2wvwpp61/nRvlYG4trYpQvhbuZzUFAreeBMpgcZjdZGfI9ddccKZ" +
                    "+49lx45BFOLo4H7C73xQJ7p+Tus3+VP0w7feERw38XwaW3XfEK7HL5UulW+sPxwpp34mTmvAvVSx5xk07FTfY2EG" +
                    "+1U4tP8Jlt07iugKo2+b",
            //LOG_SHARE_FB
            "224oxQJEDWFvYgBPBZBiJtJYAldZe5UoNA6DMqg8NMTSGTZNoaEj1VVG5q17kEmkyPXDwwtUL4x+kxjvRKMazw==",
            //GET_APP_LIST
            "e2IlZ2d/iBoQrryYx2rsXnqpkbL8KQ2wEYyzJ4UXdfdQ44rdAqba0T/nkJL5c7vgEZWydej8vjyOnPLP23TniGAvF6F9U" +
                    "+yzOjQH7UDNIrUQizgLv7gvSblSfKNx/ZFaKN+6gMBVIDSNbT2AbhKj4Q==",
            //SEND_LOG_APP
            "h6pTj4QiXhkzp2qN265LXXDMCCa6p6h90qBaRtHk/Y2vBv32xChRumXiZlPJEqj2AK3qIb9WsCuI9QN9oCMmbA==",
            //GET_METADATA_WITH_ACTION
            "IKmyOCxXb7DuQhPfbij1RVZyTnAPTfJCQF5ggRJUGxHqAsN2vYa9DDyyZ+HCK3ZhMht" +
                    "/KqERYcamolzNfI2lSuXQMxmSw7xur68QKiwD7uI=",
            //GET_LIKE_TITLE_V2
            "aZzB6UgIyS0ny9C1B5W4133mGaIsxhOqtCq4r0VsWMY/dLcf1KZgZv8NLsk4vKMbb1UptPCFKzTBu9mBri12RiLpAKrxvTqlgfjEHLCzEEI=",
            //GET_PACK_DATA_INFO
            "gXoGxyO+bLEIEyntBUaQb2DjKqonoF5" +
                    "+DtZCit7GDzmXpjUFMJAMRsrSfP1Qqtl7V5FVvrupSCmmd5CThd0pBdHAX3AVT4xUjOd6CdD8s6mQk7tde" +
                    "/8fM3QmokHwKAY9W675rWBNX1GerCyLypcQ4A==",
            //FAKE_MO
            "winvr2UgyVbf7VWP6qNbI2NahaF72BJbu8gdfia6ZTx3Yw9N/7+oYrKTz1qDr08k",
            //GET_APP_DRIVER
            "fUdRq0m+7pxHY9e96PsCn1Y9JbPiA2QTSUe06qt1XnX3RXvYfpjVBEJPFmtG4xbAGukQBHt++VSPXbaWjd1wXR3Ft4nAX/R3V8i" +
                    "+64tYWwnFyhxL8y4NKqmPfqlBYAP+vkvNpnub839QX7hbTSTQ5vJi1ZJM1cwnpNBvEoiOo04=",
            //REDUCE_TOTAL_NOTIFY
            "TCV8j+rV0jI8vdx3Ja" +
                    "/QpfPIY4AxO41tsngUr7Ydh6mQSv9pKS1TmddmATu3qjffm7dr3iXaK6nLRLOj2PrlkdzvOS9ssrcdzMSQXQlU1Sw=",
            //AVATAR_GROUP_UPLOAD
            "kGy1du6bH3A9LmEkPtuYVb4rNj39TR5J4rXiYZrQ2rJqZzXZtO7n8yqVs1yo6gxR",
            //AVATAR_GROUP_DOWNLOAD
            "v7uGmL+af44kgaQ+lP21DCvJgJ6DogjvxDBdb0zIjUZwmCs+V+kJnsonenOVCrtWuRkCERHiGnC68j0kQMO" +
                    "/cirOAYXgJ6rK0BfynH70O5Vd4fLq3BMrxo7loPATrffAo3fDieeS24+xGGIrbbJC1A==",
            //SET_HIDE_STRANGER_HISTORY
            "qxXzkuvzXNjU1Tx/OxNDlDZkissqpvoEQBojNeZ6UuUoER1Q7DWRmFXX9L5uVuI2R8OE73roFLKmjLVys3r3UJstocmLlWdgAC" +
                    "+EznYWlp072vvbp94LjjzciUvdPv4K7aplsPDZYjRrKLmZO0sfPzdn0q4BDGeqLO2hPnb+JgNTap6J4rwBTVEzONF//wx0",
            //APP_DRIVER_REDIRECT
            "iU8qfanmHwfnEiB27cBG59deWk4o0Z2H5laS3RQ08rJsqn5FrGOTL2Atx" +
                    "+1XiPG4r3RH9aC3ezWAQkpM3EKvY3FCzwxtTtgCcwVlf4ih39ZM3abpRUOInCYmbRQwFIzCiqDbzMpErXpOtnaWWDqW9mqBRIOXuHtum/zJYBX3zREaNhgH/zmHn58WFD7wEjtn",
            //STRANGER_AROUND
            "FtJ3Ia/k80xqZgeKRs0PKpK/02Fs5uvoHJiGOOvlInguW81mvjpZws89XAYRs8qHFoccmNVJPkNBrQI0qBmB8Uh" +
                    "+/ncEWjmlbSzOqqapyjA=",
            //ONMEDIA_ACTION_APP_V6
            "jADeOC9JNPGCmqxLqZAdtHQTjAz0+7I1gQbQfS8f6I7dEPDiBpbDhM9nD2dmFxJiIL56gssiL1Fqr9F5FrPM07JpYBnHSseT23ku59S+0DY=",
            //GET_FORTUNE
            "jEtHZolUyQtWt554D2ShYp4EOpXqkwq7aEYG/EHTbX+0c7BgbFnr5ncql9YZU8GYzK7mrLQPBCPJgeos0q80Cg==",
            //ONMEDIA_GET_DETAIL_NOTIFY_V1
            "4/r1pYLXmRnzsWb5nzF9gcAh0SKz0i3qO5MSbJsXY+zHuQoaoYLkaCnA8m2yzw9yt7ugQT3tnzorjooOwii5XSc1uh+yAfVJUQcYScC7Uyy48zaJDR2k6EiSoUJzOQsi",
            //DELETE_STRANGER_HISTORY
            "/Cm/slXVzpXTFadP15uDtXq3KaSVUO6IwFCPQONuCutS3ETaAZY1pVAtcu22gHFsSPXeHhOVxJo+SJFY/URXvQ==",
            //ONMEDIA_FOLLOW_OFFICIAL
            "QgEbPjJi5ORcD7VlxJtW4zJiCON1HPXZSAtj3SRjNTIJBhhrqWC6K4KQUGon1M6eOPqh7BUTi60EIozEVW+tbA==",
            //ONMEDIA_GET_PROFILE_OFFICIAL
            "W1IHKQvXbGvg+CW1DLufgACAIUpZBzj3Bj1fYRr6LZhBiJOMzuSw6o9LxkybpPZctB0Nx1JmJM/w2UucVwLAobsFS0Gy19qXhydVQ" +
                    "+yijEM=",
            //ONMEDIA_GET_ACTIVITIES_OFFICIAL_V2
            "orI5/OHdWsreo25BLQgGUUEMDAfafR1IekEMYAO91G/k20n+cXUz6y/ihDEbD1fw+rWBHpFZ55HkZFLk4ZwSQid9le8V1VbW/61wIfJauR+hPRqBgKS3CcFcbJAPUc/S",
            //INAPP_ACTION_SMS_SENT
            "lGKO01Ds4CtpBoj/sgLRv3XP9QOn87N2aLroJjxXNTkQ56UtAPEqxMrxGjosMqTpyVUQoFWO7GyAyNHn6mZKQQ==",
            //INAPP_CHECK_FREE_SMS
            "BJE56+QTgrT2VwBNbUL7KtdigKWSMHcLzERME7fYAKJo6LQhvdlk8cEft1zTzLHHcbTQ0dcm7uy78zUla4WUC9MAXMMxdwusXjIyOPB1" +
                    "+JW/gNo5APKJQV+YTTAk0EWXZHA+co22AzrVgf0NdY9dwYJCc3KlMI47bCSuShr4fCMgYzmT3CQf7hJJz+wsn6UM",
            //REQUEST_INVITE_ROOM
            "oa8PZQNz0HEo3eldX+rPnF7u6i8YdffirgCpeoBI+GmhZUyKejqfEKn6AoDLuvB35rV9HRINDwlxTZ/NQZ6EOYAOv/1P5" +
                    "+OaZTM5YHqY0fw=",
            //SEARCH_USER_MOCHA
            "OzKM87rvLKPas2eCUZSZczf+vAXLPur2rNOVnDP+gDwS7AwQAp/atVlc2EENFK6rC9YU+oAm6/Fp7wx/2qe5uA==",
            //ONMEDIA_GET_CONTENT_DISCOVERY
            "QxAU08MWBgOqg2LU5GkjfcnOZ3TpI6V55ZDGET9Yz1tOLG8ACwg8yMOITDEOkjL996kRC7VfoXzDr5PoWO2CF+IS" +
                    "+EXwvnoDkXXbLRuBPjzlQ6shYJx9DjvhCwZSj7pr",
            //AUTO_SMS_OUT
            "u2gfvbSi9bMLuSbkabWpIC1frMxFj57eAt8pHkRYaX2uHINaToHf6ZBskVl1XoMLXap3NgZURvqB+RF2sPAufPpxd" +
                    "/6AbhGeoyUNMY7u9Vvpo8Z4HYIJEhR9DRKh//ki1cxNP6rPBOA4R8yG2eR4T0JZzr+OwFTT8Eh99nA2M3U=",
            //SOCIAL_SEND_REQUEST
            "c1hflUPC7EXV/sqPQkTfIKDX8V6jGa5rsPmf75uOKok56iB" +
                    "/CoYTYAanfxq65zjjxp07n63wwDVzenvB4CgTjedbvBEzgY4HMx83xj909SrLuetDwvAsMeKZsBgXt59X",
            //SOCIAL_ACCEPT_REQUEST
            "dkjNXkZhMnEDG37X5awd/n0IIb2Wa+aOOCWKQ/T1ORjhxbTyqU/+MiDwEiOToR1fcHQ0Lu3" +
                    "+hEppy1YvOg8CmBrxJ5hm8yWMEEvtMeMfuQhn+f8MFtVwcHRDmAvOIZwf",
            //SOCIAL_CANCEL_MY_REQUEST
            "gM24KsaAdjH9hqwoNgMC0aChOdrWiS5q1c4JDXmMNlfekzqMKw/sl1sfhpD" +
                    "+tEFiqaBct9K3yAWYcRtCaZFRDHc4xZKYaOHAoXTNa6ebRj1H1GBOGLd96jxmTP+tNMim",
            //SOCIAL_CANCEL_FRIEND_REQUEST
            "gM24KsaAdjH9hqwoNgMC0cFzKPBCLdHyhQw1jFexJzB49PRNTdI9KfL8qY1nEGCm+tkR" +
                    "/DgtPIiUBtrHYxysY1k4zNIfT15m1u3825rdjMOBRGFAIzRTaBWM1LBzws20q+Uz84TLRLHeYWQliU6BwQ==",
            //SOCIAL_CANCEL_FRIEND
            "gM24KsaAdjH9hqwoNgMC0XGQk/AvQXsLfzXqkkERhoQHCQPsK7AGor6FlxPXGU" +
                    "+i344qFM6vyWGE9KMaw8cZIz00RKCO3MdZpjReykVTlQw=",
            //SOCIAL_GET_DETAIL
            "QjvvPKI/gVLUED30KB06WVDR4SLF5vjaejGQCLvXzRhUDvsO/GQMUCxyvOqzOoo6XTFAEbWljwF9und+ro54eTxYitRcD7KqfBWjE4" +
                    "/AHUaTkclSYjTktCGoizQ4qGisKbCpGwfXsV4BwNwNwG+tjUkM2bMIBW8r9R8IKtcKGCd0VhHXSZIijc7xFYnhl+MY",
            //SOCIAL_GET_FRIEND_REQUESTS
            "pYvYkNru+/qPDCT/O81PJtqoAvlrTvTRbo+zqqHYebMx9SfYHlCcOqIASliMAONxnoUVDQeP8v4tCXLzkacjEKM6e3CY" +
                    "/uErn7sLAN6c4Z9OtpyNyzRXIokaXJgzDldmXFZXNFietsEWmfScilhT+SAtSK8crecfG7RZuTN8mL+zrSGdM/lpUCBDTMo" +
                    "/fO3ZLZ/fblCFbg/WddvblEQAKQ==",
            //SOCIAL_GET_FRIENDS
            "pYvYkNru+/qPDCT/O81PJiV1AfEvqMR0TXMjaQVysSLPKenxRexvcNGTRk/WbtZ81iaDVIwCcijVYrmpnr5I0nBql4" +
                    "+b4GxwwQRJ8h1Jh+pp0p6V3H271tDf" +
                    "/SjuKcoqLW1g5QYxiPbmKuXswONyiyc3PV5LdHjJWbhUMxFTZft63Ja4fGDhaxbYrgP2T7X3",
            //POLL_CREATE
            "TjPtcTKVTNQgmF4OVJc4n/oH7Ns5IM4PrjCwpCjfpgJuE+pAQucOVhQ4WbKHWWNsReTUEZCIrSgNsSKp3X2H/g==",
            //POLL_VOTE
            "fhZOht1WbYgxOYj08QrX5CIRnQeQj05i/nnxFlQdU1+gBXxnR650LN6gK1lZTQTt",
            //POOL_GET_DETAIL
            "gk1YUQxIlPaw8BibK81IvzQdzEkZFrQdVSZtEQqedazlwmaojq+ekSie2pb9pADTw9NumhvWJjkQaPwLiOEKGBkCk" +
                    "/8YxCxHkbAsjmpbwoJ77OKfkK9Cqydw+zyLf3m1G1ccD2QqL/6UIEjtKuJQRQ==",
            //POOL_GET_ITEM_DETAIL
            "O7nmwkKrX4C80G5m5AyI41vdf37tSZUmYwRhQeNc3ZVFnI2U6Yn4I3FEhSNUNWoYI6Z6wYb" +
                    "+WGQX3mZ3CkG7Xbc3Kb9nrMuqdwtRd5GQ0esA50obDJRrSAlYq4HhDR04fCuVjMSpMK9mQq0w2jmhsFvp6eSYYV9M5lsor3" +
                    "+gfWb1MH1okhb/htlJdbzhJ/yN",
            //LUCKY_WHEEL_SPIN
            "fEAb8ripzPcOV4AFSAt/N+wNBYAmFbyhQuoKZdVQH1w2e5AU25xG3FsY3WlLeTQR8nmAwyVkzcTwMmZa08gbrA==",
            //LUCKY_WHEEL_SOS_SEND
            "ovOXc23w7+vuDMPbOBScEzL3BnoM3FEPn0l2xe7E3yHewSf8FEAvUi28CZX3xnz9Qk3oPuszzqdViT4K83uBxPuku7EaEg78DSZVSR" +
                    "+Bq3g=",
            //LUCKY_WHEEL_SOS_ACCEPT
            "ovOXc23w7+vuDMPbOBScEwNKfWIlAVb3nyfAi4mwgnF72MJ4vZt7ci6sgOUI5fAF2U6GZJQxUATGB7K8Sy1mP+U2Nkt" +
                    "/OFolYXpT0uYlEX0Gt51h/M3Yn/BoxcCBH8rP",
            //LUCKY_WHEEL_GET_BUDGET
            "KEN4q48P4BTdPE1L/p2W65VGGtH7KXS/9+htaGn6HDhKlXehtEBRX2yO2oV+VPPRe4gXe5PswEl/UK2pCtfEoCzs" +
                    "/9xBlI3MweqLedvEDnuZ+iOaJ4OnMW80pgADgCI7edZpkOLvnLmncEXUk" +
                    "/GitMUlv8KA8lt13sR1CFEOweEVl5GPaXgm1peIMsCJmoQjwrBWMB8gqGlT8eNftUSoHg==",
            //ONMEDIA_UNFOLLOW_OFFICIAL_TIMELINE
            "cAnVmEsUBaUvOT63CNNs00mhMV9QG+hmh9XsC49fPgvmkAuUa6/4pwq0QXf93PePxQeJQsbiBQvkWW9D5j" +
                    "/eI0YtZXOihkRTsoNtT51gM4zT7hx6sizXlPj3HOPE8NHA",
            //ONMEDIA_GET_LIKE_AND_COMMENT_IMAGE
            "4ICfEmeydffoSXp/k95zFAASWZV1lc6IuQ4EsKMdJgt" +
                    "/fZ3SKVa5A4cHt7KTo6s0tg0s6CeyM1Byp6UVtEBpVtORVCyT89VL1JT3rvubffiT4/nOlMz+zHNvZdvAwZqB",
            //GET_LIST_ACCUMULATE_V4
            "vVrkK/97p7JV8/KPASrjVgaJSNAVyUFRLq8D8m9Jnzx+gLYwl38Kn322jc4VRByQwN0Voc6k/4cTeZKRZUS1ZXwnlwxy/iuVTgqVJu+8gCw=",
            //LOG_ACCUMULATE
            "9ERk0/I37kvuMR5sto+aRbr/OjrAh3QvFsqkiCexKh6OxWbXLMlNZFI/ZtkVuo0q+AUdDuOQI2nNX6Oh8VbyAw==",
            //ACCUMULATE_CONVERT
            "Pj50quaqRWsxljX9Uo4+l2l0gXG5WNgFw8XYD9mJb8ZEr6UpodZBM1SgEhjzlKWK4fEQmF7ZOiHNpwRtUf+J6NoSXPuuT/ReRaqs0RuCtGU=",
            //ACCUMULATE_GET_POINT
            "78ycEqGKNBXxBKF8VSAwpr6mIbnka3mmRyxSBktGNnObmdrOH23J1/bcu5kRLLb+n5usSs/0kkEGu5S/Xb/OEjnX9D3Yg5rhGmPik+4KBC4=",
            //GET_SONG_UPLOAD
            "bVKMH/Wy7kxYtc2xbDH8/Mi3oBT0rDo7Wh/ErBR95p0ftj1C5IcZ+SXSJyFmMHRRCOgB93b40SqURGpqIQaJsDJptfECfuo/eIUWH9bahYTKl9fG5zfzeNzuxa+YKLgJ3DrgOm4CjYj+HCXQn91djA==",
            //DEEPLINK_CAMPAIGN
            "LNbLK2Mqjuk/UGdAO70IiGx1jd7lnlAZWBbSB0zzdNY0G6/0SvPA1YOP2ygZ5uSaCb8mPwhcHzb/5Q28VEoOZA==",
            //SCAN_QR_CODE
            "hlSFSW/pjU/Rcio9i2DcjfrLa1SFYNPUAxCd3CYfentvLF+ybGZV9Dna0IDUv0MkOzNfo/LGFevXnVchydsuLQ==",
            //WATCH_VIDEO_GET_LIST_VIDEO
            "/B0XGR/6LoHwSOtrh4rVF6TMMdSlndIF98VgnIvYQ4Zhppv5+5iYjQiFLxc1g0wDNF4XCK/BeEiXKB2UM3JxnKuxmW/s27" +
                    "+3QTbrVxQNvajD7w3UmuLS8rToPWgg2zHShMeLy0aXJ+W1kgD5SIaocbVO0IL1mhSl1JeBS6J28+4=",
            //WATCH_VIDEO_GET_DETAIL
            "/B0XGR/6LoHwSOtrh4rVF0sbtIopWvf5gOefkm2Urfy02Jl6A6fIJmZC52g1PxuKx5PI5v0YHzklxvUp1cOsNsd7aKNplmEeLLfVZudBDh6FpTiAFIlrE82J27nG6BtTZe8ACHdoL99AuRW4/hzbIoJaezvVwWe3x+U9h8ORvJ8=",
            //FEEDBACK_GET_DEFAULT_V2
            "ZZGu9zPABqnmqMNhLw2LnZoE2S68fu3c36IY1gfwBOQUGW5Iz3wFtJ++ggfVRGdJfTR" +
                    "/GDBzVbpiPx25KwphHkBBXVsVHswWw2EMiVeU4iUdkFpvFM9PcmMRLICrcyABJfAwYKC0yY8mCAr2HOZaI2C12dvccFcpZpWzH2o6vlcvDNguABJFX4cEnDByGw2N",
            //FEEDBACK_SEND_KEY_V2
            "NJhHgtd60lUWa7L9P/kOXjgjVwWa+XB1gU3Q5zMswua8+i8ondlbzMjNPXc29c4JI5Ja1" +
                    "/LqDv63MLBxDFNSdiKFPr5oZvnnIaeK2g5oNnNx9SdgR0/EsH9S3qAkFG8HBk8DK6Adjjox0tAE5M4yajmmPLVBsafNFvo" +
                    "/OTUhHmrBjCsUwPB9M3k8PIaZlVTj",
            //GET_LIST_VIDEO_HOT
            "YnxdNTgD6kkenuGbHlSvpj4LyM4w0s5D1nXiELCJYg5xXrdQqnKJafYkt1h09Imn",
            //GET_DETAIL_VIDEO
            "t/x3d12gF241jo1nNvTedU2Tz6BIi0IpKCYGH4tjWTsPkwMLNZlxAT5kw8LlZFF9Daeh1uPYII5ljlZ0nUSkUuLgHJGg3BS64keKMAk3Tcg=",
            //SEARCH_VIDEO
            "jDEsfLq3kc4aJ78hF1bQkgtnDCyyeNF/s7QDueF1F3zYcF/fW85whJ2xRmDxuckF",
            //SETTING_RECEIVE_SMSOUT
            "eYSJeafFFlLzmPMbZwgtesFKYyd4nKMK5Oc5vkQFRz/qmSanH" +
                    "+42HiWR6NbEGaeqVcuU5rF9h0e2zI6nEgdEWlh8ZpgjsbSksKPuyTAbnXDPTcPc5BDF77KlwF+xwvKyH4MmAdM/I" +
                    "+VcqEOUf7KtBpeVFHpZb2W2fJLV/Vq6Tlw=",
            //GET_LIST_OFFICER_ACCOUNT
            "DJBIgRB/TCWYKoaAs6Z6hiW32H5TY0b0Pna3mybpxJmdX1WsS+ANtwrYWFTUItLt/7l7bVn9YcSNf/3GJjdqYoSC/yQesDHPF" +
                    "/KJ8Qi5OPLTtOiFATQbN6gD04QAJ+qnsOcufLbQd9VkxBOWYTuYmnAQ3BXjfG2JHHTaBbNA5NQ=",
            //SEARCH_OFFICER_ACCOUNT
            "90nLI+7zZlZV8I2sce7WGpehOAKUnLwsjDjVHlJdpGZTsG8bMxcCuziInmL6rnhhcCmaENcAmNTYAczpZvXtpUiaNKvamwARn56eahaCNqDoUDN10gUP9yvf/XwkqdznnlismMpD1Qv6Q9pdttCnvUsaKmSTF0oUvY6FDvWNxw8=",
            //BOOK_GET_TEMPLATES
            "0XYFuNqKTZzVf1INXdTBkdsGf1tx5807GJ6rUxhfhaM2NJ0psiwH9dTFgD8e7LATAP/Lj76khBwZCo2qErZaSFs8KAUmrwCDH9Lu1" +
                    "+vBILrh2IaJ/0mUytwQqg/fOg6PF9lpBksLTjAJHdHGcvra+w==",
            //BOOK_GET_TEMPLATE_DETAIL
            "0XYFuNqKTZzVf1INXdTBkWASrs1Q" +
                    "/p5pLMKOtOBJkLPUA17DQPhmTopGlqQ5W3465jfnO2ftYG1SATHGjYg18I8zOEOX3mOWcfCEbZsdETIrT3XRa9Rp3R" +
                    "+1v6RQQpTCRmQWF+DHh3J4wJYtSdiSQq5SBdSsLGIfkzj4U1peqNk=",
            //BOOK_GET_STICKERS
            "Hk/zji+UEMna39FvW0NgAjJVGZ3ABN6HraIJ+5VeCV" +
                    "/Xybz57PUIcKxNBF4Yc4VFjsrtuM8NWhv4JSY7fEzxuTqNSHsfpmzQeyotEDFeKfHNqGmiXsIIMvR9sDq4+GmTnC" +
                    "/+vYsRqjJ1JJ9KlQXumA==",
            //BOOK_UPLOAD_IMAGE
            "8Dc8ivHanjo/Bs+4HZhKH2KcQdgjZdJNmrPWWfXIcekv3ZkxO7zWnkaUYcdZ9BsIgCv1IRMH1hbzx5dP/GKxYw==",
            //BOOK_SAVE
            "gsBMBTZAOoOjvuqb5mvcWeph3vnBQJeS13v3nG6kMVWWs5dv7F/ZaALwPVniEUiT",
            //BOOK_ASSIGN_PAGE
            "Ihl+y79dUhqvkVE0JIihF591ExULekh8L9dpAmic5P34kciKsR5ShFngvZD3aHq7awfsIATiTxtcafr5U5lAlA==",
            //BOOK_GET_LIST_BOOK
            "82tujpJ+3dRVNAZvlydLbpSzy58ROMSAP4Xlgq1QxZ8dDJ4SGrS2" +
                    "/1lhlXnslOpD5L7kzvLUtThzDjBfWgmM3GMNo5eaGi2kbjMNNOdyIl7WUUqczhJJ6Pj6AN+mog8lIFMmToMZFcG5Hw7jPd" +
                    "/PrbJYPEOCaqmnfgpEMGLLDl8=",
            //BOOK_GET_BOOK_DETAIL
            "E2eAqXDFczaSHcYNK47/f1+p01xwBF4gbp2D5KUGKYnvDYbr/mDCHakPgf5+fq1Mm" +
                    "/oEjn6qjMCIZypxt4BeyQuh007JuiznOFnzqZjocb7jNxBRUtKnJD9wEQdxHMR5PtlN3zCFhUifKU89BdieSQ==",
            //BOOK_GET_PAGES_ASSIGNED
            "6htP+O3GMSMgTLIoXhQ87WYzjDJx+4Q0QtrXJzNzCrcmJHMYfnv8Gu1S4XNlwKbjL2wxAiVfIzv" +
                    "+cQeBUTQUNSI8yp1nVSiNbhUIVQDw8haMOZnjCI/1yRYhyssdTxcOXdhGGT8RjAqFNbCKNFrCLEyUjFnMwwMrytlmJ3jQXD4=",
            //BOOK_PROCESS_BOOK
            "wY2KZDDUHSbIPpKYKeQfaDeRZDz/bLj/23hZZfER6KlhxY83H8mByC+YPi7NU4c5R31KNq+rieh2cSY0MWtv22EzzeDyjhLvs/TSfTV" +
                    "/2B0=",
            //BOOK_GET_BACKGROUND
            "jLJ+HkytdfM8gYgXgLMrZsP9ywCPbSE1yi930DNxu8jDEnHYVbWItpRgGGaf15HOvmqWvlhpCpHnXvgSuKX5KS6UNrpl+fuhpY+audk" +
                    "/i0Pm9VpFKXfX9smEF6d60QvvTeoxSzmrym1rA4VJGaDw8w==",
            //BOOK_GET_MUSIC
            "PGgu0XaBx+tQL6UyQ//4xfETkIyzaKtOjSoxgcSE3bmUf9XuwN4Ntr9U" +
                    "+8nQ4ZYjB2E6DIDkVwR4tXvi7oFuJiToSVit7dWwBzvNKPpaqmqRfB1oNeZXrsYvV4KwJQA3NdEHjmVKrgL6H/tp+WSpQg==",
            //BOOK_VOTE_GET_LIST
            "6Oxhe+mKM0+fzr5LPCSWo5UIxXugecXmOA/AxKnQaIauRiyRJeZ3kTakOj1YTvjtvFMvUwb" +
                    "+5lNcqdMWP1x4Di7aJSLoKVibfgTNUXBdGuv7IC3cbYLEuZxEeHhhiUbX70G2y1ACtlRRV8a0kJvxj2o29b2" +
                    "/lcMlBUBqqZY42AY=",
            //BOOK_VOTE_GET_DETAIL
            "6Oxhe+mKM0+fzr5LPCSWo" +
                    "+LbpCPFe9J7WqnubVEbC6S0CtAVYXCu35Bk5fP536tEgEqokk3d74gd3bjJruSSltmZz0Lccznnwt0x6MJtlQ" +
                    "+uTfRUaDM94c+WjtBU8TrZvW6WA/9Op2SvhD4dz8t1lz8avjmW+s77RJ+AQiGfUk4=",
            //BOOK_VOTE
            "zMuLWSHKg15OxDISjEhINkzszBhgMqGwETxFYnplFnkOSjCPzBNTwVpwyJUAZFFiWOemDVUIIgj0YRvvS+8Qjw==",
            //GET_GROUP_DOCUMENT
            "94YPX/Khjq8gxUuqZitEvHQz8oK4ie9BDSTKSml8I8ejE+7DFbRIknEiUKfboGuGT8+ISBoV/aDQsVE96" +
                    "/rnwSUYB9BMI9jzFkHrUW7tzPWR2JFPD46mrPktvlcNxrIoROYlfDOfbYi4BTAYQIhE7HV5vi7pd2Gk1oes34IyB8k=",
            //SPONSOR_READ_SUCCESS
            "y9w0NVkJoBEudS8JWOeiQVfEnsgOnjl3tOAbf+ho3RbZhWcgpusDX0b" +
                    "/MRKcpaHTtvQphEyLWBji5Rm4G53xOlocjw279hKUcj9mpFezhuM=",
            //ONMEDIA_GET_DETAIL_URL
            "4/r1pYLXmRnzsWb5nzF9ge+imEnaHkM" +
                    "/yY3qN0n6KkolcZMx7oQ2Zs1pxXrblgAM4sXrvARURW4e1ruGqrppR0eoPygjcFppoFIVqGVNI98=",
            //ONMEDIA_UPLOAD_ALBUM
            "A8/gYwPvERTaRGMie3v7W8zq5NOTU+LuXMr7HV/SEEFWh1UvIKhPNWum/QIxFjqcMhajgi4efZg8LHkNVhoSGA==",
            //ONMEDIA_GET_IMAGE_DETAIL_V3
            "t6qrCxKHkSvh7p4mNyYP88e7O+BhzZS5bj/u6DkSgfHByByM21yli938Q/gozECtZzQty2VDUsOxCwXYZL0R7UjhihVzKNtlAk7N8IR7Ero=",
            //ONMEDIA_GET_HOME_TIMELINE_V6
            "23pTBiHBc/1V0iDblEpn1FiP+iQaTR11F+kHYqH3FW5h4ZPOfP7H3mjvM5P3GHlexkKE7HQyJUEiab9ws1w4G+3EHFPCwPLB4igtoVi5/2+Z48Ev/sKxqRfFdU5HYgOt",
            //GET_LIST_GAME
            "Y3uzeI5+X0sEZ7rhrITcdBce4Zbe54csLbv+HfnAxK/ojYyoq/i5LH/nnPkDO3F2dDq9NrHGOIbE9eh9gBC12w==",
            //SHARE_SIEU_HAI
            "kdE8OC8M01v/CrDmb6wJDcV14ov7ojaqlq9Mfav0iRrCehizeHpNGp41Al52WPev",
            //CALL_SUBSCRIPTION_GET_REMAIN
            "V8TPEhj4gSRoGpqMGS+5WQrWsQ3kU8trOpOLco4YFDzABEll76KPxA94TQDEEiXsIkHV3FzumPAzsr/MfbM41KxiHH" +
                    "/psw2cSBVIFhrST0E=",
            //POST_STRANGER_CONFIDE
            "bKne8TvidbL0+tIKx+SkvYAT62MHZ7I4mbzY/G16kEFMuxwn0YdZpDVVfiuQr/brkg+P11e3rCabrp4YZs4zg1b" +
                    "+1NLzn0PMERgXg3Sa3e4=",
            //CANCEL_STRANGER_CONFIDE
            "ZjEbxaEpvHONrYj6ZXbwbfw/dKucUurTn1dxNyCxt/3A3657PAQgSU/KMoFNU9fgutRKYI56//jssInCizg5MhAZy87lM+PpBXY" +
                    "+Gz0L9+k=",
            //ACCEPT_STRANGER_CONFIDE
            "Dt9yy07WgVFD49raQu7f+8ul4B4d/1TJ/1kGn0o/QJ1p4bQjFsXaHfx40NyEKBMblLkMGMuDVfOT8lmzpll+xFC2vEG" +
                    "+kJxHGcVYzsSDFXY=",
            //GET_STRANGER_CONFIDE
            "gIBX+gDGiC+Y4R5KNbAWDdV8N/u3IOS0z8p8yoDElZW0aT76oCqFekGQOrK5EPDTWMPVImLSQlYdGSCQwn2iaRZCBkkVgopSsHliFIhKb9DZcsOpAHqF8fy67GhVocCNUGmgomya8X1Y5iVD+hs8z9uBinFkl/hHmcqJhR4hjsZn504fQrNQ37iAYUQeAPcE3yNBHMOZQLyUVEZqyVr46/QE6uAHmwLNuOkR+4jRHwjfjnSz8gL6Fr/CR7o+iV2qAh+sIhglBF79FtH9C/6bj++EuaWAuIssphzDeJWo8iwO7MVGS2PuXD33pQCjwZ9iQsDelhUSq5PnKZT8QErKnftz/NhXnoEj9tPp2GnKOoiOwjuIawGj8Ww4UBuUxVdN",
            //UPLOAD_IMAGE_SOCIAL_ONMEDIA
            "9kY/Mst/tSI+b10Lj0yhGnSORxmK/DbFosChiK2Oaq8OKq5ldEbQ6+o19L8GuoA8aD03IJ694KnC/e2iemotaw==",
            //GET_LIST_VIDEO_DISCOVERY
            "YnxdNTgD6kkenuGbHlSvpjuxiFGMj52s2aOoHUbHimiatS3E60j8UP6a0LY6r2S984QCe8GefSNavyyMZbrNDw==",
            //SET_REMINDER
            "iXXv7nvscQ+fLXsONlTBMRx8hxBzCPhUzJXJiMsIeLyFnhLir0ugxHFFgPbPD/tvfQHkoNck7BlOiKKt7JCNSQ==",
            //LOGGER_CALL
            "jyMCq7CAnHDWw18/p4DY4XDEfvUI0ijiUvOVE9Mjf27VveNiSTpBAxz/0wEhdyBI5f9jqyRkTrHPB1b9yTx58Q==",
            //SEARCH_VIDEO_NEXT
            "Mpi16U/hRnA12AT7d2ms4q5eEiyQmGnLfNJkRaf74wcJJmsqq1alRpJk1QKKuc5t2HdgoXi35m7ph3vrUjbDfKxH6U/kDX" +
                    "/E8Su2uKv9JU8=",
            //DETAIL_VIDEO_NEXT
            "s3+NfrLsBkEGGdSHweDbBKF/ceEosnRwMy7kiiDSMGxbBHDPA80/XHxt62mRuWVQ5Oeh7xForCFQ0g6p8nM/hRPJ5dEGTG2yyZ9QH4" +
                    "/lez0=",
            //ONMEDIA_GET_LIST_SUGGEST_FRIEND
            "4ICfEmeydffoSXp/k95zFKgQERDRwEpSQQPba9HbF95qgLd4Tctomifu3BZgiKkz0f33t6" +
                    "+S9RRxgf9ZE12IdfUWNfmzUTOjoomm1KRoyKLAhlcqvwEAnfaxKRmt7N6J",
            //GET_SUGGEST_CONTACT
            "+Fx0Mc4kp0JMKQgDi+aPQAVslCJ1sBrkeNlP3MQiU6KcHKLgJvd4ezgeogQyZRbZ2ZpcSN/cTLl76u2CrOzyTQzmqu" +
                    "+wqKvq0Yt8A6l04gM=",
            //UPLOAD_LOG
            "szdtfLelyZgQrNOg8901ks+mOzX7Idiajf/aRqUYguUuKBhwtwWDeltupnFgMZ2Z",
            //LOG_VIEW_SIEUHAI
            "cVwXeYusfB53FmeCktzdympsKa6YMEluvyibknzbKJSpZ+gxJ" +
                    "+/1NymfTtA439puecTuiq8s8clt5tlfu9b8oEpcLDkp5XQe8BltZmS3NjgoavecpJvqTwyVForooGBX",
            //GET_BLOCKLIST_V5
            "vniP8LsrMnc7drrMSfH/rsBcZrOGs45ObRqyvqbdLjyJNbcJef21s/DfldZ3bPOfdazzyHcBBQ/Ggb+VcS8mdg==",
            //SET_BLOCKLIST_V5
            "ts6q2xUJ9J3H+Ys87elXzoKmoQG1aCdjVrJa7hr688kfPrUHeTHEU1PM6VU/hgLpCrluOKcIOYcfM/mpWRvaug==",
            //LOGGER_CALL_QUALITY
            "kPXlx4HCWHL9VhJb6GwooQd/1qFCGtdccT94p4DWNubyFwhD9RMPpZeieWyHMaJaORxwqpRoHIaQJZhyjRQWPHskSyxmVXWEmExcQbR+dEk=",
            //GAME_SHAKE
            "G7O6EN9alR7fXwAkb/bRo/SLls9p3u5EumQRD4pO/Ov9tSuk/3N19wZXoDRBOa4Bpj9k/2OJYeOmgkOwRMMZhg==",
            //LIXI_OPEN
            "O1KTXQrfMo7LYPxgRLM3WA6PoasdCvmi5cDFf92+a1qDijeLuYIPsoZaaGPpKNSq",
            //LIXI_GET_LIST_IMAGE
            "jA3gCmJtjY9Q/xMJIK+vPN8/n1MTJMMHpJB1kBimq1aBBdd5rvzXaCKo+IwNG+lpL19cAAWWfq4QbqImHgb/7g==",
            //LIXI_LOG_BPLUS_FAIL
            "Hv8IBdLw81yjhH0SJLxIpIGdcTZrgdx04iyMGIOpQYjlXmC/R0qum3aTOdOwgbSjk+mZxnPVfAiLwdLAuA3/YQ==",
            //AVNO_REGISTER
            "EoGyIVN+7Y7KbWrQMBZd3xdaj0LD3DzQ9EcaQjiaKMqNeh/xlnYdAQVfcndW48jGNIvBM1GLqhy1StbcWUoZVQ==",
            //AVNO_SUGGEST
            "gaso2Iy8sC+pYsieDYOfNC1m87Shcc+5LD9Q5j640U4+QJb6ZEVRSMt1DDZc0CC7LtoiIUuZR+Y+9csB5yg3Nw==",
            //AVNO_SEARCH
            "Zt6mCXfM6ybnByZ4mNFwPIeu0yhxas8aceCy51ilTDUS5S6/weEJngZAE8RqzGij",
            //GET_GIFT_WOMEN
            "ZYTrZnTc5hih62/zA5xTrfHSdmog421ygxLcD5/JCRn8fjkToO1BKUdZH4KzOi57go45LuKm5jN93f6fW9SnXw==",
            //RATING CALL
            "TZx9sXmjKk5Pl49A12U1qn96D3Oc6MzX+jJLvLl8GKKIo97LzGHoj5C0pvUiRm9w",
            //AVNO_PAYMENT_SCRATCH_CARD
            "qFBNwl4B2dWPGQ5rzcRZN5HP52dSIQmL96hFUnBaHxEdyQdm5yjsO/rQO3zEH9a8M2DMKB18CYrPnigUNIaFlai7mVhm+RpAXF4pJDYqFeE=",
            //AVNO_PAYMENT_CONVERT_SPOINT
            "H2fc1BeLO3cc8K29+wOPuPwsEKLGjaPpGnjzWe+ldg4U2k2dRV+IhGusrQ5Bb4vy11rsOudxnf0xVTHQd2HfnEA0EzBwm971iJIRCaq7tSM=",
            //AVNO_UPLOAD_IC
            "dESBq5VGYWwXfWTghQQFj6/ijFYD2jLhT51/kkyfPigKBb+1MF9Wb/JwdUCTcoY1",
            //AVNO_REMOVE_IC
            "2ETe0PoftzDGF2CZMWxsUaxfaEtVMIP4oahLiiXoTg6EKPYWrZqwa1rYSnk2q+bA",
            //GET_LIST_GROUP
            "C27rHmmgMN058dua8sVIDyym2V9JYBvmPwjLXHNm+RpaAZ6R0N5Sf0U4oZkcbLdDYcRJwft55vccstmxh6nhJg==",
            //UPLOAD_LOG_DEBUG
            "PhLPysRQ29G+OHkNEVRQ+VeZZMoOXLLmuMY/D6ndjiv/8x9F2W5sCWOM4+iK9SL1",
            //BROADCAST_CHANGE_NUMBER_AVNO
            "32Umi5kNompsr9hnzLYA6H4aqe35uFKuhNzzsIczRV0nFeXRl7pA/6kYG2F0c4bgXDHVyRE2hdgenCkui6zB2g==",
            //CALL_SUBSCRIPTION_SET_FREE_FROM_CALLOUTGUIDE
            "V8TPEhj4gSRoGpqMGS+5WfS6gQxet84Qed+XjtJ8cIQ2Lhmp6u5ZvwhtvkzTeDD/SUTZKhBeJ/Rjsmy6Qs9ErTxxEy0psP3u8QnY60t21wv1jaLgXyDY+CvsOO/NAskb",
            //AVNO_REGISTER_FREE
            "d142ISGKcn0+ZFFIEX8yNQ4XElCFlURdN68uyChDJMeOoZifI3PW6adxNnqFTelX23R+1APHZiZ7x34tUEUqJw==",
            //AVNO_ABORT
            "Fbl2j1m/DUsQYePzrDpX06lImbwPSUz4G6GfpfILDO41466NCfSHL0EPlwHZ/Awi",
            //SEND_LOG_KPI
            "AEg06XhwtPOFWMuK1Z4i3KAngMJ8K17xAjVlYWb/veSg25uikX4hLQdt9Gd+btUedIdq8Gnsb8iGfev/iI20fw==",
            //POST_LOCATION
            "ub8E91ORSL2om1oicY2gMt+YTPzpHYAmXwIXtH8Qo+xgsaxZlH4vy+VA4aFewkr2aGW9JRy3gsemlGmKiWYPmw==",
            //GET_COUNTRY
            "Qg7dseIBnnspiw3IBlw8xtCmtgHzqFZVDnBCjPJoV7MGh3FpKqizK+8YfFb8wnhwSRUbbyYFZQvWFBqREuKGzQ==",
            //AVNO_REGISTER_PACKAGE_V2
            "d142ISGKcn0+ZFFIEX8yNcnasW5CxUaFeOvxjRzAEYAnZp0J+myAFh7YQ4L3PWwEcKNGwk5Pg3KmvBolJGfA+l+dqagRqUTZZiq4bAU/HxY=",
            //AVNO_CANCEL_PACKAGE_V2
            "5HvqO4QBuUYKUTVtCZpHR3fFc90RVo2e6K1KwLK88IP97dMwgSLfZrSdHgVIk5OGueaEnFYul69Uax4j/sN8Qw==",
            //GET_POPUP_INTRO
            "jo8zlAggUYcjxv4mkhAi8IqdatXd+lzxRJoR1u02FsT5ct6cYTmr+xthhFu2UGiW9frlnmMFxY90yccx38cNpA==",
            //LOG_ERROR
            "DmHDBkdj9EXk1OTjpSvVdK6Cfbsdo9/VG1G8pO6gMo+qMuZHqhEY8Vjtk3fSYPQh33Mr60Shu0A1Rp581AjNTQ==",
            //CALLOUT_GET_INFO
            "F2/SKbaeIQr3dl4Fv4TtT8Yc1eI5UFt4+IiQeiv/S1vurGAPBB3iNMPJISj9PNnfTfue1/82u6SvNITdESZXTw==",
            //CALLOUT_REGISTER_PACKAGE
            "+BqdnlPe6kZaMudHX0a2d4OYYe4Kc1Pc2gP/3kR9YjXPAxUe+cXglaSPBiJ+3k+aKFaHxICdcW7jKiKmCeiPXgIjIuuGalmCPkWr2bHJkAY=",
            //CALLOUT_CANCEL_PACKAGE
            "dO9GR8i6VsGSuYqwx/x+NMt8FoFmz+d5Md78WklExbHF/sT8bkV5Z5L5fq9TPhrp2ysR9qZaWysgo9op3Uv9jw==",
            //CALLOUT_SET_CALL_LIMITED
            "lQHvr384vkxzoy0Uulv1oyPgel5kfY7v3WDt9xsAYEQrSrHqbJQpwsJEA/8kTYW9F91SA5j0Y62+M2LUfA9V6+ejAEJ31SEt+mObRJ2mcMQ=",
            //CALLOUT_REGISTER_FREE
            "+BqdnlPe6kZaMudHX0a2d4FqIwYo/BxmY2cOzODjyaIpvqH+acAfGs+h2qwB7s95Do1+M1SlXMyyV0si2mvxPQ=="


    };

    public static String[] getPlmTye() {
        return PLM_TYE;
    }

    public static String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        if (domain.startsWith("www.")) {
            return domain.substring(4);
        }
        return domain;
    }
}