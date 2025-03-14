package com.example.livestream_update.Ringme.LiveStream.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vtm.R;
import com.vtm.databinding.RmDialogQualityVideoBinding;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.common.utils.ScreenManager;
import com.vtm.ringme.model.livestream.AdaptiveLinkModel;
import com.vtm.ringme.model.tab_video.Resolution;
import com.vtm.ringme.utils.Utilities;

import java.util.ArrayList;


public class QualityDialog extends BottomSheetDialog {


    private OnQualityVideoListener mOnQualityVideoListener;
    private ArrayList<Resolution> resolutions;
    private AdaptiveLinkModel adaptiveLinkModel;
    private String hlsLink;
    private Activity context;

    private View bottomSheet;
    private RmDialogQualityVideoBinding binding;

    public QualityDialog(@NonNull Activity context) {
        super(context);
        this.context = context;
    }

    public QualityDialog setCurrentVideo(AdaptiveLinkModel adaptiveLinkModel, String hlsLink) {
        this.adaptiveLinkModel = adaptiveLinkModel;
        this.hlsLink = hlsLink;
        return this;
    }

    public QualityDialog setOnQualityVideoListener(OnQualityVideoListener mOnQualityVideoListener) {
        this.mOnQualityVideoListener = mOnQualityVideoListener;
        return this;
    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=RmDialogQualityVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getWindow() != null)
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        resolutions = new ArrayList<>();
        if (adaptiveLinkModel != null) {
            if (adaptiveLinkModel.getLink240() != null)
                resolutions.add(new Resolution("240", "240p", adaptiveLinkModel.getLink240()));
            if (adaptiveLinkModel.getLink360() != null)
                resolutions.add(new Resolution("360", "360p", adaptiveLinkModel.getLink360()));
            if (adaptiveLinkModel.getLink480() != null)
                resolutions.add(new Resolution("480", "480p", adaptiveLinkModel.getLink480()));
            if (adaptiveLinkModel.getLink720() != null)
                resolutions.add(new Resolution("720", "720p", adaptiveLinkModel.getLink720()));
            if (adaptiveLinkModel.getLink1080() != null)
                resolutions.add(new Resolution("1080", "1080p", adaptiveLinkModel.getLink1080()));
            if (hlsLink != null)
                resolutions.add(new Resolution("auto", "auto", hlsLink));
        }
        View view;
        TextView tvCheckBox;
        ImageView ivCheckBox;
        if (Utilities.notEmpty(resolutions)) {
            String configResolution = ApplicationController.self().getConfigResolutionVideo();
            boolean check = false;
            int size = resolutions.size() - 1;
            for (int i = size; i >= 0; i--) {
                Resolution resolution = resolutions.get(i);
                view = LayoutInflater.from(context).inflate(R.layout.rm_layout_radio_button, null, false);
                view.setOnClickListener(mOnClickListener);
                view.setId(i);
                ivCheckBox = view.findViewById(R.id.iv_check_box);
                tvCheckBox = view.findViewById(R.id.tv_check_box);
                tvCheckBox.setText(resolution.getTitle());
                //if (i == currentVideo.getIndexQuality())
                if (!check && (configResolution.equalsIgnoreCase(resolution.getKey()) || i == 0)) {
                    check = true;
                    checkView(ivCheckBox, tvCheckBox);
                }
                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                binding.radioGroup.addView(view, 0, layoutParams);
            }
        }

        bottomSheet = findViewById(R.id.design_bottom_sheet);
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Resources resources = context.getResources();
                if (bottomSheet != null && resources != null && bottomSheet.getWidth() > bottomSheet.getHeight()) {
                    BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                    int height = ScreenManager.getWidth(context) / 2;

                    if (bottomSheetBehavior != null)
                        if (height > bottomSheet.getHeight())
                            bottomSheetBehavior.setPeekHeight(bottomSheet.getHeight());
                        else
                            bottomSheetBehavior.setPeekHeight(height);
                }
            }
        });
    }

    private void checkView(ImageView ivCheckBox, TextView tvCheckBox) {
        ivCheckBox.setImageResource(R.drawable.rm_ic_checkbox_video);
        tvCheckBox.setTextColor(ContextCompat.getColor(context, R.color.videoColorSelect));
    }

    @Override
    public void show() {
        super.show();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mOnQualityVideoListener != null && binding.radioGroup != null) {

                int position = binding.radioGroup.indexOfChild(view);

                int idx = Math.min(Math.max(position, 0), resolutions.size() - 1);

                mOnQualityVideoListener.onQualityVideo(idx, resolutions.get(idx));
            }
            dismiss();
        }
    };

    public interface OnQualityVideoListener {
        void onQualityVideo(int idx, Resolution resolution);
    }
}