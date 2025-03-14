package com.example.livestream_update.Ringme.TabVideo.channelDetail.video;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.vtm.R;
import com.vtm.databinding.RmFragmentChannelVideoBinding;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.api.HomeApi;
import com.vtm.ringme.api.VideoApi;
import com.vtm.ringme.common.api.ApiCallbackV2;
import com.vtm.ringme.common.utils.ShareUtils;
import com.vtm.ringme.helper.NetworkHelper;
import com.vtm.ringme.listener.OnClickMoreItemListener;
import com.vtm.ringme.livestream.activity.LivestreamDetailActivity;
import com.vtm.ringme.model.tab_video.Channel;
import com.vtm.ringme.model.tab_video.Video;
import com.vtm.ringme.tabvideo.BaseAdapterV2;
import com.vtm.ringme.tabvideo.fragment.BaseViewStubFragment;
import com.vtm.ringme.tabvideo.listener.OnVideoChannelListener;
import com.vtm.ringme.utils.DialogUtils;
import com.vtm.ringme.utils.ToastUtils;
import com.vtm.ringme.utils.Utilities;
import com.vtm.ringme.values.Constants;

import org.json.JSONException;

import java.util.ArrayList;


public class VideoChannelFragment extends BaseViewStubFragment implements SwipeRefreshLayout.OnRefreshListener
        , OnVideoChannelListener, OnClickMoreItemListener {

    private static final String CHANNEL = "channel";
    public static final int LIMIT = 20;

    private boolean hasHeaderLine = false;

    public static VideoChannelFragment newInstance(Channel channel, boolean hasHeaderLine) {
        Bundle args = new Bundle();
        args.putSerializable(CHANNEL, channel);
        args.putBoolean("HAS_HEADER_LINE", hasHeaderLine);
        VideoChannelFragment fragment = new VideoChannelFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private @Nullable
    Channel mChannel;
    private Object loadMore;

    private VideoApi mVideoApi;

    private VideoChannelAdapter adapter;

    private int offset = 0;
    private String lastId = "";
    private ArrayList<Object> datas;
    private boolean loading = false;
    private boolean isLoadMore = false;

    private boolean isGetInfoSuccess = true;
    private RmFragmentChannelVideoBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RmFragmentChannelVideoBinding.inflate(getLayoutInflater());
        loadMore = new Object();
        Bundle bundle = getArguments();
        if (bundle != null) {
            mChannel = (Channel) bundle.getSerializable(CHANNEL);
            hasHeaderLine = bundle.getBoolean("HAS_HEADER_LINE");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView();
        return binding.getRoot();
    }

    @Override
    protected void onCreateViewAfterViewStubInflated(View inflatedView, Bundle savedInstanceState) {
    }

    @Override
    protected int getViewStubLayoutResource() {
        return R.layout.rm_fragment_channel_video;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }


    @Override
    public void onRefresh() {
        refreshData();
    }

    private boolean isEmptyData() {
        if (datas == null || datas.isEmpty()) return true;
        return (datas.size() == 2 && datas.contains(VideoChannelAdapter.TYPE_SPACE_HEADER) &&
                datas.contains(VideoChannelAdapter.TYPE_SPACE_BOTTOM));
    }

    private void initView() {
        hideError();
        adapter = new VideoChannelAdapter(activity);
        adapter.setOnLoadMoreListener(onLoadMoreListener);
        adapter.setOnVideoChannelListener(this);
        adapter.setChannel(mChannel);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerView.setHasFixedSize(true);

        binding.refreshLayout.removeCallbacks(refreshRunnable);
        binding.refreshLayout.setOnRefreshListener(this);
        binding.refreshLayout.post(refreshRunnable);

        callApiGetVideoByChannel();
    }


    public void callApiGetVideoByChannel() {
        HomeApi.getInstance().getVideosByChannelId(mChannel.getId(), offset, LIMIT, lastId, new ApiCallbackV2<ArrayList<Video>>() {
            @Override
            public void onSuccess(String msg, ArrayList<Video> result) throws JSONException {
                if (adapter == null) return;
                isGetInfoSuccess = true;
                lastId = msg;
                ArrayList<Video> videos = provideVideo(result);
                if (datas == null) datas = new ArrayList<>();
                if (offset == 0) datas.clear();
                if (Utilities.isEmpty(datas) && hasHeaderLine) {
                    datas.add(VideoChannelAdapter.TYPE_SPACE_HEADER);
                }
                isLoadMore = result.size() >= LIMIT;
                datas.remove(loadMore);

                if (Utilities.notEmpty(videos)) {
                    for (Video video : videos) {
                        if (datas.contains(video)) continue;
                        datas.add(video);
                    }
                }
                if (isLoadMore) {
                    datas.add(loadMore);
                } else {
                    datas.add(VideoChannelAdapter.TYPE_SPACE_BOTTOM);
                }
                binding.recyclerView.stopScroll();
                adapter.bindData(datas);

                if (!isEmptyData())
                    hideError();
                else
                    onError("");
            }

            @Override
            public void onError(String s) {
                isGetInfoSuccess = false;
                if (!isEmptyData()) return;
                showError();
                if (!NetworkHelper.isConnectInternet(activity)) {
                    showErrorNetwork();
                    hideErrorDataEmpty();
                } else {
                    showErrorDataEmpty();
                    hideErrorNetwork();
                }
            }

            @Override
            public void onComplete() {
                loading = false;
                binding.refreshLayout.setRefreshing(false);
            }
        });
    }

    private final Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            if (binding.refreshLayout == null) return;
            binding.refreshLayout.setRefreshing(true);
            loadData();
        }
    };

    private final BaseAdapterV2.OnLoadMoreListener onLoadMoreListener = () -> {
        if (loading) return;
        if (!isLoadMore) return;
        loadMoreData();
    };

    private ArrayList<Video> provideVideo(ArrayList<Video> results) {
        ArrayList<Video> videos = new ArrayList<>();
        if (mChannel == null) return videos;

        if (mChannel.isMyChannel()) {
            videos.addAll(results);
        } else {
            for (Video result : results) {
                if (result != null /*&& result.getItemStatus() == Video.Status.APPROVED.VALUE*/) {
                    videos.add(result);
                }
            }
        }
        return videos;
    }

    private void loadData() {
        if (loading) return;
        if (mChannel == null) return;
        loading = true;
    }

    private void refreshData() {
        offset = 0;
        lastId = "";
        loadData();
    }

    private void loadMoreData() {
        offset = offset + LIMIT;
        loadData();
    }

    private void showErrorDataEmpty() {
        binding.viewEmpty.tvEmptyTitle.setVisibility(View.VISIBLE);
        binding.viewEmpty.icEmpty.setVisibility(View.VISIBLE);
        if (mChannel != null && mChannel.isMyChannel()) {
            binding.viewEmpty.tvEmptyTitle.setText(getString(R.string.no_channel_video));
            binding.viewEmpty.tvEmptyDes.setVisibility(View.VISIBLE);
            binding.viewEmpty.tvEmptyDes.setText(getString(R.string.no_channel_video_des));
            binding.viewEmpty.btnUpload.setVisibility(View.VISIBLE);
        } else {
            binding.viewEmpty.tvEmptyTitle.setText(getString(R.string.no_channel_contain_video));
            binding.viewEmpty.tvEmptyDes.setVisibility(View.GONE);
        }
    }

    private void hideErrorDataEmpty() {
        binding.viewEmpty.tvEmptyTitle.setVisibility(View.GONE);
        binding.viewEmpty.tvEmptyDes.setVisibility(View.GONE);
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
    public void onClickPlaylistVideo(View view, int position) {
        if (mChannel == null || activity == null || activity.isFinishing() || adapter == null)
            return;
        if (!isEmptyData() && datas.size() > position && position >= 0) {
            if (mChannel.isHasFilmGroup()) {
                Object item = adapter.getItem(position);
//                if (item instanceof Video)
//                    VideoPlayerActivity.start(application, (Video) item, "", true);
            } else {
                ArrayList<Video> list = new ArrayList<>();
                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i) instanceof Video) {
                        list.add((Video) datas.get(i));
                    }
                }
                if (Utilities.notEmpty(list)) {
                    Object item = adapter.getItem(position);
                    if (list.size() < 5) {
                        if (item instanceof Video) {
                            if (((Video) item).isLive()) {
                                Intent intent = new Intent(activity, LivestreamDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(Constants.KeyData.video, ((Video) item).getLivestream());
                                intent.putExtra(Constants.KeyData.data, bundle);
                                startActivity(intent);
                            } else {

                            }
                        }
                    } else {
                        if (item instanceof Video) {
                            Video video = (Video) item;
                            if (video.isLive()) {
                                Intent intent = new Intent(activity, LivestreamDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(Constants.KeyData.video, video.getLivestream());
                                intent.putExtra(Constants.KeyData.data, bundle);
                                startActivity(intent);
                                return;
                            }
//                            list.remove(item);
//                            list.add(0, (Video) item);
                            video.setPlayMyChannel(true);
                        }
//                        String channelId = mChannel.getId();
//                        VideoPlayerActivity.start(activity, (Video) item, "", true);
                    }
                }
            }
        }
    }

    @Override
    public void onClickMorePlaylistVideo(View view, int position) {
        if (mChannel == null || activity == null || activity.isFinishing() || adapter == null)
            return;
        if (!isEmptyData() && datas.size() > position && position >= 0) {
            Object item = adapter.getItem(position);
            if (item instanceof Video) {
                DialogUtils.showOptionVideoItemMyChannel(activity, (Video) item, mChannel.isMyChannel(), this);
            }
        }
    }

    @Override
    public void onClickMoreItem(Object object, int menuId) {
        if (activity != null && !activity.isFinishing() && object != null) {
            switch (menuId) {
                case Constants.MENU.MENU_SHARE_LINK:
                    ShareUtils.openShareMenu(activity, object);
                    break;
                case Constants.MENU.MENU_SAVE_VIDEO:
                    if (object instanceof Video) {
                        ToastUtils.showToast(ApplicationController.self(), activity.getString(R.string.videoSavedToLibrary));
                    }
                    break;
                case Constants.MENU.MENU_ADD_LATER:
                    if (object instanceof Video) {
                        ToastUtils.showToast(ApplicationController.self(), activity.getString(R.string.add_later_success));
                    }
                    break;
                case Constants.MENU.MENU_ADD_FAVORITE:
                    break;
            }
        }
    }


}
