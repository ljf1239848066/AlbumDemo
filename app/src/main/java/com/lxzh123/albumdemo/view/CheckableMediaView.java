package com.lxzh123.albumdemo.view;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxzh123.albumdemo.R;
import com.lxzh123.albumdemo.common.Constant;
import com.lxzh123.albumdemo.model.MediaBean;
import com.lxzh123.albumdemo.view.AlbumGridView.IGridImageLoader;

/**
 * description 支持选中状态变化的媒体文件展示视图
 * author      Created by lxzh
 * date        2018/8/23
 */
public class CheckableMediaView extends FrameLayout{
    private final String TAG="CheckableMediaView";
    private ImageView ivThumb;
    private CheckBox cbChecked;
    private ImageView ivFlag;
    private TextView tvTime;
    //interface of imageloader
    private IGridImageLoader mImageLoader;
    private MediaBean mediaBean;
    private boolean mAttached;
    private OnCheckedChangedListener mOnCheckedChangedListener;

    public ImageView getImageView() {
        return ivThumb;
    }

    public CheckBox getCheckBox() {
        return cbChecked;
    }

    public void setMediaBean(MediaBean mediaBean) {
        this.mediaBean = mediaBean;
    }

    public IGridImageLoader getImageLoader() {
        return mImageLoader;
    }

    public void setImageLoader(IGridImageLoader mageLoader) {
        this.mImageLoader = mageLoader;
    }

    public CheckableMediaView(@NonNull Context context) {
        super(context);
    }

    public CheckableMediaView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_checked_media_view,this);
        initView();
    }

    private void initView(){
        ivThumb=findViewById(R.id.iv_thumb);
        cbChecked=findViewById(R.id.cb_checked);
        ivFlag=findViewById(R.id.iv_flag);
        tvTime=findViewById(R.id.tv_video_time);
        cbChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG,"cbChecked.onCheckedChanged");
                setChecked(isChecked);
                if(mOnCheckedChangedListener!=null){
                    mOnCheckedChangedListener.OnCheckedChanged(cbChecked);
                }
            }
        });
    }

    public void setOnCheckedChangedListener(OnCheckedChangedListener listener){
        mOnCheckedChangedListener=listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0,widthMeasureSpec),getDefaultSize(0,heightMeasureSpec));
        int childwid=getMeasuredWidth();
        heightMeasureSpec=widthMeasureSpec=MeasureSpec.makeMeasureSpec(childwid,MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public void setChecked(boolean checked){
        mediaBean.setChecked(checked);
        cbChecked.setChecked(checked);
        ivThumb.setAlpha(checked?150:255);
    }

    /**
     * 设置视频时间
     * @param text 视频时间字符串
     */
    public void setTime(String text){
        ivFlag.setVisibility(VISIBLE);
        tvTime.setVisibility(VISIBLE);
        tvTime.setText(text);
    }

    /**
     * 隐藏视频标记与时间
     */
    public void hideVideoTime(){
        ivFlag.setVisibility(GONE);
        tvTime.setVisibility(GONE);
        tvTime.setText("");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttached = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAttached = false;
    }

    public boolean getAttached(){
        return mAttached;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    interface OnCheckedChangedListener{
        public void OnCheckedChanged(View view);
    }

    public void loadImage(String path){
        AttachImageRunnable runnable=new AttachImageRunnable(
                this,mediaBean,getContext());
        if(this.getAttached()){
            this.post(runnable);
        }else{
            Handler handler=new Handler();
            handler.post(runnable);
        }
    }

    private class AttachImageRunnable implements Runnable{

        private CheckableMediaView imageContainer;
        private MediaBean mediaBean;
        private Context context;

        public AttachImageRunnable(CheckableMediaView imageContainer, MediaBean mediaBean, Context context) {
            this.imageContainer = imageContainer;
            this.mediaBean = mediaBean;
            this.context = context;
        }

        @Override
        public void run() {
            if (mImageLoader != null) {
                mImageLoader.displayGridImage(getContext(), mediaBean.getThumbPath(),
                        imageContainer.getImageView(), Constant.IMG_THUMBNAIL_SCALRE);

            } else {
                Log.w("NineGridView", "Can not display the image of NineGridView, you'd better set a imageloader!!!!");
            }
        }
    }
}
