package com.example.livestream_update.MainActivity_Star;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.livestream_update.R;

public class MainActivity_Star extends AppCompatActivity {

    private FrameLayout frameLayoutRecharge; // Layout hiển thị nạp tiền
    private Button btnTopUp; // Nút Top Up

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.rm_activity_main_star);

        // Ánh xạ ID
        frameLayoutRecharge = findViewById(R.id.rm_activity_main_star_framelayout_01);
        btnTopUp = findViewById(R.id.rm_activity_main_star_btn_12);

        // Ẩn layout nạp tiền ban đầu
        frameLayoutRecharge.setVisibility(View.GONE);

        // Xử lý sự kiện khi nhấn nút "Top Up"
        btnTopUp.setOnClickListener(v -> {
            // Hiển thị Fragment nạp tiền
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.rm_activity_main_star_framelayout_01, new MainActivity_Star_Fragment01())
                    .commit();

            // Hiện layout
            frameLayoutRecharge.setVisibility(View.VISIBLE);
        });

        // Xử lý sự kiện khi chạm ngoài layout để đóng giao diện nạp tiền
        frameLayoutRecharge.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                frameLayoutRecharge.setVisibility(View.GONE);
                return true;
            }
            return false;
        });
    }
}
