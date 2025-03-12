package com.example.livestream_update.MainActivity_Home;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livestream_update.MainActivity_Home.Fragment.MainActivity_Home_Fragment01;
import com.example.livestream_update.MainActivity_Home.Fragment.MainActivity_Home_Fragment02;
import com.example.livestream_update.MainActivity_Home.Fragment.MainActivity_Home_Fragment03;
import com.example.livestream_update.MainActivity_Home.Fragment.MainActivity_Home_Fragment04;
import com.example.livestream_update.R;

import java.util.Arrays;
import java.util.List;

public class MainActivity_Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rm_activity_main_home);

        // Tìm RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rm_activity_main_home_recyclerview);

        // Danh sách các Fragment cần hiển thị
        List<Fragment> fragments_home = Arrays.asList(
                new MainActivity_Home_Fragment01(),
                new MainActivity_Home_Fragment02(),
                new MainActivity_Home_Fragment03(),
                new MainActivity_Home_Fragment04()
        );

        // Thiết lập Adapter cho RecyclerView
        MainActivity_Home_Adapter adapter = new MainActivity_Home_Adapter(this, fragments_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
}
