package com.example.livestream_update.Ringme.LiveStream.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vtm.R;
import com.vtm.ringme.livestream.holder.LivestreamReportHolder;
import com.vtm.ringme.livestream.model.Report;
import com.vtm.ringme.livestream.socket.ListenerV2;

import java.util.List;

import io.reactivex.annotations.NonNull;

public class LivestreamReportAdapter extends RecyclerView.Adapter<LivestreamReportHolder> {
    List<Report> reports;
    ListenerV2.ReportListener listener;
    Context context;

    public LivestreamReportAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Report> data) {
        this.reports = data;
        notifyDataSetChanged();
    }

    @NonNull
    @androidx.annotation.NonNull
    @Override
    public LivestreamReportHolder onCreateViewHolder(@NonNull @androidx.annotation.NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rm_holder_livestream_report, viewGroup, false);
        return new LivestreamReportHolder(view);
    }

    public void setListener(ListenerV2.ReportListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull @androidx.annotation.NonNull LivestreamReportHolder livestreamReportHolder, int position) {
        Report report = reports.get(position);
        livestreamReportHolder.getReport().setText(report.getTitle());
        if (report.isChosen()) {
            livestreamReportHolder.getReport().setTextColor(context.getResources().getColor(R.color.main_color_new));
            Glide.with(context)
                    .load(R.drawable.rm_ic_radio_checked)
                    .into(livestreamReportHolder.getRadio());
        } else {
            livestreamReportHolder.getReport().setTextColor(Color.WHITE);
            Glide.with(context)
                    .load(R.drawable.rm_ic_radio_unchecked)
                    .into(livestreamReportHolder.getRadio());
        }

        if (listener != null) {
            livestreamReportHolder.itemView.setOnClickListener(v -> listener.onReportClick(report));
        }
    }

    @Override
    public int getItemCount() {
        return reports == null ? 0 : reports.size();
    }
}

