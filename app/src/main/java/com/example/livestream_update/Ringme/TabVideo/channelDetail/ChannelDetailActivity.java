package com.example.livestream_update.Ringme.TabVideo.channelDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vtm.R;
import com.vtm.ringme.model.tab_video.Channel;
import com.vtm.ringme.tabvideo.BaseActivity;
import com.vtm.ringme.tabvideo.channelDetail.main.ChannelDetailFragment;


public class ChannelDetailActivity extends BaseActivity {

    private static final String CHANNEL = "channel";

    public static void start(Context context, Channel channel) {
        if (context != null && channel != null) {
            Intent intent = new Intent(context, ChannelDetailActivity.class);
            intent.putExtra(CHANNEL, channel);
            context.startActivity(intent);
        }
    }

    private Channel mChannel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rm_activity_channel_detail_v2);
        mChannel = (Channel) getIntent().getSerializableExtra(CHANNEL);
        addMain();
    }

    public void addMain() {
        getSupportFragmentManager().beginTransaction().add(R.id.frame_content, ChannelDetailFragment.newInstance(mChannel)).commitAllowingStateLoss();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }
}
