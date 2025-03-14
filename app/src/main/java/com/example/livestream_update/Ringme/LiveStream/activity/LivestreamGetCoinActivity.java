package com.example.livestream_update.Ringme.LiveStream.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.vtm.R;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.dialog.LoadingDialog;
import com.vtm.ringme.livestream.apis.LivestreamApiInstance;
import com.vtm.ringme.livestream.apis.LivestreamServices;
import com.vtm.ringme.livestream.apis.response.LiveStreamBuyCoinResponse;
import com.vtm.ringme.model.ReengAccount;
import com.vtm.ringme.utils.ToastUtils;
import com.vtm.ringme.values.Constants;

import io.reactivex.annotations.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LivestreamGetCoinActivity extends AppCompatActivity {
    AppCompatButton btn1, btn10, btn20, btn30, btn40, btn50;
    EditText edAmount;
    Button btnGetCoin;
    LoadingDialog loadingDialog;

    ApplicationController appController;
    ReengAccount account;

    @Override
    protected void onCreate(@Nullable @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rm_activity_livestream_get_coin);
        initController();
        initView();
    }

    private void initController() {
        appController = ApplicationController.self();
        account = appController.getCurrentAccount();
    }

    private void initView() {
        btn1 = findViewById(R.id.btn_get_coin_1);
        btn10 = findViewById(R.id.btn_get_coin_10);
        btn20 = findViewById(R.id.btn_get_coin_20);
        btn30 = findViewById(R.id.btn_get_coin_30);
        btn40 = findViewById(R.id.btn_get_coin_40);
        btn50 = findViewById(R.id.btn_get_coin_50);
        btnGetCoin = findViewById(R.id.btn_get_coin);
        edAmount = findViewById(R.id.et_amount);
        loadingDialog = new LoadingDialog(this, false);
        initListener();
    }

    private void initListener() {
        btn1.setOnClickListener(v -> edAmount.setText(getResources().getString(R.string.get_coin_1)));
        btn10.setOnClickListener(v -> edAmount.setText(getResources().getString(R.string.get_coin_10)));
        btn20.setOnClickListener(v -> edAmount.setText(getResources().getString(R.string.get_coin_20)));
        btn30.setOnClickListener(v -> edAmount.setText(getResources().getString(R.string.get_coin_30)));
        btn40.setOnClickListener(v -> edAmount.setText(getResources().getString(R.string.get_coin_40)));
        btn50.setOnClickListener(v -> edAmount.setText(getResources().getString(R.string.get_coin_50)));

        btnGetCoin.setOnClickListener(v -> doGetCoin(edAmount.getText().toString()));
    }


    private void doGetCoin(String amount) {
        showLoadingDialog(getString(R.string.loading_livestream), getString(R.string.loading_livestream));
        LivestreamServices livestreamServices = LivestreamApiInstance.getLiveStreamInstance();
        //TODO sau sửa type payment
        livestreamServices.buyCoin(amount, System.currentTimeMillis(), Constants.Server.listAllGift, account.getJidNumber())
                .enqueue(new Callback<LiveStreamBuyCoinResponse>() {
                    @Override
                    public void onResponse(@androidx.annotation.NonNull Call<LiveStreamBuyCoinResponse> call, @androidx.annotation.NonNull Response<LiveStreamBuyCoinResponse> response) {
                        hideLoadingDialog();
                        if (response.isSuccessful()) {
                            if ((response.body() != null)) {
                                if (response.body().getCode() == 200) {
                                    setResult(Activity.RESULT_OK);
                                    finish();
                                } else {
                                    showToast(response.message());
                                }
                            } else {
                                showToast(getString(R.string.e500_internal_server_error));
                            }
                        } else {
                            showToast(getString(R.string.e500_internal_server_error));
                        }
                    }

                    @Override
                    public void onFailure(@androidx.annotation.NonNull Call<LiveStreamBuyCoinResponse> call, @androidx.annotation.NonNull Throwable throwable) {
                        hideLoadingDialog();
                        showToast(getString(R.string.e500_internal_server_error));
                    }
                });

    }


    public void showLoadingDialog(final String title, final String message) {
        if (isFinishing()) return;
        runOnUiThread(() -> {
            loadingDialog.setLabel(title);
            loadingDialog.setMessage(message);
            loadingDialog.show();
        });
    }

    public void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    public void showToast(final String msg) {
        runOnUiThread(() -> ToastUtils.showToast(LivestreamGetCoinActivity.this, msg));
    }
}

