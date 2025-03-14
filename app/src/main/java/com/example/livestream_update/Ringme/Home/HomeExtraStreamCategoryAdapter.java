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

import com.vtm.databinding.RmItemListLivestreamHomeExtraBinding;
import com.vtm.ringme.base.CustomLinearLayoutManager;
import com.vtm.ringme.livestream.listener.OnEndlessScrollListener;
import com.vtm.ringme.model.livestream.LivestreamModel;

import java.util.ArrayList;

public class HomeExtraStreamCategoryAdapter extends RecyclerView.Adapter<HomeExtraStreamCategoryAdapter.HomeExtraStreamCategoryViewHolder> {

    private final AppCompatActivity context;
    private LiveStreamListMainHome liveStreamData;
    private HomeLiveStreamAdapter liveStreamListAdapter;
    private boolean isLeftData;
    private boolean isLoading;
    private IScrollListener iScrollListener;

    public HomeExtraStreamCategoryAdapter(AppCompatActivity context) {
        this.context = context;
        this.isLeftData = true;
        this.isLoading = false;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setLiveStreamsData(LiveStreamListMainHome liveStreamData) {
        this.liveStreamData = liveStreamData;
        notifyDataSetChanged();
    }

    public void setIScrollListener(IScrollListener iScrollListener) {
        this.iScrollListener = iScrollListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addData(ArrayList<LivestreamModel> listGame){
        liveStreamData.getLiveStreamTestList().addAll(listGame);
        if(listGame.size() < 10){
            isLeftData = false;
        }
        isLoading = false;
        liveStreamListAdapter.notifyDataSetChanged();
    }

    public class HomeExtraStreamCategoryViewHolder extends RecyclerView.ViewHolder {

        private final RmItemListLivestreamHomeExtraBinding binding;

        public HomeExtraStreamCategoryViewHolder(@NonNull RmItemListLivestreamHomeExtraBinding binding) {
            super(binding.getRoot());
            LinearLayoutManager layoutMgr = new CustomLinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
            this.binding = binding;
            binding.rcvLiveStreamList.setFocusable(false);
            binding.rcvLiveStreamList.setLayoutManager(layoutMgr);
        }
    }

    @NonNull
    @Override
    public HomeExtraStreamCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RmItemListLivestreamHomeExtraBinding binding = RmItemListLivestreamHomeExtraBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HomeExtraStreamCategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeExtraStreamCategoryViewHolder holder, int position) {
        liveStreamListAdapter = new HomeLiveStreamAdapter(context);
        liveStreamListAdapter.setLiveStreamData(liveStreamData.getLiveStreamTestList());

        holder.binding.tvCategoryName.setText(liveStreamData.getName());
        holder.binding.rcvLiveStreamList.setAdapter(liveStreamListAdapter);
        holder.binding.rcvLiveStreamList.addOnScrollListener(new OnEndlessScrollListener(4) {
            @Override
            public void onLoadNextPage(View view) {
                super.onLoadNextPage(view);
                if (isLeftData && !isLoading && liveStreamData.getLiveStreamTestList().size() >= 10) {
                    iScrollListener.onScrollListener(liveStreamData.getLiveStreamTestList().size() / 10, 10);
                    isLoading = true;
                }
            }
        });

        holder.binding.viewHashtagLiveStream.setOnClickListener(view -> {
            Intent intent = new Intent(context, ViewAllLivestreamActivity.class);
            intent.putExtra(ViewAllLivestreamActivity.TYPE, ViewAllLivestreamActivity.TYPE_LIVESTREAM);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (liveStreamData != null) {
            if (liveStreamData.getLiveStreamTestList().size() != 0) {
                return 1;
            }
        }
        return 0;
    }

}