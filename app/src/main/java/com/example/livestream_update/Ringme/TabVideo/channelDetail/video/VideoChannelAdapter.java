package com.example.livestream_update.Ringme.TabVideo.channelDetail.video;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vtm.R;
import com.vtm.databinding.RmItemVideoChannelBinding;
import com.vtm.ringme.livestream.listener.OnSingleClickListener;
import com.vtm.ringme.model.tab_video.Channel;
import com.vtm.ringme.model.tab_video.Video;
import com.vtm.ringme.tabvideo.BaseAdapterV2;
import com.vtm.ringme.tabvideo.listener.OnVideoChannelListener;
import com.vtm.ringme.utils.DateTimeUtils;
import com.vtm.ringme.utils.Utilities;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//adapter channel cu
public class VideoChannelAdapter extends BaseAdapterV2<Object, LinearLayoutManager, RecyclerView.ViewHolder> {

    public static final int TYPE_SPACE_HEADER = -2;
    public static final int TYPE_SPACE_BOTTOM = -1;
    public static final int LOAD_MORE = 0;
    public static final int NORMAL = 1;
    private OnVideoChannelListener onVideoChannelListener;
    private Channel mChannel;

    public VideoChannelAdapter(Activity act) {
        super(act);
    }

    public void setOnVideoChannelListener(OnVideoChannelListener onVideoChannelListener) {
        this.onVideoChannelListener = onVideoChannelListener;
    }

    @Override
    public int getItemViewType(int position) {
        Object object = items.get(position);
        if (object instanceof Video) {
            return NORMAL;
        } else if (Utilities.equals(object, TYPE_SPACE_HEADER)) {
            return TYPE_SPACE_HEADER;
        } else if (Utilities.equals(object, TYPE_SPACE_BOTTOM)) {
            return TYPE_SPACE_BOTTOM;
        } else {
            return LOAD_MORE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case NORMAL:
                RmItemVideoChannelBinding videoBinding = RmItemVideoChannelBinding.inflate(layoutInflater, parent, false);
                return new VideoHolder(activity, videoBinding, parent, onVideoChannelListener);
            case TYPE_SPACE_HEADER:
                return new SpaceHolder(activity, layoutInflater, parent, R.color.v5_cancel);
            case TYPE_SPACE_BOTTOM:
                return new SpaceHolder(activity, layoutInflater, parent, android.R.color.white);
            default:
                return new LoadMoreHolder(activity, layoutInflater, parent);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VideoHolder) {
            ((VideoHolder) holder).setChannel(mChannel);
            ((VideoHolder) holder).bindData(items, position);
        }
    }

    public Object getItem(int position) {
        if (items.size() > position && position >= 0) {
            return items.get(position);
        }
        return null;
    }

    public void setChannel(Channel channel) {
        mChannel = channel;
    }

    public static class SpaceHolder extends LoadMoreHolder {

        SpaceHolder(Activity activity, LayoutInflater layoutInflater, ViewGroup parent, int color) {
            super(layoutInflater.inflate(R.layout.rm_item_channel_space_v5, parent, false));
            itemView.setBackgroundColor(ContextCompat.getColor(activity, color));
        }
    }

    public static class VideoHolder extends ViewHolder {



        private RmItemVideoChannelBinding binding;
        private Channel mChannel;

        public VideoHolder(Activity activity, RmItemVideoChannelBinding RmItemVideoChannelBinding, ViewGroup parent, final OnVideoChannelListener listener) {
            super(RmItemVideoChannelBinding.getRoot());
            this.binding=RmItemVideoChannelBinding;
            itemView.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                        if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION)
                            listener.onClickPlaylistVideo(view, getAdapterPosition());

                }
            });
            if (binding.ivMore != null) {
                binding.ivMore.setOnClickListener(view -> {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION)
                        listener.onClickMorePlaylistVideo(view, getAdapterPosition());
                });
            }
        }

        @Override
        public void bindData(ArrayList<Object> items, int position) {
            super.bindData(items, position);
            Object item = items.get(position);
            if (item instanceof Video) {
                Video mVideo = (Video) item;
                binding.tvTitle.setText(mVideo.getTitle());
                if (!TextUtils.isEmpty(mVideo.getChannelName())) {
                    binding.tvChannel.setVisibility(View.VISIBLE);
                    binding.tvChannel.setText(mVideo.getChannelName());
                } else {
                    binding.tvChannel.setVisibility(View.GONE);
                }
                if (binding.tvDuration != null && mVideo.getPublishTime() > 0) {
                    binding.tvDuration.setVisibility(View.VISIBLE);
                    binding.tvDuration.setText(DateTimeUtils.calculateTime(binding.tvDuration.getResources(), mVideo.getPublishTime()));
                }
                if (binding.tvDuration != null && !TextUtils.isEmpty(mVideo.getDuration())) {
                    binding.tvDuration.setVisibility(View.VISIBLE);
                    binding.tvDuration.setText(mVideo.getDuration());
                }
                ExecutorService executor = Executors.newFixedThreadPool(5);
                asyncSetText(binding.tvNumberSeen, mVideo.getTotalView(), executor);
                executor.shutdown();
                if (binding.ivLiveStream != null)
                    binding.ivLiveStream.setVisibility(mVideo.isLive() ? View.VISIBLE : View.GONE);
                    if (mVideo.getItemStatus() == Video.Status.NOT_APPROVED.VALUE) {
                        binding.ivApproved.setVisibility(View.VISIBLE);
                        binding.ivApproved.setBackground(binding.ivApproved.getResources().getDrawable(R.drawable.rm_bg_wait_approve));
                        binding.ivApproved.setText(R.string.wait_for_approved);
                        binding.tvTitle.setTextColor(binding.ivApproved.getResources().getColor(R.color.v5_text_30));
                    }

                if (mChannel != null && mChannel.isMyChannel()) {
                    if(mVideo.isPaid()) {
                        binding.ivPaid.setVisibility(View.VISIBLE);
                    } else {
                        binding.ivPaid.setVisibility(View.GONE);
                    }
                }
                Glide.with(binding.ivImage)
                        .load(((Video) item).getImagePath())
                        .centerCrop()
                        .placeholder(R.drawable.rm_placeholder)
                        .into(binding.ivImage);
            }
        }

        private void asyncSetText(TextView textView, final long numberView, Executor executor) {
            final WeakReference<TextView> textViewRef = new WeakReference<>(textView);
            executor.execute(() -> {
                TextView textView12 = textViewRef.get();
                if (textView12 == null) return;
                textView12.post(() -> {
                    TextView textView1 = textViewRef.get();
                    if (textView1 == null) return;
                    textView1.setText(String.format((numberView <= 1) ? textView1.getContext().getString(R.string.view)
                            : textView1.getContext().getString(R.string.video_views), Utilities.getTotalView(numberView)));
                });
            });
        }

        public void setChannel(Channel channel) {
            mChannel = channel;
        }
    }
}
