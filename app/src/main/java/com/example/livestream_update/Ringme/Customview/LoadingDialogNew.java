package com.example.livestream_update.Ringme.Customview;



import static com.vtm.ringme.values.Constants.LoadingDialogMode.loadingGreen;
import static com.vtm.ringme.values.Constants.LoadingDialogMode.loadingWhite;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import com.bumptech.glide.Glide;
import com.vtm.R;

public class LoadingDialogNew extends Dialog {
    Context context;
    //TODO mode để đánh dấu dùng màu nào, mặc định là xanh lá
    // 0 - xanh lá
    // 1 - trắng
    int MODE;
    private DismissListenerNew listener;
    private ImageView imageView;
    private ProgressBar progressBar;

    @Override
    public void dismiss() {
        super.dismiss();
        if (listener != null) {
            listener.onDismissListener();
        }
    }

    public void setListener(DismissListenerNew listener) {
        this.listener = listener;
    }

    public interface DismissListenerNew {
        void onDismissListener();
    }

    public LoadingDialogNew(@NonNull Context context) {
        super(context);
        this.context = context;
        this.MODE = loadingGreen;
        setCancelable(true);
    }

    public LoadingDialogNew(@NonNull Context context, boolean cancelable) {
        super(context);
        this.context = context;
        this.MODE = loadingGreen;
        setCancelable(cancelable);
    }

    public LoadingDialogNew(@NonNull Context context, int colorMode) {
        super(context);
        this.context = context;
        this.MODE = colorMode;
        setCancelable(true);
    }

    public void setMODE(int MODE) {
        this.MODE = MODE;
        if (MODE == loadingWhite) {
            progressBar.setIndeterminateDrawable(AppCompatResources.getDrawable(context, R.drawable.rm_loading_progress_white));
            Glide.with(context).load(R.drawable.rm_ic_kakoak_new_white).into(imageView);
        } else if (MODE == loadingGreen) {
            progressBar.setIndeterminateDrawable(AppCompatResources.getDrawable(context, R.drawable.rm_loading_progress_green));
            Glide.with(context).load(R.drawable.rm_ic_kakoak_new_green).into(imageView);
        }
    }

    public LoadingDialogNew(@NonNull Context context, int colorMode, boolean cancelable) {
        super(context);
        this.context = context;
        this.MODE = colorMode;
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rm_dialog_loading_new);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        imageView = findViewById(R.id.logo);
        progressBar = findViewById(R.id.progress_wheel);

        if (MODE == loadingWhite) {
            progressBar.setIndeterminateDrawable(AppCompatResources.getDrawable(context, R.drawable.rm_loading_progress_white));
            Glide.with(context).load(R.drawable.rm_ic_kakoak_new_white).into(imageView);
        } else if (MODE == loadingGreen) {
            progressBar.setIndeterminateDrawable(AppCompatResources.getDrawable(context, R.drawable.rm_loading_progress_green));
            Glide.with(imageView.getContext()).load(R.drawable.rm_ic_kakoak_new_green).into(imageView);
        }

    }


}
