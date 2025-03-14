package com.example.livestream_update.Ringme.Base;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewbinding.ViewBinding;

import com.vtm.ringme.tabvideo.listener.OnInternetChangedListener;
import com.vtm.ringme.utils.Log;


public abstract class BaseFragmentNew extends Fragment implements OnInternetChangedListener {

    protected final String TAG = getName();
    protected AppCompatActivity mActivity;
    protected Toolbar toolbar;
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated;
    protected boolean isViewInitiated;

    protected SwipeRefreshLayout layout_refresh;
    protected boolean isRefresh = false;


    public abstract String getName();



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume isVisible: " + isVisibleToUser + " -------");
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause ------------------------------");
        super.onPause();
    }
    public abstract ViewBinding setViewBinding();
    @Override
    public void onDestroy() {
        isViewInitiated = false;
        Log.d(TAG, "onDestroy ------------------------------");
        super.onDestroy();
    }

    public void setTitle(String title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }

    public abstract int getResIdView();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isViewInitiated = false;
        return setViewBinding().getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated visible: " + isVisibleToUser + " -------------------------");
        isViewInitiated = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        Log.d(TAG, "setUserVisibleHint " + isVisibleToUser);
    }

    public boolean canLazyLoad() {
        return isVisibleToUser && isViewInitiated && !isDataInitiated;
    }

    @Override
    public void onInternetChanged() {

    }

    protected void showRefresh() {
        if (layout_refresh != null) {
            layout_refresh.setRefreshing(true);
        }
    }

    protected void hideRefresh() {
        if (layout_refresh != null) {
            isRefresh = false;

            layout_refresh.setRefreshing(false);
            layout_refresh.destroyDrawingCache();
            layout_refresh.clearAnimation();
        }
    }
}