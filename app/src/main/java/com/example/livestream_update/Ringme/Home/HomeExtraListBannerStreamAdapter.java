package com.example.livestream_update.Ringme.Home;


import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.vtm.databinding.RmItemListBannerHomeExtraBinding;
import com.vtm.ringme.model.livestream.LivestreamModel;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HomeExtraListBannerStreamAdapter extends RecyclerView.Adapter<HomeExtraListBannerStreamAdapter.HomeExtraStreamCategoryViewHolder> {

    private final AppCompatActivity context;
    private final ArrayList<LivestreamModel> liveStreamData = new ArrayList<>();
    private RmItemListBannerHomeExtraBinding binding;
    private Timer timer;

    public HomeExtraListBannerStreamAdapter(AppCompatActivity context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addData(ArrayList<LivestreamModel> data) {
        liveStreamData.addAll(data);
        notifyDataSetChanged();
    }

    public static class HomeExtraStreamCategoryViewHolder extends RecyclerView.ViewHolder {

        private final RmItemListBannerHomeExtraBinding binding;

        public HomeExtraStreamCategoryViewHolder(@NonNull RmItemListBannerHomeExtraBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public HomeExtraStreamCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RmItemListBannerHomeExtraBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HomeExtraStreamCategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeExtraStreamCategoryViewHolder holder, int position) {
        HomeExtraLiveStreamBannerAdapter liveStreamListAdapter = new HomeExtraLiveStreamBannerAdapter(context);
        liveStreamListAdapter.setLiveStreamData(liveStreamData);
        holder.binding.vpBannerHomeExtra.setAdapter(liveStreamListAdapter);
        holder.binding.indicator.setViewPager(holder.binding.vpBannerHomeExtra);

        if (timer == null) {
            autoSlide(liveStreamData);
            binding.vpBannerHomeExtra.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrollStateChanged(int state) {
                    super.onPageScrollStateChanged(state);
                    timer.cancel();
                    autoSlide(liveStreamData);
                }
            });
        }
        if (!liveStreamListAdapter.hasObservers())
            liveStreamListAdapter.registerAdapterDataObserver(binding.indicator.getAdapterDataObserver());
    }

    private void autoSlide(ArrayList<LivestreamModel> data){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    int currentItem = binding.vpBannerHomeExtra.getCurrentItem();
                    int totalItem = data.size();
                    if (currentItem < totalItem - 1) {
                        currentItem++;
                        binding.vpBannerHomeExtra.setCurrentItem(currentItem);
                    } else {
                        binding.vpBannerHomeExtra.setCurrentItem(0);
                    }
                });
            }
        }, 5000, 5000);
    }

    @Override
    public int getItemCount() {
        if (liveStreamData != null) {
            if (liveStreamData.size() != 0) {
                return 1;
            }
        }
        return 0;
    }

}