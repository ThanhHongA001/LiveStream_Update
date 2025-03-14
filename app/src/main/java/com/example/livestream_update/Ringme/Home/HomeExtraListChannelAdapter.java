package com.example.livestream_update.Ringme.Home;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vtm.R;
import com.vtm.databinding.RmItemListLivestreamHomeExtraBinding;
import com.vtm.ringme.base.CustomLinearLayoutManager;
import com.vtm.ringme.livestream.listener.OnEndlessScrollListener;
import com.vtm.ringme.model.tab_video.Channel;

import java.util.List;

public class HomeExtraListChannelAdapter extends RecyclerView.Adapter<HomeExtraListChannelAdapter.ListChannelViewHolder> {
    private final AppCompatActivity context;
    private List<Channel> channels;
    private boolean isLeftData;
    private boolean isLoading;
    private IScrollListener iScrollListener;
    private VideoSearchChannelListAdapter channelListAdapter;

    public HomeExtraListChannelAdapter(AppCompatActivity context) {
        this.context = context;
        this.isLeftData = true;
        this.isLoading = false;
    }

    public void setIScrollListener(IScrollListener iScrollListener) {
        this.iScrollListener = iScrollListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Channel> channels) {
        this.channels = channels;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addData(List<Channel> channels) {
        this.channels.addAll(channels);
        if (channels.size() < 5) {
            isLeftData = false;
        }
        isLoading = false;
        channelListAdapter.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListChannelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RmItemListLivestreamHomeExtraBinding binding = RmItemListLivestreamHomeExtraBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ListChannelViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListChannelViewHolder holder, int position) {
        channelListAdapter = new VideoSearchChannelListAdapter(channels, context);
        channelListAdapter.setChannelsMainHomes(channels);
        holder.binding.tvCategoryName.setText(context.getResources().getString(R.string.search_tab_channel));
        holder.binding.rcvLiveStreamList.setAdapter(channelListAdapter);
        holder.binding.rcvLiveStreamList.addOnScrollListener(new OnEndlessScrollListener(5) {
            @Override
            public void onLoadNextPage(View view) {
                super.onLoadNextPage(view);
                if (isLeftData && !isLoading && channels.size() >= 5) {
                    iScrollListener.onScrollListener(channels.size() / 5, 5);
                    isLoading = true;
                }
            }
        });
        holder.binding.viewHashtagLiveStream.setOnClickListener(view -> {
            Intent intent = new Intent(context, ViewAllLivestreamActivity.class);
            intent.putExtra(ViewAllLivestreamActivity.TYPE, ViewAllLivestreamActivity.TYPE_CHANNEL);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (channels == null || channels.size() == 0) {
            return 0;
        } else return 1;
    }

    public class ListChannelViewHolder extends RecyclerView.ViewHolder {
        private final RmItemListLivestreamHomeExtraBinding binding;
        public ListChannelViewHolder(@NonNull RmItemListLivestreamHomeExtraBinding binding) {
            super(binding.getRoot());
            LinearLayoutManager layoutMgr = new CustomLinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
            this.binding = binding;
            binding.rcvLiveStreamList.setFocusable(false);
            binding.rcvLiveStreamList.setLayoutManager(layoutMgr);
        }
    }
}
