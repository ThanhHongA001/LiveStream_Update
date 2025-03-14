package com.example.livestream_update.Ringme.Adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vtm.ringme.activities.LivestreamFutureActivity;
import com.vtm.ringme.fragment.LivestreamCategoryFragment;


public class LivestreamCategoryPagerAdapter extends FragmentStateAdapter {
    private final LivestreamFutureActivity activity;
    public LivestreamCategoryPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.activity = (LivestreamFutureActivity) fragmentActivity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new LivestreamCategoryFragment(activity, 6);
            case 1: return new LivestreamCategoryFragment(activity, 7);
            default: return new LivestreamCategoryFragment(activity, 5);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
