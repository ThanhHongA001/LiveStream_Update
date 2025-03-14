package com.example.livestream_update.Ringme.LiveStream.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vtm.R;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.api.HomeApi;
import com.vtm.ringme.api.response.TopDonateResponse;
import com.vtm.ringme.common.api.http.HttpCallBack;
import com.vtm.ringme.dialog.TopDonateDialog;
import com.vtm.ringme.livestream.activity.LivestreamDetailActivity;
import com.vtm.ringme.livestream.adapter.TopWeeklyDonateAdapter;
import com.vtm.ringme.livestream.apis.LivestreamApiInstance;
import com.vtm.ringme.livestream.apis.LivestreamServices;
import com.vtm.ringme.livestream.apis.response.LiveStreamDetailResponse;
import com.vtm.ringme.livestream.eventbus.EventBusEvents;
import com.vtm.ringme.livestream.eventbus.SubOrUnSubEvent;
import com.vtm.ringme.livestream.model.TopDonate;
import com.vtm.ringme.model.livestream.LivestreamModel;
import com.vtm.ringme.tabvideo.LiveStreamIsFollowEvent;
import com.vtm.ringme.utils.Utilities;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import io.reactivex.annotations.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressLint("NonConstantResourceId")
public class LivestreamInfoFragment extends Fragment implements View.OnClickListener {
    //view
    private LivestreamModel video;
    private ImageView imgAvatar;
    private TextView btnFollow;
    private TextView tvDescription, tvName, totalFollow, tvSeeMore, tvTitle;
    private RecyclerView rcvListSenders;
    private RelativeLayout layoutTopDonate, root;
    private AppCompatButton btnSendStars, btnSeeMore;
    private TopWeeklyDonateAdapter topWeeklyDonateAdapter;
    private ArrayList<TopDonate> listTopDonate;
    private LivestreamDetailActivity activity;
    private TopDonateDialog topDonateDialog;
    private boolean isViewAllDes = false;
    int numFollow;
    private ScrollView scrollView;

    public LivestreamInfoFragment(LivestreamModel video) {
        this.video = video;
    }

    public LivestreamInfoFragment() {
    }

