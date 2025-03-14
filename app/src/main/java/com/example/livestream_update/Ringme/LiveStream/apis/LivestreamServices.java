package com.example.livestream_update.Ringme.LiveStream.apis;


import com.vtm.ringme.livestream.apis.response.BaseResponse;
import com.vtm.ringme.livestream.apis.response.LiveStreamBuyCoinResponse;
import com.vtm.ringme.livestream.apis.response.LiveStreamCoinResponse;
import com.vtm.ringme.livestream.apis.response.LiveStreamDetailResponse;
import com.vtm.ringme.livestream.apis.response.LiveStreamDonateResponse;
import com.vtm.ringme.livestream.apis.response.LiveStreamFollowResponse;
import com.vtm.ringme.livestream.apis.response.LiveStreamGetReportListResponse;
import com.vtm.ringme.livestream.apis.response.LiveStreamGiftResponse;
import com.vtm.ringme.livestream.apis.response.LiveStreamLikeResponse;
import com.vtm.ringme.livestream.apis.response.LiveStreamListResponse;
import com.vtm.ringme.values.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LivestreamServices {

    @GET(Constants.Server.listLivestreamAll)
    Call<LiveStreamListResponse> getAllLivestreamList(@Query("featureId") int featureId,
                                                      @Query("page") int page,
                                                      @Query("size") int size,
                                                      @Query("userId") long userId);

    @GET(Constants.Server.livestreamInformation)
    Call<LiveStreamDetailResponse> getLivestreamDetail(@Path("id") String id,
                                                       @Query("userId") String userId);


    @GET(Constants.Server.listAllGift)
    Call<LiveStreamGiftResponse> getGiftFromServer(@Query("page") int page,
                                                   @Query("size") int size,
                                                   @Query("timestamp") long timestamp);

    @POST(Constants.Server.like)
    Call<LiveStreamLikeResponse> likeLiveStream(@Query("livestreamId") String livestreamId,
                                                @Query("userId") String userId);

    @GET(Constants.Server.getReportContentType)
    Call<LiveStreamGetReportListResponse> getReportTypeList();

    @POST(Constants.Server.getCurrentCoin)
    Call<LiveStreamCoinResponse> getCurrentCoin(@Header("authenAPIKey") String authenAPIKey,
                                                @Header("dataEncrypt") String dataEncrypt,
                                                @Query("timestamp") long timestamp,
                                                @Query("userId") String userId,
                                                @Query("username") String username);

    @POST(Constants.Server.donate)
    Call<LiveStreamDonateResponse> donate(@Header("authenAPIKey") String authenAPIKey,
                                          @Header("dataEncrypt") String dataEncrypt,
                                          @Query("avatar") String avatar,
                                          @Query("giftId") String giftId,
                                          @Query("livestreamId") String livestreamId,
                                          @Query("streamerId") String streamerId,
                                          @Query("timestamp") long timestamp,
                                          @Query("userId") String userId,
                                          @Query("username") String username);

    @POST(Constants.Server.buyCoin)
    Call<LiveStreamBuyCoinResponse> buyCoin(@Query("amountStar") String amountStar,
                                            @Query("timestamp") long timestamp,
                                            @Query("typePayment") String typePayment,
                                            @Query("userId") String userId);

    @POST(Constants.Server.follow)
    Call<LiveStreamFollowResponse> followStreamer(@Query("streamerId") String streamerId,
                                                  @Query("userId") String userId);

    @POST(Constants.Server.reportLivestream)
    Call<BaseResponse> reportLivestream(@Query("livestreamId") int livestreamId,
                                        @Query("reportContent") String reportContent,
                                        @Query("reportId") int reportId,
                                        @Query("userId") String userId);
}
