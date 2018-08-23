package com.lxzh123.albumdemo.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lxzh123.albumdemo.R;

/**
 * description $desc$
 * author      Created by lxzh
 * date        2018/8/23
 */
public class HeaderViewHolder extends RecyclerView.ViewHolder {

    TextView tvTimeInfo;
    TextView tvSelect;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        tvTimeInfo=(TextView)itemView.findViewById(R.id.tv_time_info);
        tvSelect=(TextView)itemView.findViewById(R.id.tv_select_unselect_all);
    }

    public void render(String text){
        tvTimeInfo.setText(text);
    }
}
