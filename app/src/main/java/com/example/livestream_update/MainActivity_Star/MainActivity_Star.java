package com.example.livestream_update.MainActivity_Star;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.livestream_update.R;

public class MainActivity_Star extends AppCompatActivity {

    private Button btnTopUp; // Nút Top Up

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.rm_activity_main_star);

        // Ánh xạ ID
        btnTopUp = findViewById(R.id.rm_activity_main_star_btn_12);

        // Xử lý sự kiện khi nhấn nút "Top Up"
        btnTopUp.setOnClickListener(v -> {
            // Hiển thị DialogFragment
            MainActivity_Star_Fragment01 dialog = new MainActivity_Star_Fragment01();
            dialog.show(getSupportFragmentManager(), "StarFragmentDialog");
        });
    }
}
