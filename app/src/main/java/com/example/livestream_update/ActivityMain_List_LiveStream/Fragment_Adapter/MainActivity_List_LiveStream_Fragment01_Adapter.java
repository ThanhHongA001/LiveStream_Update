package com.example.livestream_update.ActivityMain_List_LiveStream.Fragment_Adapter;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livestream_update.R;

import java.util.List;

public class MainActivity_List_LiveStream_Fragment01_Adapter extends RecyclerView.Adapter<MainActivity_List_LiveStream_Fragment01_Adapter.ViewHolder> {

    private List<MainActivity_List_LiveStream_Fragment01_Model> itemList;
    private RecyclerView recyclerView;
    private Handler handler = new Handler();
    private Runnable runnable;

    public MainActivity_List_LiveStream_Fragment01_Adapter(List<MainActivity_List_LiveStream_Fragment01_Model> itemList, RecyclerView recyclerView) {
        this.itemList = itemList;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rm_activity_item01, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int actualPosition = position % itemList.size();
        MainActivity_List_LiveStream_Fragment01_Model item = itemList.get(actualPosition);

        holder.imageView.setImageResource(item.getImageResource());
        holder.textViewName.setText(item.getStreamName());
        holder.textViewViewCount.setText(item.getViewCount() + " View");
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName, textViewViewCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.rm_activity_item01_iv_01);
            textViewName = new TextView(itemView.getContext());
            textViewViewCount = new TextView(itemView.getContext());
        }
    }

    // Auto scroll để tự động vuốt qua các item
    public void startAutoScroll() {
        runnable = new Runnable() {
            @Override
            public void run() {
                int currentPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                recyclerView.smoothScrollToPosition(currentPosition + 1);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    public void stopAutoScroll() {
        handler.removeCallbacks(runnable);
    }
}
