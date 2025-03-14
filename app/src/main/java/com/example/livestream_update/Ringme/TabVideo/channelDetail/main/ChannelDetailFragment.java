package com.example.livestream_update.Ringme.TabVideo.channelDetail.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.vtm.R;
import com.vtm.databinding.RmFragmentChannelDetailBinding;
import com.vtm.ringme.api.HomeApi;
import com.vtm.ringme.api.RRestChannelInfoModel;
import com.vtm.ringme.base.utils.ImageBusiness;
import com.vtm.ringme.common.api.http.HttpCallBack;
import com.vtm.ringme.common.utils.image.ImageManager;
import com.vtm.ringme.customview.SubscribeChannelLayout;
import com.vtm.ringme.event.BacktoChannelEvent;
import com.vtm.ringme.event.SubChannelEvent;
import com.vtm.ringme.helper.NetworkHelper;
import com.vtm.ringme.livestream.apis.response.LiveStreamFollowResponse;
import com.vtm.ringme.livestream.listener.OnSingleClickListener;
import com.vtm.ringme.livestream.network.APICallBack;
import com.vtm.ringme.model.tab_video.Channel;
import com.vtm.ringme.tabvideo.BaseFragment;
import com.vtm.ringme.tabvideo.channelDetail.ChannelPagerAdapter;
import com.vtm.ringme.tabvideo.channelDetail.info.InfoChannelFragment;
import com.vtm.ringme.tabvideo.channelDetail.video.VideoChannelFragment;
import com.vtm.ringme.tabvideo.listener.OnChannelChangedDataListener;
import com.vtm.ringme.utils.RRetrofitClientInstance;
import com.vtm.ringme.utils.Utilities;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import retrofit2.Response;

