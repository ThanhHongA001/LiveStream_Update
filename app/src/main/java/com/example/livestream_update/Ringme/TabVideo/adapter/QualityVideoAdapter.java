package com.example.livestream_update.Ringme.TabVideo.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.vtm.R;
import com.vtm.databinding.RmLayoutRadioButtonBinding;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.base.BaseAdapter;
import com.vtm.ringme.model.tab_video.Resolution;

public class QualityVideoAdapter extends BaseAdapter<BaseAdapter.ViewHolder, Resolution> {
    private QualityVideoListener listener;
    private boolean isCheck;
    public QualityVideoAdapter(Activity activity) {
        super(activity);
    }

    public void setListener(QualityVideoListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QualityVideoHolder(RmLayoutRadioButtonBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(getItem(position), position);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public class QualityVideoHolder extends ViewHolder{
        private final RmLayoutRadioButtonBinding binding;

        public QualityVideoHolder(RmLayoutRadioButtonBinding bin) {
            super(bin.getRoot());
            binding = bin;
        }

        @Override
        public void bindData(Object item, int position) {
            super.bindData(item, position);
            if(item instanceof Resolution) {
                String configResolution = ApplicationController.self().getConfigResolutionVideo();
                binding.tvCheckBox.setText(((Resolution) item).getTitle());
                if(!isCheck && (configResolution.equalsIgnoreCase(((Resolution) item).getKey()) || getItems().size() -1 == position)){
                    binding.ivCheckBox.setVisibility(View.VISIBLE);
                    binding.ivCheckBox.setImageResource(R.drawable.rm_ic_checkbox_video);
                    binding.tvCheckBox.setTextColor(ContextCompat.getColor(binding.tvCheckBox.getContext(), R.color.videoColorSelect));
                    isCheck = true;
                } else {
                    binding.ivCheckBox.setVisibility(View.INVISIBLE);
                    binding.tvCheckBox.setTextColor(ContextCompat.getColor(binding.tvCheckBox.getContext(), R.color.v5_text));
                }
                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClickQuality(position);
                    }
                });
            }
        }
    }

    public interface QualityVideoListener{
        void onClickQuality(int position);
    }
}
