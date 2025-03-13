package com.example.livestream_update.MainActivity_All_Channel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.livestream_update.R;
import java.util.List;

public class MainActivity_All_Channel_Adapter extends RecyclerView.Adapter<MainActivity_All_Channel_Adapter.ViewHolder> {

    private List<MainActivity_All_Channel_Model> channelList;

    public MainActivity_All_Channel_Adapter(List<MainActivity_All_Channel_Model> channelList) {
        this.channelList = channelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rm_activity_item07, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainActivity_All_Channel_Model channel = channelList.get(position);
        holder.userName.setText(channel.getUserName());
        holder.followersCount.setText(channel.getFollowersCount());
    }

    @Override
    public int getItemCount() {
        return channelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName, followersCount;
        ImageView profileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.textView);
            followersCount = itemView.findViewById(R.id.textView2);
            profileImage = itemView.findViewById(R.id.rm_activity_item05_iv_01);
        }
    }
}
