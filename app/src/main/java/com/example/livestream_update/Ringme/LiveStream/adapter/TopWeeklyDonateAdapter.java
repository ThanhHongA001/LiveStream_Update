package com.example.livestream_update.Ringme.LiveStream.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vtm.R;
import com.vtm.databinding.RmItemTopDonateWeeklyBinding;
import com.vtm.ringme.livestream.model.TopDonate;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TopWeeklyDonateAdapter extends RecyclerView.Adapter<TopWeeklyDonateAdapter.TopDonateViewHolder> {
    private final Context context;
    private ArrayList<TopDonate> list;

    public TopWeeklyDonateAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(ArrayList<TopDonate> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TopDonateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopDonateViewHolder(RmItemTopDonateWeeklyBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull TopDonateViewHolder holder, int position) {
        TopDonate item = list.get(position);
        holder.binding.userName.setText(item.getName());
        holder.binding.starNumber.setText(shortenNumber(item.getStarNumber()));
        item.setRank(position + 1);
        Glide.with(context)
                .load(item.getAvatar())
                .placeholder(R.drawable.rm_ic_avatar_default)
                .into(holder.binding.avatar);

        if (item.getRank() == 1) {
            Glide.with(context)
                    .load(R.drawable.rm_ic_top_donate_1)
                    .into(holder.binding.imvRank);
            holder.binding.avatar.setBackground(context.getResources().getDrawable(R.drawable.rm_bg_donate_top1));
        } else if (item.getRank() == 2) {
            Glide.with(context)
                    .load(R.drawable.rm_ic_top_donate_2)
                    .into(holder.binding.imvRank);
            holder.binding.avatar.setBackground(context.getResources().getDrawable(R.drawable.rm_bg_donate_top2));
        } else {
            Glide.with(context)
                    .load(R.drawable.rm_ic_top_donate_3)
                    .into(holder.binding.imvRank);
            holder.binding.avatar.setBackground(context.getResources().getDrawable(R.drawable.rm_bg_donate_top3));
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class TopDonateViewHolder extends RecyclerView.ViewHolder {
        RmItemTopDonateWeeklyBinding binding;

        public TopDonateViewHolder(@NonNull RmItemTopDonateWeeklyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private String shortenNumber(long value) {
        String shortenValue = "";
        if (value < 1000) {
            shortenValue = String.valueOf(value);
        } else if (value < 999999) {
            shortenValue = new DecimalFormat("#.##").format((double) value / 1000) + "K";
        } else if (value < 999999999) {
            shortenValue = new DecimalFormat("#.##").format((double) value / 1000000) + "M";
        } else if (value < Long.parseLong("999999999999")) {
            shortenValue = new DecimalFormat("#.##").format((double) value / 1000000000) + "B";
        } else if (value < Long.parseLong("999999999999999")) {
            shortenValue = new DecimalFormat("#.##").format((double) value / Long.parseLong("1000000000000")) + "T";
        } else {
            shortenValue = new DecimalFormat("#.##").format((double) value / Long.parseLong("1000000000000000")) + "Q";
        }
        return shortenValue;
    }
}
