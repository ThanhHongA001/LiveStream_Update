package com.example.livestream_update.Ringme.TabVideo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vtm.ringme.ApplicationController;


/**
 * Created by tuanha00 on 3/22/2018.
 */

public class BaseActivity extends AppCompatActivity {

    protected boolean runnable;

    protected ApplicationController application;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = ApplicationController.self();

        runnable = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        runnable = false;
    }

    public ApplicationController getApplicationController() {
        return application;
    }
}
