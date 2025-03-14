package com.example.livestream_update.Ringme.LiveStream.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vtm.R;
import com.vtm.ringme.base.BaseFragment;
import com.vtm.ringme.livestream.listener.OnSingleClickListener;
import com.vtm.ringme.utils.ToastUtils;


import butterknife.BindView;
import butterknife.ButterKnife;

public class DisableCommentLiveStreamFragment extends BaseFragment {

    @SuppressLint("StaticFieldLeak")
    private static DisableCommentLiveStreamFragment mInstance;

    @BindView(R.id.rootView)
    View rootView;
    @BindView(R.id.layout_content)
    View viewContent;
    private boolean isLandscape;
    private boolean isVerticalVideo;

    public static DisableCommentLiveStreamFragment newInstance() {
        if (mInstance == null) mInstance = new DisableCommentLiveStreamFragment();
        return mInstance;
    }

    public static DisableCommentLiveStreamFragment self() {
        return mInstance;
    }

    @Override
    public String getName() {
        return "DisableCommentLiveStreamFragment";
    }

    @Override
    public int getResIdView() {
        return R.layout.rm_fragment_disable_comment_livestream;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        rootView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (!isVerticalVideo && isLandscape) {
                    ToastUtils.showToast(getActivity().getApplicationContext(),getString(R.string.msg_click_view_comment));
                }
            }
        });
        return view;
    }

    public View getRootView() {
        return rootView;
    }

    public void onChangeOrientation(boolean isLandscape, boolean isVerticalVideo) {
        this.isLandscape = isLandscape;
        this.isVerticalVideo = isVerticalVideo;
        if (isVerticalVideo) {
            if (viewContent != null) viewContent.setVisibility(View.GONE);
        } else {
            if (viewContent != null)
                viewContent.setVisibility(isLandscape ? View.GONE : View.VISIBLE);
        }
    }

}

