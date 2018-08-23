package com.lxzh123.albumdemo.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lxzh123.albumdemo.R;
import com.lxzh123.albumdemo.view.AlbumGridView.GridBean;

import java.util.List;

/**
 * description 自定义相册选择器适配器
 * author      Created by lxzh
 * date        2018/8/22
 */
public class AlbumSelectAdapter extends BaseAdapter {
    private Context context;
    private List<GridBean> dataList;

    public AlbumSelectAdapter(Context context, List<GridBean> data) {
        this.context=context;
        this.dataList=data;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
//            LayoutInflater inflater=(LayoutInflater.from(context));
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.layout_media_list_item,null);

            viewHolder=new ViewHolder();
            viewHolder.tvTimeInfo=(TextView)convertView.findViewById(R.id.tv_time_info);
            viewHolder.tvSelect=(TextView)convertView.findViewById(R.id.tv_time_info);
//            viewHolder.gridView=(AlbumSelectGridView)convertView.findViewById(R.id.asg_images);

            convertView.setTag(viewHolder);

        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }

        return convertView;
    }

    static class ViewHolder{
        TextView tvTimeInfo;
        TextView tvSelect;
        CheckedImageView gridView;
    }
}
