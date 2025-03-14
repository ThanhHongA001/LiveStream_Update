package com.example.livestream_update.Ringme.Values;


import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.vtm.databinding.RmHomeMainLiveStreamListBinding;
import com.vtm.ringme.HomeLiveStreamHolder;
import com.vtm.ringme.base.BaseAdapter;
import com.vtm.ringme.livestream.listener.HomeListener;
import com.vtm.ringme.model.HomePage;


public class HomeNewAdapter extends BaseAdapter<BaseAdapter.ViewHolder, HomePage> {
    private AppCompatActivity activity;
    private HomeListener homeListener;
    private HomeLiveStreamHolder liveStreamHolder;

    public HomeNewAdapter(AppCompatActivity activity, HomeListener homeListener) {
        super(activity);
        this.activity = activity;
        this.homeListener = homeListener;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (viewType == 0) {
            liveStreamHolder = new HomeLiveStreamHolder(RmHomeMainLiveStreamListBinding.inflate(layoutInflater, parent, false), activity, homeListener);
            return liveStreamHolder;
//        }
//        return new EmptyHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(getItem(position), position);
    }

    public void updateBtnNotifyLivestream(int position, boolean notify) {
        if (liveStreamHolder != null) {
            liveStreamHolder.updateBtnNotifyLivestream(position, notify);
        }
    }
}