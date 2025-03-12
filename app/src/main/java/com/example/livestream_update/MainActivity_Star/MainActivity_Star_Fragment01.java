package com.example.livestream_update.MainActivity_Star;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.livestream_update.R;

public class MainActivity_Star_Fragment01 extends Fragment {

    private ImageView btnClose;
    private FrameLayout fragmentContainer;

    public MainActivity_Star_Fragment01() {
        // Required empty public constructor
    }

    public static MainActivity_Star_Fragment01 newInstance() {
        return new MainActivity_Star_Fragment01();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rm_activity_main_star_fragment01, container, false);

        // Ánh xạ ID
        btnClose = view.findViewById(R.id.rm_activity_main_star_recharge_imageview_01);
        fragmentContainer = view.findViewById(R.id.rm_activity_main_star_recharge_framelayout_01);

        // Xử lý sự kiện khi nhấn nút đóng
        btnClose.setOnClickListener(v -> getParentFragmentManager().beginTransaction().remove(this).commit());

        return view;
    }
}
