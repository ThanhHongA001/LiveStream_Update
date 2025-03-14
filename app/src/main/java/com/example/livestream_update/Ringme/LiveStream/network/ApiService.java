package com.example.livestream_update.Ringme.LiveStream.network;

import com.vtm.ringme.api.RRestChannelInfoModel;
import com.vtm.ringme.home.RRestVideoModel;
import com.vtm.ringme.livestream.network.parse.RestListLiveStreamMessage;
import com.vtm.ringme.livestream.network.parse.RestLiveStreamMessage;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @Headers({
            "Accept-language: en",
            "Client-Type: Android",
            "sec-api: 123"
    })
    @GET("video/{id}/channel/v2")
    Call<RRestVideoModel> getVideoByChannel(@Path("id") String channelId, @Query("msisdn") String msisdn, @Query("lastHashId") String lastHashId, @Query("page") int page, @Query("size") int size, @Query("timestamp") String timestamp, @Query("security") String security);


    @Headers({
            "Accept-language: en",
            "Client-Type: Android",
            "sec-api: 123"
    })
    @GET("channel/{id}/follow")
    Call<RRestChannelInfoModel> followChannel(@Path("id") String channelId, @Query("msisdn") String msisdn, @Query("timestamp") String timestamp, @Query("security") String security);

    @Headers({
            "Accept-language: en",
            "Client-Type: Android",
            "sec-api: 123"
    })
    @GET("channel/{id}/unfollow")
    Call<RRestChannelInfoModel> unFollowChannel(@Path("id") String channelId, @Query("msisdn") String msisdn, @Query("timestamp") String timestamp, @Query("security") String security);


    @GET("chat/getMessages")
    Call<RestListLiveStreamMessage> getListMessages(@Query("msisdn") String msisdn, @Query("roomID") String roomId
            , @Query("timestamp") long timestamp, @Query("token") String token, @Query("security") String security);

    @GET("chat/getMessagesLevel2")
    Call<RestListLiveStreamMessage> getListMessagesLevel2(@Query("msisdn") String msisdn, @Query("roomID") String roomId, @Query("id") String id
            , @Query("timestamp") long timestamp, @Query("token") String token, @Query("security") String security);

    @Headers("Content-Type: application/json")
    @POST("chat/postMessage")
    Call<RestLiveStreamMessage> postMessage(@Body RequestBody body
            , @Header("timestamp") long timestamp, @Header("token") String token, @Header("security") String security);

    @Headers({
            "Accept-language: en",
            "Client-Type: Android",
            "sec-api: 123"
    })
    @GET("channel/{id}/info")
    Call<RRestChannelInfoModel> getChannelInfo(@Path("id") String channelId, @Query("msisdn") String msisdn, @Query("timestamp") String timestamp, @Query("security") String security);

}
