package com.example.livestream_update.Ringme.Adapter;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vtm.R;
import com.vtm.databinding.RmItemLivestreamByCategoryBinding;
import com.vtm.ringme.activities.LivestreamFutureActivity;
import com.vtm.ringme.helper.TimeHelper;
import com.vtm.ringme.model.livestream.LivestreamModel;
import com.vtm.ringme.utils.DateTimeUtils;
import com.vtm.ringme.utils.Utilities;

import java.util.ArrayList;

public class LivestreamCategoryAdapter extends RecyclerView.Adapter<LivestreamCategoryAdapter.LivestreamCategoryHolder> {
    private final LivestreamFutureActivity context;
    private ArrayList<LivestreamModel> list;

    public LivestreamCategoryAdapter(LivestreamFutureActivity context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(ArrayList<LivestreamModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LivestreamCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LivestreamCategoryHolder(RmItemLivestreamByCategoryBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LivestreamCategoryHolder holder, int position) {
        LivestreamModel item = list.get(position);

        Glide.with(context)
                .load(item.getLinkAvatar())
                .placeholder(R.drawable.rm_placeholder)
                .into(holder.binding.imgAvatar);

        holder.binding.tvTitle.setText(item.getTitle());

        if (item.getStatus() == 1) {
            holder.binding.live.setVisibility(View.VISIBLE);
            holder.binding.tvLive.setText(R.string.live);
            holder.binding.view.setVisibility(View.VISIBLE);
            holder.binding.tvView.setText(Utilities.shortenLongNumber(item.getTotalView()));
            holder.binding.tvTime.setText(DateTimeUtils.calculateTime(context.getResources(), item.getTimeStart()));
        } else if (item.getStatus() == 0) {
            holder.binding.live.setVisibility(View.VISIBLE);
            holder.binding.tvLive.setText(R.string.upcoming);
            holder.binding.view.setVisibility(View.GONE);
            holder.binding.tvTime.setText(context.getString(R.string.live_at,
                    TimeHelper.formatTimeEventMessage(item.getTimeEventStart())));
        } else {
            holder.binding.live.setVisibility(View.GONE);
            holder.binding.view.setVisibility(View.GONE);
            holder.binding.tvTime.setText(DateTimeUtils.calculateTime(context.getResources(), item.getTimeStart()));
        }

        holder.binding.getRoot().setOnClickListener(view -> {
            context.onClickOtherVideo(item);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class LivestreamCategoryHolder extends RecyclerView.ViewHolder {

        private final RmItemLivestreamByCategoryBinding binding;
        public LivestreamCategoryHolder(@NonNull RmItemLivestreamByCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
