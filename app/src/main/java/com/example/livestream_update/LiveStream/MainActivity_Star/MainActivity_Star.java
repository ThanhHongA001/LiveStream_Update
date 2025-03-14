package com.example.livestream_update.LiveStream.MainActivity_Star;

import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.livestream_update.R;

public class MainActivity_Star extends AppCompatActivity {

    private Button btnTopUp;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.rm_activity_main_star);

        // Ánh xạ ID
        btnTopUp = findViewById(R.id.rm_activity_main_star_btn_12);
        btnTopUp.setOnClickListener(v -> {
            // Hiển thị DialogFragment
            MainActivity_Star_Fragment01 dialog = new MainActivity_Star_Fragment01();
            dialog.show(getSupportFragmentManager(), "StarFragmentDialog");
        });

    }
}
