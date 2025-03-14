package com.example.livestream_update.Ringme.Values;


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
import com.vtm.databinding.RmItemLivestreamV2Binding;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.activities.LivestreamFutureActivity;
import com.vtm.ringme.livestream.activity.LivestreamDetailActivity;
import com.vtm.ringme.livestream.listener.HomeListener;
import com.vtm.ringme.model.livestream.LivestreamModel;
import com.vtm.ringme.model.tab_video.Video;
import com.vtm.ringme.values.Constants;
import com.vtm.ringme.values.ViewsUnitEnum;

import java.util.List;

public class HomeLiveStreamV2Adapter extends RecyclerView.Adapter<HomeLiveStreamV2Adapter.LiveStreamsCarosuelOnlyThumbViewHolder> {

    private final AppCompatActivity context;
    private List<LivestreamModel> liveStreamData;
    private final HomeListener mListener;

    public HomeLiveStreamV2Adapter(AppCompatActivity context, HomeListener mListener) {
        this.context = context;
        this.mListener = mListener;
    }

    public void setLiveStreamData(List<LivestreamModel> liveStreamData) {
        this.liveStreamData = liveStreamData;
    }

    public void updateBtnNotifyLivestream(int position, boolean notify) {
        liveStreamData.get(position).setIsNotified(!notify);
        notifyItemChanged(position);
    }

    public static class LiveStreamsCarosuelOnlyThumbViewHolder extends RecyclerView.ViewHolder {

        private final RmItemLivestreamV2Binding binding;

        public LiveStreamsCarosuelOnlyThumbViewHolder(@NonNull RmItemLivestreamV2Binding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public LiveStreamsCarosuelOnlyThumbViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RmItemLivestreamV2Binding binding = RmItemLivestreamV2Binding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LiveStreamsCarosuelOnlyThumbViewHolder(binding);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull LiveStreamsCarosuelOnlyThumbViewHolder holder, int position) {
        LivestreamModel liveStreamItem = liveStreamData.get(position);
        if (liveStreamItem == null) {
            return;
        }
        if (liveStreamItem.getStatus() == 1) {
            holder.binding.live.setVisibility(View.VISIBLE);
            holder.binding.tvLive.setText(R.string.live);
            holder.binding.btnNotifyMe.setVisibility(View.GONE);
            holder.binding.loutView.setVisibility(View.VISIBLE);
            holder.binding.tvViewNumber.setText(ViewsUnitEnum.getTextDisplay(liveStreamItem.getTotalView()));
        } else if (liveStreamItem.getStatus() == 0) {
            holder.binding.live.setVisibility(View.VISIBLE);
            holder.binding.tvLive.setText(R.string.upcoming);
            holder.binding.btnNotifyMe.setVisibility(View.VISIBLE);
            if (!liveStreamItem.getIsNotified()) {
                holder.binding.btnNotifyMe.setText(R.string.notify_me);
                holder.binding.btnNotifyMe.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rm_ic_livestream_notify_me, 0, 0, 0);
                holder.binding.btnNotifyMe.setCompoundDrawablePadding(5);
                holder.binding.btnNotifyMe.setBackgroundDrawable(context.getDrawable(R.drawable.rm_bg_btn_notify_me));
            } else {
                holder.binding.btnNotifyMe.setText(R.string.notified_me);
                holder.binding.btnNotifyMe.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rm_ic_tick_v6, 0, 0, 0);
                holder.binding.btnNotifyMe.setCompoundDrawablePadding(5);
                holder.binding.btnNotifyMe.setBackgroundDrawable(context.getDrawable(R.drawable.rm_bg_btn_notified_me));
            }
            holder.binding.loutView.setVisibility(View.GONE);
        } else {
            holder.binding.live.setVisibility(View.GONE);
            holder.binding.btnNotifyMe.setVisibility(View.GONE);
            holder.binding.loutView.setVisibility(View.VISIBLE);
            holder.binding.tvViewNumber.setText(ViewsUnitEnum.getTextDisplay(liveStreamItem.getTotalView()));
        }
        Glide.with(holder.binding.getRoot())
                .load(liveStreamItem.getLinkAvatar())
                .centerCrop()
                .placeholder(R.drawable.rm_placeholder)
                .into(holder.binding.imvLiveStreamThumb);

        holder.binding.tvTitle.setText(liveStreamItem.getTitle());
        holder.binding.tvName.setText(liveStreamItem.getChannel().getName());
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
        holder.binding.btnNotifyMe.setOnClickListener(view -> {
            mListener.onClickNotifyMe(liveStreamItem.getId(), liveStreamItem.getTimeEventStart(), position, liveStreamItem.getIsNotified());
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