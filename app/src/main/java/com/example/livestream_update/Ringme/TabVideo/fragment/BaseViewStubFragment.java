package com.example.livestream_update.Ringme.TabVideo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;

import com.vtm.R;
import com.vtm.ringme.tabvideo.BaseFragment;

public abstract class BaseViewStubFragment extends BaseFragment {

    private Bundle mSavedInstanceState;
    private boolean mHasInflated = false;
    private ViewStub mViewStub;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rm_fragment_viewstub, container, false);
        mViewStub = view.findViewById(R.id.fragmentViewStub);
        mViewStub.setLayoutResource(getViewStubLayoutResource());
        mSavedInstanceState = savedInstanceState;

        if (getUserVisibleHint() && !mHasInflated) {
            View inflatedView = mViewStub.inflate();
            onCreateViewAfterViewStubInflated(inflatedView, mSavedInstanceState);
            afterViewStubInflated(view);
        }

        return view;
    }

    protected abstract void onCreateViewAfterViewStubInflated(View inflatedView, Bundle savedInstanceState);

    /**
     * The layout ID associated with this ViewStub
     *
     * @return
     * @see ViewStub#setLayoutResource(int)
     */
    @LayoutRes
    protected abstract int getViewStubLayoutResource();

    /**
     * @param originalViewContainerWithViewStub
     */
    @CallSuper
    protected void afterViewStubInflated(View originalViewContainerWithViewStub) {
        mHasInflated = true;
        if (originalViewContainerWithViewStub != null) {
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && mViewStub != null && !mHasInflated) {
            View inflatedView = mViewStub.inflate();
            onCreateViewAfterViewStubInflated(inflatedView, mSavedInstanceState);
            afterViewStubInflated(getView());
        }
    }

    // Thanks to Noa Drach, this will fix the orientation change problem
    @Override
    public void onDetach() {
        super.onDetach();
        mHasInflated = false;
    }
}