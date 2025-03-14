package com.example.livestream_update.Ringme.Base;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vtm.ringme.utils.Log;


public class CustomGridLayoutManager extends GridLayoutManager {

    public CustomGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        // TODO Auto-generated constructor stub
    }

    public CustomGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
        // TODO Auto-generated constructor stub
    }

    public CustomGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("CustomGridLayoutManager", e);
        } catch (Exception e) {
            Log.e("CustomGridLayoutManager", e);
        }
    }
}
