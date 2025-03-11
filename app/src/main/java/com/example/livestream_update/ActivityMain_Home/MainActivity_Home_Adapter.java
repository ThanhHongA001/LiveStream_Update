package com.example.livestream_update.ActivityMain_Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.example.livestream_update.ActivityMain_Home.Fragment.MainActivity_Home_Fragment01;
import com.example.livestream_update.ActivityMain_Home.Fragment.MainActivity_Home_Fragment02;
import com.example.livestream_update.ActivityMain_Home.Fragment.MainActivity_Home_Fragment03;
import com.example.livestream_update.ActivityMain_Home.Fragment.MainActivity_Home_Fragment04;
import com.example.livestream_update.R;

import java.util.List;

public class MainActivity_Home_Adapter extends RecyclerView.Adapter<MainActivity_Home_Adapter.ViewHolder> {
    private final FragmentActivity activity;
    private final List<Fragment> fragmentList;

    public MainActivity_Home_Adapter(FragmentActivity activity, List<Fragment> fragments) {
        this.activity = activity;
        this.fragmentList = fragments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rm_activity_home_fragment_container, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.rm_activity_home_frame_01, new MainActivity_Home_Fragment01());
        transaction.replace(R.id.rm_activity_home_frame_02, new MainActivity_Home_Fragment02());
        transaction.replace(R.id.rm_activity_home_frame_03, new MainActivity_Home_Fragment03());
        transaction.replace(R.id.rm_activity_home_frame_04, new MainActivity_Home_Fragment04());

        transaction.commit();
    }

    @Override
    public int getItemCount() {
        return 1; // Chỉ có một mục chứa tất cả Fragment
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
