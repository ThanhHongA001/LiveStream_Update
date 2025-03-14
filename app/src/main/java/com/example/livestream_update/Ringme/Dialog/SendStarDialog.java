package com.example.livestream_update.Ringme.Dialog;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.vtm.R;
import com.vtm.databinding.RmDialogSendStarBinding;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.activities.DonateTermsActivity;
import com.vtm.ringme.api.HomeApi;
import com.vtm.ringme.api.response.CurrentStarNumberResponse;
import com.vtm.ringme.api.response.ListDonateResponse;
import com.vtm.ringme.common.api.http.HttpCallBack;
import com.vtm.ringme.customview.CustomSuccessToast;
import com.vtm.ringme.livestream.activity.LivestreamDetailActivity;
import com.vtm.ringme.livestream.adapter.LivestreamDonateAdapter;
import com.vtm.ringme.livestream.apis.response.BaseResponse;
import com.vtm.ringme.livestream.model.Donate;
import com.vtm.ringme.model.livestream.LivestreamModel;
import com.vtm.ringme.utils.ToastUtils;

import java.text.DecimalFormat;

public class SendStarDialog extends BottomSheetDialog {
    private final LivestreamDetailActivity context;
    private final LivestreamModel livestream;
    private final RmDialogSendStarBinding binding;
    private Bitmap avtBitmap;
    private LivestreamDonateAdapter adapter;
    private Dialog donateFailDialog;
    int loading = 0;
    long balance = 0;
    private Donate currentDonate;

    public SendStarDialog(@NonNull LivestreamDetailActivity context, LivestreamModel livestream) {
        super(context);
        this.context = context;
        this.livestream = livestream;
        binding = RmDialogSendStarBinding.inflate(LayoutInflater.from(context));
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
        getUserStarNumber();
    }

