package com.example.livestream_update.Ringme.LiveStream.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vtm.R;
import com.vtm.ringme.livestream.holder.LivestreamGiftHolder;
import com.vtm.ringme.livestream.model.Gift;
import com.vtm.ringme.livestream.socket.ListenerV2;

import java.util.List;

import io.reactivex.annotations.NonNull;

public class LivestreamGiftAdapter extends RecyclerView.Adapter<LivestreamGiftHolder> {
    List<Gift> gifts;

    ListenerV2.DonateListener listener;

    public void setListener(ListenerV2.DonateListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Gift> gifts) {
        this.gifts = gifts;
        notifyDataSetChanged();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void addData(List<Gift> gifts) {
        this.gifts.addAll(gifts);
        notifyDataSetChanged();
    }

    @NonNull
    @androidx.annotation.NonNull
    @Override
    public LivestreamGiftHolder onCreateViewHolder(@NonNull @androidx.annotation.NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rm_holder_livestream_gift, viewGroup, false);
// ImageView ivBg = view.findViewById(R.id.img_gift);
// ViewGroup.LayoutParams layoutParams = ivBg.getLayoutParams();
// layoutParams.width = layoutParams.width / 6;
// ivBg.setLayoutParams(layoutParams);
        return new LivestreamGiftHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @androidx.annotation.NonNull LivestreamGiftHolder livestreamGiftHolder, int i) {
        Gift gift = gifts.get(i);
        Glide.with(livestreamGiftHolder.itemView.getContext()).load(gift.getAvatar()).into(livestreamGiftHolder.getImgGift());
        livestreamGiftHolder.getTvPrice().setText(String.valueOf(gift.getAmountStar()));
        livestreamGiftHolder.getTvGiftName().setText(gift.getName());
        if (gift.isChosen()) {
                livestreamGiftHolder.getGiftLayout().setBackgroundResource(R.drawable.rm_layout_gift_selected);
        } else {
            livestreamGiftHolder.getGiftLayout().setBackgroundResource(R.drawable.rm_layout_gift_unselected);
        }
        if (listener != null) {
            livestreamGiftHolder.getGiftLayout().setOnClickListener(v -> listener.onDonateClick(gift));
        }
    }

    @Override
    public int getItemCount() {
        return gifts == null ? 0 : gifts.size();
    }
}
