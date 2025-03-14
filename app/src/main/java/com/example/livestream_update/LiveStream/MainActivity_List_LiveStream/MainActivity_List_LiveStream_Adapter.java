package com.example.livestream_update.LiveStream.MainActivity_List_LiveStream;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.example.livestream_update.LiveStream.MainActivity_List_LiveStream.Fragment.MainActivity_List_LiveStream_Fragment01;
import com.example.livestream_update.LiveStream.MainActivity_List_LiveStream.Fragment.MainActivity_List_LiveStream_Fragment02;
import com.example.livestream_update.LiveStream.MainActivity_List_LiveStream.Fragment.MainActivity_List_LiveStream_Fragment03;
import com.example.livestream_update.R;

import java.util.List;

public class MainActivity_List_LiveStream_Adapter extends RecyclerView.Adapter<MainActivity_List_LiveStream_Adapter.ViewHolder> {
    private final FragmentActivity activity;

    public MainActivity_List_LiveStream_Adapter(FragmentActivity activity, List<Fragment> fragments_list_livestream) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rm_activity_main_list_live_stream_fragment_container, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Kiểm tra nếu FrameLayout tồn tại trước khi replace Fragment
        if (holder.fragment01Container != null) {
            transaction.replace(holder.fragment01Container.getId(), new MainActivity_List_LiveStream_Fragment01());
        }
        if (holder.fragment02Container != null) {
            transaction.replace(holder.fragment02Container.getId(), new MainActivity_List_LiveStream_Fragment02());
        }
        if (holder.fragment03Container != null) {
            transaction.replace(holder.fragment03Container.getId(), new MainActivity_List_LiveStream_Fragment03());
        }

        transaction.commit();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View fragment01Container;
        View fragment02Container;
        View fragment03Container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fragment01Container = itemView.findViewById(R.id.rm_activity_main_list_live_stream_fragment01_fl_01);
            fragment02Container = itemView.findViewById(R.id.rm_activity_main_list_live_stream_fragment02_fl_01);
            fragment03Container = itemView.findViewById(R.id.rm_activity_main_list_live_stream_fragment03_fl_01);
        }
    }
}
