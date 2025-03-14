package com.example.livestream_update.Ringme.TabVideo;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.utils.Log;

/**
 * Created by tuanha00 on 3/22/2018.
 */

public class BaseFragment extends Fragment {
    protected String TAG = getClass().getSimpleName();
    protected ApplicationController application;
    protected AppCompatActivity activity;
    protected boolean runnable;
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated;
    protected boolean isViewInitiated;
    private Dialog requireRegisterDataDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "-------------------- onCreate");
        runnable = true;
        activity = (AppCompatActivity) getActivity();
        application = (ApplicationController) activity.getApplication();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "-------------------- onActivityCreated isVisibleToUser: " + isVisibleToUser);
        this.isViewInitiated = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        Log.i(TAG, "-------------------- setUserVisibleHint: " + isVisibleToUser + ", canLazyLoad: " + canLazyLoad());
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "-------------------- onDestroyView");
        runnable = false;
        isViewInitiated = false;
        isDataInitiated = false;
        super.onDestroyView();
    }

    public boolean canLazyLoad() {
        return isVisibleToUser && isViewInitiated && !isDataInitiated;
    }
}
