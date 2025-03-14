package com.example.livestream_update.Ringme.TabVideo.dialog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vtm.R;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.adapter.BottomSheetMenuAdapter;
import com.vtm.ringme.base.DividerMenuItemDecoration;
import com.vtm.ringme.listener.RecyclerClickListener;
import com.vtm.ringme.livestream.listener.BottomSheetListener;
import com.vtm.ringme.model.ItemContextMenu;

import java.util.ArrayList;

public class OptionsVideoDialog extends BottomSheetDialog {

    private static final String TAG = OptionsVideoDialog.class.getSimpleName();

    private final AppCompatActivity activity;
    private final ApplicationController mApplication;
    private ArrayList<ItemContextMenu> listItem;
    private BottomSheetListener mCallBack;
    private RecyclerView mRecyclerView;
    private TextView mTitle;
    private BottomSheetMenuAdapter mAdapter;
    private boolean isShowTitle = true;

    private View bottomSheet;

    public OptionsVideoDialog(@NonNull AppCompatActivity activity, boolean isCancelable) {
        super(activity, R.style.style_bottom_sheet_dialog_v2);
        this.activity = activity;
        this.mApplication = (ApplicationController) activity.getApplicationContext();
        setCancelable(isCancelable);
    }

    public OptionsVideoDialog setListItem(ArrayList<ItemContextMenu> items) {
        this.listItem = items;
        return this;
    }

    public OptionsVideoDialog setHasTitle(boolean isShowTitle) {
        this.isShowTitle = isShowTitle;
        return this;
    }

    public OptionsVideoDialog setListener(BottomSheetListener listener) {
        this.mCallBack = listener;
        return this;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rm_dialog_video_options);
        Window window = getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        mRecyclerView = findViewById(R.id.bottom_sheet_recycler_view);
        mTitle = findViewById(R.id.tv_title);
        if (mTitle != null) {
            mTitle.setVisibility(isShowTitle ? View.VISIBLE : View.GONE);
        }
        mAdapter = new BottomSheetMenuAdapter(listItem, getContext());
        mAdapter.setRecyclerClickListener(new RecyclerClickListener() {
            @Override
            public void onClick(View v, int pos, Object object) {
                if (mCallBack != null) {
                    ItemContextMenu item = (ItemContextMenu) object;
                    mCallBack.onItemClick(item.getActionTag(), item.getObj());
                }
                dismiss();
            }

            @Override
            public void onLongClick(View v, int pos, Object object) {

            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mApplication));
        mRecyclerView.addItemDecoration(new DividerMenuItemDecoration(mApplication, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        bottomSheet = findViewById(R.id.design_bottom_sheet);
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                if (bottomSheetBehavior == null) return;
                bottomSheetBehavior.setPeekHeight(1000);
            }
        });
    }
}