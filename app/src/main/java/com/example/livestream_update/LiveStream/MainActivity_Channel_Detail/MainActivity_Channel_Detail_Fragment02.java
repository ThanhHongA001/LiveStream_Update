package com.example.livestream_update.LiveStream.MainActivity_Channel_Detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.livestream_update.R;
import java.util.ArrayList;
import java.util.List;

public class MainActivity_Channel_Detail_Fragment02 extends Fragment {
    private RecyclerView recyclerView;
    private MainActivity_Channel_Detail_Fragment02_Adapter adapter;
    private List<MainActivity_Channel_Detail_Fragment02_Model> itemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rm_activity_main_channel_detail_fragment02, container, false);

        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.rm_activity_main_channel_detail_fragment02_rv_01);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Thêm dữ liệu mẫu vào danh sách
        itemList = new ArrayList<>();
        itemList.add(new MainActivity_Channel_Detail_Fragment02_Model("Category 1", "Lorem Ipsum text", "2h ago", R.drawable.rm_default_image));
        itemList.add(new MainActivity_Channel_Detail_Fragment02_Model("Category 2", "Another text", "5h ago", R.drawable.rm_default_image));
        itemList.add(new MainActivity_Channel_Detail_Fragment02_Model("Category 3", "Another ", "7h ago", R.drawable.rm_default_image));

        // Khởi tạo Adapter và kết nối với RecyclerView
        adapter = new MainActivity_Channel_Detail_Fragment02_Adapter(itemList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
