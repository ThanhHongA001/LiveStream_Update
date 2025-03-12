package com.example.livestream_update.MainActivty_All_LiveStream;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.livestream_update.R;

public class MainActivity_All_LiveStream extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.rm_activity_main_all_live_stream);


    }
}
