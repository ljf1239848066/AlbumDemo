package com.lxzh123.albumdemo.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lxzh123.albumdemo.R;
import com.lxzh123.albumdemo.model.MediaBean;
import com.lxzh123.albumdemo.model.MediaDateGroupBean;
import com.lxzh123.albumdemo.util.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * description $desc$
 * author      Created by lxzh
 * date        2018/8/23
 */
public class DateGroupMediaAdapter extends GroupRecyclerViewAdapter<DateGroupMediaAdapter.HeaderViewHolder,
        DateGroupMediaAdapter.MediaDateGroupViewHolder> {

    private final String TAG="DateGroupMediaAdapter";
    protected Context context = null;
    private List<MediaDateGroupBean> mediaDateGroupBeanList;

    public DateGroupMediaAdapter(Context context) {
        this.context = context;
        mediaDateGroupBeanList=new ArrayList<>();
    }

    public void setMediaDateGroupBeanList(List<MediaDateGroupBean> mediaDateGroupBeanList) {
        this.mediaDateGroupBeanList = mediaDateGroupBeanList;
    }

    @Override
    protected int getItemCountForSection(int section) {
        Log.d(TAG,"getItemCountForSection:cnt="+mediaDateGroupBeanList.get(section).getMediaBeans().size());
        return mediaDateGroupBeanList.get(section).getMediaBeans().size();
    }

    @Override
    protected int getSectionCount() {
        Log.d(TAG,"getSectionCount:cnt="+mediaDateGroupBeanList.size());
        return mediaDateGroupBeanList.size();
    }

    protected LayoutInflater getLayoutInflater(){
        return LayoutInflater.from(context);
    }

    @Override
    protected HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.layout_album_date_head, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    protected MediaDateGroupViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.layout_album_list_item, parent, false);
        return new MediaDateGroupViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(HeaderViewHolder holder, int section) {
        String txtTimeInfo=mediaDateGroupBeanList.get(section).getMediaBeans().get(0).getTimeStr();
        holder.setTimeInfoText(txtTimeInfo);
        holder.setSelectStatusText(context.getString(R.string.select_all));
    }

    @Override
    protected void onBindItemViewHolder(MediaDateGroupViewHolder holder, int section, int position) {
        MediaBean bean=mediaDateGroupBeanList.get(section).getMediaBeans().get(position);
        holder.ivMedia.setMediaBean(bean);
        holder.ivMedia.setImageLoader(new ImageLoader());
        holder.ivMedia.loadImage(bean.getPath());
        Log.d(TAG,"onBindItemViewHolder:s="+section+",p="+position+",path="+bean.getPath());
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView tvTimeInfo;
        TextView tvSelect;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            tvTimeInfo=(TextView)itemView.findViewById(R.id.tv_time_info);
            tvSelect=(TextView)itemView.findViewById(R.id.tv_select_unselect_all);
        }

        public void setTimeInfoText(String text){
            tvTimeInfo.setText(text);
        }

        public void setSelectStatusText(String text){
            tvSelect.setText(text);
        }
    }

    class MediaDateGroupViewHolder extends RecyclerView.ViewHolder {

        CheckedImageView ivMedia;

        public MediaDateGroupViewHolder(View itemView) {
            super(itemView);
            ivMedia=(CheckedImageView)itemView.findViewById(R.id.civ_media);
        }
    }

}
