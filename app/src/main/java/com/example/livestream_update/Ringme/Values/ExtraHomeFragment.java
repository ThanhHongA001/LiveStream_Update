package com.example.livestream_update.Ringme.Values;


import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewbinding.ViewBinding;

import com.google.gson.Gson;
import com.vtm.R;
import com.vtm.databinding.RmFragmentHomeExtraBinding;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.api.ApiCollections;
import com.vtm.ringme.api.ApiSummoner;
import com.vtm.ringme.api.HomeApi;
import com.vtm.ringme.api.VideoApi;
import com.vtm.ringme.api.response.LiveStreamResponse;
import com.vtm.ringme.base.BaseFragmentNew;
import com.vtm.ringme.common.api.http.HttpCallBack;
import com.vtm.ringme.home.ChannelSearchResponse;
import com.vtm.ringme.home.HomeExtraListBannerStreamAdapter;
import com.vtm.ringme.home.HomeExtraListChannelAdapter;
import com.vtm.ringme.home.HomeExtraStreamCategoryAdapter;
import com.vtm.ringme.home.IScrollListener;
import com.vtm.ringme.home.LiveStreamListMainHome;
import com.vtm.ringme.home.ReturnHomeTabEvent;
import com.vtm.ringme.home.VideoMainHome;
import com.vtm.ringme.home.VideoSearchModel;
import com.vtm.ringme.home.VideoSearchNewAdapter;
import com.vtm.ringme.model.livestream.LivestreamModel;
import com.vtm.ringme.utils.InputMethodUtils;
import com.vtm.ringme.values.Constants;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExtraHomeFragment extends BaseFragmentNew {
    private static ExtraHomeFragment mSelf;
    private final String TAG = "ExtraHomeFragment";
    private final Handler handlerSearch = new Handler();
    private ConcatAdapter concatAdapter;
    private String type;
    private RmFragmentHomeExtraBinding binding;
    private int numberCategory;
    private int page = 0;
    private HomeExtraStreamCategoryAdapter topLivestreamAdapter;
    private HomeExtraListChannelAdapter channelAdapter;
    private HomeExtraListBannerStreamAdapter bannerStreamAdapter;
    private Runnable runnableSearch;
    private String keySearch = "";
    private String oldKeySearch = "";
    private HomeApi mApi;
    private VideoSearchModel searchDataResult = new VideoSearchModel();
    private VideoSearchNewAdapter searchAdapterNew;
    private int searchData1;

    public ExtraHomeFragment(String type) {
        this.type = type;
    }

    //todo require blank constructor
    public ExtraHomeFragment() {

    }

    public static ExtraHomeFragment self() {
        return mSelf;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public ViewBinding setViewBinding() {
        binding = RmFragmentHomeExtraBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public int getResIdView() {
        return 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RmFragmentHomeExtraBinding.inflate(inflater, container, false);
        mApi = HomeApi.getInstance();
        binding.loadingViewExtra.showLoading();
        binding.searchLayout.setVisibility(View.GONE);
        topLivestreamAdapter = new HomeExtraStreamCategoryAdapter(mActivity);
        searchAdapterNew = new VideoSearchNewAdapter(searchDataResult, mActivity);
        channelAdapter = new HomeExtraListChannelAdapter(mActivity);
        setupToolbar();
        initRecyclerViewMainContainer();
        initListCategory();
        binding.srlSwipeRefreshDataHomeExtra.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.v5_main_color));
        binding.srlSwipeRefreshDataHomeExtra.setOnRefreshListener(() -> {
            topLivestreamAdapter = new HomeExtraStreamCategoryAdapter(mActivity);
            channelAdapter = new HomeExtraListChannelAdapter(mActivity);
            bannerStreamAdapter = new HomeExtraListBannerStreamAdapter(mActivity);
            initRecyclerViewMainContainer();
            initListCategory();
            binding.srlSwipeRefreshDataHomeExtra.setRefreshing(false);
        });
        binding.loadingViewExtra.setOnClickRetryListener(v -> {
            if (binding.searchLayout.getVisibility() == View.GONE) {
                binding.loadingViewExtra.showLoading();
                initRecyclerViewMainContainer();
                initListCategory();
            }
        });
        binding.btnBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        runnableSearch = this::searchData;

        return binding.getRoot();
    }

    private void setupToolbar() {
        binding.btnSearch.setOnClickListener(v -> showToolbarSearch());
        binding.btnBackSearch.setOnClickListener(v -> hideToolbarSearch());
        binding.btnClearText.setOnClickListener(v -> binding.toolbarSearchText.setText(""));
        binding.toolbarSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                keySearch = binding.toolbarSearchText.getText().toString();

                if (TextUtils.isEmpty(keySearch)) {
                    binding.btnClearText.setVisibility(View.GONE);
                    searchDataResult = new VideoSearchModel();
                    if (searchAdapterNew != null) {
                        searchAdapterNew = new VideoSearchNewAdapter(searchDataResult, mActivity);
                        binding.searchData.setAdapter(searchAdapterNew);
                    }
                    hideSearchLayout();
                    binding.loadingViewExtra.showLoadedSuccess();
                    oldKeySearch = "";
                } else if (!keySearch.equals(oldKeySearch)) {
                    binding.loadingViewExtra.setVisibility(View.VISIBLE);
                    binding.btnClearText.setVisibility(View.VISIBLE);
                    searchDataResult = new VideoSearchModel();
                    if (searchAdapterNew != null) {
                        searchAdapterNew = new VideoSearchNewAdapter(searchDataResult, mActivity);
                        binding.searchData.setLayoutManager(new LinearLayoutManager(mActivity));
                        binding.searchData.setAdapter(searchAdapterNew);
                    }
                    showSearchLayout();
                    binding.loadingViewExtra.showLoading();
                    handlerSearch.removeCallbacks(runnableSearch);
                    handlerSearch.postDelayed(runnableSearch, 800);
                }
                oldKeySearch = keySearch;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void showSearchLayout() {
        binding.searchLayout.setVisibility(View.VISIBLE);
        binding.srlSwipeRefreshDataHomeExtra.setVisibility(View.GONE);
    }

    private void hideSearchLayout() {
        binding.searchLayout.setVisibility(View.GONE);
        binding.srlSwipeRefreshDataHomeExtra.setVisibility(View.VISIBLE);
    }

    private void hideToolbarSearch() {
        this.page = 0;
        binding.toolbarSearch.setVisibility(View.GONE);
        binding.toolbarFunction.setVisibility(View.VISIBLE);
        binding.toolbarSearchText.setText("");
        InputMethodUtils.hideSoftKeyboard(binding.toolbarSearchText, requireContext());
    }

    private void showToolbarSearch() {
        binding.toolbarSearch.setVisibility(View.VISIBLE);
        binding.toolbarFunction.setVisibility(View.GONE);
        InputMethodUtils.showSoftKeyboard(requireContext(), binding.toolbarSearchText);
        InputMethodUtils.hideKeyboardWhenTouch(binding.searchData, mActivity);
    }

    private void initRecyclerViewMainContainer() {
        concatAdapter = new ConcatAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        binding.rcvHomeExtra.setLayoutManager(linearLayoutManager);
        binding.rcvHomeExtra.setAdapter(concatAdapter);
    }

    private void initListCategory() {

        if (!StringUtils.isEmpty(type)) {
            switch (type) {

                case Constants.Intent.stream:
                    binding.tvToolbarName.setText(R.string.live_stream_list_tab_main_home);
                    binding.layout.setBackgroundColor(getResources().getColor(R.color.color_bg_main_game));
                    callApiStream();
                    break;
            }
        } else onBackPressed();
    }



    private void callApiStream() {
        HomeApi.getInstance().getListChannelLivestream(0, 5, new HttpCallBack() {
            @Override
            public void onSuccess(String data) throws Exception {
                try {
                    Gson gson = new Gson();
                    ChannelSearchResponse response = gson.fromJson(data, ChannelSearchResponse.class);
                    if (response != null) {
                        channelAdapter.setData(response.getData());
                    }
                    channelAdapter.setIScrollListener(new IScrollListener() {
                        @Override
                        public void onScrollListener(int page, int size) {
                            // cancel load more
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        bannerStreamAdapter = new HomeExtraListBannerStreamAdapter(mActivity);
        int page = 0;
        int size = 10;
        String userId = ApplicationController.self().getJidNumber();
        ApiCollections apiCollections = ApiSummoner.liveStreamApi();
        apiCollections.getListLiveStream(1, page, size, userId).enqueue(new Callback<LiveStreamResponse>() {
            @Override
            public void onResponse(@NonNull Call<LiveStreamResponse> call, @NonNull Response<LiveStreamResponse> response) {
                try {
                    if (response.body() != null && response.body().getListLivStream().size() != 0) {
                        LiveStreamListMainHome topLivestream = new LiveStreamListMainHome(
                                mActivity.getResources().getString(R.string.top_live), response.body().getListLivStream());
                        topLivestreamAdapter.setLiveStreamsData(topLivestream);
                        topLivestreamAdapter.setIScrollListener(new IScrollListener() {
                            @Override
                            public void onScrollListener(int page1, int size1) {
//                                livestreamLoadMore(1, page1, topLivestreamAdapter); // cancel load more
                            }
                        });
                        binding.loadingViewExtra.showLoadedSuccess();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LiveStreamResponse> call, @NonNull Throwable t) {

            }
        });

        apiCollections.getListLiveStream(4, page, 5, userId).enqueue(new Callback<LiveStreamResponse>() {
            @Override
            public void onResponse(@NonNull Call<LiveStreamResponse> call, @NonNull Response<LiveStreamResponse> response) {
                try {
                    if (response.body() != null && response.body().getListLivStream().size() != 0) {
                        bannerStreamAdapter.addData(response.body().getListLivStream());
                        binding.loadingViewExtra.showLoadedSuccess();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LiveStreamResponse> call, @NonNull Throwable t) {

            }
        });


        concatAdapter.addAdapter(bannerStreamAdapter);
        concatAdapter.addAdapter(channelAdapter);
        concatAdapter.addAdapter(topLivestreamAdapter);
        if (topLivestreamAdapter.getItemCount() == 0 && bannerStreamAdapter.getItemCount() == 0) {
            binding.loadingViewExtra.showLoadedError();
        }
    }


    private void searchVideo(String keySearch) {
        VideoApi.getInstance().searchVideo(0, 20, keySearch, new HttpCallBack() {
            @Override
            public void onSuccess(String data) throws Exception {
                try {
                    searchData1--;
                    Gson gson = new Gson();
                    LiveStreamResponse response = gson.fromJson(data, LiveStreamResponse.class);
                    if (response != null) {
                        ArrayList<LivestreamModel> listLiveSearch = response.getListLivStream();
                        ArrayList<VideoMainHome> listLiveConvert = new ArrayList<>();
                        for(LivestreamModel item : listLiveSearch) {
                            listLiveConvert.add(VideoMainHome.convertLivestreamToVideo(item));
                        }
                        searchDataResult.setVideos(listLiveConvert);
                        searchDataResult.setDataNum(searchDataResult.getDataNum() + 1);
                        binding.loadingViewExtra.showLoadedSuccess();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
                searchData1--;
                binding.loadingViewExtra.showLoadedError();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                if (searchData1 == 0) {
                    searchAdapterNew.setModels(searchDataResult);
                    Log.d(TAG, "onCompleted: " + searchAdapterNew.getItemCount());
                }
            }
        });
    }

    private void searchChannel(String keySearch) {
        VideoApi.getInstance().searchChannel(0, 20, keySearch, new HttpCallBack() {
            @Override
            public void onSuccess(String data) throws Exception {
                try {
                    searchData1--;
                    Gson gson = new Gson();
                    ChannelSearchResponse response = gson.fromJson(data, ChannelSearchResponse.class);
                    if (response != null) {
                        searchDataResult.setChannels(response.getData());
                        searchDataResult.setDataNum(searchDataResult.getDataNum() + 1);
                        binding.loadingViewExtra.showLoadedSuccess();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
                searchData1--;
                binding.loadingViewExtra.showLoadedError();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                if (searchData1 == 0) {
                    searchAdapterNew.setModels(searchDataResult);
                    Log.d(TAG, "onCompleted: " + searchAdapterNew.getItemCount());
                }
            }
        });
    }

    private void searchData() {
        page = 0;
        binding.btnClearText.setVisibility(View.VISIBLE);
        showSearchLayout();
        if (!StringUtils.isEmpty(type)) {
            switch (type) {
                case Constants.Intent.stream:
                    searchDataVideo(keySearch);
                    binding.loadingViewExtra.setOnClickRetryListener(v -> {
                        page = 0;
                        if (binding.searchLayout.getVisibility() == View.VISIBLE) {
                            searchDataVideo(keySearch);
                        }
                    });
                    break;
            }
        }
    }

    private void searchDataVideo(String keySearch) {
        searchDataResult = new VideoSearchModel();
        searchData1 = 2;
        searchChannel(keySearch);
        searchVideo(keySearch);

    }

    public void onBackPressed() {
        EventBus.getDefault().postSticky(new ReturnHomeTabEvent());
    }

    @Override
    public void onResume() {
        super.onResume();
        mSelf = this;
    }

    @Override
    public void onPause() {
        super.onPause();
        mSelf = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSelf = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mSelf = isVisibleToUser ? this : null;
    }
}