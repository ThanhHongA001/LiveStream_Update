package com.example.livestream_update.LiveStream.MainActivity_Channel_Detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.livestream_update.R;
import java.util.List;

public class MainActivity_Channel_Detail_Fragment02_Adapter extends RecyclerView.Adapter<MainActivity_Channel_Detail_Fragment02_Adapter.ViewHolder> {

    private List<MainActivity_Channel_Detail_Fragment02_Model> itemList;

    // Constructor
    public MainActivity_Channel_Detail_Fragment02_Adapter(List<MainActivity_Channel_Detail_Fragment02_Model> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rm_activity_item06, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainActivity_Channel_Detail_Fragment02_Model item = itemList.get(position);
        holder.titleTextView.setText(item.getTitle());
        holder.descriptionTextView.setText(item.getDescription());
        holder.timeTextView.setText(item.getTime());
        holder.imageView.setImageResource(item.getImageResource());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, timeTextView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.rm_activity_item06_tv_01);
            descriptionTextView = itemView.findViewById(R.id.rm_activity_item06_tv_02);
            timeTextView = itemView.findViewById(R.id.rm_activity_item06_tv_04);
            imageView = itemView.findViewById(R.id.rm_activity_item06_iv_01);
        }
    }
}
