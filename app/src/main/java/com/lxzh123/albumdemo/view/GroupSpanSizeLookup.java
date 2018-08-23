package com.lxzh123.albumdemo.view;

import android.support.v7.widget.GridLayoutManager;

/**
 * description $desc$
 * author      Created by lxzh
 * date        2018/8/23
 */
public class GroupSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    protected GroupRecyclerViewAdapter<?, ?> adapter = null;
    protected GridLayoutManager layoutManager = null;

    public GroupSpanSizeLookup(GroupRecyclerViewAdapter<?, ?> adapter, GridLayoutManager layoutManager) {
        this.adapter = adapter;
        this.layoutManager = layoutManager;
    }

    @Override
    public int getSpanSize(int position) {

        if(adapter.isSectionHeaderPosition(position)){
            return layoutManager.getSpanCount();
        }else{
            return 1;
        }

    }
}