package com.example.livestream_update.Ringme.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.vtm.R;
import com.vtm.ringme.adapter.BottomSheetMenuAdapter;
import com.vtm.ringme.base.BaseAdapter;
import com.vtm.ringme.common.utils.ScreenManager;
import com.vtm.ringme.dialog.BottomSheetDialog;
import com.vtm.ringme.listener.OnClickMoreItemListener;
import com.vtm.ringme.listener.RecyclerClickListener;
import com.vtm.ringme.model.ItemContextMenu;
import com.vtm.ringme.model.tab_video.Video;
import com.vtm.ringme.values.Constants;

import java.util.ArrayList;

public class DialogUtils {
    public static void onClickUpload(AppCompatActivity activity, int type) {

    }
    public static void showOptionVideoItem(AppCompatActivity activity, final Video item
            , final OnClickMoreItemListener listener) {
        ArrayList<ItemContextMenu> data = new ArrayList<>();
        ItemContextMenu itemMenu;
        itemMenu = new ItemContextMenu(activity.getString(R.string.share), R.drawable.rm_ic_share_option, null, Constants.MENU.MENU_SHARE_LINK);
        data.add(itemMenu);
//        itemMenu = new ItemContextMenu(activity.getString(R.string.add_favorite), R.drawable.ic_movie_liked_option, null, Constants.MENU.MENU_ADD_FAVORITE);
//        data.add(itemMenu);
        itemMenu = new ItemContextMenu(activity.getString(R.string.save), R.drawable.rm_ic_add_option, null, Constants.MENU.MENU_SAVE_VIDEO);
        data.add(itemMenu);
        itemMenu = new ItemContextMenu(activity.getString(R.string.add_later), R.drawable.rm_ic_movie_later_option, null, Constants.MENU.MENU_ADD_LATER);
        data.add(itemMenu);

        itemMenu = new ItemContextMenu(activity.getString(R.string.btn_cancel_option), R.drawable.rm_ic_close_option, null, Constants.MENU.MENU_EXIT);
        data.add(itemMenu);

        final BottomSheetDialog dialog = new BottomSheetDialog(activity);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.rm_dialog_more_tab_video, null, false);
        RecyclerView recyclerView = sheetView.findViewById(R.id.recycler_view);

