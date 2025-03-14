package com.example.livestream_update.Ringme.LiveStream.holder;

import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.vtm.R;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.customview.TagGroup;
import com.vtm.ringme.livestream.listener.MessageActionListener;
import com.vtm.ringme.livestream.model.LiveStreamMessage;


public class MsgLikeVideoHolder extends BaseMessageLiveStreamHolder {

    private TagGroup tagGroup;
    private AppCompatActivity activity;
    private ApplicationController app;
    private MessageActionListener listenerMsg;
    private LiveStreamMessage message;

    public MsgLikeVideoHolder(View itemView, AppCompatActivity act, MessageActionListener listener) {
        super(itemView);

        this.activity = act;
        app = (ApplicationController) activity.getApplication();
        this.listenerMsg = listener;
        tagGroup = itemView.findViewById(R.id.tagLikeVideo);
        String s1 = activity.getString(R.string.ls_like);
        String s2 = activity.getString(R.string.ls_share);
        tagGroup.setTags(s1, s2);
        tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(TagGroup.TagView tagView, String tag) {
                if (listenerMsg != null && !TextUtils.isEmpty(tag))
                    if (tag.equals(activity.getString(R.string.ls_like)) || tag.equals(activity.getString(R.string.ls_unlike)))
                        listenerMsg.onLikeVideo(tagGroup);
                    else
                        listenerMsg.onShareVideo(false);
            }
        });
    }

    @Override
    public void setElement(Object obj, int pos) {
        if (obj instanceof LiveStreamMessage) {
            message = (LiveStreamMessage) obj;

            if (message.getCurrentVideo() != null) {
                boolean isLike = message.getCurrentVideo().isLike();
                String s1 = activity.getString(R.string.ls_like);
                if (isLike)
                    s1 = activity.getString(R.string.ls_unlike);
                String s2 = activity.getString(R.string.ls_share);
                tagGroup.setTags(s1, s2);
            }
        }
    }
}
