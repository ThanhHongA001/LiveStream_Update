package com.example.livestream_update.Ringme.Home;

/*
 * Copyright (c) https://bigzun.blogspot.com/
 * Email: bigzun.com@gmail.com
<<<<<<< HEAD
 * Created by namnh40 on 2019/11/12
=======
 * Created by namnh40 on 2020/2/4
 *
 */


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.vtm.R;
import com.vtm.ringme.helper.NetworkHelper;


public class LoadingView extends LinearLayout {

    ProgressBar progressLoading;
    TextView tvEmpty;
    TextView tvEmptyRetry1;
    TextView tvEmptyRetry2;
    ImageView btnRetry;
    LinearLayout emptyLayout;

    public LoadingView(Context context) {
        super(context);
        initView(context);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    public void initView(Context context) {
        if (!isInEditMode()) {
            ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.rm_include_loading_view, this, true);
            progressLoading = getRootView().findViewById(R.id.empty_progress);
            tvEmpty = getRootView().findViewById(R.id.empty_text);
            tvEmptyRetry1 = getRootView().findViewById(R.id.empty_retry_text1);
            tvEmptyRetry2 = getRootView().findViewById(R.id.empty_retry_text2);
            btnRetry = getRootView().findViewById(R.id.empty_retry_button);
            emptyLayout = getRootView().findViewById(R.id.empty_layout);
            emptyLayout.setBackgroundColor(Color.parseColor("#00000000"));
//            try {
//                Blurry.with(context)
//                        .radius(10)
//                        .sampling(8)
//                        .color(Color.argb(66, 255, 255, 0))
//                        .async()
//                        .animate(500)
//                        .onto((ViewGroup) tvEmpty.getParent());
//            } catch (Exception e) {
//                Log.e("LoadingViewBlur", e.getMessage());
//            }
        }
    }

    public void setBackgroundColor(int color){
        emptyLayout.setBackgroundColor(color);
    }

    public void setOnClickRetryListener(OnClickListener listener) {
        if (btnRetry != null) btnRetry.setOnClickListener(listener);
    }

    public void showLoading() {
        setVisibility(View.VISIBLE);
        if (progressLoading != null) progressLoading.setVisibility(View.VISIBLE);
        if (tvEmpty != null) tvEmpty.setVisibility(View.GONE);
        if (tvEmptyRetry1 != null) tvEmptyRetry1.setVisibility(View.GONE);
        if (tvEmptyRetry2 != null) tvEmptyRetry2.setVisibility(View.GONE);
        if (btnRetry != null) btnRetry.setVisibility(View.GONE);
    }

    public void showLoadedEmpty() {
        setVisibility(View.VISIBLE);
        if (tvEmpty != null) {
            tvEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText(R.string.no_data);
        }
        if (progressLoading != null) progressLoading.setVisibility(View.GONE);
        if (tvEmptyRetry1 != null) tvEmptyRetry1.setVisibility(View.GONE);
        if (tvEmptyRetry2 != null) tvEmptyRetry2.setVisibility(View.GONE);
        if (btnRetry != null) btnRetry.setVisibility(View.GONE);
    }

    public void showLoadedEmpty(String message) {
        setVisibility(View.VISIBLE);
        if (tvEmpty != null) {
            tvEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText(message);
        }
        if (progressLoading != null) progressLoading.setVisibility(View.GONE);
        if (tvEmptyRetry1 != null) tvEmptyRetry1.setVisibility(View.GONE);
        if (tvEmptyRetry2 != null) tvEmptyRetry2.setVisibility(View.GONE);
        if (btnRetry != null) btnRetry.setVisibility(View.GONE);
    }

    public void showLoadedSuccess() {
        setVisibility(View.GONE);
        if (progressLoading != null) progressLoading.setVisibility(View.GONE);
        if (tvEmpty != null) tvEmpty.setVisibility(View.GONE);
        if (tvEmptyRetry1 != null) tvEmptyRetry1.setVisibility(View.GONE);
        if (tvEmptyRetry2 != null) tvEmptyRetry2.setVisibility(View.GONE);
        if (btnRetry != null) btnRetry.setVisibility(View.GONE);
    }

    public void showLoadedError() {
        setVisibility(View.VISIBLE);
        if (btnRetry != null) btnRetry.setVisibility(View.VISIBLE);
        if (progressLoading != null) progressLoading.setVisibility(View.GONE);
        if (tvEmpty != null) tvEmpty.setVisibility(View.GONE);
        if (getContext() != null) {
            if (NetworkHelper.isConnectInternet(getContext())) {
                if (tvEmptyRetry1 != null) tvEmptyRetry1.setVisibility(View.VISIBLE);
                if (tvEmptyRetry2 != null) tvEmptyRetry2.setVisibility(View.GONE);
            } else {
                if (tvEmptyRetry1 != null) tvEmptyRetry1.setVisibility(View.GONE);
                if (tvEmptyRetry2 != null) tvEmptyRetry2.setVisibility(View.VISIBLE);
            }
        } else {
            if (tvEmptyRetry1 != null) tvEmptyRetry1.setVisibility(View.VISIBLE);
            if (tvEmptyRetry2 != null) tvEmptyRetry2.setVisibility(View.GONE);
        }
    }

    public void showLoadedError(String message) {
        setVisibility(View.VISIBLE);
        if (btnRetry != null) btnRetry.setVisibility(View.VISIBLE);
        if (progressLoading != null) progressLoading.setVisibility(View.GONE);
        if (tvEmpty != null) tvEmpty.setVisibility(View.GONE);
        if (tvEmptyRetry1 != null) {
            tvEmptyRetry1.setText(message);
            tvEmptyRetry1.setVisibility(View.VISIBLE);
        }
        if (tvEmptyRetry2 != null) tvEmptyRetry2.setVisibility(View.GONE);
    }

    public void showLoadedError(String message, int drawableRes) {
        setVisibility(View.VISIBLE);
        if (btnRetry != null) {
            btnRetry.setVisibility(View.VISIBLE);
            if (getContext() != null)
                btnRetry.setImageDrawable(getContext().getResources().getDrawable(drawableRes));
        }
        if (progressLoading != null) progressLoading.setVisibility(View.GONE);
        if (tvEmpty != null) tvEmpty.setVisibility(View.GONE);
        if (tvEmptyRetry1 != null) {
            tvEmptyRetry1.setText(message);
            tvEmptyRetry1.setVisibility(View.VISIBLE);
        }
        if (tvEmptyRetry2 != null) tvEmptyRetry2.setVisibility(View.GONE);
    }
}
