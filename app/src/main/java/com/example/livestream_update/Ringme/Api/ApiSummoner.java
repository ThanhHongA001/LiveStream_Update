package com.example.livestream_update.Ringme.Api;


import com.vtm.ringme.api.ApiCollections;
import com.vtm.ringme.helper.ConfigLocalized;
import com.vtm.ringme.values.ApiConfig;


public class ApiSummoner {

    //todo api livestream
    public static ApiCollections liveStreamApi() {
        return ApiConfig.getClient(ConfigLocalized.DOMAIN_LIVESTREAM).create(ApiCollections.class);
    }
}