    private void initView() {
        Glide.with(context)
                .load(livestream.getChannel().getUrlImage())
                .placeholder(R.drawable.rm_ic_avatar_default)
                .centerCrop()
                .into(binding.streamerAvatar);
        binding.streamerName.setText(Html.fromHtml(context.getResources().
                getString(R.string.send_star_to_support, livestream.getChannel().getName())));
        binding.tvSendStarsDes.setText(context.getString(R.string.send_star_des2, livestream.getChannel().getName()));

        binding.edtStarNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.edtStarNumber.getText().toString().trim())) {
                    disableSendButton();
                } else {
//                    sendStarNumber = binding.edtStarNumber.getText().toString().trim();
//                    try {
//                        if (Long.parseLong(sendStarNumber) > 0) {
//                            binding.edtComment.setText(context.getString(R.string.sent_stars, shortenNumber(Long.parseLong(sendStarNumber))));
//                            binding.tvBtnSendStar.setText(context.getString(R.string.text_btn_send_star, shortenNumber(Long.parseLong(sendStarNumber))));
//                            enableSendButton();
//                        } else {
//                            disableSendButton();
//                        }
//                    } catch (Exception e) {
//                        ToastUtils.showToast(context, context.getResources().getString(R.string.invalid_number_notify));
//                        disableSendButton();
//                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.tvBtnSendStar.setText(context.getString(R.string.text_btn_send_star, ""));
        adapter = new LivestreamDonateAdapter(context);

        adapter.setClickStarPackageListener(starPackage -> {

            if (TextUtils.isEmpty(binding.edtComment.getText().toString())
                    || binding.edtComment.getText().toString().matches("Donate .*")) {
                binding.edtComment.setText(context.getString(R.string.sent_gift, starPackage.getName()));
            }
            binding.tvBtnSendStar.setText(context.getString(R.string.text_btn_send_star, shortenNumber((long) starPackage.getAmountStar())));
            binding.layoutStarNumberInput.animate().scaleY(0f).setDuration(200);
            binding.layoutStarNumberInput.postDelayed(() -> binding.layoutStarNumberInput.setVisibility(View.GONE), 200);
            currentDonate = starPackage;
            enableSendButton();

        });
        binding.rcvStarPackages.setAdapter(adapter);
        binding.rcvStarPackages.setLayoutManager(new GridLayoutManager(context, 3));
        binding.btnRecharge.setOnClickListener(view -> {
            dismiss();
            new RechargeStarDialog(context).show();
        });
        binding.layoutLoading.setOnClickListener(view -> Log.d("TAG", "initView: loading"));
        binding.tvTerm.setOnClickListener(view -> {
            Intent i = new Intent(context, DonateTermsActivity.class);
            context.startActivity(i);
        });
    }

    private void getListStarPackage() {
        binding.layoutLoading.setVisibility(View.VISIBLE);
        HomeApi.getInstance().getListDonate(new HttpCallBack() {
            @Override
            public void onSuccess(String data) throws Exception {
                Gson gson = new Gson();
                ListDonateResponse response = gson.fromJson(data, ListDonateResponse.class);
                if (response != null && response.getData() != null) {
                    adapter.setList(response.getData());
                    binding.layoutLoading.setVisibility(View.GONE);
                } else {
                    binding.layoutLoading.setVisibility(View.GONE);
                    ToastUtils.showToast(context,context.getString(R.string.error_message_default));
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
        binding.btnSendStars.setOnClickListener(view -> {
            context.switchToTabChat();
            apiSendStar();
        });
    }

    private Bitmap getCircleFromBitmap(Bitmap bitmapImg) {
        int w = bitmapImg.getWidth();
        int h = bitmapImg.getHeight();

        int radius = Math.min(h / 2, w / 2);
        Bitmap output = Bitmap.createBitmap(w + 20, h + 20, Bitmap.Config.ARGB_8888);

        Paint p = new Paint();
        p.setAntiAlias(true);

        Canvas c = new Canvas(output);
        c.drawARGB(0, 0, 0, 0);
        p.setStyle(Paint.Style.FILL);

        c.drawCircle((w / 2) + 10, (h / 2) + 10, radius, p);

        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        c.drawBitmap(bitmapImg, 10, 10, p);
        p.setXfermode(null);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.parseColor("#E5FFCC"));
        p.setStrokeWidth(1);
        c.drawCircle((w / 2) + 10, (h / 2) + 10, radius, p);

        return output;
    }

    private void enableSendButton() {
        binding.btnSendStars.setBackgroundResource(R.drawable.rm_bg_btn_send_stars_blue);
        binding.tvBtnSendStar.setTextColor(Color.parseColor("#ffffff"));

        sendStars();
    }

    private void disableSendButton() {
        binding.btnSendStars.setBackgroundResource(R.drawable.rm_bg_btn_send_stars_disable);
        binding.edtComment.setText("");
        binding.tvBtnSendStar.setText(context.getString(R.string.text_btn_send_star, ""));
        binding.tvBtnSendStar.setTextColor(Color.parseColor("#33FFFFFF"));
        binding.btnSendStars.setOnClickListener(null);
    }

    private void apiSendStar() {
        try {
            if(currentDonate == null) return;
            if (balance < currentDonate.getAmountStar()) {
                ToastUtils.showToast(context, context.getString(R.string.not_enough_stars), 3000, 0, null);
                return;
            }
            binding.layoutLoading.setVisibility(View.VISIBLE);
            String message = TextUtils.isEmpty(binding.edtComment.getText().toString().trim())
                    ? ""
                    : binding.edtComment.getText().toString().trim();
            HomeApi.getInstance().sendStar(message, String.valueOf(currentDonate.getId()), livestream.getChannel().getId(), livestream.getId(), new HttpCallBack() {
                @Override
                public void onSuccess(String data) throws Exception {
                    BaseResponse response = ApplicationController.self().getGson().fromJson(data, BaseResponse.class);
                    if(response.getCode() == 200) {
                        CustomSuccessToast.makeText(context, CustomSuccessToast.SHORT).show();
                        dismiss();
                    } else {
                        initDonateFailDialog();
                    }
                }

                @Override
                public void onFailure(String message) {
                    super.onFailure(message);
                    initDonateFailDialog();
                }

                @Override
                public void onCompleted() {
                    super.onCompleted();
                    binding.layoutLoading.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            ToastUtils.showToast(context, context.getResources().getString(R.string.invalid_number_notify));
            binding.layoutLoading.setVisibility(View.GONE);
        }

    }

    public void initDonateFailDialog() {
        if (context != null) {
            donateFailDialog = new Dialog(context);
            if (donateFailDialog.getWindow() != null) {
                donateFailDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                donateFailDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                WindowManager.LayoutParams wlp = donateFailDialog.getWindow().getAttributes();
                wlp.gravity = Gravity.CENTER;
                donateFailDialog.getWindow().setAttributes(wlp);
            }

            donateFailDialog.setContentView(R.layout.rm_dialog_donate_fail);
            donateFailDialog.setCancelable(false);
            donateFailDialog.setCanceledOnTouchOutside(false);

            donateFailDialog.findViewById(R.id.dialog_confirm_button_positive).setOnClickListener(view -> {
                donateFailDialog.dismiss();
                apiSendStar();
            });

            donateFailDialog.findViewById(R.id.dialog_confirm_button_negative).setOnClickListener(v -> {
                donateFailDialog.dismiss();
                dismiss();
            });
            donateFailDialog.show();
        }
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

    public void getUserStarNumber() {
        binding.layoutLoading.setVisibility(View.VISIBLE);
        HomeApi.getInstance().getUserStarNumber(new HttpCallBack() {
            @Override
            public void onSuccess(String data) {
                Gson gson = new Gson();
                CurrentStarNumberResponse response = gson.fromJson(data, CurrentStarNumberResponse.class);
                if (response != null) {
                    binding.tvStarBalance.setText(shortenNumber(response.getData().getTotalStar()));
                    balance = response.getData().getTotalStar();
                }
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);

            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                if (loading < 2)
                    ++loading;
                if (loading == 2) binding.layoutLoading.setVisibility(View.GONE);
            }
        });
    }
}

