package com.example.livestream_update.Ringme.LiveStream.network;

import androidx.annotation.NonNull;

import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.BuildConfig;
import com.vtm.ringme.helper.encrypt.EncryptUtil;
import com.vtm.ringme.helper.encrypt.RSAEncrypt;
import com.vtm.ringme.livestream.model.ConfigLiveComment;
import com.vtm.ringme.livestream.network.parse.RestListLiveStreamMessage;
import com.vtm.ringme.livestream.network.parse.RestLiveStreamMessage;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.values.Constants;

import org.json.JSONObject;

import java.security.PublicKey;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private String TAG = RetrofitClientInstance.class.getCanonicalName();
    private Retrofit retrofit;
    private ConfigLiveComment configLiveComment;
    private PublicKey publicKey;

    public RetrofitClientInstance(@NonNull ConfigLiveComment configLive) {
        configLiveComment = configLive;
        try {
            publicKey = RSAEncrypt.getPublicKeyFromString(configLiveComment.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(configLiveComment.getDomainAPI())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private String encryptDataToSecurity(String msisdn, String token, long timestamp) {
        try {
            Log.d("LiveComment", "msisdn: " + msisdn);
            Log.d("LiveComment", "token: " + token);
            Log.d("LiveComment", "timestamp: " + timestamp);
            StringBuilder sb = new StringBuilder();
            sb.append(msisdn);
            sb.append(token);
            sb.append(timestamp);
            String md5Encrypt = EncryptUtil.encryptMD5(sb.toString());
            Log.d("GameLiveStream", "md5Encrypt: " + md5Encrypt);
            JSONObject data = new JSONObject();
            try {
                data.put(Constants.HTTP.REST_TOKEN, token);
                data.put(Constants.HTTP.REST_MD5, md5Encrypt);
            } catch (Exception e) {
                return null;
            }
            return RSAEncrypt.encrypt(data.toString(), publicKey);
        } catch (Exception e) {
        }
        return "";
    }

    public void getMessages(String roomId, String msisdn, APICallBack<RestListLiveStreamMessage> callBack) {
        long timestamp = System.currentTimeMillis();
        ApplicationController app = ApplicationController.self();
        String token = app.getToken();
        String security = encryptDataToSecurity(msisdn, token, timestamp);
        ApiService service = getRetrofitInstance().create(ApiService.class);
        Call<RestListLiveStreamMessage> call = service.getListMessages(msisdn, roomId, timestamp, token, security);
        if (BuildConfig.DEBUG) Log.d(TAG, "getMessages: " + call.request());
        call.enqueue(new Callback<RestListLiveStreamMessage>() {
            @Override
            public void onResponse(Call<RestListLiveStreamMessage> call, Response<RestListLiveStreamMessage> response) {
                Log.d(TAG, "getMessages onResponse: " + response);
                callBack.onResponse(response);
            }

            @Override
            public void onFailure(Call<RestListLiveStreamMessage> call, Throwable t) {
                Log.d(TAG, "getMessages onFailure");
                callBack.onError(t);
            }
        });
    }

    public void getMessagesLevel2(String id, String roomId, String msisdn, APICallBack<RestListLiveStreamMessage> callBack) {
        long timestamp = System.currentTimeMillis();
        ApplicationController app = ApplicationController.self();
        String token = app.getToken();
        String security = encryptDataToSecurity(msisdn, token, timestamp);
        ApiService service = getRetrofitInstance().create(ApiService.class);
        Call<RestListLiveStreamMessage> call = service.getListMessagesLevel2(msisdn, roomId, id, timestamp, token, security);
        if (BuildConfig.DEBUG) Log.d(TAG, "getMessagesLevel2: " + call.request());
        call.enqueue(new Callback<RestListLiveStreamMessage>() {
            @Override
            public void onResponse(Call<RestListLiveStreamMessage> call, Response<RestListLiveStreamMessage> response) {
                Log.d(TAG, "getMessagesLevel2 onResponse: " + response);
                callBack.onResponse(response);
            }

            @Override
            public void onFailure(Call<RestListLiveStreamMessage> call, Throwable t) {
                Log.d(TAG, "getMessagesLevel2 onFailure");
                callBack.onError(t);
            }
        });
    }

    public void postMessage(RequestBody body, APICallBack<RestLiveStreamMessage> callBack) {
        long timestamp = System.currentTimeMillis();
        ApplicationController app = ApplicationController.self();
        String token = app.getToken();
        String msisdn = app.getJidNumber();
        String security = encryptDataToSecurity(msisdn, token, timestamp);
        ApiService service = getRetrofitInstance().create(ApiService.class);
        Call<RestLiveStreamMessage> call = service.postMessage(body, timestamp, token, security);
        if (BuildConfig.DEBUG) Log.d(TAG, "postMessage: " + call.request());
        call.enqueue(new Callback<RestLiveStreamMessage>() {
            @Override
            public void onResponse(Call<RestLiveStreamMessage> call, Response<RestLiveStreamMessage> response) {
                Log.d(TAG, "postMessage onResponse: " + response);
                callBack.onResponse(response);
            }

            @Override
            public void onFailure(Call<RestLiveStreamMessage> call, Throwable t) {
                Log.d(TAG, "postMessage onFailure");
                callBack.onError(t);
            }
        });
    }
}
