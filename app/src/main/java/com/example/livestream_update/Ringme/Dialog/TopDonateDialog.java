package com.example.livestream_update.Ringme.Dialog;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vtm.databinding.RmDialogTopDonateBinding;
import com.vtm.ringme.livestream.activity.LivestreamDetailActivity;
import com.vtm.ringme.livestream.adapter.TopDonateAdapter;
import com.vtm.ringme.livestream.model.TopDonate;

import java.util.ArrayList;


public class TopDonateDialog extends BottomSheetDialog {
    private final LivestreamDetailActivity context;
    private final ArrayList<TopDonate> listSenders;
    private final RmDialogTopDonateBinding binding;
    private TopDonateAdapter topDonateAdapter;
    public TopDonateDialog(@NonNull LivestreamDetailActivity context, ArrayList<TopDonate> listSenders) {
        super(context);
        binding = RmDialogTopDonateBinding.inflate(LayoutInflater.from(context));
        this.context = context;
        this.listSenders = listSenders;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        initListSender();
        binding.btnBack.setOnClickListener(view -> dismiss());
        binding.btnSendStars.setOnClickListener(view -> {
            context.openDialogSendStars();
            dismiss();
        });
    }

    private void initListSender() {
        binding.rcvListSenders.setLayoutManager(new LinearLayoutManager(context));
        topDonateAdapter = new TopDonateAdapter(context);
        topDonateAdapter.setList(listSenders);
        binding.rcvListSenders.setAdapter(topDonateAdapter);
    }
}
