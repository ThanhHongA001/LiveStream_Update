package com.example.livestream_update.Ringme.Base.widget;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.vtm.ringme.listener.RecyclerClickListener;


public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    private boolean isSelectMode = false;
    private RecyclerClickListener mRecyclerClickListener;
    private final View itemView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public boolean isSelectMode() {
        return isSelectMode;
    }

    public void setSelectMode(boolean selectMode) {
        isSelectMode = selectMode;
    }

    public void setRecyclerClickListener(RecyclerClickListener mRecyclerClickListener) {
        this.mRecyclerClickListener = mRecyclerClickListener;
    }

    public RecyclerClickListener getRecyclerClickListener() {
        return mRecyclerClickListener;
    }

    public abstract void setElement(Object obj);

    public View getItemView() {
        return itemView;
    }

    public void setViewClick(final int position, final Object object) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecyclerClickListener != null) {
                    mRecyclerClickListener.onClick(v, position, object);
                }
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mRecyclerClickListener != null) {
                    mRecyclerClickListener.onLongClick(v, position, object);
                    return true;
                }
                return false;
            }
        });
    }
}

