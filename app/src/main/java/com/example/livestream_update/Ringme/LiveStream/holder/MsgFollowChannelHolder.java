package com.example.livestream_update.Ringme.LiveStream.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vtm.ringme.ApplicationController;
import com.vtm.R;
import com.vtm.ringme.customview.TagGroup;
import com.vtm.ringme.model.tab_video.Video;
import com.vtm.ringme.livestream.listener.MessageActionListener;
import com.vtm.ringme.livestream.model.LiveStreamMessage;

/**
 * Created by thanhnt72 on 8/15/2019.
 */

public class MsgFollowChannelHolder extends BaseMessageLiveStreamHolder {

    private TagGroup tagGroup;
    private TextView tvSayHi;
    private AppCompatActivity activity;
    private ApplicationController app;
    private MessageActionListener listenerMsg;
    private LiveStreamMessage message;

    public MsgFollowChannelHolder(View itemView, AppCompatActivity act, Video video, MessageActionListener listener) {
        super(itemView);

        this.activity = act;
        app = (ApplicationController) activity.getApplication();
        this.listenerMsg = listener;
        tagGroup = itemView.findViewById(R.id.tagFollowChannel);
        tvSayHi = itemView.findViewById(R.id.tvSayHi);

        String nameChannel = "";
        if (video != null && video.getChannel() != null) {
            nameChannel = video.getChannel().getName();
        }
        tvSayHi.setText(String.format(activity.getString(R.string.ls_like_this_channel), nameChannel));

        String s1 = activity.getString(R.string.onmedia_follow);
        tagGroup.setTags(s1);
        tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(TagGroup.TagView tagView, String tag) {
                if (listenerMsg != null && !TextUtils.isEmpty(tag))
                    listenerMsg.onFollowChannel(message, tagGroup);
            }
        });
    }

    @Override
    public void setElement(Object obj, int pos) {
        if (obj instanceof LiveStreamMessage) {
            message = (LiveStreamMessage) obj;
            if (message.getCurrentVideo() != null && message.getCurrentVideo().getChannel() != null) {
                boolean isFollow = message.getCurrentVideo().getChannel().isFollow();
                String s1 = activity.getString(R.string.onmedia_follow);
                if (isFollow)
                    s1 = activity.getString(R.string.onmedia_unfollow);
                tagGroup.setTags(s1);
            }
        }

    }
}
