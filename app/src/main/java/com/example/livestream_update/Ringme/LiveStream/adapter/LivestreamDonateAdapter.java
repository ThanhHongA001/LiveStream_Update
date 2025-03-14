package com.example.livestream_update.Ringme.LiveStream.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.vtm.databinding.RmItemDonateBinding;
import com.vtm.ringme.livestream.listener.ClickDonateListener;
import com.vtm.ringme.livestream.model.Donate;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class LivestreamDonateAdapter extends RecyclerView.Adapter<LivestreamDonateAdapter.DonateViewHolder> {
    private final Context context;
    private ArrayList<Donate> list;
    private ClickDonateListener clickStarPackageListener;

    public LivestreamDonateAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(ArrayList<Donate> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setClickStarPackageListener(ClickDonateListener clickStarPackageListener) {
        this.clickStarPackageListener = clickStarPackageListener;
    }

    @NonNull
    @Override
    public DonateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DonateViewHolder(RmItemDonateBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DonateViewHolder holder, int position) {
        Donate item = list.get(position);
        Glide.with(context).load(item.getImage()).into(holder.binding.ivCover);
        holder.binding.tvPrice.setText("$" + shortenNumber((long) item.getAmountStar()));
        holder.binding.getRoot().setOnClickListener(view -> {
            for (Donate obj : list) {
                obj.setSelected(false);
                notifyItemChanged(list.indexOf(obj));
            }
            item.setSelected(true);
            notifyItemChanged(position);
            clickStarPackageListener.onClickStarPackage(item);
        });
        if (item.isSelected()) {
            holder.binding.frame.setBackgroundColor(Color.parseColor("#3B88EE"));
//            holder.binding.tvPrice.setBackgroundColor(Color.parseColor("#635127"));
        } else {
            holder.binding.frame.setBackgroundColor(Color.parseColor("#303032"));
//            holder.binding.tvPrice.setBackgroundColor(Color.parseColor("#303032"));
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class DonateViewHolder extends RecyclerView.ViewHolder {
        private final RmItemDonateBinding binding;
        public DonateViewHolder(@NonNull RmItemDonateBinding binding) {
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
