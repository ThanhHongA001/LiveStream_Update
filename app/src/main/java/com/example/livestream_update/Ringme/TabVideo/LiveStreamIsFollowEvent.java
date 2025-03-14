package com.example.livestream_update.Ringme.TabVideo;

public class LiveStreamIsFollowEvent {
    private boolean isFollow;

    public LiveStreamIsFollowEvent(boolean isFollow) {
        this.isFollow = isFollow;
    }

    public boolean isFollow() {
        return isFollow;
    }
}

