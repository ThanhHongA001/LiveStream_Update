/*
 * Copyright (c) https://bigzun.blogspot.com/
 * Email: bigzun.com@gmail.com
 * Created by namnh40 on 2019/6/12
 *
 */

package com.example.livestream_update.Ringme.Base;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vtm.R;
import com.vtm.ringme.utils.Log;

import java.util.ArrayList;

import butterknife.ButterKnife;

public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder, T> extends RecyclerView.Adapter<VH> {

    public static final int TYPE_EMPTY = -1;

    protected final String TAG = getClass().getSimpleName();
    protected final LayoutInflater layoutInflater;
    protected ArrayList<T> items;
    protected Activity activity;

    public BaseAdapter(Activity activity) {
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(this.activity);
    }

    public BaseAdapter(Activity activity, ArrayList<T> list) {
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(this.activity);
        this.items = list;
    }

    public static void setupGridRecycler(Context context, RecyclerView recyclerView, GridLayoutManager layoutManager
            , RecyclerView.Adapter adapter, int spanCount, int resIdSpacing, boolean includeEdge) {
        if (context != null && recyclerView != null && adapter != null) {
            if (layoutManager == null)
                layoutManager = new CustomGridLayoutManager(context, spanCount);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            if (recyclerView.getItemDecorationCount() <= 0 && resIdSpacing > 0)
                recyclerView.addItemDecoration(new DetailGridItemDecoration(spanCount
                        , context.getResources().getDimensionPixelOffset(resIdSpacing), includeEdge));
            recyclerView.setAdapter(adapter);
        }
    }

    public static void setupVerticalRecycler(Context context, RecyclerView recyclerView
            , LinearLayoutManager layoutManager, RecyclerView.Adapter adapter, boolean addDecoration) {
        if (context != null && recyclerView != null && adapter != null) {
            if (layoutManager == null)
                layoutManager = new CustomLinearLayoutManager(context, LinearLayout.VERTICAL, false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setFocusable(false);
            recyclerView.setLayoutManager(layoutManager);
            if (recyclerView.getItemDecorationCount() <= 0 && addDecoration)
                recyclerView.addItemDecoration(new DividerItemDecoration(context, R.drawable.rm_divider_default));
            recyclerView.setAdapter(adapter);
        }
    }

    public static void setupVerticalMenuRecycler(Context context, RecyclerView recyclerView
            , LinearLayoutManager layoutManager, RecyclerView.Adapter adapter, boolean addDecoration) {
        if (context != null && recyclerView != null && adapter != null) {
            if (layoutManager == null)
                layoutManager = new CustomLinearLayoutManager(context, LinearLayout.VERTICAL, false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setFocusable(false);
            recyclerView.setLayoutManager(layoutManager);
            if (recyclerView.getItemDecorationCount() <= 0 && addDecoration)
                recyclerView.addItemDecoration(new DividerMenuItemDecoration(context, LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(adapter);
        }
    }

    public static void setupVerticalRecycler(Context context, RecyclerView recyclerView
            , LinearLayoutManager layoutManager, RecyclerView.Adapter adapter, int resDivider) {
        if (context != null && recyclerView != null && adapter != null) {
            if (layoutManager == null)
                layoutManager = new CustomLinearLayoutManager(context, LinearLayout.VERTICAL, false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setFocusable(false);
            recyclerView.setLayoutManager(layoutManager);
            if (recyclerView.getItemDecorationCount() <= 0 && resDivider > 0)
                recyclerView.addItemDecoration(new DividerItemDecoration(context, resDivider, true));
            recyclerView.setAdapter(adapter);
        }
    }

    public static void setupVerticalRecyclerView(Context context, RecyclerView recyclerView
            , LinearLayoutManager layoutManager, RecyclerView.Adapter adapter, boolean addDecoration, int cacheSize) {
        if (context != null && recyclerView != null && adapter != null) {
            if (layoutManager == null)
                layoutManager = new CustomLinearLayoutManager(context, LinearLayout.VERTICAL, false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            if (cacheSize > 0) {
                layoutManager.setInitialPrefetchItemCount(cacheSize);
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemViewCacheSize(cacheSize);
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            }
            if (recyclerView.getItemDecorationCount() <= 0 && addDecoration)
                recyclerView.addItemDecoration(new DividerItemDecoration(context, R.drawable.rm_divider_default, true));
            recyclerView.setAdapter(adapter);
        }
    }

    public static void setupHorizontalRecycler(Context context, RecyclerView recyclerView
            , LinearLayoutManager layoutManager, RecyclerView.Adapter adapter, boolean addDecoration) {
        if (context != null && recyclerView != null && adapter != null) {
            if (layoutManager == null)
                layoutManager = new CustomLinearLayoutManager(context, LinearLayout.HORIZONTAL, false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setFocusable(false);
            recyclerView.setLayoutManager(layoutManager);
            if (recyclerView.getItemDecorationCount() <= 0 && addDecoration)
                recyclerView.addItemDecoration(new DividerItemDecoration(context, R.drawable.rm_divider_default, true));
            recyclerView.setAdapter(adapter);
        }
    }

    public static void setupHorizontalRecyclerV5(Context context, RecyclerView recyclerView
            , LinearLayoutManager layoutManager, RecyclerView.Adapter adapter, boolean addDecoration) {
        if (context != null && recyclerView != null && adapter != null) {
            if (layoutManager == null)
                layoutManager = new CustomLinearLayoutManager(context, LinearLayout.HORIZONTAL, false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setFocusable(false);
            recyclerView.setLayoutManager(layoutManager);
            if (recyclerView.getItemDecorationCount() <= 0 && addDecoration)
                recyclerView.addItemDecoration(new DividerItemDecoration(context, R.drawable.rm_divider_horizonal_v5, true));
            recyclerView.setAdapter(adapter);
        }
    }

    public static void setupHorizontalRecycler(Context context, RecyclerView recyclerView
            , LinearLayoutManager layoutManager, RecyclerView.Adapter adapter, int resDivider) {
        if (context != null && recyclerView != null && adapter != null) {
            if (layoutManager == null)
                layoutManager = new CustomLinearLayoutManager(context, LinearLayout.HORIZONTAL, false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setFocusable(false);
            recyclerView.setLayoutManager(layoutManager);
            if (recyclerView.getItemDecorationCount() <= 0 && resDivider > 0)
                recyclerView.addItemDecoration(new DividerItemDecoration(context, resDivider, true));
            recyclerView.setAdapter(adapter);
        }
    }

    public static void setRecyclerViewLoadMore(RecyclerView recyclerView, final OnLoadMoreListener listener) {
        if (recyclerView != null && recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                final int visibleThreshold = 3;
                int lastVisibleItem, totalItemCount, previousTotal;
                boolean isLoading;

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = layoutManager.getItemCount();
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    if (isLoading) {
                        if (totalItemCount > previousTotal) {
                            isLoading = false;
                        }
                    }
                    previousTotal = totalItemCount;

                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        Log.d("BaseAdapter", "LoadMore nao ....");
                        if (listener != null) {
                            listener.onLoadMore();
                            isLoading = true;
                        }
                    }
                }
            });
        }
    }

    public ArrayList<T> getItems() {
        return items;
    }

    public void setItems(ArrayList<T> items) {
        this.items = items;
    }

    public T getItem(int position) {
        if (items != null && position >= 0 && position < items.size())
            return items.get(position);
        return null;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public Activity getActivity() {
        return activity;
    }

    public abstract int getItemViewType(int position);

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindData(Object item, int position) {
        }

    }

    public static class EmptyHolder extends ViewHolder {

        public EmptyHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.rm_holder_empty, parent, false));
        }
    }
}
