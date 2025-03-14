package com.example.livestream_update.Ringme.LiveStream.listener;

public interface LivestreamCommentActionListener {
    void onClickDeleteComment(String commentId, String avatar, String userName);

    void onClickUserProfile(String msisdn, String name);
}

