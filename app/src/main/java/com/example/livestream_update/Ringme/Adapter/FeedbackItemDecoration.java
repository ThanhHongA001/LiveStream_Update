package com.example.livestream_update.Ringme.Adapter;


import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class FeedbackItemDecoration extends RecyclerView.ItemDecoration {
    private final int padding;
    private final boolean includeEdge;
    private final int spanCount;

    public FeedbackItemDecoration(int padding, boolean includeEdge, int spanCount) {
        this.padding = padding;
        this.includeEdge = includeEdge;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column
        if (includeEdge) {
            outRect.left = padding - column * padding / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * padding / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = padding;
            }
            outRect.bottom = padding; // item bottom
        } else {
            outRect.left = column * padding / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = padding - (column + 1) * padding / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = padding; // item top
            }
        }
    }


}
