package com.example.livestream_update.Ringme.Dialog;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.vtm.R;
import com.vtm.databinding.RmDialogRechargeStarBinding;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.activities.DonateTermsActivity;
import com.vtm.ringme.api.HomeApi;
import com.vtm.ringme.api.response.OtpPackageStarResponse;
import com.vtm.ringme.api.response.PackageStarResponse;
import com.vtm.ringme.base.BaseDialogFragment;
import com.vtm.ringme.common.api.http.HttpCallBack;
import com.vtm.ringme.customview.CustomSuccessToast;
import com.vtm.ringme.livestream.activity.LivestreamDetailActivity;
import com.vtm.ringme.livestream.adapter.LivestreamPackageStarAdapter;
import com.vtm.ringme.livestream.apis.response.BaseResponse;
import com.vtm.ringme.livestream.model.PackageStar;
import com.vtm.ringme.utils.ToastUtils;

import java.text.DecimalFormat;

public class RechargeStarDialog extends BottomSheetDialog {
    private final LivestreamDetailActivity context;
    private final RmDialogRechargeStarBinding binding;
    private Bitmap avtBitmap;
    private LivestreamPackageStarAdapter adapter;
    private Dialog confirmDialog;
    PackageStar currentPackage;
    int loading = 0;

    public RechargeStarDialog(@NonNull LivestreamDetailActivity context) {
        super(context);
        this.context = context;
        binding = RmDialogRechargeStarBinding.inflate(LayoutInflater.from(context));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        binding.layoutLoading.setVisibility(View.VISIBLE);
        initView();
        getListStarPackage();
    }

