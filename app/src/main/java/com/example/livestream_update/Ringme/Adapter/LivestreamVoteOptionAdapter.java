package com.example.livestream_update.Ringme.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vtm.R;
import com.vtm.databinding.RmItemLivestreamVoteOptionBinding;
import com.vtm.ringme.livestream.model.LivestreamVoteOptionModel;

import java.util.ArrayList;

public class LivestreamVoteOptionAdapter extends RecyclerView.Adapter<LivestreamVoteOptionAdapter.LivestreamVoteOptionViewHolder> {
    private final Context context;
    private ArrayList<LivestreamVoteOptionModel> list;
    private OnClickVoteOptionListener listener;

    public LivestreamVoteOptionAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<LivestreamVoteOptionModel> list) {
        this.list = list;
    }

    public void setListener(OnClickVoteOptionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public LivestreamVoteOptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LivestreamVoteOptionViewHolder(RmItemLivestreamVoteOptionBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull LivestreamVoteOptionViewHolder holder, int position) {
        LivestreamVoteOptionModel item = list.get(position);
        holder.binding.tvVoteOptionName.setText(item.getTitle());
        if (item.isSelect()) {
            holder.binding.icRadio.setImageResource(R.drawable.rm_ic_livestream_vote_option_selected);
            holder.binding.root.setCardBackgroundColor(Color.parseColor("#332DB742"));
            holder.binding.ll.setBackgroundResource(R.drawable.rm_bg_transparent_green_border_8);
        } else {
            holder.binding.icRadio.setImageResource(R.drawable.rm_bg_border_outline_grey_corner_20);
            holder.binding.root.setCardBackgroundColor(Color.parseColor("#141415"));
            holder.binding.ll.setBackgroundResource(R.drawable.rm_bg_transparent_grey_border_8);
        }
        holder.binding.root.setOnClickListener(view -> {
            for (LivestreamVoteOptionModel model : list) {
                model.setSelect(false);
            }
            item.setSelect(true);
            listener.onClickVoteOption(item);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class LivestreamVoteOptionViewHolder extends RecyclerView.ViewHolder {

        public final RmItemLivestreamVoteOptionBinding binding;
        public LivestreamVoteOptionViewHolder(@NonNull RmItemLivestreamVoteOptionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnClickVoteOptionListener {
        void onClickVoteOption(LivestreamVoteOptionModel option);
    }
}
