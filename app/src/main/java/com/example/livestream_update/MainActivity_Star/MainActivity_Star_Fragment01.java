package com.example.livestream_update.MainActivity_Star;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.livestream_update.R;

public class MainActivity_Star_Fragment01 extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rm_activity_main_star_fragment01, container, false);

        // Ánh xạ ID của ImageView nút đóng hộp thoại
        ImageView closeButton = view.findViewById(R.id.rm_activity_main_star_recharge_imageview_01);

        // Thiết lập sự kiện bấm vào để đóng hộp thoại
        closeButton.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            if (window != null) {
                // Thiết lập vị trí hộp thoại ở dưới cùng
                window.setGravity(Gravity.BOTTOM);

                // Cài đặt kích thước hộp thoại
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                // Hiệu ứng animation khi mở hộp thoại
                window.setWindowAnimations(R.style.DialogAnimation);

                // Làm mờ nền phía sau
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                window.setDimAmount(0.5f); // Độ mờ của nền (0.0f: trong suốt - 1.0f: mờ hoàn toàn)

                // Đặt nền của hộp thoại trong suốt
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
