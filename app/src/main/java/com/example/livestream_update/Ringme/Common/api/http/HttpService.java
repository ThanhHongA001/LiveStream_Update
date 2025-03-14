package com.example.livestream_update.Ringme.Common.api.http;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by Hoang Anh Tuan on 11/1/2017.
 */

interface HttpService {

    @POST
    @FormUrlEncoded
    Call<ResponseBody> getDataByMethodPost(@Url String url,
                                           @HeaderMap Map<String, String> header,
                                           @FieldMap Map<String, String> body);

    @Multipart
    @POST
    Call<ResponseBody> getDataByMethodFile(@Url String url,
                                           @HeaderMap Map<String, String> header,
                                           @Part List<MultipartBody.Part> files,
                                           @PartMap() Map<String, RequestBody> descriptions);

    @GET
    Call<ResponseBody> getDataByMethodGet(@Url String url,
                                          @HeaderMap Map<String, String> header,
                                          @QueryMap Map<String, String> body);

    @POST
    @FormUrlEncoded
    Observable<ResponseBody> post(@Url String url,
                                  @HeaderMap Map<String, String> header,
                                  @FieldMap Map<String, String> body);

    @Multipart
    @POST
    Observable<ResponseBody> file(@Url String url,
                                  @HeaderMap Map<String, String> header,
                                  @Part List<MultipartBody.Part> files,
                                  @PartMap() Map<String, RequestBody> descriptions);

    @GET
    Observable<ResponseBody> get(@Url String url,
                                 @HeaderMap Map<String, String> header,
                                 @QueryMap Map<String, String> body);

    @POST
    Observable<ResponseBody> postByRequestBody(@Url String url,
                                               @HeaderMap Map<String, String> header,
                                               @QueryMap Map<String, String> param,
                                               @Body Object body);
}