public class ChannelDetailFragment extends BaseFragment implements
        SubscribeChannelLayout.SubscribeChannelListener,
        OnChannelChangedDataListener {

    private static final String CHANNEL = "channel";

    private Channel mChannel;
    private ArrayList<Fragment> fragments;
    private ArrayList<String> chars;
    private boolean isGetInfoSuccess = true;
    private boolean isShowUpload = false;
    private int checkFragment;
    private RmFragmentChannelDetailBinding binding;
    private HomeApi mApi;

    public static ChannelDetailFragment newInstance(Channel channel) {
        Bundle args = new Bundle();
        args.putSerializable(CHANNEL, channel);
        ChannelDetailFragment fragment = new ChannelDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mChannel = (Channel) (bundle != null ? bundle.getSerializable(CHANNEL) : null);

        mApi = HomeApi.getInstance();
        fragments = new ArrayList<>();
        chars = new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RmFragmentChannelDetailBinding.inflate(getLayoutInflater());
        initView();
        initAction();
        return  binding.getRoot();
    }

    private void initAction() {
        binding.ivBack.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                EventBus.getDefault().postSticky(new BacktoChannelEvent(mChannel.isFollow()));
                activity.onBackPressed();
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initView() {
        hideError();
        showLoading();
        mApi.getChannelInfo(mChannel.getId(), new HttpCallBack() {
            @Override
            public void onSuccess(String data) throws Exception {
                Gson gson = new Gson();
                RRestChannelInfoModel response = gson.fromJson(data, RRestChannelInfoModel.class);
                if (response != null && response.getData() != null) {
                    if (binding.viewPager == null) return;
                    isGetInfoSuccess = true;
                    Channel channel=response.getData();
                    if (channel == null || Utilities.isEmpty(channel.getId())) {
                        onGetChannelInfoError("");
                        return;
                    }
                    mChannel = channel;
                    hideError();
                    bindTab(channel);
                    bindChannelInfo(channel);
                    binding.content.setVisibility(View.VISIBLE);
                    showBtnUpload();
                    hideLoading();
                }
            }

            @Override
            public void onFailure(String message) {
                isGetInfoSuccess = false;
                showError();
                if (!NetworkHelper.isConnectInternet(activity)) {
                    showErrorNetwork();
                    hideErrorDataEmpty();
                } else {
                    showErrorDataEmpty();
                    hideErrorNetwork();
                }
                super.onFailure(message);
            }
        });


    }

    @Override
    public void onChannelSubscribeChanged(Channel channel) {
        if (channel == null || !channel.equals(mChannel)) return;
        mChannel.setNumFollow(channel.getNumfollow());
        mChannel.setFollow(channel.isFollow());
        bindSub(mChannel);
    }


    @Override
    public void onOpenApp(Channel channel, boolean isInstall) {
        Utilities.openApp(activity, channel.getPackageAndroid());
    }

    @Override
    public void onSub(Channel channel) {

//            channelApi.callApiSubOrUnsubChannel(channel.getId(), channel.isFollow());
        subLivestreamChannel(channel);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSubChannel(SubChannelEvent event) {

        if (mChannel != null) {
                RRetrofitClientInstance retrofitClientInstance = new RRetrofitClientInstance();
                retrofitClientInstance.getChannelInfo(mChannel.getId(), new APICallBack<RRestChannelInfoModel>() {
                    @Override
                    public void onResponse(Response<RRestChannelInfoModel> response) {
                        if (response != null && response.body() != null && response.body().getData() != null) {
                            mChannel = response.body().getData();
                            bindChannelInfo(mChannel);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                    }
                });
        }

    }

    public void onGetChannelInfoError(String s) {
        isGetInfoSuccess = false;
        showError();
        if (!NetworkHelper.isConnectInternet(activity)) {
            showErrorNetwork();
            hideErrorDataEmpty();
        } else {
            showErrorDataEmpty();
            hideErrorNetwork();
        }
    }


    @SuppressWarnings("deprecation")
    private void bindChannelInfo(Channel channel) {
        if (channel == null)
            return;
        bindSub(channel);
        if (channel.getIsOfficial() == 1) {
         binding.itemTabVideoHeaderChannel.ivChannelName.setVisibility(View.VISIBLE);
        } else {
            binding.itemTabVideoHeaderChannel.ivChannelName.setVisibility(View.GONE);
        }

        binding.tvTitle.setText(channel.getName());
        binding.itemTabVideoHeaderChannel.tvChannelName.setText(channel.getName().trim());
        ImageManager.showImage(channel.getUrlImageCover(), binding.itemTabVideoHeaderChannel.ivChannelCover);
        ImageBusiness.setAvatarChannel(binding.itemTabVideoHeaderChannel.ivChannelAvatar, channel.getUrlImage());
    }

    private void bindSub(Channel channel) {
        if (channel == null)
            return;
        if (channel.isMyChannel()) {
            binding.itemTabVideoHeaderChannel.ivChannelEditor.setVisibility(View.VISIBLE);
        } else {
            binding.itemTabVideoHeaderChannel.buttonChannelSubscription.setSubscribeChannelListener(this);
            binding.itemTabVideoHeaderChannel.buttonChannelSubscription.setChannel(channel);
            binding.itemTabVideoHeaderChannel.ivChannelEditor.setVisibility(View.GONE);
        }
        binding.itemTabVideoHeaderChannel.tvNumberSubscriptions.setText(String.format(getString(R.string.people_subscription), channel.getNumFollow() + ""));
        binding.itemTabVideoHeaderChannel.tvNumberVideo.setVisibility(View.VISIBLE);
        if (channel.getNumVideo() <= 1)
            binding.itemTabVideoHeaderChannel.tvNumberVideo.setText(getString(R.string.music_total_video, channel.getNumVideo()));
        else
            binding.itemTabVideoHeaderChannel.tvNumberVideo.setText(getString(R.string.music_total_videos, channel.getNumVideo()));
    }

    private void bindTab(Channel channel) {
        PagerAdapter channelPagerAdapter = providePagerAdapter(channel);
        binding.viewPager.setAdapter(channelPagerAdapter);
        binding.viewPager.setOffscreenPageLimit(3);
        if (channelPagerAdapter.getCount() > 3) {
            binding.tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
            binding.tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            binding.tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    private PagerAdapter providePagerAdapter(@Nullable Channel channel) {
        if (channel != null) {
            addFragment(getString(R.string.channel_detail_tab_video), VideoChannelFragment.newInstance(channel, true));
            addFragment(getString(R.string.channel_detail_tab_info), InfoChannelFragment.newInstance(channel));
        }
        ChannelPagerAdapter channelPagerAdapter = new ChannelPagerAdapter(getChildFragmentManager());
        channelPagerAdapter.bindData(fragments, chars);
        return channelPagerAdapter;
    }


    private void addFragment(String name, Fragment fragment) {
        fragments.add(fragment);
        chars.add(name);
    }

    private void showBtnUpload() {
        if (mChannel != null && mChannel.isMyChannel())
            binding.ivUploadVideo.setVisibility(View.VISIBLE);
    }

    private void hideBtnUpload() {
        binding.ivUploadVideo.setVisibility(View.GONE);
    }

    private void showLoading() {
        binding.frameLoading.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        binding.frameLoading.setVisibility(View.GONE);
    }

    private void showErrorDataEmpty() {
        binding.viewEmpty.emptyText.setVisibility(View.VISIBLE);
    }

    private void hideErrorDataEmpty() {
        binding.viewEmpty.emptyText.setVisibility(View.GONE);
    }

    private void showErrorNetwork() {
        binding.viewEmpty.emptyRetryButton.setVisibility(View.VISIBLE);
        binding.viewEmpty.emptyRetryText2.setVisibility(View.VISIBLE);
    }

    private void hideErrorNetwork() {
        binding.viewEmpty.emptyRetryButton.setVisibility(View.GONE);
        binding.viewEmpty.emptyRetryText2.setVisibility(View.GONE);
    }

    private void showError() {
        binding.frameEmpty.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        binding.frameEmpty.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if ((position == 0 || position == 1) && mChannel != null && mChannel.isMyChannel() && !isShowUpload) {
                    showBtnUpload();
                } else {
                    hideBtnUpload();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }


    private void subLivestreamChannel(Channel channel) {

        String streamerId = channel.getId();

        HomeApi.getInstance().followLivestreamChannel("0", streamerId, new HttpCallBack() {
            @Override
            public void onSuccess(String data) throws Exception {
                try {
                    Gson gson = new Gson();
                    LiveStreamFollowResponse response = gson.fromJson(data, LiveStreamFollowResponse.class);
                    if (response != null) {
                        Log.d("subLivestreamChannel", "onSuccess: sub live success");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
