package com.example.livestream_update.Ringme.Utils;

import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.api.HttpsTrustManager;
import com.vtm.ringme.api.RRestChannelInfoModel;
import com.vtm.ringme.helper.Config;
import com.vtm.ringme.helper.ConfigLocalized;
import com.vtm.ringme.home.RRestVideoModel;
import com.vtm.ringme.livestream.network.APICallBack;
import com.vtm.ringme.livestream.network.ApiService;
import com.vtm.ringme.values.Constants;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RRetrofitClientInstance {
    private static Retrofit retrofit;
    private static String baseUrl;



    public RRetrofitClientInstance() {
        baseUrl = ConfigLocalized.DOMAIN_KAKOAK_VIDEO + "/LivestreamAPI/v2/";
    }

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = HttpsTrustManager.getUnsafeOkHttpClient(60, true);

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public void getVideoByChannel(String channelId, int page, String lastIdStr, APICallBack<RRestVideoModel> callBack) {
        String time = System.currentTimeMillis() + "";
        String msisdn = ApplicationController.self().getJidNumber();

        ApiService service = getRetrofitInstance().create(ApiService.class);
        Call<RRestVideoModel> call = service.getVideoByChannel(channelId, msisdn, lastIdStr, page, 20, time, "");
        call.enqueue(new Callback<RRestVideoModel>() {
            @Override
            public void onResponse(Call<RRestVideoModel> call, retrofit2.Response<RRestVideoModel> response) {
                callBack.onResponse(response);
            }

            @Override
            public void onFailure(Call<RRestVideoModel> call, Throwable t) {
                callBack.onError(t);
            }
        });
    }

    public void followChannel(boolean isFollow, String channelId, APICallBack<RRestChannelInfoModel> callBack) {
        String time = System.currentTimeMillis() + "";
        String msisdn = ApplicationController.self().getJidNumber();

        if (isFollow) {
            ApiService service = getRetrofitInstance().create(ApiService.class);
            Call<RRestChannelInfoModel> call = service.followChannel(channelId, msisdn, time, "");
            call.enqueue(new Callback<RRestChannelInfoModel>() {
                @Override
                public void onResponse(Call<RRestChannelInfoModel> call, retrofit2.Response<RRestChannelInfoModel> response) {
                    callBack.onResponse(response);
                }

                @Override
                public void onFailure(Call<RRestChannelInfoModel> call, Throwable t) {
                    callBack.onError(t);
                }
            });
        } else {
            ApiService service = getRetrofitInstance().create(ApiService.class);
            Call<RRestChannelInfoModel> call = service.unFollowChannel(channelId, msisdn, time, "");
            call.enqueue(new Callback<RRestChannelInfoModel>() {
                @Override
                public void onResponse(Call<RRestChannelInfoModel> call, retrofit2.Response<RRestChannelInfoModel> response) {
                    callBack.onResponse(response);
                }

                @Override
                public void onFailure(Call<RRestChannelInfoModel> call, Throwable t) {
                    callBack.onError(t);
                }
            });
        }
    }


    private static Retrofit getRetrofitInstance(long timeout) {
        if (retrofit == null || timeout != 60) {
            OkHttpClient okHttpClient = HttpsTrustManager.getUnsafeOkHttpClient(timeout, true);

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public static RequestBody createPartFromString(String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }

    public void getChannelInfo(String channelId, APICallBack<RRestChannelInfoModel> callBack) {
        String time = System.currentTimeMillis() + "";
        String msisdn = ApplicationController.self().getJidNumber();

        ApiService service = getRetrofitInstance().create(ApiService.class);
        Call<RRestChannelInfoModel> call = service.getChannelInfo(channelId, msisdn, time, "");
        call.enqueue(new Callback<RRestChannelInfoModel>() {
            @Override
            public void onResponse(Call<RRestChannelInfoModel> call, retrofit2.Response<RRestChannelInfoModel> response) {
                callBack.onResponse(response);
            }

            @Override
            public void onFailure(Call<RRestChannelInfoModel> call, Throwable t) {
                callBack.onError(t);
            }
        });
    }

    private static class RequestInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            HttpUrl newUrl = originalRequest.url().newBuilder()
                    .addQueryParameter("clientType", Constants.HTTP.CLIENT_TYPE_STRING)
                    .addQueryParameter("revision", Config.REVISION)
                    .build();

            Request newRequest = originalRequest.newBuilder()
                    .url(newUrl)
//                    .addHeader("Authorization", "auth-value") //thêm một header với name và value.
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept-language", "")
                    .addHeader("sec-api", "123")
                    .method(originalRequest.method(), originalRequest.body()) // Adds request method and request body
                    .build();

            return chain.proceed(newRequest);
        }
    }

}
