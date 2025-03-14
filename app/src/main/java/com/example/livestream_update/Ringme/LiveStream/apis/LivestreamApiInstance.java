package com.example.livestream_update.Ringme.LiveStream.apis;

import com.vtm.ringme.values.Constants;

public class LivestreamApiInstance {

    public static LivestreamServices getLiveStreamInstance() {
        return ClientApi.getClient(Constants.Server.domain).create(LivestreamServices.class);
    }
}
