package com.example.livestream_update.Ringme.TabVideo.channelDetail;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class ChannelPagerAdapter extends FragmentStatePagerAdapter {

    private final ArrayList<Fragment> fragments;
    private final ArrayList<String> chars;

    public ChannelPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        chars = new ArrayList<>();
    }

    public void bindData(ArrayList<Fragment> fragmentResults, ArrayList<String> charResults) {
        fragments.clear();
        fragments.addAll(fragmentResults);

        chars.clear();
        chars.addAll(charResults);

        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return chars.get(position);
    }
}
