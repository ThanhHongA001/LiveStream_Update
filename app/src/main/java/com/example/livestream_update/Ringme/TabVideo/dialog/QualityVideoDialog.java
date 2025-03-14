package com.example.livestream_update.Ringme.TabVideo.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vtm.R;
import com.vtm.ringme.base.BaseAdapter;
import com.vtm.ringme.common.utils.ScreenManager;
import com.vtm.ringme.model.tab_video.Resolution;
import com.vtm.ringme.model.tab_video.Video;
import com.vtm.ringme.tabvideo.adapter.QualityVideoAdapter;
import com.vtm.ringme.utils.Utilities;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QualityVideoDialog extends BottomSheetDialog {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.radio_group)
    LinearLayout radioGroup;

    private OnQualityVideoListener mOnQualityVideoListener;
    private ArrayList<Resolution> resolutions;
    private Video currentVideo;
    private final Activity context;

    private View bottomSheet;
    private QualityVideoAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvEmpty;

    public QualityVideoDialog(@NonNull Activity context) {
        super(context);
        this.context = context;
    }

    public QualityVideoDialog setCurrentVideo(Video currentVideo) {
        this.currentVideo = currentVideo;
        return this;
    }

    public QualityVideoDialog setOnQualityVideoListener(OnQualityVideoListener mOnQualityVideoListener) {
        this.mOnQualityVideoListener = mOnQualityVideoListener;
        return this;
    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rm_dialog_quality_video);
        recyclerView = findViewById(R.id.recycler_view);
        tvEmpty = findViewById(R.id.tv_empty);
        if (getWindow() != null)
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ButterKnife.bind(this);
        if (currentVideo != null)
            resolutions = currentVideo.getListResolution();
        if(Utilities.notEmpty(resolutions)) {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            Collections.sort(resolutions);
            adapter = new QualityVideoAdapter(context);
            adapter.setItems(resolutions);
            adapter.setListener(new QualityVideoAdapter.QualityVideoListener() {
                @Override
                public void onClickQuality(int position) {
                    mOnQualityVideoListener.onQualityVideo(position, currentVideo, resolutions.get(position));
                    dismiss();
                }
            });
            BaseAdapter.setupVerticalRecycler(context, recyclerView, null, adapter, false);
        } else {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
//        View view;
//        TextView tvCheckBox;
//        ImageView ivCheckBox;
//        if (Utilities.notEmpty(resolutions)) {
//            String configResolution = ApplicationController.self().getConfigResolutionVideo();
//            boolean check = false;
//            int size = resolutions.size() - 1;
//            for (int i = size; i >= 0; i--) {
//                Resolution resolution = resolutions.get(i);
//                view = LayoutInflater.from(context).inflate(R.layout.layout_radio_button, null, false);
//                view.setOnClickListener(mOnClickListener);
//                view.setId(i);
//                ivCheckBox = view.findViewById(R.id.iv_check_box);
//                tvCheckBox = view.findViewById(R.id.tv_check_box);
//                tvCheckBox.setText(resolution.getTitle());
//                //if (i == currentVideo.getIndexQuality())
//                if (!check && (configResolution.equalsIgnoreCase(resolution.getKey()) || i == 0)) {
//                    check = true;
//                    checkView(ivCheckBox, tvCheckBox);
//                }
//                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
//                radioGroup.addView(view, 0, layoutParams);
//            }
//        }

        bottomSheet = findViewById(R.id.design_bottom_sheet);
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Resources resources = context.getResources();
                if (bottomSheet != null && resources != null && bottomSheet.getWidth() > bottomSheet.getHeight()) {
                    BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                    int height = ScreenManager.getWidth(context) / 2;

                    if (bottomSheetBehavior != null) {
                        if (height > bottomSheet.getHeight())
                            bottomSheetBehavior.setPeekHeight(bottomSheet.getHeight());
                        else
                            bottomSheetBehavior.setPeekHeight(height);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
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

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mOnQualityVideoListener != null && radioGroup != null) {

                int position = radioGroup.indexOfChild(view);

                int idx = Math.min(Math.max(position, 0), resolutions.size() - 1);

                mOnQualityVideoListener.onQualityVideo(idx, currentVideo, resolutions.get(idx));
            }
            dismiss();
        }
    };

    public interface OnQualityVideoListener {
        void onQualityVideo(int idx, Video currentVideo, Resolution resolution);
    }
}
