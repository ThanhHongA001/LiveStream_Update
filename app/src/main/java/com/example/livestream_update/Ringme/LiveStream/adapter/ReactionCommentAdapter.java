package com.example.livestream_update.Ringme.LiveStream.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vtm.R;
import com.vtm.databinding.RmItemReactionCommentBinding;
import com.vtm.ringme.livestream.model.ReactionCommentModel;

import java.util.ArrayList;
import java.util.Collections;

public class ReactionCommentAdapter extends RecyclerView.Adapter<ReactionCommentAdapter.ReactionCommentViewHolder> {
    private ArrayList<ReactionCommentModel> list;

    public ReactionCommentAdapter() {
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(ArrayList<ReactionCommentModel> list) {
        if (list != null && list.size() > 0) {
            if (list.size() > 1)
                Collections.sort(list);
            if (list.size() > 3) {
                ArrayList<ReactionCommentModel> temp = list;
                list = new ArrayList<>();
                list.add(temp.get(0));
                list.add(temp.get(1));
                list.add(temp.get(2));
            }

            ArrayList<ReactionCommentModel> emptyReactList = new ArrayList<>();
            for (ReactionCommentModel item : list) {
                if (item.getNumber() <= 0) {
                    emptyReactList.add(item);
                }
            }

            if (emptyReactList.size() != 0) {
                list.removeAll(emptyReactList);
            }
            this.list = list;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ReactionCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RmItemReactionCommentBinding binding = RmItemReactionCommentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ReactionCommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReactionCommentViewHolder holder, int position) {
        if (list != null && list.size() > 0) {
            ReactionCommentModel item = list.get(position);
            switch (item.getType()) {
                case "1":
                    holder.binding.ivItemReact.setImageResource(R.drawable.rm_ic_react_like);
                    break;
                case "2":
                    holder.binding.ivItemReact.setImageResource(R.drawable.rm_ic_react_heart);
                    break;
                case "3":
                    holder.binding.ivItemReact.setImageResource(R.drawable.rm_ic_react_haha);
                    break;
                case "4":
                    holder.binding.ivItemReact.setImageResource(R.drawable.rm_ic_react_wow);
                    break;
                case "5":
                    holder.binding.ivItemReact.setImageResource(R.drawable.rm_ic_react_sad);
                    break;
                case "6":
                    holder.binding.ivItemReact.setImageResource(R.drawable.rm_ic_react_angry);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return Math.min(list.size(), 3);
        }
        return 0;
    }

    public static class ReactionCommentViewHolder extends RecyclerView.ViewHolder {
        private final RmItemReactionCommentBinding binding;
        public ReactionCommentViewHolder(@NonNull RmItemReactionCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
