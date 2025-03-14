package com.example.livestream_update.Ringme.LiveStream.listener;

import com.vtm.ringme.livestream.model.LiveStreamMessage;

import java.util.ArrayList;

public interface MessageListener {
    void onNewMessage(LiveStreamMessage message);

    void onGetListMessage(ArrayList<LiveStreamMessage> messages);
}
