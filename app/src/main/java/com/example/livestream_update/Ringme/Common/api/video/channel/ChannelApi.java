package com.example.livestream_update.Ringme.Common.api.video.channel;

import com.vtm.ringme.common.api.video.callback.OnChannelInfoCallback;

/**
 * Created by tuanha00 on 3/14/2018.
 */

public interface ChannelApi {
    void callApiSubOrUnsubChannel(String id, boolean follow);

    void getChannelInfo(String id, OnChannelInfoCallback channelInfoCallback);
}
