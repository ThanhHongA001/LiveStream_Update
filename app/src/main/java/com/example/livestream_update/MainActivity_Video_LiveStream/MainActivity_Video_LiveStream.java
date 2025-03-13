package com.example.livestream_update.MainActivity_Video_LiveStream;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livestream_update.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_Video_LiveStream extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MainActivity_Video_LiveStream_Adapter adapter;
    private List<MainActivity_Video_LiveStream_Model> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rm_activity_main_video_live_stream);

        // Ánh xạ ID của RecyclerView
        recyclerView = findViewById(R.id.rm_activity_main_all_live_stream_rv_01);

        // Cấu hình LayoutManager cho RecyclerView (hiển thị theo chiều dọc)
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Khởi tạo danh sách dữ liệu
        dataList = new ArrayList<>();
        dataList.add(new MainActivity_Video_LiveStream_Model("Title 1", "Description 1", "User 1", "5h ago"));
        dataList.add(new MainActivity_Video_LiveStream_Model("Title 2", "Description 2", "User 2", "3h ago"));
        dataList.add(new MainActivity_Video_LiveStream_Model("Title 3", "Description 3", "User 3", "1h ago"));
        dataList.add(new MainActivity_Video_LiveStream_Model("Title 1", "Description 1", "User 1", "5h ago"));
        dataList.add(new MainActivity_Video_LiveStream_Model("Title 2", "Description 2", "User 2", "3h ago"));
        dataList.add(new MainActivity_Video_LiveStream_Model("Title 3", "Description 3", "User 3", "1h ago"));
        dataList.add(new MainActivity_Video_LiveStream_Model("Title 1", "Description 1", "User 1", "5h ago"));
        dataList.add(new MainActivity_Video_LiveStream_Model("Title 2", "Description 2", "User 2", "3h ago"));
        dataList.add(new MainActivity_Video_LiveStream_Model("Title 3", "Description 3", "User 3", "1h ago"));


        // Khởi tạo Adapter và gán vào RecyclerView
        adapter = new MainActivity_Video_LiveStream_Adapter(this, dataList);
        recyclerView.setAdapter(adapter);
    }
}
