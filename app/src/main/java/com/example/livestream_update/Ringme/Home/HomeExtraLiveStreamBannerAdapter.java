package com.example.livestream_update.Ringme.Home;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vtm.R;
import com.vtm.databinding.RmItemBannerHomeExtraBinding;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.livestream.activity.LivestreamDetailActivity;
import com.vtm.ringme.model.livestream.LivestreamModel;
import com.vtm.ringme.model.tab_video.Video;
import com.vtm.ringme.values.Constants;

import java.util.List;

public class HomeExtraLiveStreamBannerAdapter extends RecyclerView.Adapter<HomeExtraLiveStreamBannerAdapter.LiveStreamBannerViewHolder> {

    private final AppCompatActivity context;
    private List<LivestreamModel> liveStreamData;

    public HomeExtraLiveStreamBannerAdapter(AppCompatActivity context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setLiveStreamData(List<LivestreamModel> liveStreamData) {
        this.liveStreamData = liveStreamData;
        notifyDataSetChanged();
    }

    public static class LiveStreamBannerViewHolder extends RecyclerView.ViewHolder {

        private final RmItemBannerHomeExtraBinding binding;

        public LiveStreamBannerViewHolder(@NonNull RmItemBannerHomeExtraBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public LiveStreamBannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RmItemBannerHomeExtraBinding binding = RmItemBannerHomeExtraBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LiveStreamBannerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveStreamBannerViewHolder holder, int position) {
        LivestreamModel liveStreamItem = liveStreamData.get(position);
        if (liveStreamItem == null) {
            return;
        }
        Glide.with(holder.binding.getRoot())
                .load(liveStreamItem.getLinkAvatar())
                .centerCrop()
                .placeholder(R.drawable.rm_placeholder)
                .into(holder.binding.imvItemBanner);
        holder.binding.tvDescription.setText(liveStreamItem.getTitle());
        holder.binding.getRoot().setOnClickListener(view -> {
            if (liveStreamItem.getStatus() == 1) {
                Intent intent = new Intent(context, LivestreamDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.KeyData.video, liveStreamItem);
                intent.putExtra(Constants.KeyData.data, bundle);
                context.startActivity(intent);
            } else {
                ApplicationController.self().openVideoDetail(context, Video.convertLivestreamToVideo(liveStreamItem));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (liveStreamData != null) {
            return liveStreamData.size();
        }
        return 0;
    }
}