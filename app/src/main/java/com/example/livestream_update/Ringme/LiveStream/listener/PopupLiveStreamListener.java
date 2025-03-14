/*
 * Copyright (c) https://bigzun.blogspot.com/
 * Email: bigzun.com@gmail.com
 * Created by namnh40 on 2020/2/25
 *
 */

package com.example.livestream_update.Ringme.LiveStream.listener;

import com.vtm.ringme.model.tab_video.Channel;

public interface PopupLiveStreamListener {
    void subscriberChannel(Channel channel);

    void likeVideo();

    void commentVideo();

    void shareVideo();

    void onFullScreen(boolean isFullScreen);

    void qualityVideo();

    void dismissPopup();

    void onBack();

    void switchComment();
}
