package com.example.livestream_update.Ringme.Base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private final Drawable mDivider;
    private int mOrientation;
    private boolean includeEdge;

    public DividerItemDecoration(Context context, int divider) {
        mDivider = context.getResources().getDrawable(divider);
    }

    public DividerItemDecoration(Context context, int divider, boolean includeEdge) {
        mDivider = context.getResources().getDrawable(divider);
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mDivider != null) {
            mOrientation = getOrientation(parent);
            if (mOrientation == LinearLayoutManager.VERTICAL) {
                if (includeEdge) {
                    if (parent.getChildAdapterPosition(view) == 0)
                        outRect.set(0, mDivider.getIntrinsicHeight(), 0, mDivider.getIntrinsicHeight());
                    else
                        outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
                } else {
                    outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
                }
            } else {
                if (includeEdge) {
                    if (parent.getChildAdapterPosition(view) == 0)
                        outRect.set(mDivider.getIntrinsicWidth(), 0, mDivider.getIntrinsicWidth(), 0);
                    else
                        outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
                } else {
                    outRect.set(mDivider.getIntrinsicWidth(), 0, 0, 0);
                }
            }
        } else {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }

    private int getOrientation(RecyclerView parent) {
        if (!(parent.getLayoutManager() instanceof LinearLayoutManager))
            throw new IllegalStateException("Layout manager must be an instance of LinearLayoutManager");
        return ((LinearLayoutManager) parent.getLayoutManager()).getOrientation();
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            drawHorizontalDividers(canvas, parent);
        } else if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVerticalDividers(canvas, parent);
        }
    }

    private void drawHorizontalDividers(Canvas canvas, RecyclerView parent) {
        int parentTop = parent.getPaddingTop();
        int parentBottom = parent.getHeight() - parent.getPaddingBottom();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int parentLeft = child.getRight() + params.rightMargin;
            int parentRight = parentLeft + mDivider.getIntrinsicWidth();
            mDivider.setBounds(parentLeft, parentTop, parentRight, parentBottom);
            mDivider.draw(canvas);
        }
    }

    private void drawVerticalDividers(Canvas canvas, RecyclerView parent) {
        int parentLeft = parent.getPaddingLeft();
        int parentRight = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int parentTop = child.getBottom() + params.bottomMargin;
            int parentBottom = parentTop + mDivider.getIntrinsicHeight();
            mDivider.setBounds(parentLeft, parentTop, parentRight, parentBottom);
            mDivider.draw(canvas);
        }
    }
}