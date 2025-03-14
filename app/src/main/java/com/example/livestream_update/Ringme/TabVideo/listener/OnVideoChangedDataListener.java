package com.example.livestream_update.Ringme.TabVideo.listener;

import com.vtm.ringme.model.tab_video.Video;

/**
 * Created by HoangAnhTuan on 3/26/2018.
 */

public interface OnVideoChangedDataListener {
    void onVideoLikeChanged(Video video);

    void onVideoShareChanged(Video video);

    void onVideoCommentChanged(Video video);

    void onVideoSaveChanged(Video video);

    void onVideoWatchLaterChanged(Video video);
}
