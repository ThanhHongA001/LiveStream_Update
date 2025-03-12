package com.example.livestream_update.MainActivity_Home.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livestream_update.MainActivity_Home.Fragment_Adapter.MainActivity_Home_Fragment04_Adapter;
import com.example.livestream_update.MainActivity_Home.Fragment_Model.MainActivity_Home_Fragment04_Model;
import com.example.livestream_update.MainActivity_Button;
import com.example.livestream_update.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_Home_Fragment04 extends Fragment {

    private RecyclerView recyclerView;
    private MainActivity_Home_Fragment04_Adapter adapter;
    private List<MainActivity_Home_Fragment04_Model> itemList;

    private Button btnSeeAll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rm_activity_main_home_fragment04, container, false);

        // Ánh xạ nút bấm
        btnSeeAll = view.findViewById(R.id.rm_fragment_activity_main_home_04_btn_01);

        // Sự kiện click để chuyển sang MainActivity_Home
        btnSeeAll.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity_Button.class);
            startActivity(intent);
        });

        recyclerView = view.findViewById(R.id.rm_fragment_activity_main_home_04_rv_01);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Tạo danh sách item
        itemList = new ArrayList<>();
        itemList.add(new MainActivity_Home_Fragment04_Model(R.drawable.rm_img_maskgroup_home, "LiveStream 1", 100));
        itemList.add(new MainActivity_Home_Fragment04_Model(R.drawable.rm_img_maskgroup_home, "LiveStream 2", 200));
        itemList.add(new MainActivity_Home_Fragment04_Model(R.drawable.rm_img_maskgroup_home, "LiveStream 3", 300));
        itemList.add(new MainActivity_Home_Fragment04_Model(R.drawable.rm_img_maskgroup_home, "LiveStream 1", 100));
        itemList.add(new MainActivity_Home_Fragment04_Model(R.drawable.rm_img_maskgroup_home, "LiveStream 2", 200));
        itemList.add(new MainActivity_Home_Fragment04_Model(R.drawable.rm_img_maskgroup_home, "LiveStream 3", 300));
        itemList.add(new MainActivity_Home_Fragment04_Model(R.drawable.rm_img_maskgroup_home, "LiveStream 1", 100));
        itemList.add(new MainActivity_Home_Fragment04_Model(R.drawable.rm_img_maskgroup_home, "LiveStream 2", 200));
        itemList.add(new MainActivity_Home_Fragment04_Model(R.drawable.rm_img_maskgroup_home, "LiveStream 3", 300));

        // Thiết lập adapter
        adapter = new MainActivity_Home_Fragment04_Adapter(itemList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
