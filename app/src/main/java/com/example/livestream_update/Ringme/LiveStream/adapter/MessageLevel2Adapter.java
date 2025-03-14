package com.example.livestream_update.Ringme.LiveStream.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vtm.R;
import com.vtm.ringme.livestream.holder.BaseMessageLiveStreamHolder;
import com.vtm.ringme.livestream.holder.MsgNormalLevel2Holder;
import com.vtm.ringme.livestream.listener.MessageActionListener;
import com.vtm.ringme.livestream.model.LiveStreamMessage;
import com.vtm.ringme.model.TagRingMe;

import java.util.ArrayList;

public class MessageLevel2Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private AppCompatActivity activity;
    private ArrayList<LiveStreamMessage> messages;
    private MessageActionListener listener;
    private TagRingMe.OnClickTag onClickTag;

    public MessageLevel2Adapter(AppCompatActivity activity, ArrayList<LiveStreamMessage> messages, MessageActionListener listener, TagRingMe.OnClickTag onClickTag) {
        this.activity = activity;
        this.messages = messages;
        this.inflater = LayoutInflater.from(activity);
        this.listener = listener;
        this.onClickTag = onClickTag;
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
        View view = inflater.inflate(R.layout.rm_holder_ls_msg, parent, false);
        BaseMessageLiveStreamHolder viewHolder = new MsgNormalLevel2Holder(view, activity, listener, onClickTag);
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
