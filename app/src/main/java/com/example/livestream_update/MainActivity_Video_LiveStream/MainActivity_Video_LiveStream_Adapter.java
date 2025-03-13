package com.example.livestream_update.MainActivity_Video_LiveStream;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livestream_update.R;

import java.util.List;

public class MainActivity_Video_LiveStream_Adapter extends RecyclerView.Adapter<MainActivity_Video_LiveStream_Adapter.ViewHolder> {

    private Context context;
    private List<MainActivity_Video_LiveStream_Model> dataList;

    public MainActivity_Video_LiveStream_Adapter(Context context, List<MainActivity_Video_LiveStream_Model> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rm_activity_item06, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainActivity_Video_LiveStream_Model model = dataList.get(position);

        // Gán dữ liệu vào các thành phần giao diện
        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        holder.username.setText(model.getUsername());
        holder.timeAgo.setText(model.getTimeAgo());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, username, timeAgo;
        ImageView thumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Ánh xạ các thành phần từ rm_activity_item06.xml
            title = itemView.findViewById(R.id.rm_activity_item06_tv_01);
            description = itemView.findViewById(R.id.rm_activity_item06_tv_02);
            username = itemView.findViewById(R.id.rm_activity_item06_tv_03);
            timeAgo = itemView.findViewById(R.id.rm_activity_item06_tv_04);
            thumbnail = itemView.findViewById(R.id.rm_activity_item06_iv_01);
        }
    }
}
