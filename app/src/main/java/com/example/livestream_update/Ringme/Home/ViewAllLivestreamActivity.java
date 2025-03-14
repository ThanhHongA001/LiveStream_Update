package com.example.livestream_update.Ringme.Home;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.google.gson.Gson;
import com.vtm.R;
import com.vtm.databinding.RmActivityViewAllLivestreamBinding;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.api.ApiCollections;
import com.vtm.ringme.api.ApiSummoner;
import com.vtm.ringme.api.HomeApi;
import com.vtm.ringme.api.response.LiveStreamResponse;
import com.vtm.ringme.base.BaseActivityNew;
import com.vtm.ringme.common.api.http.HttpCallBack;
import com.vtm.ringme.common.api.video.channel.ChannelApi;
import com.vtm.ringme.customview.SubscribeChannelLayout;
import com.vtm.ringme.livestream.listener.OnEndlessScrollListener;
import com.vtm.ringme.model.livestream.LivestreamModel;
import com.vtm.ringme.model.tab_video.Channel;
import com.vtm.ringme.utils.Utilities;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllLivestreamActivity extends BaseActivityNew implements SubscribeChannelLayout.SubscribeChannelListener {
    public static final String TYPE = "type";
    public static final String TYPE_LIVESTREAM = "type_livestream";
    public static final String TYPE_CHANNEL = "type_channel";
    public static final String TYPE_NEWS = "type_news";
    RmActivityViewAllLivestreamBinding binding;
    String type;
    boolean isLoading = false;
    boolean isLeftData = true;
    private ChannelApi channelApi;
    private ViewAllLiveStreamAdapter liveStreamListAdapter;
    private ArrayList<LivestreamModel> listLivestream = new ArrayList<>();
    private ViewAllChannelAdapter channelListAdapter;
    private ArrayList<Channel> listChannel = new ArrayList<>();

    @Override
    public ViewBinding getViewBinding() {
        binding = RmActivityViewAllLivestreamBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        type = getIntent().getStringExtra(TYPE);
        binding.loadingViewExtra.showLoading();
        initView();

        binding.swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.v5_main_color));
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            initView();
            if (Objects.equals(type, TYPE_CHANNEL)) {
                callApiListChannel();
            }
        });

        binding.loadingViewExtra.setOnClickRetryListener(view -> {
            binding.loadingViewExtra.showLoading();
            initView();
            if (Objects.equals(type, TYPE_CHANNEL)) {
                callApiListChannel();
            }
        });
    }

    private void initView() {
        binding.btnBack.setOnClickListener(view -> onBackPressed());
        binding.rcvListLivestream.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        switch (type) {
            case TYPE_LIVESTREAM:
                liveStreamListAdapter = new ViewAllLiveStreamAdapter(this);
                binding.rcvListLivestream.setAdapter(liveStreamListAdapter);
                binding.tvToolbarName.setText(getString(R.string.all_livestream));
                callApiListLivestream();
                break;
            case TYPE_CHANNEL:
                channelListAdapter = new ViewAllChannelAdapter(this);
                channelListAdapter.setSubscribeListener(this);
                binding.rcvListLivestream.setAdapter(channelListAdapter);
                binding.tvToolbarName.setText(getString(R.string.all_channel));
//                callApiListChannel();
                break;
        }
    }

    private void callApiListLivestream() {
        String userId = ApplicationController.self().getJidNumber();
        ApiCollections apiCollections = ApiSummoner.liveStreamApi();
        apiCollections.getListLiveStream(1, 0, 10, userId).enqueue(new Callback<LiveStreamResponse>() {
            @Override
            public void onResponse(@NonNull Call<LiveStreamResponse> call, @NonNull Response<LiveStreamResponse> response) {
                try {
                    if (response.body() != null && response.body().getListLivStream().size() != 0) {
                        listLivestream = response.body().getListLivStream();
                        liveStreamListAdapter.setLiveStreamData(listLivestream);
                        binding.rcvListLivestream.addOnScrollListener(new OnEndlessScrollListener(3) {
                            @Override
                            public void onLoadNextPage(View view) {
                                super.onLoadNextPage(view);
                                if (isLeftData && !isLoading && listLivestream.size() >= 10) {
                                    loadMoreLivestream(listLivestream.size() / 10);
                                    isLoading = true;
                                }
                            }
                        });
                        binding.loadingViewExtra.showLoadedSuccess();
                    } else binding.loadingViewExtra.showLoadedError();
                    binding.swipeRefreshLayout.setRefreshing(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LiveStreamResponse> call, @NonNull Throwable t) {
                binding.loadingViewExtra.showLoadedError();
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadMoreLivestream(int page) {
        String userId = ApplicationController.self().getJidNumber();
        ApiCollections apiCollections = ApiSummoner.liveStreamApi();
        apiCollections.getListLiveStream(1, page, 10, userId).enqueue(new Callback<LiveStreamResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<LiveStreamResponse> call, @NonNull Response<LiveStreamResponse> response) {
                try {
                    if (response.body() != null && response.body().getListLivStream().size() != 0) {
                        listLivestream.addAll(response.body().getListLivStream());
                        liveStreamListAdapter.notifyDataSetChanged();
                        isLoading = false;
                        if (response.body().getListLivStream().size() < 10) {
                            isLeftData = false;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LiveStreamResponse> call, @NonNull Throwable t) {

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void callApiListChannel() {
        if (channelListAdapter.getItemCount() != 0) {
            channelListAdapter.clearData();
        }
        HomeApi.getInstance().getListChannelLivestream(0, 20, new HttpCallBack() {
            @Override
            public void onSuccess(String data) throws Exception {
                try {
                    Gson gson = new Gson();
                    ChannelSearchResponse response = gson.fromJson(data, ChannelSearchResponse.class);
                    if (response != null) {
                        listChannel = (ArrayList<Channel>) response.getData();
                        channelListAdapter.setChannels(listChannel);
                    }
                    binding.rcvListLivestream.addOnScrollListener(new OnEndlessScrollListener(3) {
                        @Override
                        public void onLoadNextPage(View view) {
                            super.onLoadNextPage(view);
                            if (isLeftData && !isLoading && listChannel.size() >= 20) {
                                loadMoreChannel(listChannel.size() / 20);
                                isLoading = true;
                            }
                        }
                    });
                    binding.loadingViewExtra.showLoadedSuccess();
                    binding.swipeRefreshLayout.setRefreshing(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
                binding.loadingViewExtra.showLoadedError();
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadMoreChannel(int page) {
        HomeApi.getInstance().getListChannelLivestream(page, 20, new HttpCallBack() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(String data) throws Exception {
                try {
                    Gson gson = new Gson();
                    ChannelSearchResponse response = gson.fromJson(data, ChannelSearchResponse.class);
                    if (response != null) {
                        listChannel.addAll(response.getData());
                        channelListAdapter.notifyDataSetChanged();
                        isLoading = false;
                        if (response.getData().size() < 20) {
                            isLeftData = false;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onOpenApp(Channel channel, boolean isInstall) {
        Utilities.openApp(this, channel.getPackageAndroid());
    }

    @Override
    public void onSub(Channel channel) {
        if (channelApi == null) return;
        channelApi.callApiSubOrUnsubChannel(channel.getId(), !channel.isFollow());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (type.equals(TYPE_CHANNEL)) {
            callApiListChannel();
        }
    }
}
