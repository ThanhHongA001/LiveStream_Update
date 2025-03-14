package com.example.livestream_update.Ringme.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.vtm.R;
import com.vtm.ringme.base.widget.BaseViewHolder;
import com.vtm.ringme.listener.RecyclerClickListener;
import com.vtm.ringme.model.ItemContextMenu;
import com.vtm.ringme.utils.Log;

import java.util.ArrayList;


public class BottomSheetMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = BottomSheetMenuAdapter.class.getSimpleName();
    private final LayoutInflater mLayoutInflater;
    private final ArrayList<ItemContextMenu> listItem;
    private RecyclerClickListener mRecyclerClickListener;

    public BottomSheetMenuAdapter(ArrayList<ItemContextMenu> listItem, Context context) {
        this.mLayoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listItem = listItem;
    }

    public void setRecyclerClickListener(RecyclerClickListener mRecyclerClickListener) {
        this.mRecyclerClickListener = mRecyclerClickListener;
    }

    @Override
    public int getItemCount() {
        return (listItem != null) ? listItem.size() : 0;
    }

    public Object getItem(int position) {
        return (listItem != null && listItem.size() > position) ? listItem.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        Log.d(TAG, "onCreateViewHolder");
        BaseViewHolder holder;
        View view = mLayoutInflater.inflate(R.layout.rm_holder_bottom_sheet_menu, parent, false);
        holder = new ViewHolder(view);
        holder.setRecyclerClickListener(mRecyclerClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        ((BaseViewHolder) holder).setViewClick(position, getItem(position));
        ((ViewHolder) holder).setElement(getItem(position));
    }

    private static class ViewHolder extends BaseViewHolder {
        private final TextView mTvwMessage;
        private final AppCompatImageView mImgIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            mImgIcon = itemView.findViewById(R.id.bottom_sheet_holder_icon);
            mTvwMessage = itemView.findViewById(R.id.bottom_sheet_holder_text);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void setElement(Object obj) {
            try {
                ItemContextMenu mEntry = (ItemContextMenu) obj;
                mTvwMessage.setText(mEntry.getText());
                if (mEntry.getImageRes() != -1) {
                    mImgIcon.setVisibility(View.VISIBLE);
                    mImgIcon.setImageResource(mEntry.getImageRes());
                } else {
                    mImgIcon.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}