    @Nullable
    @androidx.annotation.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable @androidx.annotation.Nullable ViewGroup container, @Nullable @androidx.annotation.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rm_fragment_streamer_info, container, false);
        try {
            activity = (LivestreamDetailActivity) requireActivity();
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
            initView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initView(View view) {
        imgAvatar = view.findViewById(R.id.streamer_avatar);
        btnFollow = view.findViewById(R.id.tv_subscriptions_channel);
        tvDescription = view.findViewById(R.id.livestream_description);
        tvName = view.findViewById(R.id.streamer_name);
        totalFollow = view.findViewById(R.id.follow_number);
        tvSeeMore = view.findViewById(R.id.tv_see_more);
        rcvListSenders = view.findViewById(R.id.rcv_list_senders);
        layoutTopDonate = view.findViewById(R.id.layout_top_donate);
        btnSendStars = view.findViewById(R.id.btn_send_stars);
        btnSeeMore = view.findViewById(R.id.btn_see_more);
        tvTitle = view.findViewById(R.id.tv_title);
        scrollView = view.findViewById(R.id.scroll_view);
        root = view.findViewById(R.id.root);
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            video = (LivestreamModel) bundle.getSerializable(Constants.KeyData.video);
//        }
        btnSendStars.setOnClickListener(view1 -> activity.openDialogSendStars());
        drawDetail();
        initData();
        initListener();
        seeMoreDes();
        getTopDonate();
    }

    private void initListener() {
        btnFollow.setOnClickListener(this);
    }

    private void initData() {
        //data
        reloadVideoData();
        numFollow = (int) video.getChannel().getNumFollow();
    }

    private void reloadVideoData() {
        LivestreamServices livestreamServices = LivestreamApiInstance.getLiveStreamInstance();
        livestreamServices.getLivestreamDetail(String.valueOf(video.getId()), ApplicationController.self().getJidNumber())
                .enqueue(new Callback<LiveStreamDetailResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LiveStreamDetailResponse> call, @NonNull Response<LiveStreamDetailResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getVideo() != null) {
                                    video.getChannel().setFollow(response.body().getVideo().getChannel().isFollow());
                                    updateFollowUi();
                                    updateBlockUi(response.body().getVideo().isBlockComment());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LiveStreamDetailResponse> call, @NonNull Throwable throwable) {
                        Log.d("reloadChannel", "onFailure: ");
                    }
                });
    }

    private void updateBlockUi(boolean blockComment) {
        EventBus.getDefault().postSticky(new EventBusEvents.UpdateBlockEvent(blockComment));
    }

    @SuppressLint("SetTextI18n")
    private void drawDetail() {
        try {
            setUpScreenType();
            Glide.with(this).load(video.getChannel().getUrlImage()).placeholder(R.drawable.rm_ic_avatar_default).centerCrop().into(imgAvatar);
            imgAvatar.setOnClickListener(view -> ApplicationController.self().openChannelInfo(activity, video.getChannel()));
            tvName.setText(video.getChannel().getName());
            totalFollow.setText(Utilities.shortenLongNumber(video.getChannel().getNumFollow()) + " Followers");
//        tvTimeStart.setText(DateTimeUtils.calculateTime(getResources(), video.getTimeStart()));
            tvDescription.setText(video.getDescription());
            tvTitle.setText(video.getTitle());

            updateFollowUi();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LivestreamInfoFragment newInstance(Bundle args) {
        LivestreamInfoFragment fragment = new LivestreamInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_subscriptions_channel) {

//            ApplicationController.self().getApplicationComponent().providerChannelApi()
//                    .callApiSubOrUnsubChannel(video.getChannel().getId(), !video.getChannel().isFollow());
            FollowAndUnFollow();
            video.getChannel().setFollow(!video.getChannel().isFollow());
            updateFollowUi();
            updateNumberFollow();
        }
    }

    private void FollowAndUnFollow(){
        if(!video.getChannel().isFollow()){
            HomeApi.getInstance().liveStreamFollow(video.getChannel().getId(), new HttpCallBack() {
                @Override
                public void onSuccess(String data) throws Exception {

                }

                @Override
                public void onFailure(String message) {
                    super.onFailure(message);
                }
            });
        } else {
            HomeApi.getInstance().liveStreamUnFollow(video.getChannel().getId(), new HttpCallBack() {
                @Override
                public void onSuccess(String data) throws Exception {

                }

                @Override
                public void onFailure(String message) {
                    super.onFailure(message);
                }
            });
        }
    }



    @SuppressLint("SetTextI18n")
    private void updateFollowUi() {
        if (video.getChannel().isFollow()) {
            btnFollow.setText(R.string.unsubscribeChannel_v2);
            btnFollow.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.rm_ic_notification_2, 0);
            btnFollow.setCompoundDrawablePadding(16);
            EventBus.getDefault().postSticky(new SubOrUnSubEvent(true));
        } else {
            btnFollow.setText(R.string.subscribeChannel_v2);
            btnFollow.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            btnFollow.setCompoundDrawablePadding(16);
            EventBus.getDefault().postSticky(new SubOrUnSubEvent(false));
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateNumberFollow() {
        if (video.getChannel().isFollow()) {
            video.getChannel().setNumFollow(video.getChannel().getNumFollow() + 1);
        } else {
            video.getChannel().setNumFollow(video.getChannel().getNumFollow() - 1);
        }
        totalFollow.setText(Utilities.shortenLongNumber(video.getChannel().getNumFollow()) + " Followers");
    }

    private void seeMoreDes() {
        tvDescription.postDelayed(() -> {
            Layout l = tvDescription.getLayout();
            if (l != null) {
                int lines = l.getLineCount();
                if (lines > 0)
                    if (l.getEllipsisCount(lines - 1) > 0)
                        tvSeeMore.setVisibility(View.VISIBLE);
            }
        }, 250);

        tvSeeMore.setOnClickListener(view -> {
            if (!isViewAllDes) {
                tvDescription.setMaxLines(Integer.MAX_VALUE);
                tvSeeMore.setText(R.string.see_less);
            } else {
                tvDescription.setMaxLines(3);
                tvSeeMore.setText(R.string.see_more);
            }
            isViewAllDes = !isViewAllDes;
        });
    }

    public void getTopDonate() {
        listTopDonate = new ArrayList<>();
        HomeApi.getInstance().getTopDonateLivestream(video.getChannel().getId(), video.getId(),new HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                try {
                    Gson gson = new Gson();
                    TopDonateResponse response = gson.fromJson(data, TopDonateResponse.class);
                    if (response != null && response.getData() != null) {
                        if (response.getData().size() > 0) {
                            listTopDonate = response.getData();
                            ArrayList<TopDonate> listWeekly = new ArrayList<>();
                            listWeekly.add(listTopDonate.get(0));
                            if (listTopDonate.size() >= 2 && listTopDonate.get(1) != null)
                                listWeekly.add(listTopDonate.get(1));
                            if (listTopDonate.size() >= 3 && listTopDonate.get(2) != null)
                                listWeekly.add(listTopDonate.get(2));
                            topWeeklyDonateAdapter = new TopWeeklyDonateAdapter(activity);
                            topWeeklyDonateAdapter.setList(listWeekly);

                            rcvListSenders.setAdapter(topWeeklyDonateAdapter);
                            rcvListSenders.setLayoutManager(new LinearLayoutManager(activity));

                            if (listTopDonate.size() == 0) {
                                layoutTopDonate.setVisibility(View.GONE);
                            } else {
                                if (video.getScreenType() == 0) {
                                    layoutTopDonate.setVisibility(View.VISIBLE);
                                    seeAllTopDonate();
                                }
                                else layoutTopDonate.setVisibility(View.GONE);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void seeAllTopDonate() {
        topDonateDialog = new TopDonateDialog(activity, listTopDonate);
        btnSeeMore.setOnClickListener(view -> topDonateDialog.show());
    }

    private void setUpScreenType() {
        if (video.getScreenType() == 0) {
            root.setGravity(Gravity.TOP);
        } else {
            root.setGravity(Gravity.BOTTOM);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void LiveStreamIsFollowEvent(LiveStreamIsFollowEvent event) {
        Log.e("TAG", "LiveStreamIsFollowEvent: "+event.isFollow());
        video.getChannel().setFollow(event.isFollow());
        updateFollowUi();
        EventBus.getDefault().removeStickyEvent(event);
    }
}