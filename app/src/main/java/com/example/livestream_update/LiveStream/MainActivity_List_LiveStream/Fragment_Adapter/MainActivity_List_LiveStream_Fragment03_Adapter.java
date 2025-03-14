package com.example.livestream_update.LiveStream.MainActivity_List_LiveStream.Fragment_Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livestream_update.LiveStream.MainActivity_List_LiveStream.Fragment_Model.MainActivity_List_LiveStream_Fragment03_Model;
import com.example.livestream_update.R;

import java.util.List;

public class MainActivity_List_LiveStream_Fragment03_Adapter extends RecyclerView.Adapter<MainActivity_List_LiveStream_Fragment03_Adapter.ViewHolder> {
    private List<MainActivity_List_LiveStream_Fragment03_Model> itemList;

    // Constructor
    public MainActivity_List_LiveStream_Fragment03_Adapter(List<MainActivity_List_LiveStream_Fragment03_Model> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rm_activity_item05, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainActivity_List_LiveStream_Fragment03_Model item = itemList.get(position);
        holder.imageView.setImageResource(item.getImageResource());
        holder.textView.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.rm_activity_item05_iv_01);
            textView = itemView.findViewById(R.id.rm_activity_item05_tv_01);
        }
    }
}
