package com.example.livestream_update.Ringme.LiveStream.listener;


import android.view.View;
import android.view.View.OnClickListener;

/**
 * Created by toanvk2 on 6/28/14.
 */
public class ClickListener implements OnClickListener {
    private int mId;
    private IconListener mCallBack;
    private Object mEntry;

    public ClickListener(Object entry, IconListener callBack, int menuId) {
        this.mEntry = entry;
        this.mId = menuId;
        this.mCallBack = callBack;
    }

    @Override
    public void onClick(View v) {
        if (mCallBack == null)
            return;
        mCallBack.onIconClickListener(v, mEntry, mId);
    }


    public interface IconListener {
        void onIconClickListener(View view, Object entry, int menuId);
    }

    // checkbox
    public interface CheckboxListener {
        void onChecked(boolean isChecked);
    }
}
