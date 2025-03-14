package com.example.livestream_update.Ringme.Activities;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.vtm.databinding.RmActivityDonateTermsBinding;


public class DonateTermsActivity extends AppCompatActivity {

    private RmActivityDonateTermsBinding binding;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RmActivityDonateTermsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        binding.pdfView.fromAsset("term.pdf")
//                .password(null)
//                .defaultPage(0)
//                .enableSwipe(true)
//                .enableDoubletap(true)
//                .load();
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.getSettings().setMediaPlaybackRequiresUserGesture(true);
        binding.webView.getSettings().setAllowContentAccess(true);
        binding.webView.loadUrl("http://liveapi.tls.tl/donate-term");

        binding.ivBack.setOnClickListener(v -> onBackPressed());
    }
}