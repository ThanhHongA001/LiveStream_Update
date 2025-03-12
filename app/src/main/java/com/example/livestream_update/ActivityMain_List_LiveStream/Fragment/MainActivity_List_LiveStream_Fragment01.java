package com.example.livestream_update.ActivityMain_List_LiveStream.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livestream_update.ActivityMain_List_LiveStream.Fragment_Adapter.MainActivity_List_LiveStream_Fragment01_Adapter;
import com.example.livestream_update.ActivityMain_List_LiveStream.Fragment_Adapter.MainActivity_List_LiveStream_Fragment01_Model;
import com.example.livestream_update.R;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator2;

public class MainActivity_List_LiveStream_Fragment01 extends Fragment {

    private RecyclerView recyclerView;
    private MainActivity_List_LiveStream_Fragment01_Adapter adapter;
    private List<MainActivity_List_LiveStream_Fragment01_Model> itemList;
    private CircleIndicator2 circleIndicator;
    private LinearLayoutManager layoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rm_activity_main_list_live_stream_fragment01, container, false);

        // Ánh xạ RecyclerView
        recyclerView = view.findViewById(R.id.rrm_activity_main_list_live_stream_fragment01_rv_01);
        circleIndicator = view.findViewById(R.id.rrm_activity_main_list_live_stream_fragment01_ci_01);

        // Cấu hình RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Khởi tạo danh sách dữ liệu
        itemList = new ArrayList<>();
        itemList.add(new MainActivity_List_LiveStream_Fragment01_Model(R.drawable.rm_error, "Live Stream 1", 100));
        itemList.add(new MainActivity_List_LiveStream_Fragment01_Model(R.drawable.rm_error, "Live Stream 2", 250));
        itemList.add(new MainActivity_List_LiveStream_Fragment01_Model(R.drawable.rm_error, "Live Stream 3", 300));
        itemList.add(new MainActivity_List_LiveStream_Fragment01_Model(R.drawable.rm_error, "Live Stream 1", 100));
        itemList.add(new MainActivity_List_LiveStream_Fragment01_Model(R.drawable.rm_error, "Live Stream 2", 250));
        itemList.add(new MainActivity_List_LiveStream_Fragment01_Model(R.drawable.rm_error, "Live Stream 3", 300));
        itemList.add(new MainActivity_List_LiveStream_Fragment01_Model(R.drawable.rm_error, "Live Stream 1", 100));
        itemList.add(new MainActivity_List_LiveStream_Fragment01_Model(R.drawable.rm_error, "Live Stream 2", 250));
        itemList.add(new MainActivity_List_LiveStream_Fragment01_Model(R.drawable.rm_error, "Live Stream 3", 300));

        // Gán Adapter
        adapter = new MainActivity_List_LiveStream_Fragment01_Adapter(itemList, recyclerView);
        recyclerView.setAdapter(adapter);

        // Bắt đầu tự động cuộn
        adapter.startAutoScroll();

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

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null) {
            adapter.stopAutoScroll();
        }
    }

    // Cập nhật CircleIndicator2 khi cuộn RecyclerView
    private void updateIndicator(int position) {
        int realPosition = position % itemList.size();
        circleIndicator.animatePageSelected(realPosition);
    }
}
