package com.example.livestream_update.Ringme.Values;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vtm.databinding.RmHomeMainLiveStreamListBinding;
import com.vtm.ringme.HomeLiveStreamV2Adapter;
import com.vtm.ringme.base.BaseAdapter;
import com.vtm.ringme.livestream.listener.HomeListener;
import com.vtm.ringme.model.HomePage;
import com.vtm.ringme.values.Constants;


public class HomeLiveStreamHolder extends BaseAdapter.ViewHolder {
    private final RmHomeMainLiveStreamListBinding binding;
    private final AppCompatActivity activity;
    private final HomeListener mListener;
    private HomeLiveStreamV2Adapter adapter;

    public HomeLiveStreamHolder(RmHomeMainLiveStreamListBinding binding, AppCompatActivity activity, HomeListener mListener) {
        super(binding.getRoot());
        this.binding = binding;
        this.mListener = mListener;
        this.activity = activity;
    }

    @Override
    public void bindData(Object item, int position) {
        if (item instanceof HomePage) {
            final String type = Constants.Intent.stream;
            binding.rcvLiveStreamList.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
            adapter = new HomeLiveStreamV2Adapter(activity, mListener);
            adapter.setLiveStreamData(((HomePage) item).getListLive());
            binding.rcvLiveStreamList.setAdapter(adapter);
            binding.viewHashtagLiveStream.setOnClickListener(view -> mListener.onClickNextButton(type));
        }
    }

    public void updateBtnNotifyLivestream(int position, boolean notify) {
        if (adapter != null) {
            adapter.updateBtnNotifyLivestream(position, notify);
        }
    }
}