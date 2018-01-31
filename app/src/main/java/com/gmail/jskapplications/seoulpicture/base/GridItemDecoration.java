package com.gmail.jskapplications.seoulpicture.base;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by for on 2017-10-14.
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int mTop;
    private int mMiddle;
    private int mMargin;  // Left and Right margin

    public GridItemDecoration(int top, int middle, int margin) {
        mTop = top;
        mMiddle = middle;
        mMargin = margin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view);

        StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams)view .getLayoutParams();
        int spanIndex = lp.getSpanIndex();

        // Add top margin only for the first item to avoid double space between items
        if (position == 0
                || parent.getChildLayoutPosition(view) == 1) {
            outRect.top = mTop;
        } else {
            outRect.top = 0;
        }

        outRect.bottom = mMiddle;
        if(position >= 0) {
            //if(spanIndex == 0) {
            outRect.right= mMiddle/2;
            outRect.left= mMiddle/2;
            //} else {
            //    outRect.right= mMargin;
            //}
        }
    }
}
