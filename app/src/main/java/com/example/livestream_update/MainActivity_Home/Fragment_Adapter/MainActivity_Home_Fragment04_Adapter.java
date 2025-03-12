package com.example.livestream_update.MainActivity_Home.Fragment_Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livestream_update.MainActivity_Home.Fragment_Model.MainActivity_Home_Fragment04_Model;
import com.example.livestream_update.R;

import java.util.List;

public class MainActivity_Home_Fragment04_Adapter extends RecyclerView.Adapter<MainActivity_Home_Fragment04_Adapter.ViewHolder> {

    private List<MainActivity_Home_Fragment04_Model> itemList;

    public MainActivity_Home_Fragment04_Adapter(List<MainActivity_Home_Fragment04_Model> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rm_activity_item04, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainActivity_Home_Fragment04_Model item = itemList.get(position);

        holder.imageView.setImageResource(item.getImageResource());
        holder.textViewName.setText(item.getLivestreamName());
        holder.textViewViews.setText(item.getViewCount() + " View");
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName, textViewViews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.rm_activity_item04_iv_01);
            textViewName = itemView.findViewById(R.id.rm_activity_item04_tv_01);
            textViewViews = itemView.findViewById(R.id.rm_activity_item04_tv_02);
        }
    }
}
