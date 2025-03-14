package com.example.livestream_update.Ringme.LiveStream.holder;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.vtm.R;

public class LivestreamReportHolder extends RecyclerView.ViewHolder {
    private TextView report;
    private final View line;
    private AppCompatImageView radio;

    public View getLine() {
        return line;
    }

    public LivestreamReportHolder(View itemView) {
        super(itemView);
        report = itemView.findViewById(R.id.tv_report);
        line = itemView.findViewById(R.id.line);
        radio = itemView.findViewById(R.id.radio);
    }

    public TextView getReport() {
        return report;
    }

    public AppCompatImageView getRadio() {
        return radio;
    }
}
