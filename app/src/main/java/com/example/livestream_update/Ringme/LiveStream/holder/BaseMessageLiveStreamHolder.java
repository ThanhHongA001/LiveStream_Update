package com.example.livestream_update.Ringme.LiveStream.holder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public abstract class BaseMessageLiveStreamHolder extends RecyclerView.ViewHolder {

    private View itemView;

    public BaseMessageLiveStreamHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public abstract void setElement(Object obj, int pos);
}
