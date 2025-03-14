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
import com.vtm.databinding.RmItemViewAllLivestreamBinding;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.activities.LivestreamFutureActivity;
import com.vtm.ringme.helper.TimeHelper;
import com.vtm.ringme.livestream.activity.LivestreamDetailActivity;
import com.vtm.ringme.model.livestream.LivestreamModel;
import com.vtm.ringme.utils.DateTimeUtils;
import com.vtm.ringme.utils.Utilities;
import com.vtm.ringme.values.Constants;

import java.util.List;

public class ViewAllLiveStreamAdapter extends RecyclerView.Adapter<ViewAllLiveStreamAdapter.LiveStreamsCarosuelOnlyThumbViewHolder> {

    private final AppCompatActivity context;
    private List<LivestreamModel> liveStreamData;

    public ViewAllLiveStreamAdapter(AppCompatActivity context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setLiveStreamData(List<LivestreamModel> liveStreamData) {
        this.liveStreamData = liveStreamData;
        notifyDataSetChanged();
    }

    public static class LiveStreamsCarosuelOnlyThumbViewHolder extends RecyclerView.ViewHolder {

        private final RmItemViewAllLivestreamBinding binding;

        public LiveStreamsCarosuelOnlyThumbViewHolder(@NonNull RmItemViewAllLivestreamBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public LiveStreamsCarosuelOnlyThumbViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RmItemViewAllLivestreamBinding binding = RmItemViewAllLivestreamBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LiveStreamsCarosuelOnlyThumbViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveStreamsCarosuelOnlyThumbViewHolder holder, int position) {
        LivestreamModel liveStreamItem = liveStreamData.get(position);
        if (liveStreamItem == null) {
            return;
        }
        if (liveStreamItem.getStatus() == 1) {
            holder.binding.live.setVisibility(View.VISIBLE);
            holder.binding.tvLive.setText(R.string.live);
        } else if (liveStreamItem.getStatus() == 0) {
            holder.binding.live.setVisibility(View.VISIBLE);
            holder.binding.tvLive.setText(R.string.upcoming);
        } else {
            holder.binding.live.setVisibility(View.GONE);
        }

        Glide.with(holder.binding.getRoot())
                .load(liveStreamItem.getLinkAvatar())
                .centerCrop()
                .placeholder(R.drawable.rm_placeholder)
                .into(holder.binding.imvLiveStreamThumb);
        Glide.with(holder.binding.getRoot())
                .load(liveStreamItem.getChannel().getUrlImage())
                .centerCrop()
                .placeholder(R.drawable.rm_df_channel_avatar)
                .into(holder.binding.ivAvatar);
        if (liveStreamItem.getStatus() == 0) {
            holder.binding.layoutViewNumber.setVisibility(View.GONE);
            holder.binding.tvDatetime.setText(context.getString(R.string.live_at,
                    TimeHelper.formatTimeEventMessage(liveStreamItem.getTimeEventStart())));
        } else {
            holder.binding.layoutViewNumber.setVisibility(View.VISIBLE);
            holder.binding.tvDatetime.setText(DateTimeUtils.calculateTime(context.getResources(), liveStreamItem.getTimeStart()));
            holder.binding.tvViewNumber.setText(context.getString(liveStreamItem.getTotalView() > 1 ? R.string.view : R.string.video_views,
                    Utilities.shortenLongNumber(liveStreamItem.getTotalView())));
        }

        holder.binding.tvTitle.setText(liveStreamItem.getTitle());
        holder.binding.imvLiveStreamThumb.setOnClickListener(view -> {
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

            }
        });

        holder.binding.ivAvatar.setOnClickListener(view -> {
            ApplicationController.self().openChannelInfo(context, liveStreamItem.getChannel());
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