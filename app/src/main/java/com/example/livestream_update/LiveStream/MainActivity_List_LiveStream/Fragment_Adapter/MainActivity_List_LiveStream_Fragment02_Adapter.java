package com.example.livestream_update.LiveStream.MainActivity_List_LiveStream.Fragment_Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livestream_update.LiveStream.MainActivity_List_LiveStream.Fragment_Model.MainActivity_List_LiveStream_Fragment02_Model;
import com.example.livestream_update.R;

import java.util.List;

public class MainActivity_List_LiveStream_Fragment02_Adapter extends RecyclerView.Adapter<MainActivity_List_LiveStream_Fragment02_Adapter.ViewHolder> {

    private List<MainActivity_List_LiveStream_Fragment02_Model> dataList;

    public MainActivity_List_LiveStream_Fragment02_Adapter(List<MainActivity_List_LiveStream_Fragment02_Model> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Gắn layout rm_activity_item02 vào ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rm_activity_item02, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Lấy dữ liệu tại vị trí hiện tại
        MainActivity_List_LiveStream_Fragment02_Model item = dataList.get(position);

        // Gán dữ liệu vào giao diện
        holder.imageView.setImageResource(item.getImageResId());
        holder.textViewStreamName.setText(item.getStreamName());
        holder.textViewViewCount.setText(item.getViewCount());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewStreamName, textViewViewCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.rm_activity_item02_iv_01);
            textViewStreamName = itemView.findViewById(R.id.rm_activity_item02_tv_01);
            textViewViewCount = itemView.findViewById(R.id.rm_activity_item02_tv_02);
        }
    }
}
