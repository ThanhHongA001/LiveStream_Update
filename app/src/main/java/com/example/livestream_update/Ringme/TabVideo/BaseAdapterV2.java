package com.example.livestream_update.Ringme.TabVideo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.vtm.R;
import com.vtm.ringme.ApplicationController;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseAdapterV2<T extends Object, F extends RecyclerView.LayoutManager, E extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<E> {

    public static final int SPACE = -1;
    public static final int NORMAL = 0;
    public static final int TYPE_END = -1;
    public static final int TYPE_LOAD_MORE = -2;
    public static final int TYPE_EMPTY = -3;

    protected final LayoutInflater layoutInflater;
    protected final ArrayList<T> items = new ArrayList<>();
    protected Activity activity;
    protected Context context;
    protected int numberHolderLoadMore = 3;
    protected OnLoadMoreListener onLoadMoreListener;

    public BaseAdapterV2(Activity activity) {
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(this.activity);
    }

    public BaseAdapterV2(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    public void setNumberHolderLoadMore(int numberHolderLoadMore) {
        this.numberHolderLoadMore = numberHolderLoadMore;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public ArrayList<T> getItems() {
        return items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public final void bindData(ArrayList<T> results) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(results, items));
        items.clear();
        items.addAll(results);
        diffResult.dispatchUpdatesTo(this);
    }

    public final void updateData(ArrayList<T> results) {
        items.clear();
        items.addAll(results);
    }

    public Activity getActivity() {
        return activity;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull E holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getAdapterPosition();
        if (getItemCount() - numberHolderLoadMore < position && onLoadMoreListener != null)
            onLoadMoreListener.onLoadMore();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull E holder) {
        super.onViewDetachedFromWindow(holder);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindData(ArrayList<Object> items, int position, @NonNull List<Object> payloads) {
        }

        public void bindData(ArrayList<Object> items, int position) {
        }

        public void onViewAttachedToWindow() {
        }

        public void onViewDetachedFromWindow() {
        }
    }

    public static class LoadMoreHolder extends ViewHolder {

        public LoadMoreHolder(Activity activity, LayoutInflater layoutInflater, ViewGroup parent) {
            this(layoutInflater.inflate(R.layout.rm_sample_common_list_footer, parent, false));
        }

        public LoadMoreHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bindData(ArrayList<Object> items, int position) {
            super.bindData(items, position);
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams staggeredGridParams = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                staggeredGridParams.setFullSpan(true);
                itemView.setLayoutParams(staggeredGridParams);
            }
        }
    }

    public static class EndHolder extends ViewHolder {

        public EndHolder(Activity activity, LayoutInflater layoutInflater, ViewGroup parent) {
            this(layoutInflater.inflate(R.layout.rm_item_end, parent, false));
        }

        EndHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bindData(ArrayList<Object> items, int position) {
            super.bindData(items, position);
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams staggeredGridParams = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                staggeredGridParams.setFullSpan(true);
                itemView.setLayoutParams(staggeredGridParams);
            }
        }
    }

    public static class SpaceHolder extends ViewHolder {

        public SpaceHolder(int margin) {
            super(provideView(margin));
        }

        private static View provideView(int margin) {
            Context context = ApplicationController.self();
            View view = new View(context);
            view.setLayoutParams(new ViewGroup.LayoutParams(margin, margin));
            return view;
        }
    }

    public static class EmptyHolder extends ViewHolder {

        public EmptyHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.rm_holder_empty, parent, false));
        }
    }

    private class DiffCallback extends DiffUtil.Callback {

        List<T> oldObjects;
        List<T> newObjects;

        DiffCallback(List<T> newObjects, List<T> oldObjects) {
            this.newObjects = newObjects;
            this.oldObjects = oldObjects;
        }

        @Override
        public int getOldListSize() {
            return oldObjects.size();
        }

        @Override
        public int getNewListSize() {
            return newObjects.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldObjects.get(oldItemPosition).equals(newObjects.get(newItemPosition));
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldObjects.get(oldItemPosition).equals(newObjects.get(newItemPosition));
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }
    }

}

