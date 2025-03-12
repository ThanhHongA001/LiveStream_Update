package com.example.livestream_update.MainActivity_Home.Fragment_Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livestream_update.MainActivity_Home.Fragment_Model.MainActivity_Home_Fragment01_Model;
import com.example.livestream_update.R;

import java.util.List;

public class MainActivity_Home_Fragment01_Adapter extends RecyclerView.Adapter<MainActivity_Home_Fragment01_Adapter.ViewHolder> {

    private List<MainActivity_Home_Fragment01_Model> itemList;
    private Context context;

    public MainActivity_Home_Fragment01_Adapter(Context context, List<MainActivity_Home_Fragment01_Model> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rm_activity_item01, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int realPosition = position % itemList.size();
        holder.imageView.setImageResource(itemList.get(realPosition).getImageResId());
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.rm_activity_item01_iv_01);
        }
    }

    // Lấy vị trí thực tế trong danh sách
    public int getRealPosition(int position) {
        return position % itemList.size();
    }
}
