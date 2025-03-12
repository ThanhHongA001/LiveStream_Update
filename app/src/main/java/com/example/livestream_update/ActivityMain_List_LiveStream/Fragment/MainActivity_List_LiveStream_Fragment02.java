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

import com.example.livestream_update.ActivityMain_List_LiveStream.Fragment_Adapter.MainActivity_List_LiveStream_Fragment02_Adapter;
import com.example.livestream_update.ActivityMain_List_LiveStream.Fragment_Adapter.MainActivity_List_LiveStream_Fragment02_Model;
import com.example.livestream_update.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_List_LiveStream_Fragment02 extends Fragment {

    private RecyclerView recyclerView;
    private MainActivity_List_LiveStream_Fragment02_Adapter adapter;
    private List<MainActivity_List_LiveStream_Fragment02_Model> dataList;

    public MainActivity_List_LiveStream_Fragment02() {
        // Constructor rỗng
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Gắn layout của Fragment
        View view = inflater.inflate(R.layout.rm_activity_main_list_live_stream_fragment02, container, false);

        // Ánh xạ RecyclerView
        recyclerView = view.findViewById(R.id.rm_activity_main_list_live_stream_fragment02_rv_01);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Khởi tạo danh sách dữ liệu
        dataList = new ArrayList<>();
        dataList.add(new MainActivity_List_LiveStream_Fragment02_Model(R.drawable.rm_error, "LiveStream 1", "1.2K View"));
        dataList.add(new MainActivity_List_LiveStream_Fragment02_Model(R.drawable.rm_error, "LiveStream 2", "850 View"));
        dataList.add(new MainActivity_List_LiveStream_Fragment02_Model(R.drawable.rm_error, "LiveStream 3", "500 View"));
        dataList.add(new MainActivity_List_LiveStream_Fragment02_Model(R.drawable.rm_error, "LiveStream 1", "1.2K View"));
        dataList.add(new MainActivity_List_LiveStream_Fragment02_Model(R.drawable.rm_error, "LiveStream 2", "850 View"));
        dataList.add(new MainActivity_List_LiveStream_Fragment02_Model(R.drawable.rm_error, "LiveStream 3", "500 View"));
        dataList.add(new MainActivity_List_LiveStream_Fragment02_Model(R.drawable.rm_error, "LiveStream 1", "1.2K View"));
        dataList.add(new MainActivity_List_LiveStream_Fragment02_Model(R.drawable.rm_error, "LiveStream 2", "850 View"));
        dataList.add(new MainActivity_List_LiveStream_Fragment02_Model(R.drawable.rm_error, "LiveStream 3", "500 View"));

        // Khởi tạo Adapter và gán vào RecyclerView
        adapter = new MainActivity_List_LiveStream_Fragment02_Adapter(dataList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
