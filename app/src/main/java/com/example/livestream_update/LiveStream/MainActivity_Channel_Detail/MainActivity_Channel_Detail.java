package com.example.livestream_update.LiveStream.MainActivity_Channel_Detail;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.example.livestream_update.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_Channel_Detail extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ChannelDetailPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rm_activity_main_channel_detail);

        tabLayout = findViewById(R.id.rm_activity_main_channel_detail_tl_01);
        viewPager2 = findViewById(R.id.rm_activity_main_channel_detail_vp2_01);

        // Khởi tạo Adapter
        pagerAdapter = new ChannelDetailPagerAdapter(this);
        viewPager2.setAdapter(pagerAdapter);

        // Kết nối TabLayout với ViewPager2
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Information");
                    break;
                case 1:
                    tab.setText("Video");
                    break;
            }
        }).attach();
    }

    // Adapter cho ViewPager2
    private static class ChannelDetailPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragments = new ArrayList<>();

        public ChannelDetailPagerAdapter(AppCompatActivity activity) {
            super(activity);
            fragments.add(new MainActivity_Channel_Detail_Fragment01());
            fragments.add(new MainActivity_Channel_Detail_Fragment02());
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }
}
