package com.lxzh123.albumdemo.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * description $desc$
 * author      Created by lxzh
 * date        2018/8/23
 */
public class GridDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int mSpace;
    private boolean includeEdge;
    private int mEdgeSpace;

    public GridDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.mSpace = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left=mSpace;
        outRect.right=mSpace;
        outRect.top=mSpace;
        outRect.bottom=mSpace;
    }
}