package com.example.livestream_update.Ringme.LiveStream.listener;

import com.vtm.ringme.customview.TagGroup;
import com.vtm.ringme.livestream.model.LiveStreamMessage;


public interface MessageActionListener {
    void onClickUser(LiveStreamMessage message, int pos);

    void onQuickSendText(String content);

    void onClickShowKeyboard();

    void onReplyMessage(LiveStreamMessage message, int pos);

    void onReplyMessageLevel2(LiveStreamMessage message);

    void onInviteFriend();

    void onShareVideo(boolean shareSocial);

    void onClickLikeMessage(LiveStreamMessage message, int pos);

    void onFollowChannel(LiveStreamMessage message, TagGroup tagGroup);

    void onLikeVideo(TagGroup tagGroup);

    void onSendMessage(LiveStreamMessage message);
}
