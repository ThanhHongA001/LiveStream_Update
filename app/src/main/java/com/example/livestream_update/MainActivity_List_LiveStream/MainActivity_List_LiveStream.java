package com.example.livestream_update.MainActivity_List_LiveStream;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livestream_update.MainActivity_Button;
import com.example.livestream_update.MainActivity_Home.MainActivity_Home;
import com.example.livestream_update.MainActivity_List_LiveStream.Fragment.MainActivity_List_LiveStream_Fragment01;
import com.example.livestream_update.MainActivity_List_LiveStream.Fragment.MainActivity_List_LiveStream_Fragment02;
import com.example.livestream_update.MainActivity_List_LiveStream.Fragment.MainActivity_List_LiveStream_Fragment03;
import com.example.livestream_update.MainActivity_Star.MainActivity_Star;
import com.example.livestream_update.R;
import java.util.Arrays;
import java.util.List;

public class MainActivity_List_LiveStream extends AppCompatActivity {

    private ImageFilterView rm_activity_main_list_live_stream_iv_01,rm_activity_main_list_live_stream_iv_02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rm_activity_main_list_live_stream);

        RecyclerView recyclerView = findViewById(R.id.rm_activity_main_list_live_stream_rv_01);

        // Danh sách các Fragment cần hiển thị
        List<Fragment> fragments_list_livestream = Arrays.asList(
                new MainActivity_List_LiveStream_Fragment01(),
                new MainActivity_List_LiveStream_Fragment02(),
                new MainActivity_List_LiveStream_Fragment03()
        );

        rm_activity_main_list_live_stream_iv_01 = findViewById(R.id.rm_activity_main_list_live_stream_iv_01);
        rm_activity_main_list_live_stream_iv_01.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity_List_LiveStream.this, MainActivity_Home.class);
            startActivity(intent);
            finish();
        });

        rm_activity_main_list_live_stream_iv_02 = findViewById(R.id.rm_activity_main_list_live_stream_iv_02);
        rm_activity_main_list_live_stream_iv_02.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity_List_LiveStream.this, MainActivity_Star.class);
            startActivity(intent);
            finish();
        });

        // Thiết lập Adapter
        MainActivity_List_LiveStream_Adapter adapter = new MainActivity_List_LiveStream_Adapter(this, fragments_list_livestream);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
}
