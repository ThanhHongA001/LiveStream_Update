package com.example.livestream_update;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.livestream_update.LiveStream.MainActivity_All_Channel.MainActivity_All_Channel;
import com.example.livestream_update.LiveStream.MainActivity_Channel_Detail.MainActivity_Channel_Detail;
import com.example.livestream_update.LiveStream.MainActivity_Channel_Detail.MainActivity_Channel_Detail_Fragment01;
import com.example.livestream_update.LiveStream.MainActivity_Channel_Detail.MainActivity_Channel_Detail_Fragment02;

import com.example.livestream_update.LiveStream.MainActivity_Home.Fragment.MainActivity_Home_Fragment01;
import com.example.livestream_update.LiveStream.MainActivity_Home.Fragment.MainActivity_Home_Fragment02;
import com.example.livestream_update.LiveStream.MainActivity_Home.Fragment.MainActivity_Home_Fragment03;
import com.example.livestream_update.LiveStream.MainActivity_Home.Fragment.MainActivity_Home_Fragment04;
import com.example.livestream_update.LiveStream.MainActivity_Home.MainActivity_Home;

import com.example.livestream_update.LiveStream.MainActivity_List_LiveStream.Fragment.MainActivity_List_LiveStream_Fragment01;
import com.example.livestream_update.LiveStream.MainActivity_List_LiveStream.Fragment.MainActivity_List_LiveStream_Fragment02;
import com.example.livestream_update.LiveStream.MainActivity_List_LiveStream.Fragment.MainActivity_List_LiveStream_Fragment03;
import com.example.livestream_update.LiveStream.MainActivity_List_LiveStream.MainActivity_List_LiveStream;

import com.example.livestream_update.LiveStream.MainActivity_Star.MainActivity_Star;
import com.example.livestream_update.LiveStream.MainActivity_Star.MainActivity_Star_Fragment01;

import com.example.livestream_update.LiveStream.MainActivity_Video_LiveStream.MainActivity_Video_LiveStream;

public class MainActivity_Button extends AppCompatActivity {

    // Khai báo các Button tương ứng với ID trong XML
    private AppCompatButton btnHomeFragment01, btnHomeFragment02, btnHomeFragment03, btnHomeFragment04, btnHome;
    private AppCompatButton btnListLiveStreamFragment01, btnListLiveStreamFragment02, btnListLiveStreamFragment03, btnListLiveStream;
    private AppCompatButton btnStarFragment01, btnStar;
    private AppCompatButton btnAllLiveStream;
    private AppCompatButton btnAllChannel;
    private AppCompatButton btnChannelDetailFragment01, btnChannelDetailFragment02, btnChannelDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);

        // Ánh xạ ID từ XML
        btnHomeFragment01 = findViewById(R.id.Activity_Home_Fragment01);
        btnHomeFragment02 = findViewById(R.id.Activity_Home_Fragment02);
        btnHomeFragment03 = findViewById(R.id.Activity_Home_Fragment03);
        btnHomeFragment04 = findViewById(R.id.Activity_Home_Fragment04);
        btnHome = findViewById(R.id.Activity_Home);

        btnListLiveStreamFragment01 = findViewById(R.id.Activity_List_LiveStream_Fragment01);
        btnListLiveStreamFragment02 = findViewById(R.id.Activity_List_LiveStream_Fragment02);
        btnListLiveStreamFragment03 = findViewById(R.id.Activity_List_LiveStream_Fragment03);
        btnListLiveStream = findViewById(R.id.Activity_List_LiveStream);

        btnStarFragment01 = findViewById(R.id.Activity_Star_Fragment01);
        btnStar = findViewById(R.id.Activity_Star);

        btnAllLiveStream = findViewById(R.id.Activity_All_List_LiveStream);
        btnAllChannel = findViewById(R.id.Activity_All_Channel);

        btnChannelDetailFragment01 = findViewById(R.id.Activity_Channel_Deatel_Fragment01);
        btnChannelDetailFragment02 = findViewById(R.id.Activity_Channel_Deatel_Fragment02);
        btnChannelDetail = findViewById(R.id.Activity_Channel_Deatel);

        // Thiết lập sự kiện onClick cho các Button
        btnHomeFragment01.setOnClickListener(v -> openFragment(new MainActivity_Home_Fragment01()));
        btnHomeFragment02.setOnClickListener(v -> openFragment(new MainActivity_Home_Fragment02()));
        btnHomeFragment03.setOnClickListener(v -> openFragment(new MainActivity_Home_Fragment03()));
        btnHomeFragment04.setOnClickListener(v -> openFragment(new MainActivity_Home_Fragment04()));
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity_Button.this, MainActivity_Home.class);
            startActivity(intent);
        });

        btnListLiveStreamFragment01.setOnClickListener(v -> openFragment(new MainActivity_List_LiveStream_Fragment01()));
        btnListLiveStreamFragment02.setOnClickListener(v -> openFragment(new MainActivity_List_LiveStream_Fragment02()));
        btnListLiveStreamFragment03.setOnClickListener(v -> openFragment(new MainActivity_List_LiveStream_Fragment03()));
        btnListLiveStream.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity_Button.this, MainActivity_List_LiveStream.class);
            startActivity(intent);
        });

        btnStarFragment01.setOnClickListener(v -> openFragment(new MainActivity_Star_Fragment01()));
        btnStar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity_Button.this, MainActivity_Star.class);
            startActivity(intent);
        });

        btnAllLiveStream.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity_Button.this, MainActivity_Video_LiveStream.class);
            startActivity(intent);
        });

        btnAllChannel.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity_Button.this, MainActivity_All_Channel.class);
            startActivity(intent);
        });

        btnChannelDetailFragment01.setOnClickListener(v -> openFragment(new MainActivity_Channel_Detail_Fragment01()));
        btnChannelDetailFragment02.setOnClickListener(v -> openFragment(new MainActivity_Channel_Detail_Fragment02()));
        btnChannelDetail.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity_Button.this, MainActivity_Channel_Detail.class);
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
