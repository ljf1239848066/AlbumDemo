package com.lxzh123.albumdemo.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lxzh123.albumdemo.R;

/**
 * description $desc$
 * author      Created by lxzh
 * date        2018/8/23
 */
public class MediaDateGroupViewHolder extends RecyclerView.ViewHolder {

    View containerView;

    public MediaDateGroupViewHolder(View itemView) {
        super(itemView);
        containerView=(View)itemView.findViewById(R.id.container);
    }

    public void render(String text, int color){
        containerView.setBackgroundColor(color);
    }
}
