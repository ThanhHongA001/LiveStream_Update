package com.example.livestream_update.Ringme.Common.api.video.callback;

import com.vtm.ringme.model.tab_video.Channel;

/**
 * Created by tuanha00 on 3/12/2018.
 */

public interface OnChannelInfoCallback {

    void onGetChannelInfoSuccess(Channel channel);

    void onGetChannelInfoError(String s);

    void onGetChannelInfoComplete();
}
