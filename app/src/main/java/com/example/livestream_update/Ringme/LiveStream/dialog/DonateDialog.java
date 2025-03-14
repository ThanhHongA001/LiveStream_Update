package com.example.livestream_update.Ringme.LiveStream.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vtm.R;
import com.vtm.ringme.livestream.adapter.LivestreamGiftAdapter;
import com.vtm.ringme.livestream.listener.OnEndlessScrollListener;
import com.vtm.ringme.livestream.model.Gift;
import com.vtm.ringme.livestream.socket.ListenerV2;

import java.util.List;

public class DonateDialog {
    Dialog dialog;
    RecyclerView donateList;
    TextView tvGetCoin;
    TextView tvCurrentCoin, tvDonate;

    Activity activity;
    LivestreamGiftAdapter donateAdapter;

    ListenerV2.DonateListener listener;
    ListenerV2.OpenGetCoinDialogListener getCoinListener;
    ListenerV2.LoadMoreGift loadMoreGiftListener;

    private boolean isLeftData;
    private boolean isLoading;

    public void setGetCoinListener(ListenerV2.OpenGetCoinDialogListener getCoinListener) {
        this.getCoinListener = getCoinListener;
    }

    public void setLoadMoreGiftListener(ListenerV2.LoadMoreGift loadMoreGiftListener) {
        this.loadMoreGiftListener = loadMoreGiftListener;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    int currentGiftPosition;

    List<Gift> gifts;
    int currentCoin;

    public void setCurrentCoin(int currentCoin) {
        this.currentCoin = currentCoin;
        tvCurrentCoin.setText(activity.getResources().getString(R.string.current_coin, currentCoin));
    }

    public void setGifts(List<Gift> gifts) {
        this.gifts = gifts;
        donateAdapter.setData(gifts);
    }

    public void addGifts(List<Gift> gifts) {
        donateAdapter.addData(gifts);
        if (gifts.size() < 10)
            isLeftData = false;
        isLoading = false;
    }

    public DonateDialog(Activity activity) {
        this.currentCoin = 0;
        this.activity = activity;
        currentGiftPosition = -1;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.rm_dialog_donate);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setAttributes(layoutParams);

        this.isLeftData = true;
        this.isLoading = false;

        initView(dialog);
    }

    public void setListener(ListenerV2.DonateListener listener) {
        this.listener = listener;
    }

    private void initView(Dialog dialog) {
        donateList = dialog.findViewById(R.id.gifts);
        tvGetCoin = dialog.findViewById(R.id.tv_get_coin);
        tvCurrentCoin = dialog.findViewById(R.id.tv_current_coin);
        tvDonate = dialog.findViewById(R.id.tv_donate);

        tvDonate.setOnClickListener(v -> {
            if (listener != null && currentGiftPosition != -1) {
                listener.onDonateClick(gifts.get(currentGiftPosition));
            }
        });

        tvGetCoin.setOnClickListener(v -> {
            if (getCoinListener != null) {
                getCoinListener.onOpenGetCoinDialogClick();
            }
        });

        activity.getResources().getString(R.string.current_coin, currentCoin);
        donateAdapter = new LivestreamGiftAdapter();
        donateAdapter.setData(gifts);
        donateAdapter.setListener(gift -> {
            if (gift.getAmountStar() > currentCoin) {
                Toast.makeText(activity, activity.getResources().getString(R.string.livestream_not_enough_coin), Toast.LENGTH_SHORT).show();
                return;
            }
            if (currentGiftPosition == gifts.indexOf(gift)) {
                return;
            }
            if (currentGiftPosition != -1) {
                gifts.get(currentGiftPosition).setChosen(false);
                donateAdapter.notifyItemChanged(currentGiftPosition);
            }
            currentGiftPosition = gifts.indexOf(gift);
            gifts.get(currentGiftPosition).setChosen(true);
            donateAdapter.notifyItemChanged(currentGiftPosition);
            setDonateButton();
        });
        donateList.setLayoutManager(new GridLayoutManager(activity, 2, LinearLayoutManager.HORIZONTAL, false));
        donateList.setAdapter(donateAdapter);
        donateList.addOnScrollListener(new OnEndlessScrollListener(5) {
            @Override
            public void onLoadNextPage(View view) {
                super.onLoadNextPage(view);
                if (isLeftData && !isLoading && gifts.size() >= 10) {
                    loadMoreGiftListener.onLoadMoreGift(gifts.size() / 10);
                    isLoading = true;
                }
            }
        });
        setDonateButton();
    }

    public void show() {
        if (dialog.isShowing()) {
            return;
        }
        dialog.show();
    }

    public void close() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void setDonateButton() {
        if (currentGiftPosition != -1) {
            tvDonate.setBackgroundResource(R.drawable.rm_layout_button_exit);
        } else {
            tvDonate.setBackgroundResource(R.drawable.rm_layout_button_disable);
        }
    }
}

