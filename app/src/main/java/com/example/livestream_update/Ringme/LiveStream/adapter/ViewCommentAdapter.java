package com.example.livestream_update.Ringme.LiveStream.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.vtm.ringme.base.CustomFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewCommentAdapter extends CustomFragmentPagerAdapter {
    static final String TAG = "ViewPagerDetailAdapter";
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewCommentAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        try {
            return mFragmentList.get(position);
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public String getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}