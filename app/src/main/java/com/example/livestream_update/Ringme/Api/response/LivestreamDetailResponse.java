package com.example.livestream_update.Ringme.Api.response;

import com.vtm.ringme.api.BaseResponse;
import com.vtm.ringme.model.livestream.LivestreamModel;

public class LivestreamDetailResponse extends BaseResponse {
    LivestreamModel data;

    public LivestreamModel getData() {
        return data;
    }
}