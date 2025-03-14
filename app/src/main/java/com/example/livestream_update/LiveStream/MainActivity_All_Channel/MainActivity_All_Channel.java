package com.example.livestream_update.LiveStream.MainActivity_All_Channel;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.livestream_update.R;
import java.util.ArrayList;
import java.util.List;

public class MainActivity_All_Channel extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MainActivity_All_Channel_Adapter adapter;
    private List<MainActivity_All_Channel_Model> channelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rm_activity_main_all_channel);

        recyclerView = findViewById(R.id.rm_activity_main_list_live_stream_rv_01);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Thêm dữ liệu mẫu
        channelList = new ArrayList<>();
        channelList.add(new MainActivity_All_Channel_Model("KaKoak", "8k Followers"));
        channelList.add(new MainActivity_All_Channel_Model("LiveStream User 2", "10k Followers"));
        channelList.add(new MainActivity_All_Channel_Model("Streamer X", "5.5k Followers"));
        channelList.add(new MainActivity_All_Channel_Model("KaKoak", "8k Followers"));
        channelList.add(new MainActivity_All_Channel_Model("LiveStream User 2", "10k Followers"));
        channelList.add(new MainActivity_All_Channel_Model("Streamer X", "5.5k Followers"));
        channelList.add(new MainActivity_All_Channel_Model("KaKoak", "8k Followers"));
        channelList.add(new MainActivity_All_Channel_Model("LiveStream User 2", "10k Followers"));
        channelList.add(new MainActivity_All_Channel_Model("Streamer X", "5.5k Followers"));
        channelList.add(new MainActivity_All_Channel_Model("KaKoak", "8k Followers"));
        channelList.add(new MainActivity_All_Channel_Model("LiveStream User 2", "10k Followers"));
        channelList.add(new MainActivity_All_Channel_Model("Streamer X", "5.5k Followers"));

        adapter = new MainActivity_All_Channel_Adapter(channelList);
        recyclerView.setAdapter(adapter);
    }
}