        BottomSheetMenuAdapter adapter = new BottomSheetMenuAdapter(data, activity);
        adapter.setRecyclerClickListener(new RecyclerClickListener() {
            @Override
            public void onClick(View v, int pos, Object object) {
                if (object instanceof ItemContextMenu) {
                    ItemContextMenu itemMenu = (ItemContextMenu) object;
                    if (listener != null) listener.onClickMoreItem(item, itemMenu.getActionTag());
                }
                dialog.dismiss();
            }

            @Override
            public void onLongClick(View v, int pos, Object object) {

            }
        });
        BaseAdapter.setupVerticalMenuRecycler(activity, recyclerView, null, adapter, true);
        dialog.setContentView(sheetView);
        dialog.setOnShowListener(dialogInterface -> {

        });
        dialog.show();
    }


    public static void showOptionVideoItemMyChannel(AppCompatActivity activity, final Video item, boolean isMyChannel
            , final OnClickMoreItemListener listener) {
        ArrayList<ItemContextMenu> data = new ArrayList<>();
        ItemContextMenu itemMenu;
        itemMenu = new ItemContextMenu(activity.getString(R.string.share), R.drawable.rm_ic_share_option, null, Constants.MENU.MENU_SHARE_LINK);
        data.add(itemMenu);
//        itemMenu = new ItemContextMenu(activity.getString(R.string.add_favorite), R.drawable.ic_movie_liked_option, null, Constants.MENU.MENU_ADD_FAVORITE);
//        data.add(itemMenu);
        itemMenu = new ItemContextMenu(activity.getString(R.string.save), R.drawable.rm_ic_add_option, null, Constants.MENU.MENU_SAVE_VIDEO);
        data.add(itemMenu);
        itemMenu = new ItemContextMenu(activity.getString(R.string.add_later), R.drawable.rm_ic_movie_later_option, null, Constants.MENU.MENU_ADD_LATER);
        data.add(itemMenu);
        if(isMyChannel) {
            itemMenu = new ItemContextMenu(activity.getString(R.string.delete), R.drawable.rm_ic_del_option, null, Constants.MENU.MENU_DELETE_VIDEO);
            data.add(itemMenu);
        }

        itemMenu = new ItemContextMenu(activity.getString(R.string.btn_cancel_option), R.drawable.rm_ic_close_option, null, Constants.MENU.MENU_EXIT);
        data.add(itemMenu);

        final BottomSheetDialog dialog = new BottomSheetDialog(activity);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.rm_dialog_more_tab_video, null, false);
        RecyclerView recyclerView = sheetView.findViewById(R.id.recycler_view);

        BottomSheetMenuAdapter adapter = new BottomSheetMenuAdapter(data, activity);
        adapter.setRecyclerClickListener(new RecyclerClickListener() {
            @Override
            public void onClick(View v, int pos, Object object) {
                if (object instanceof ItemContextMenu) {
                    ItemContextMenu itemMenu = (ItemContextMenu) object;
                    if (listener != null) listener.onClickMoreItem(item, itemMenu.getActionTag());
                }
                dialog.dismiss();
            }

            @Override
            public void onLongClick(View v, int pos, Object object) {

            }
        });
        BaseAdapter.setupVerticalMenuRecycler(activity, recyclerView, null, adapter, true);
        dialog.setContentView(sheetView);
        dialog.setOnShowListener(dialogInterface -> {

        });
        dialog.show();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public static BottomSheetDialog showSpeedDialog(Activity activity, final float currentSpeed
            , final OnClickMoreItemListener listener) {
        if (activity == null || activity.isFinishing()) return null;
        ArrayList<ItemContextMenu> data = new ArrayList<>();
        ItemContextMenu itemMenu;
        String title;
        float speed;
        int resIcon;

        speed = 0.25f;
        title = "0.25x";
        resIcon = currentSpeed == speed ? R.drawable.rm_ic_circle_selected : R.drawable.rm_ic_circle_unselected;
        itemMenu = new ItemContextMenu(title, resIcon, speed, Constants.MENU.MENU_SETTING);
        data.add(itemMenu);

        speed = 0.5f;
        title = "0.5x";
        resIcon = currentSpeed == speed ? R.drawable.rm_ic_circle_selected : R.drawable.rm_ic_circle_unselected;
        itemMenu = new ItemContextMenu(title, resIcon, speed, Constants.MENU.MENU_SETTING);
        data.add(itemMenu);

        speed = 0.75f;
        title = "0.75x";
        resIcon = currentSpeed == speed ? R.drawable.rm_ic_circle_selected : R.drawable.rm_ic_circle_unselected;
        itemMenu = new ItemContextMenu(title, resIcon, speed, Constants.MENU.MENU_SETTING);
        data.add(itemMenu);

        speed = 1.0f;
        title = activity.getString(R.string.normal);
        resIcon = currentSpeed == speed ? R.drawable.rm_ic_circle_selected : R.drawable.rm_ic_circle_unselected;
        itemMenu = new ItemContextMenu(title, resIcon, speed, Constants.MENU.MENU_SETTING);
        data.add(itemMenu);

        speed = 1.25f;
        title = "1.25x";
        resIcon = currentSpeed == speed ? R.drawable.rm_ic_circle_selected : R.drawable.rm_ic_circle_unselected;
        itemMenu = new ItemContextMenu(title, resIcon, speed, Constants.MENU.MENU_SETTING);
        data.add(itemMenu);

        speed = 1.5f;
        title = "1.5x";
        resIcon = currentSpeed == speed ? R.drawable.rm_ic_circle_selected : R.drawable.rm_ic_circle_unselected;
        itemMenu = new ItemContextMenu(title, resIcon, speed, Constants.MENU.MENU_SETTING);
        data.add(itemMenu);

        speed = 2.0f;
        title = "2.0x";
        resIcon = currentSpeed == speed ? R.drawable.rm_ic_circle_selected : R.drawable.rm_ic_circle_unselected;
        itemMenu = new ItemContextMenu(title, resIcon, speed, Constants.MENU.MENU_SETTING);
        data.add(itemMenu);

        itemMenu = new ItemContextMenu(activity.getString(R.string.btn_cancel_option), R.drawable.rm_ic_close_option, null, Constants.MENU.MENU_EXIT);
        data.add(itemMenu);

        final BottomSheetDialog dialog = new BottomSheetDialog(activity);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.rm_dialog_playback_speedd_video, null, false);
        if (ScreenManager.isLandscape(activity)) {
            try {
                int width = ScreenManager.getWidth(activity);
                int height = ScreenManager.getHeight(activity);
                sheetView.getLayoutParams().width = Math.min(width, height);
                sheetView.requestLayout();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        RecyclerView recyclerView = sheetView.findViewById(R.id.recycler_view);
        BottomSheetMenuAdapter adapter = new BottomSheetMenuAdapter(data, activity);
        adapter.setRecyclerClickListener(new RecyclerClickListener() {
            @Override
            public void onClick(View v, int pos, Object object) {
                if (object instanceof ItemContextMenu) {
                    ItemContextMenu itemMenu = (ItemContextMenu) object;
                    if (listener != null)
                        listener.onClickMoreItem(itemMenu.getObj(), itemMenu.getActionTag());
                }
                dialog.dismiss();
            }

            @Override
            public void onLongClick(View v, int pos, Object object) {

            }
        });
        BaseAdapter.setupVerticalMenuRecycler(activity, recyclerView, null, adapter, true);
        dialog.setContentView(sheetView);
        dialog.setOnShowListener(dialogInterface -> {
            final BottomSheetBehavior behavior = dialog.getBottomSheetBehavior();
            if (behavior != null) {
                behavior.setPeekHeight(ScreenManager.getHeight(activity));
                behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                    }
                });
            }
        });
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dialog.show();
        return dialog;
    }
}
