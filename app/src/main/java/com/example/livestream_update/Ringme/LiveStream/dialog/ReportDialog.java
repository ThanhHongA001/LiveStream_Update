package com.example.livestream_update.Ringme.LiveStream.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vtm.ringme.api.BaseResponse;
import com.vtm.ringme.api.HomeApi;
import com.vtm.ringme.customview.MultiLineEditText;
import com.vtm.ringme.livestream.socket.ListenerV2;
import com.google.gson.Gson;
import com.vtm.R;
import com.vtm.ringme.common.api.http.HttpCallBack;
import com.vtm.ringme.livestream.activity.LivestreamDetailActivity;
import com.vtm.ringme.livestream.adapter.LivestreamReportAdapter;
import com.vtm.ringme.livestream.model.Report;

import com.vtm.ringme.utils.ToastUtils;

import java.util.List;

public class ReportDialog {
    private final String livestreamId;
    private final String streamerId;
    //view
    Dialog dialog;
    RecyclerView reports;
    MultiLineEditText reportContent;
    Button btnReport, btnCancel;
    //control
    ListenerV2.ReportListener listener;
    LivestreamReportAdapter reportAdapter;
    LivestreamDetailActivity activity;
    List<Report> reportList;
    //data
    private int currentPosition = -1;

    public Dialog getDialog() {
        return this.dialog;
    }

    public ReportDialog(LivestreamDetailActivity activity, List<Report> reports, ListenerV2.ReportListener listener, String livestreamId, String streamerId) {
        this.activity = activity;
        this.reportList = reports;
        this.listener = listener;
        this.livestreamId = livestreamId;
        this.streamerId = streamerId;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.rm_dialog_report);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setAttributes(layoutParams);
        dialog.setOnDismissListener(dialogInterface -> {
            activity.onDismissReportDialog();
            reportContent.setText("");
        });

        initView(dialog);

    }

    private void initView(Dialog dialog) {
        reports = dialog.findViewById(R.id.reports);
        reportContent = dialog.findViewById(R.id.report_content);
        btnReport = dialog.findViewById(R.id.btn_confirm);
        btnCancel = dialog.findViewById(R.id.btn_cancel);

        reportAdapter = new LivestreamReportAdapter(activity);
        reportAdapter.setData(reportList);
        reportAdapter.setListener(reportDetail -> {
            if (currentPosition != -1) {
                reportList.get(currentPosition).setChosen(false);
                reportAdapter.notifyItemChanged(currentPosition);
            }
            currentPosition = reportList.indexOf(reportDetail);
            reportList.get(currentPosition).setChosen(true);
            reportAdapter.notifyItemChanged(currentPosition);
        });
        reports.setLayoutManager(new LinearLayoutManager(activity));
        reports.setAdapter(reportAdapter);
        btnCancel.setOnClickListener(view -> dismiss());
        initListener();
    }

    private void initListener() {
        btnReport.setOnClickListener(v -> {
            if (currentPosition == -1) {
                ToastUtils.showToast(activity, activity.getResources().getString(R.string.livestream_report_type));
                return;
            }
            if (TextUtils.isEmpty(reportContent.getText().toString())) {
                ToastUtils.showToast(activity, activity.getResources().getString(R.string.livestream_report_content));
                return;
            }
            reportLivestream(reportContent.getText().toString(), livestreamId,
                    reportList.get(currentPosition).getId(),streamerId);
        });
    }

    public void show() {
        if (dialog.isShowing())
            return;
        dialog.show();
    }

    public void dismiss() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void reportLivestream(String content, String livestreamId, int reportType, String streamerId) {
        HomeApi.getInstance().reportLivestream(livestreamId, content, reportType, streamerId, new HttpCallBack() {
            @Override
            public void onSuccess(String data) throws Exception {
                Gson gson = new Gson();
                BaseResponse response = gson.fromJson(data, BaseResponse.class);
                if(response.getCode() == 200){
                    reportContent.setText("");
                    dialog.dismiss();
                    ToastUtils.showToast(activity, activity.getResources().getString(R.string.livestream_report));
                } else if (response.getCode() == 201) {
                    ToastUtils.showToast(activity, response.getMessage());
                } else {
                    ToastUtils.showToast(activity, activity.getResources().getString(R.string.report_livestream_error_message));
                }
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
                Log.d("reportLog", "onFailure: " + message);
                ToastUtils.showToast(activity, activity.getResources().getString(R.string.report_livestream_error_message));
            }
        });
    }


}
