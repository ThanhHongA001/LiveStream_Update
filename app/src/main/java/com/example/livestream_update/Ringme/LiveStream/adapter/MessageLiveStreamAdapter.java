package com.example.livestream_update.Ringme.LiveStream.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vtm.R;
import com.vtm.ringme.livestream.listener.SmartTextClickListener;
import com.vtm.ringme.model.TagRingMe;
import com.vtm.ringme.model.tab_video.Video;
import com.vtm.ringme.livestream.holder.BaseMessageLiveStreamHolder;
import com.vtm.ringme.livestream.holder.MsgFollowChannelHolder;
import com.vtm.ringme.livestream.holder.MsgFriendWatchHolder;
import com.vtm.ringme.livestream.holder.MsgLikeVideoHolder;
import com.vtm.ringme.livestream.holder.MsgNormalHolder;
import com.vtm.ringme.livestream.holder.MsgSayHiHolder;
import com.vtm.ringme.livestream.listener.MessageActionListener;
import com.vtm.ringme.livestream.model.LiveStreamMessage;

import java.util.ArrayList;

public class MessageLiveStreamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private AppCompatActivity activity;
    private ArrayList<LiveStreamMessage> messages;
    private Video video;
    private MessageActionListener listener;
    private TagRingMe.OnClickTag onClickTag;
    private SmartTextClickListener smartTextClickListener;

    public MessageLiveStreamAdapter(AppCompatActivity activity, ArrayList<LiveStreamMessage> messages, Video currentVideo, MessageActionListener listener, TagRingMe.OnClickTag onClickTag) {
        this.activity = activity;
        this.messages = messages;
        this.video = currentVideo;
        this.inflater = LayoutInflater.from(activity);
        this.listener = listener;
        this.onClickTag = onClickTag;
    }

    public void setSmartTextClickListener(SmartTextClickListener smartTextClickListener) {
        this.smartTextClickListener = smartTextClickListener;
    }

    public void setMessages(ArrayList<LiveStreamMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseMessageLiveStreamHolder viewHolder;
        switch (viewType) {
            case LiveStreamMessage.TYPE_SAY_HI: {
                View view = inflater.inflate(R.layout.rm_holder_ls_say_hi, parent, false);
                viewHolder = new MsgSayHiHolder(view, activity, video, listener);
            }
            break;

            case LiveStreamMessage.TYPE_FRIEND_WATCH: {
                View view = inflater.inflate(R.layout.rm_holder_ls_friend_watch, parent, false);
                viewHolder = new MsgFriendWatchHolder(view, activity, listener);
            }
            break;

            case LiveStreamMessage.TYPE_FOLLOW_CHANNEL: {
                View view = inflater.inflate(R.layout.rm_holder_ls_follow_channel, parent, false);
                viewHolder = new MsgFollowChannelHolder(view, activity, video, listener);
            }
            break;

            case LiveStreamMessage.TYPE_LIKE_VIDEO: {
                View view = inflater.inflate(R.layout.rm_holder_ls_like_video, parent, false);
                viewHolder = new MsgLikeVideoHolder(view, activity, listener);
            }
            break;
            default: {
                View view = inflater.inflate(R.layout.rm_holder_ls_msg, parent, false);
                viewHolder = new MsgNormalHolder(view, activity, listener, onClickTag, smartTextClickListener);
            }
            break;

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BaseMessageLiveStreamHolder)
            ((BaseMessageLiveStreamHolder) holder).setElement(messages.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (messages == null || messages.isEmpty())
            return 0;
        return messages.size();
    }

}