    private void initView() {

        binding.edtStarNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (TextUtils.isEmpty(binding.edtStarNumber.getText().toString().trim())) {
//                    disableSendButton();
//                } else {
//                    rechargeNumber = binding.edtStarNumber.getText().toString().trim();
//                    try {
//                        if (Long.parseLong(rechargeNumber) > 0) {
//                            money = rechargeNumber;
//                            enableSendButton();
//                        } else {
//                            disableSendButton();
//                        }
//                    } catch (Exception e) {
//                        ToastUtils.showToast(context, context.getResources().getString(R.string.invalid_number_notify));
//                        disableSendButton();
//                    }
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        adapter = new LivestreamPackageStarAdapter(context);

        adapter.setClickStarPackageListener(starPackage -> {
//            if (starPackage.getNumber() != 0) {
            binding.layoutStarNumberInput.animate().scaleY(0f).setDuration(200);
            binding.layoutStarNumberInput.postDelayed(() -> binding.layoutStarNumberInput.setVisibility(View.GONE), 200);
            currentPackage = starPackage;
            enableSendButton();
//            } else {
//                binding.layoutStarNumberInput.setVisibility(View.VISIBLE);
//                binding.edtStarNumber.setText("");
//                rechargeNumber = "";
//                money = "";
//                binding.layoutStarNumberInput.animate().scaleY(1f).setDuration(200);
//                disableSendButton();
//            }
        });
        binding.rcvStarPackages.setAdapter(adapter);
        binding.rcvStarPackages.setLayoutManager(new GridLayoutManager(context, 3));
        binding.btnBack.setOnClickListener(view -> {
            dismiss();
            context.openDialogSendStars();
        });
        binding.layoutLoading.setOnClickListener(view -> Log.d("TAG", "initView: loading"));
        binding.tvTerm.setOnClickListener(view -> {
            Intent i = new Intent(context, DonateTermsActivity.class);
            context.startActivity(i);
        });
    }

    private void getListStarPackage() {
        binding.layoutLoading.setVisibility(View.VISIBLE);
        HomeApi.getInstance().getListPackageStart(new HttpCallBack() {
            @Override
            public void onSuccess(String data) throws Exception {
                try {
                    Gson gson = new Gson();
                    PackageStarResponse response = gson.fromJson(data, PackageStarResponse.class);
                    if (response != null && response.getData() != null) {
                        adapter.setList(response.getData());
                        binding.layoutLoading.setVisibility(View.GONE);
                    } else {
                        binding.layoutLoading.setVisibility(View.GONE);
                        ToastUtils.showToast(context,context.getString(R.string.error_message_default));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
                binding.layoutLoading.setVisibility(View.GONE);
                ToastUtils.showToast(context,context.getString(R.string.error_message_default));
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                ++loading;
                if (loading == 2) binding.layoutLoading.setVisibility(View.GONE);
            }
        });
    }

    private void sendStars() {
        binding.btnRecharge.setOnClickListener(view -> {
//            context.switchToTabChat();
            initConfirmDialog();
        });
    }

    private void enableSendButton() {
        binding.btnRecharge.setBackgroundResource(R.drawable.rm_bg_btn_send_stars_blue);
        binding.tvBtnRecharge.setTextColor(Color.parseColor("#ffffff"));

        sendStars();
    }

    private void disableSendButton() {
        binding.btnRecharge.setBackgroundResource(R.drawable.rm_bg_btn_send_stars_disable);
        binding.tvBtnRecharge.setTextColor(Color.parseColor("#33FFFFFF"));
        binding.btnRecharge.setOnClickListener(null);
    }

    private void apiRechargeOtp(String code, boolean isResend) {
        HomeApi.getInstance().buyPackageStartOTP(code,new HttpCallBack() {
            @Override
            public void onSuccess(String data) throws Exception {
                OtpPackageStarResponse response = ApplicationController.self().getGson().fromJson(data, OtpPackageStarResponse.class);
                if(response.getCode() == 200) {
                    if(!isResend) {
                        showDialogOtp(response.getRequestIdCp());
                    }
                } else {
                    ToastUtils.showToast(context, response.getMessage());
                }
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
                ToastUtils.showToast(context, context.getString(R.string.error_message_default));
            }
        });

    }
    private void showDialogOtp(String requestId)
    {
        DialogConfirm dialogConfirm = DialogConfirm.newInstance(
                activity.getString(R.string.enter_otp),
                "",
                DialogConfirm.OTP_TYPE,
                R.string.cancel,
                R.string.yes);
        dialogConfirm.setSelectListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void dialogRightClick(int value) {
                dialogConfirm.dismiss();
                apiRechargeValidate(String.valueOf(value), requestId);
            }

            @Override
            public void dialogLeftClick() {

            }

            @Override
            public void onResend() {
                apiRechargeOtp(currentPackage.getCode(), true);
            }
        });
        dialogConfirm.show(activity.getSupportFragmentManager(), "");
    }
    private void apiRechargeValidate(String otp, String requestId) {
        HomeApi.getInstance().buyPackageStartValidate(currentPackage.getCode(),otp, requestId,new HttpCallBack() {
            @Override
            public void onSuccess(String data) throws Exception {
                BaseResponse response = ApplicationController.self().getGson().fromJson(data, BaseResponse.class);
                if(response.getCode() == 200) {
                    CustomSuccessToast.makeText(context, context.getString(R.string.recharge_success), CustomSuccessToast.SHORT).show();
                } else if (response.getCode() == 503) {
                    ToastUtils.showToast(context, response.getMessage());
                } else {
                    ToastUtils.showToast(context, context.getString(R.string.error_message_default));
                }
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
                ToastUtils.showToast(context, context.getString(R.string.error_message_default));
            }
        });

    }

    public void initConfirmDialog() {
        DialogConfirm dialogConfirm = DialogConfirm.newInstance(
                activity.getString(R.string.confirm),
                activity.getString(R.string.confirm_recharge_star,
                        shortenNumber((long) currentPackage.getValue()), shortenNumber((long) currentPackage.getPrice())),
                DialogConfirm.CONFIRM_TYPE,
                R.string.cancel,
                R.string.text_recharge);
        dialogConfirm.setSelectListener(new BaseDialogFragment.DialogListener() {
            @Override
            public void dialogRightClick(int value) {
                apiRechargeOtp(currentPackage.getCode(), false);
            }

            @Override
            public void dialogLeftClick() {

            }
        });
        dialogConfirm.show(activity.getSupportFragmentManager(), "");
    }

    private String shortenNumber(long value) {
        String shortenValue = "";
        if (value < 1000) {
            shortenValue = String.valueOf(value);
        } else if (value < 999999) {
            shortenValue = new DecimalFormat("#.##").format((double) value / 1000) + "K";
        } else if (value < 999999999) {
            shortenValue = new DecimalFormat("#.##").format((double) value / 1000000) + "M";
        } else if (value < Long.parseLong("999999999999")) {
            shortenValue = new DecimalFormat("#.##").format((double) value / 1000000000) + "B";
        } else if (value < Long.parseLong("999999999999999")) {
            shortenValue = new DecimalFormat("#.##").format((double) value / Long.parseLong("1000000000000")) + "T";
        } else {
            shortenValue = new DecimalFormat("#.##").format((double) value / Long.parseLong("1000000000000000")) + "Q";
        }
        return shortenValue;
    }

}
