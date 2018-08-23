package com.lxzh123.albumdemo.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * description $desc$
 * author      Created by lxzh
 * date        2018/8/23
 */
public class DateGroupMediaAdapter extends GroupRecyclerViewAdapter<HeaderViewHolder,
        MediaDateGroupViewHolder> {

    protected Context context = null;

    public DateGroupMediaAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected int getItemCountForSection(int section) {
        return section + 1;
    }

    @Override
    protected int getSectionCount() {
        return 5;
    }

    protected LayoutInflater getLayoutInflater(){
        return LayoutInflater.from(context);
    }

    @Override
    protected HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
//        View view = getLayoutInflater().inflate(R.layout.view_count_header, parent, false);
//        return new HeaderViewHolder(view);
        return null;
    }

    @Override
    protected MediaDateGroupViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
//        View view = getLayoutInflater().inflate(R.layout.view_count_item, parent, false);
//        return new MediaDateGroupViewHolder(view);
        return null;
    }

    @Override
    protected void onBindSectionHeaderViewHolder(HeaderViewHolder holder, int section) {
        holder.render("Section " + (section + 1));
    }

    protected int[] colors = new int[]{0xfff44336, 0xff2196f3, 0xff009688, 0xff8bc34a, 0xffff9800};
    @Override
    protected void onBindItemViewHolder(MediaDateGroupViewHolder holder, int section, int position) {
        holder.render(String.valueOf(position + 1), colors[section]);
    }
}
