package com.example.livestream_update.Ringme.Api;




import com.vtm.ringme.api.response.LiveStreamResponse;
import com.vtm.ringme.livestream.apis.response.BaseResponse;
import com.vtm.ringme.livestream.apis.response.LiveStreamFollowResponse;
import com.vtm.ringme.livestream.apis.response.LiveStreamGetReportListResponse;
import com.vtm.ringme.values.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
public interface ApiCollections {



    //LiveStream =======================================================================================
    @Headers({"accept: */*"})
    @GET(Constants.Apis.LIST_LIVESTREAM)
    Call<LiveStreamResponse> getListLiveStream(@Query("featureId") int featureId,
                                               @Query("page") int page,
                                               @Query("size") int size,
                                               @Query("userId") String userId);

    @POST(Constants.Apis.FOLLOW_LIVESTREAM_CHANNEL)
    Call<LiveStreamFollowResponse> followLivestreamChannel(@Query("livestreamId") String livestreamId,
                                                           @Query("streamerId") String streamerId,
                                                           @Query("userId") String userId);

    @POST(Constants.Apis.LIKE_LIVESTREAM)
    Call<LiveStreamFollowResponse> likeLivestream(@Query("livestreamId") String livestreamId,
                                                  @Query("streamerId") String streamerId,
                                                  @Query("userId") String userId);

    @GET(Constants.Apis.REPORT_LIVESTREAM)
    Call<LiveStreamGetReportListResponse> getLivestreamReportList();

    @POST(Constants.Apis.REPORT_LIVESTREAM)
    Call<BaseResponse> reportLivestream(@Query("livestreamId") String livestreamId,
                                        @Query("reportContent") String reportContent,
                                        @Query("reportId") int reportId,
                                        @Query("userId") String userId,
                                        @Query("streamerId") String streamerId);



}