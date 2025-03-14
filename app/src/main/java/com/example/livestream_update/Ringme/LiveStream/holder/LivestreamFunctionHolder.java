package com.example.livestream_update.Ringme.LiveStream.holder;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.vtm.R;

public class LivestreamFunctionHolder extends RecyclerView.ViewHolder {
    private AppCompatImageView imgFunction, imgLiked;
    private View divider;

    public LivestreamFunctionHolder(View itemView) {
        super(itemView);
        imgFunction = itemView.findViewById(R.id.img_function);
        imgLiked = itemView.findViewById(R.id.img_liked);
        divider = itemView.findViewById(R.id.divider);
    }

    public AppCompatImageView getImgFunction() {
        return imgFunction;
    }

    public AppCompatImageView getImgLiked() {
        return imgLiked;
    }

    public View getDivider() {
        return divider;
    }
}
