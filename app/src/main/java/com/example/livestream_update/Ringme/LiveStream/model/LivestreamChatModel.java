package com.example.livestream_update.Ringme.LiveStream.model;


import com.vtm.ringme.livestream.model.LiveStreamChatMessage;
import com.vtm.ringme.livestream.model.LiveStreamGiftMessage;

//todo model để hiển thị tin nhắn
public class LivestreamChatModel {
    private String type;

    private int reactionType = 0;

    private LiveStreamChatMessage chatMessage;
    private LiveStreamGiftMessage streamGift;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LiveStreamChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(LiveStreamChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public LiveStreamGiftMessage getStreamGift() {
        return streamGift;
    }

    public void setStreamGift(LiveStreamGiftMessage streamGift) {
        this.streamGift = streamGift;
    }

    public int getReactionType() {
        return reactionType;
    }

    public void setReactionType(int reactionType) {
        this.reactionType = reactionType;
    }
}
