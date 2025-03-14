package com.example.livestream_update.Ringme.Home;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vtm.R;
import com.vtm.databinding.RmItemSearchChannelListBinding;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.model.tab_video.Channel;

import java.util.List;

public class VideoSearchChannelListAdapter extends RecyclerView.Adapter<VideoSearchChannelListAdapter.VideoSearchChannelListHolder> {
    List<Channel> channels;
    AppCompatActivity activity;

    @SuppressLint("NotifyDataSetChanged")
    public void setChannelsMainHomes(List<Channel> channels) {
        this.channels = channels;
        notifyDataSetChanged();
    }

    public VideoSearchChannelListAdapter(List<Channel> channels, AppCompatActivity activity) {
        this.channels = channels;
        this.activity = activity;
    }

    @NonNull
    @Override
    public VideoSearchChannelListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoSearchChannelListHolder(RmItemSearchChannelListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoSearchChannelListHolder holder, int position) {
        Channel channel = channels.get(position);
        if (channel.getUrlImage() != null)
            Glide.with(activity)
                    .load(channel.getUrlImage())
                    .placeholder(R.drawable.rm_df_channel_avatar)
                    .centerCrop()
                    .into(holder.binding.videoItem);
        else holder.binding.videoItem.setImageResource(R.drawable.rm_df_channel_avatar);
        holder.binding.videoName.setText(channel.getName());
        holder.binding.getRoot().setOnClickListener(v -> ApplicationController.self().openChannelInfo(activity, channel));
    }

    @Override
    public int getItemCount() {
        return channels == null ? 0 : channels.size();
    }

    public static class VideoSearchChannelListHolder extends RecyclerView.ViewHolder {
        RmItemSearchChannelListBinding binding;

        public VideoSearchChannelListHolder(@NonNull RmItemSearchChannelListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
