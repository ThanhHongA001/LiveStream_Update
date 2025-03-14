package com.example.livestream_update.Ringme.Home;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vtm.R;
import com.vtm.databinding.RmItemLivestreamBinding;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.activities.LivestreamFutureActivity;
import com.vtm.ringme.livestream.activity.LivestreamDetailActivity;
import com.vtm.ringme.model.livestream.LivestreamModel;
import com.vtm.ringme.model.tab_video.Video;
import com.vtm.ringme.utils.Utilities;
import com.vtm.ringme.values.Constants;

import java.util.List;

public class HomeLiveStreamAdapter extends RecyclerView.Adapter<HomeLiveStreamAdapter.LiveStreamsCarosuelOnlyThumbViewHolder> {

    private final AppCompatActivity context;
    private List<LivestreamModel> liveStreamData;

    public HomeLiveStreamAdapter(AppCompatActivity context) {
        this.context = context;
    }

    public void setLiveStreamData(List<LivestreamModel> liveStreamData) {
        this.liveStreamData = liveStreamData;
    }

    public static class LiveStreamsCarosuelOnlyThumbViewHolder extends RecyclerView.ViewHolder {

        private final RmItemLivestreamBinding binding;

        public LiveStreamsCarosuelOnlyThumbViewHolder(@NonNull RmItemLivestreamBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public LiveStreamsCarosuelOnlyThumbViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RmItemLivestreamBinding binding = RmItemLivestreamBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LiveStreamsCarosuelOnlyThumbViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LiveStreamsCarosuelOnlyThumbViewHolder holder, int position) {
        LivestreamModel liveStreamItem = liveStreamData.get(position);
        if (liveStreamItem == null) {
            return;
        }
        if (liveStreamItem.getStatus() == 1) {
            holder.binding.live.setVisibility(View.VISIBLE);
            holder.binding.tvLive.setText(R.string.live);
            holder.binding.loutView.setVisibility(View.VISIBLE);
            holder.binding.tvViewNumber.setText(context.getString(liveStreamItem.getTotalView() > 1 ? R.string.view : R.string.video_views,
                    Utilities.shortenLongNumber(liveStreamItem.getTotalView())));
        } else if (liveStreamItem.getStatus() == 0) {
            holder.binding.live.setVisibility(View.VISIBLE);
            holder.binding.tvLive.setText(R.string.upcoming);
            holder.binding.loutView.setVisibility(View.GONE);
        } else {
            holder.binding.live.setVisibility(View.GONE);
            holder.binding.loutView.setVisibility(View.VISIBLE);
            holder.binding.tvViewNumber.setText(context.getString(liveStreamItem.getTotalView() > 1 ? R.string.view : R.string.video_views,
                    Utilities.shortenLongNumber(liveStreamItem.getTotalView())));
        }
        Glide.with(holder.binding.getRoot())
                .load(liveStreamItem.getLinkAvatar())
                .centerCrop()
                .placeholder(R.drawable.rm_placeholder)
                .into(holder.binding.imvLiveStreamThumb);

        holder.binding.tvTitle.setText(liveStreamItem.getTitle());
        holder.binding.getRoot().setOnClickListener(view -> {
            if (liveStreamItem.getStatus() == 1) {
                Intent intent = new Intent(context, LivestreamDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.KeyData.video, liveStreamItem);
                intent.putExtra(Constants.KeyData.data, bundle);
                context.startActivity(intent);
            } else if (liveStreamItem.getStatus() == 0) {
                Intent intent = new Intent(context, LivestreamFutureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.KeyData.video, liveStreamItem);
                intent.putExtra(Constants.KeyData.data, bundle);
                context.startActivity(intent);
            } else {
                    ApplicationController.self().
                            openVideoDetail(context, Video.convertLivestreamToVideo(liveStreamItem));
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