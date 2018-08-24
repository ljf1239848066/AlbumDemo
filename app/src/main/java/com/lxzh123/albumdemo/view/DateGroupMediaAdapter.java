package com.lxzh123.albumdemo.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lxzh123.albumdemo.R;
import com.lxzh123.albumdemo.dao.MediaDao;
import com.lxzh123.albumdemo.model.MediaBean;
import com.lxzh123.albumdemo.model.MediaDateGroupBean;
import com.lxzh123.albumdemo.model.MediaType;

import java.util.ArrayList;
import java.util.List;

/**
 * description 媒体文件时间分组列表适配器
 * author      Created by lxzh
 * date        2018/8/23
 */
public class DateGroupMediaAdapter extends GroupRecyclerViewAdapter<DateGroupMediaAdapter.HeaderViewHolder,
        DateGroupMediaAdapter.MediaDateGroupViewHolder> implements OnSelectDeselectAllClickedListener {

    private final String TAG="DateGroupMediaAdapter";
    protected Context context = null;
    private List<MediaDateGroupBean> mediaDateGroupBeanList;
    private OnMediaCheckedChangedListener onMediaCheckedChangedListener;
    private CheckableRecyclerView recyclerView;

    public DateGroupMediaAdapter(Context context,CheckableRecyclerView recyclerView) {
        this.context = context;
        this.recyclerView=recyclerView;
        mediaDateGroupBeanList=new ArrayList<>();
    }

    public void setMediaDateGroupBeanList(List<MediaDateGroupBean> mediaDateGroupBeanList) {
        this.mediaDateGroupBeanList = mediaDateGroupBeanList;
    }

    @Override
    protected int getItemCountForSection(int section) {
        return mediaDateGroupBeanList.get(section).getMediaBeans().size();
    }

    @Override
    protected int getSectionCount() {
        return mediaDateGroupBeanList.size();
    }

    protected LayoutInflater getLayoutInflater(){
        return LayoutInflater.from(context);
    }

    @Override
    protected HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.layout_media_date_head, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    protected MediaDateGroupViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.layout_media_media_item, parent, false);
        return new MediaDateGroupViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(HeaderViewHolder holder, int section) {
        String txtTimeInfo=mediaDateGroupBeanList.get(section).getMediaBeans().get(0).getTimeStr();
        holder.setTimeInfoText(txtTimeInfo);
        holder.setSelectStatusText(context.getString(R.string.select_all));
        holder.section=section;
        holder.setOnSelectDeselectAlClickedListener(this);
    }

    @Override
    protected void onBindItemViewHolder(MediaDateGroupViewHolder holder, int section, int position) {
        MediaBean bean=mediaDateGroupBeanList.get(section).getMediaBeans().get(position);
        bindItem(holder,section,position,bean);
//        holder.ivMedia.setImageLoader(new ImageLoader());
//        holder.ivMedia.setImageLoader(new GlideImageLoader());
//        holder.ivMedia.loadImage(bean.getPath());
        Glide.with(context).load(bean.getPath()).centerCrop().into(holder.ivMedia.getImageView());
    }

    @Override
    public void onBindItemViewHolderEx(MediaDateGroupViewHolder holder,int section, int position) {
        MediaBean bean=mediaDateGroupBeanList.get(section).getMediaBeans().get(position);
        bindItem(holder,section,position,bean);
    }

    private void bindItem(MediaDateGroupViewHolder holder, int section, int position, final MediaBean bean){
        holder.ivMedia.setMediaBean(bean);
        holder.ivMedia.setChecked(bean.isChecked());
//        holder.section=section;
//        holder.position=position;
        if(bean.getMediaType()== MediaType.VEDIO){
            holder.ivMedia.setTime(bean.getDurationStr());
        }else{
            holder.ivMedia.hideVideoTime();
        }
        holder.ivMedia.setOnCheckedChangedListener(new CheckableMediaView.OnCheckedChangedListener() {
            @Override
            public void OnCheckedChanged(View view) {
                Log.d(TAG,"bindItem:OnCheckedChanged:");
                recyclerView.OnMediaCheckedChangeListener(null);
            }
        });
    }

    @Override
    public void onSelectAllCheckedChanged(View view, int section, boolean isSelectAll) {
//        RecyclerView recyclerView=(RecyclerView)view.getParent();
//        GridLayoutManager gridLayoutManager=(GridLayoutManager) recyclerView.getLayoutManager();
//        int firstItem=gridLayoutManager.findFirstCompletelyVisibleItemPosition();
//        int lastItem=gridLayoutManager.findLastCompletelyVisibleItemPosition();

        List<MediaBean> mediaBeans=mediaDateGroupBeanList.get(section).getMediaBeans();
        int cnt=mediaBeans.size();
        int startIdx=0;
        for(int i=0;i<section;i++){
            startIdx++;//分组时间视图计数加1
            startIdx+=mediaDateGroupBeanList.get(i).getMediaBeans().size();
        }
        startIdx++;
        int curIdx=startIdx;
        for(int i=0;i<cnt;i++){
            mediaBeans.get(i).setChecked(isSelectAll);
//            curIdx=startIdx+i;
//            notifyItemChanged(curIdx,1);//局部刷新，调用重载的方法 不刷新ImageView
        }
        //随便传一个"1"作为payload进行局部刷新，调用重载的方法 不刷新ImageView
        Log.d(TAG,"onSelectAllCheckedChanged:1");
        recyclerView.BlockCheckChangedEvent();
        notifyItemRangeChanged(curIdx,curIdx+cnt-1,1);
        recyclerView.CancelBlockCheckChangedEvent();
        Log.d(TAG,"onSelectAllCheckedChanged:2");
        recyclerView.OnMediaCheckedChangeListener(view);
        Log.d(TAG,"onSelectAllCheckedChanged:3");
    }

    public List<MediaBean> getCheckedItem(){
        return MediaDao.getCheckedItem(mediaDateGroupBeanList);
    }

    public boolean hasCheckedItem(){
        return MediaDao.hasCheckedItem(mediaDateGroupBeanList);
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView tvTimeInfo;
        SelectDeselectTextView tvSelect;
        int section;
        OnSelectDeselectAllClickedListener mOnSelectDeselectAllClickedListener;

        public HeaderViewHolder(final View itemView) {
            super(itemView);
            tvTimeInfo=itemView.findViewById(R.id.tv_time_info);
            tvSelect=itemView.findViewById(R.id.tv_select_unselect_all);
            Log.d(TAG,"HeaderViewHolder:Init");
            tvSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"HeaderViewHolder:onClick1");
                    if(mOnSelectDeselectAllClickedListener !=null){
                        Log.d(TAG,"HeaderViewHolder:onClick2");
                        mOnSelectDeselectAllClickedListener.onSelectAllCheckedChanged(itemView,section,tvSelect.isSelectAll());
                    }

                    Log.d(TAG,"HeaderViewHolder:onClick3");
                    tvSelect.setSelectAll(!tvSelect.isSelectAll());
                    tvSelect.setText(tvSelect.isSelectAll()?R.string.select_all:R.string.deselect_all);
                }
            });
        }

        public void setTimeInfoText(String text){
            tvTimeInfo.setText(text);
        }

        public void setSelectStatusText(String text){
            tvSelect.setText(text);
        }

        public void setOnSelectDeselectAlClickedListener(OnSelectDeselectAllClickedListener listener){
            mOnSelectDeselectAllClickedListener =listener;
        }
    }

    class MediaDateGroupViewHolder extends RecyclerView.ViewHolder {

        CheckableMediaView ivMedia;
        MediaBean bean;
//        int section;
//        int position;

        public MediaDateGroupViewHolder(final View itemView) {
            super(itemView);
            ivMedia=itemView.findViewById(R.id.cmv_media);
        }
    }
}
