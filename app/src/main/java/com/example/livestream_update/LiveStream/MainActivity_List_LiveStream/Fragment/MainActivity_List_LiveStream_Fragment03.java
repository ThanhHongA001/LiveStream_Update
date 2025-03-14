package com.example.livestream_update.LiveStream.MainActivity_List_LiveStream.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.livestream_update.LiveStream.MainActivity_List_LiveStream.Fragment_Adapter.MainActivity_List_LiveStream_Fragment03_Adapter;
import com.example.livestream_update.LiveStream.MainActivity_List_LiveStream.Fragment_Model.MainActivity_List_LiveStream_Fragment03_Model;
import com.example.livestream_update.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_List_LiveStream_Fragment03 extends Fragment {

    private RecyclerView recyclerView;
    private MainActivity_List_LiveStream_Fragment03_Adapter adapter;
    private List<MainActivity_List_LiveStream_Fragment03_Model> itemList;

    public MainActivity_List_LiveStream_Fragment03() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rm_activity_main_list_live_stream_fragment03, container, false);
        recyclerView = view.findViewById(R.id.rrm_activity_main_list_live_stream_fragment03_rv_01);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Khởi tạo danh sách item
        itemList = new ArrayList<>();
        itemList.add(new MainActivity_List_LiveStream_Fragment03_Model(R.drawable.rm_ic_home_invite, "Loyalty"));
        itemList.add(new MainActivity_List_LiveStream_Fragment03_Model(R.drawable.rm_ic_home_invite, "Friendship"));
        itemList.add(new MainActivity_List_LiveStream_Fragment03_Model(R.drawable.rm_ic_home_invite, "Respect"));
        itemList.add(new MainActivity_List_LiveStream_Fragment03_Model(R.drawable.rm_ic_home_invite, "Loyalty"));
        itemList.add(new MainActivity_List_LiveStream_Fragment03_Model(R.drawable.rm_ic_home_invite, "Friendship"));
        itemList.add(new MainActivity_List_LiveStream_Fragment03_Model(R.drawable.rm_ic_home_invite, "Respect"));
        itemList.add(new MainActivity_List_LiveStream_Fragment03_Model(R.drawable.rm_ic_home_invite, "Loyalty"));
        itemList.add(new MainActivity_List_LiveStream_Fragment03_Model(R.drawable.rm_ic_home_invite, "Friendship"));
        itemList.add(new MainActivity_List_LiveStream_Fragment03_Model(R.drawable.rm_ic_home_invite, "Respect"));

        // Gán Adapter
        adapter = new MainActivity_List_LiveStream_Fragment03_Adapter(itemList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
