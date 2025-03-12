package com.example.livestream_update.ActivityMain_Home.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livestream_update.ActivityMain_Home.Fragment_Adapter.MainActivity_Home_Fragment01_Adapter;
import com.example.livestream_update.ActivityMain_Home.Fragment_Model.MainActivity_Home_Fragment01_Model;
import com.example.livestream_update.MainActivity_Button;
import com.example.livestream_update.R;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator2;

public class MainActivity_Home_Fragment01 extends Fragment {

    private RecyclerView recyclerView;
    private MainActivity_Home_Fragment01_Adapter adapter;
    private List<MainActivity_Home_Fragment01_Model> itemList;
    private CircleIndicator2 circleIndicator;
    private LinearLayoutManager layoutManager;

    private ImageView iv01, iv02, iv03, iv04;

    public MainActivity_Home_Fragment01() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rm_activity_main_home_fragment01, container, false);

        recyclerView = view.findViewById(R.id.rm_fragment_activity_main_home_01_rv_01);
        circleIndicator = view.findViewById(R.id.rm_fragment_activity_main_home_01_ci_01);
        iv01 = view.findViewById(R.id.rm_fragment_activity_main_home_01_iv_01);
        iv02 = view.findViewById(R.id.rm_fragment_activity_main_home_01_iv_02);
        iv03 = view.findViewById(R.id.rm_fragment_activity_main_home_01_iv_03);
        iv04 = view.findViewById(R.id.rm_fragment_activity_main_home_01_iv_04);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Khởi tạo danh sách item
        itemList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            itemList.add(new MainActivity_Home_Fragment01_Model(R.drawable.rm_error));
        }

        // Gán Adapter
        adapter = new MainActivity_Home_Fragment01_Adapter(getContext(), itemList);
        recyclerView.setAdapter(adapter);

        // Đặt vị trí ban đầu ở giữa danh sách để cuộn vô hạn
        int startPosition = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % itemList.size());
        recyclerView.scrollToPosition(startPosition);

        // Liên kết với CircleIndicator2
        circleIndicator.createIndicators(itemList.size(), 0);
        updateIndicator(startPosition);

        // Bắt sự kiện cuộn RecyclerView để cập nhật CircleIndicator2
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                updateIndicator(firstVisibleItemPosition);
            }
        });

        // Thêm sự kiện click cho các ImageView
        iv01.setOnClickListener(v -> openActivity("Loyalty"));
        iv02.setOnClickListener(v -> openActivity("Video"));
        iv03.setOnClickListener(v -> openActivity("Gamezone"));
        iv04.setOnClickListener(v -> openActivity("More"));

        return view;
    }

    // Mở MainActivity_Button và truyền dữ liệu về loại button được nhấn
    private void openActivity(String type) {
        Intent intent = new Intent(getActivity(), MainActivity_Button.class);
        intent.putExtra("button_type", type);
        startActivity(intent);
    }

    // Cập nhật CircleIndicator2 khi cuộn RecyclerView
    private void updateIndicator(int position) {
        int realPosition = position % itemList.size();
        circleIndicator.animatePageSelected(realPosition);
    }
}
