package com.example.livestream_update.Ringme.TabVideo.channelDetail.info;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vtm.R;
import com.vtm.databinding.RmFragmentChannelInfoBinding;
import com.vtm.ringme.model.tab_video.Channel;
import com.vtm.ringme.tabvideo.fragment.BaseViewStubFragment;
import com.vtm.ringme.tabvideo.listener.OnChannelChangedDataListener;
import com.vtm.ringme.utils.DateTimeUtils;
import com.vtm.ringme.utils.Utilities;

public class InfoChannelFragment extends BaseViewStubFragment implements OnChannelChangedDataListener {

    private static final String CHANNEL = "channel";


    public static InfoChannelFragment newInstance(Channel channel) {
        Bundle args = new Bundle();
        args.putSerializable(CHANNEL, channel);
        InfoChannelFragment fragment = new InfoChannelFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private @Nullable
    Channel mChannel;

    private RmFragmentChannelInfoBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mChannel = (Channel) (bundle != null ? bundle.getSerializable(CHANNEL) : null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=RmFragmentChannelInfoBinding.inflate(getLayoutInflater());
        initView(false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    protected void onCreateViewAfterViewStubInflated(View inflatedView, Bundle savedInstanceState) {
    }

    @Override
    protected int getViewStubLayoutResource() {
        return R.layout.rm_fragment_channel_info;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onChannelSubscribeChanged(Channel channel) {

    }

    private void initView(boolean isUpdate) {
        if (mChannel == null) return;
        if (Utilities.notEmpty(mChannel.getDescription())) {
            binding.llChannelInfo.setVisibility(View.VISIBLE);
            binding.tvChannelInfo.setText(Html.fromHtml(mChannel.getDescription()));
        } else {
            binding.llChannelInfo.setVisibility(View.GONE);
        }
        if (mChannel.getCreatedDate() == 0) {
            binding.tvChannelCreateDate.setVisibility(View.GONE);
        } else {
            binding.tvChannelCreateDate.setText(Html.fromHtml(DateTimeUtils.calculateTime(mChannel.getCreatedDate())));
            binding.tvChannelCreateDate.setVisibility(View.VISIBLE);
        }
    }
}
