package com.example.livestream_update.MainActivity_Home;

import androidx.fragment.app.Fragment;

public class MainActivity_Home_Model {
    private Fragment fragment;

    public MainActivity_Home_Model(Fragment fragment) {
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }
}
