package com.example.livestream_update.Ringme.Home;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vtm.R;
import com.vtm.databinding.RmItemViewAllChannelBinding;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.customview.SubscribeChannelLayout;
import com.vtm.ringme.model.tab_video.Channel;

import java.util.List;

public class ViewAllChannelAdapter extends RecyclerView.Adapter<ViewAllChannelAdapter.VideoSearchChannelListHolder> {
    List<Channel> channels;
    AppCompatActivity activity;
    SubscribeChannelLayout.SubscribeChannelListener subscribeListener;

    @SuppressLint("NotifyDataSetChanged")
    public void setChannels(List<Channel> channels) {
        this.channels = channels;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearData() {
        channels.clear();
        notifyDataSetChanged();
    }

    public void setSubscribeListener(SubscribeChannelLayout.SubscribeChannelListener subscribeListener) {
        this.subscribeListener = subscribeListener;
    }

    public ViewAllChannelAdapter(AppCompatActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public VideoSearchChannelListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoSearchChannelListHolder(RmItemViewAllChannelBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoSearchChannelListHolder holder, int position) {
        Channel channel = channels.get(position);
        if (channel.getUrlImage() != null)
            Glide.with(activity)
                    .load(channel.getUrlImage())
                    .placeholder(R.drawable.rm_df_channel_avatar)
                    .centerCrop()
                    .into(holder.binding.ivChannelAvatar);
        else holder.binding.ivChannelAvatar.setImageResource(R.drawable.rm_df_channel_avatar);
        holder.binding.tvNumberSubscriptions.setText(String.format(activity.getString(R.string.people_subscription), channel.getNumFollow() + ""));
        if (channel.getNumVideo() <= 1)
            holder.binding.tvNumberVideo.setText(activity.getString(R.string.music_total_video, channel.getNumVideo()));
        else
            holder.binding.tvNumberVideo.setText(activity.getString(R.string.music_total_videos, channel.getNumVideo()));
        holder.binding.tvChannelName.setText(channel.getName());
        holder.binding.getRoot().setOnClickListener(v -> ApplicationController.self().openChannelInfo(activity, channel));
        if (channel.isFollow()) {
            holder.binding.tvSubscriptionsChannel.setText(R.string.unsubscribeChannel_v2);
            holder.binding.tvSubscriptionsChannel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.rm_ic_notification_1, 0);
            holder.binding.tvSubscriptionsChannel.setCompoundDrawablePadding(16);
        } else {
            holder.binding.tvSubscriptionsChannel.setText(R.string.subscribeChannel_v2);
            holder.binding.tvSubscriptionsChannel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            holder.binding.tvSubscriptionsChannel.setCompoundDrawablePadding(16);
        }

        holder.binding.tvSubscriptionsChannel.setOnClickListener(view -> {
            subscribeListener.onSub(channel);
            channel.setFollow(!channel.isFollow());
            if(channel.isFollow()){
                channel.setNumFollow(channel.getNumFollow() + 1);
            } else {
                channel.setNumFollow(channel.getNumFollow() - 1);
            }
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return channels == null ? 0 : channels.size();
    }

    public static class VideoSearchChannelListHolder extends RecyclerView.ViewHolder {
        RmItemViewAllChannelBinding binding;

        public VideoSearchChannelListHolder(@NonNull RmItemViewAllChannelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
