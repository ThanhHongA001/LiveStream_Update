package com.example.livestream_update.ActivityMain_Home.Fragment_Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livestream_update.ActivityMain_Home.Fragment_Model.MainActivity_Home_Fragment02_Model;
import com.example.livestream_update.R;

import java.util.List;

public class MainActivity_Home_Fragment02_Adapter extends RecyclerView.Adapter<MainActivity_Home_Fragment02_Adapter.ViewHolder> {
    private List<MainActivity_Home_Fragment02_Model> itemList;

    // Constructor nhận danh sách dữ liệu
    public MainActivity_Home_Fragment02_Adapter(List<MainActivity_Home_Fragment02_Model> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rm_activity_item02, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainActivity_Home_Fragment02_Model item = itemList.get(position);

        // Gán dữ liệu vào các view
        holder.textViewTitle.setText(item.getTitle());
        holder.textViewViewCount.setText(item.getViewCount());
        holder.imageViewThumbnail.setImageResource(item.getImageResource());

        // Xử lý sự kiện click vào CardView
        holder.cardView.setOnClickListener(v -> {
            // Viết code xử lý khi nhấn vào livestream
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageViewThumbnail, imageViewViewIcon;
        TextView textViewTitle, textViewViewCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.rm_activity_item02_cv_01);
            imageViewThumbnail = itemView.findViewById(R.id.rm_activity_item02_iv_01);
            textViewTitle = itemView.findViewById(R.id.rm_activity_item02_tv_01);
            imageViewViewIcon = itemView.findViewById(R.id.rm_activity_item02_iv_02);
            textViewViewCount = itemView.findViewById(R.id.rm_activity_item02_tv_02);
        }
    }
}
