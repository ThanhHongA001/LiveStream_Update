package com.example.livestream_update.Ringme.LiveStream.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vtm.R;

public class LivestreamGiftHolder extends RecyclerView.ViewHolder {
    private ImageView imgGift;
    private TextView tvPrice, tvGiftName;

    public RelativeLayout getGiftLayout() {
        return giftLayout;
    }

    public void setGiftLayout(RelativeLayout giftLayout) {
        this.giftLayout = giftLayout;
    }

    private RelativeLayout giftLayout;

    public ImageView getImgGift() {
        return imgGift;
    }

    public void setImgGift(ImageView imgGift) {
        this.imgGift = imgGift;
    }

    public TextView getTvPrice() {
        return tvPrice;
    }

    public void setTvPrice(TextView tvPrice) {
        this.tvPrice = tvPrice;
    }

    public TextView getTvGiftName() {
        return tvGiftName;
    }

    public void setTvGiftName(TextView tvGiftName) {
        this.tvGiftName = tvGiftName;
    }

    public LivestreamGiftHolder(View itemView) {
        super(itemView);
        imgGift = itemView.findViewById(R.id.img_gift);
        tvPrice = itemView.findViewById(R.id.price);
        giftLayout = itemView.findViewById(R.id.gift_layout);
        tvGiftName = itemView.findViewById(R.id.tv_gift_name);
    }
}