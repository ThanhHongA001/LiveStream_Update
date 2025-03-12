package com.example.livestream_update;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.livestream_update.MainActivity_Home.Fragment.MainActivity_Home_Fragment01;
import com.example.livestream_update.MainActivity_Home.Fragment.MainActivity_Home_Fragment02;
import com.example.livestream_update.MainActivity_Home.Fragment.MainActivity_Home_Fragment03;
import com.example.livestream_update.MainActivity_Home.Fragment.MainActivity_Home_Fragment04;

import com.example.livestream_update.MainActivity_Home.MainActivity_Home;

import com.example.livestream_update.MainActivity_List_LiveStream.Fragment.MainActivity_List_LiveStream_Fragment01;
import com.example.livestream_update.MainActivity_List_LiveStream.Fragment.MainActivity_List_LiveStream_Fragment02;
import com.example.livestream_update.MainActivity_List_LiveStream.Fragment.MainActivity_List_LiveStream_Fragment03;

import com.example.livestream_update.MainActivity_List_LiveStream.MainActivity_List_LiveStream;
import com.example.livestream_update.MainActivity_Star.MainActivity_Star;
import com.example.livestream_update.MainActivity_Star.MainActivity_Star_Fragment01;

public class MainActivity_Button extends AppCompatActivity {

    // Khai báo các Button
    private AppCompatButton btnHomeFragment01, btnHomeFragment02, btnHomeFragment03, btnHomeFragment04, btnHomeFragment05;
    private AppCompatButton btnListLiveStreamFragment01, btnListLiveStreamFragment02, btnListLiveStreamFragment03, btnListLiveStreamFragment04;
    private AppCompatButton btnActivity_Star_Fragment01, btnActivity_Star_Fragment02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);

        // Ánh xạ ID từ XML
        btnHomeFragment01 = findViewById(R.id.Activity_Home_Fragment01);
        btnHomeFragment02 = findViewById(R.id.Activity_Home_Fragment02);
        btnHomeFragment03 = findViewById(R.id.Activity_Home_Fragment03);
        btnHomeFragment04 = findViewById(R.id.Activity_Home_Fragment04);
        btnHomeFragment05 = findViewById(R.id.Activity_Home_Fragment05);

        btnListLiveStreamFragment01 = findViewById(R.id.Activity_List_LiveStream_Fragment01);
        btnListLiveStreamFragment02 = findViewById(R.id.Activity_List_LiveStream_Fragment02);
        btnListLiveStreamFragment03 = findViewById(R.id.Activity_List_LiveStream_Fragment03);
        btnListLiveStreamFragment04 = findViewById(R.id.Activity_List_LiveStream_Fragment04);

        btnActivity_Star_Fragment01 = findViewById(R.id.Activity_Star_Fragment01);
        btnActivity_Star_Fragment02 = findViewById(R.id.Activity_Star_Fragment02);

        // Thiết lập sự kiện onClick cho các Button
        btnHomeFragment01.setOnClickListener(v -> openFragment(new MainActivity_Home_Fragment01()));
        btnHomeFragment02.setOnClickListener(v -> openFragment(new MainActivity_Home_Fragment02()));
        btnHomeFragment03.setOnClickListener(v -> openFragment(new MainActivity_Home_Fragment03()));
        btnHomeFragment04.setOnClickListener(v -> openFragment(new MainActivity_Home_Fragment04()));

        btnListLiveStreamFragment01.setOnClickListener(v -> openFragment(new MainActivity_List_LiveStream_Fragment01()));
        btnListLiveStreamFragment02.setOnClickListener(v -> openFragment(new MainActivity_List_LiveStream_Fragment02()));
        btnListLiveStreamFragment03.setOnClickListener(v -> openFragment(new MainActivity_List_LiveStream_Fragment03()));

        btnActivity_Star_Fragment02.setOnClickListener(v -> openFragment(new MainActivity_Star_Fragment01()));

        // Sự kiện chuyển sang MainActivity_Home khi bấm Activity_Home_Fragment05
        btnHomeFragment05.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity_Button.this, MainActivity_Home.class);
            startActivity(intent);
        });

        // Sự kiện chuyển sang Activity khác nếu cần (chưa có Fragment 05)
        btnListLiveStreamFragment04.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity_Button.this, MainActivity_List_LiveStream.class);
            startActivity(intent);
        });

        // Sự kiện chuyển sang màn MainActivity_Star khi bấm Activity_Star_Fragment01
        btnActivity_Star_Fragment01.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity_Button.this, MainActivity_Star.class);
            startActivity(intent);
        });

    }

    // Hàm mở Fragment mới
    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(android.R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
