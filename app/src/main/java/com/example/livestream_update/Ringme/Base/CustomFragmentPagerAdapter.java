package com.example.livestream_update.Ringme.Base;


import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.vtm.ringme.utils.Log;


public abstract class CustomFragmentPagerAdapter extends FragmentPagerAdapter {
    final String TAG = "CustomFragmentPagerAdapter";

    public CustomFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        try {
//            if (container != null)
            super.finishUpdate(container);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        try {
            super.destroyItem(container, position, object);
        } catch (IllegalStateException e) {
            Log.e(TAG, e);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
    }
}
