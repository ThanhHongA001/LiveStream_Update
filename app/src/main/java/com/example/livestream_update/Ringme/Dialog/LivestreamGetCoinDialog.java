package com.example.livestream_update.Ringme.Dialog;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vtm.R;
import com.vtm.databinding.RmDialogLivestreamGetCoinBinding;
import com.vtm.ringme.livestream.adapter.LivestreamGetCoinAdapter;
import com.vtm.ringme.livestream.model.GetCoin;
import com.vtm.ringme.livestream.socket.ListenerV2;

import java.util.ArrayList;

public class LivestreamGetCoinDialog extends BottomSheetDialog {
    private final Context context;
    private int balance;
    private ArrayList<GetCoin> list;
    private LivestreamGetCoinAdapter livestreamGetCoinAdapter;
    private ListenerV2.GetCoinListener getCoinListener;
    RmDialogLivestreamGetCoinBinding binding;

    public LivestreamGetCoinDialog(@NonNull Context context) {
        super(context);
        binding = RmDialogLivestreamGetCoinBinding.inflate(LayoutInflater.from(context));
        this.context = context;
    }

    public void setBalance(int balance) {
        this.balance = balance;
        binding.tvBalance.setText(context.getResources().getString(R.string.current_coin, balance));
    }

    public void setGetCoinListener(ListenerV2.GetCoinListener getCoinListener) {
        this.getCoinListener = getCoinListener;
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
        initListCoin();
        initView();
    }

    private void initView() {
        binding.tvBalance.setText(context.getResources().getString(R.string.current_coin, balance));
        binding.btnGetCoin.setOnClickListener(v -> {
            for (GetCoin item : list) {
                if (item.isSelected()) {
                    doGetCoin(String.valueOf(item.getNumber()));
                    break;
                }
            }
        });
        livestreamGetCoinAdapter = new LivestreamGetCoinAdapter(context);
//        livestreamGetCoinAdapter.setList(list);
        binding.rcvListCoin.setLayoutManager(new GridLayoutManager(context, 3));
        binding.rcvListCoin.setAdapter(livestreamGetCoinAdapter);
    }

    private void initListCoin() {
        list = new ArrayList<>();
        list.add(new GetCoin(16));
        list.add(new GetCoin(70));
        list.add(new GetCoin(350));
        list.add(new GetCoin(700));
        list.add(new GetCoin(1050));
        list.add(new GetCoin(1400));
    }

    private void doGetCoin(String amount) {
        getCoinListener.onGetCoinListener(amount);
        cancel();
    }
}
