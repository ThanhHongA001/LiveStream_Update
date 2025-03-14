package com.example.livestream_update.Ringme.Glide.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewbinding.ViewBinding;

import com.google.gson.Gson;
import com.vtm.databinding.RmFragmentLivestreamCategoryBinding;
import com.vtm.ringme.activities.LivestreamFutureActivity;
import com.vtm.ringme.adapter.LivestreamCategoryAdapter;
import com.vtm.ringme.api.HomeApi;
import com.vtm.ringme.api.response.LiveStreamResponse;
import com.vtm.ringme.base.BaseFragmentNew;
import com.vtm.ringme.common.api.http.HttpCallBack;
import com.vtm.ringme.livestream.listener.OnEndlessScrollListener;
import com.vtm.ringme.model.livestream.LivestreamModel;

import java.util.ArrayList;

public class LivestreamCategoryFragment extends BaseFragmentNew {
    private RmFragmentLivestreamCategoryBinding binding;
    private ArrayList<LivestreamModel> listLivestream;
    private LivestreamFutureActivity activity;
    private LivestreamCategoryAdapter adapter;
    private int featureId;
    private boolean isLoading = false;

    public LivestreamCategoryFragment(LivestreamFutureActivity activity, int featureId) {
        this.activity = activity;
        this.featureId = featureId;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public ViewBinding setViewBinding() {
        binding = RmFragmentLivestreamCategoryBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public int getResIdView() {
        return 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RmFragmentLivestreamCategoryBinding.inflate(inflater, container, false);

        listLivestream = new ArrayList<>();
        apiGetListLivestream();
        binding.rcv.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new LivestreamCategoryAdapter(activity);
        binding.rcv.setAdapter(adapter);
        return binding.getRoot();
    }

    private void apiGetListLivestream() {
        HomeApi.getInstance().getListLivestream(featureId, 0, new HttpCallBack() {
            @Override
            public void onSuccess(String data) throws Exception {
                try {
                    Gson gson = new Gson();
                    LiveStreamResponse response = gson.fromJson(data, LiveStreamResponse.class);
                    if (response != null && response.getListLivStream() != null) {
                        if (response.getListLivStream() != null && response.getListLivStream().size() > 0) {
                            listLivestream = response.getListLivStream();
                            adapter.setList(listLivestream);
                            binding.tvNoData.setVisibility(View.GONE);
                            binding.rcv.setVisibility(View.VISIBLE);
                            binding.rcv.addOnScrollListener(new OnEndlessScrollListener(4) {
                                @Override
                                public void onLoadNextPage(View view) {
                                    apiGetListLivestreamLoadMore();
                                }
                            });
                        } else {
                            binding.tvNoData.setVisibility(View.VISIBLE);
                            binding.rcv.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
            }
        });
    }

    private void apiGetListLivestreamLoadMore() {
        if (listLivestream.size() % 10 > 0) {
            return;
        }
        if (isLoading) {
            return;
        } else {
            isLoading = true;
            HomeApi.getInstance().getListLivestream(featureId, listLivestream.size() / 10, new HttpCallBack() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onSuccess(String data) throws Exception {
                    try {
                        Gson gson = new Gson();
                        LiveStreamResponse response = gson.fromJson(data, LiveStreamResponse.class);
                        if (response != null && response.getListLivStream() != null) {
                            if (response.getListLivStream() != null && response.getListLivStream().size() > 0) {
                                listLivestream.addAll(response.getListLivStream());
                                adapter.notifyDataSetChanged();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isLoading = false;
                }

                @Override
                public void onFailure(String message) {
                    super.onFailure(message);
                    isLoading = false;
                }
            });
        }
    }
}
