package com.lxzh123.albumdemo.view;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lxzh123.albumdemo.R;
import com.lxzh123.albumdemo.common.Constant;
import com.lxzh123.albumdemo.model.MediaBean;
import com.lxzh123.albumdemo.view.AlbumGridView.IGridImageLoader;

/**
 * description $desc$
 * author      Created by lxzh
 * date        2018/8/23
 */
public class CheckedImageView extends FrameLayout{
    private ImageView ivThumb;
    private CheckBox cbChecked;
    //interface of imageloader
    private IGridImageLoader mImageLoader;
    private MediaBean mediaBean;
    private boolean mAttached;
    private static int ViewWidth=-1;

    public ImageView getIvThumb() {
        return ivThumb;
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

    public CheckedImageView(@NonNull Context context) {
        super(context);
    }

    public CheckedImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_media_item,this);
        initView();
    }

    private void initView(){
        ivThumb=findViewById(R.id.iv_thumb);
        cbChecked=findViewById(R.id.cb_checked);
        cbChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setChecked(isChecked);
            }
        });
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
        cbChecked.setChecked(checked);
        ivThumb.setAlpha(checked?150:255);
        mediaBean.setChecked(checked);
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
//        loadImage(mediaBean.getPath());
    }

    public void setOnCheckedChangeListener(@Nullable CompoundButton.OnCheckedChangeListener listener){
        cbChecked.setOnCheckedChangeListener(listener);
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

        private CheckedImageView imageContainer;
        private MediaBean mediaBean;
        private Context context;

        public AttachImageRunnable(CheckedImageView imageContainer, MediaBean mediaBean,Context context) {
            this.imageContainer = imageContainer;
            this.mediaBean = mediaBean;
            this.context = context;
        }

        @Override
        public void run() {
            if (mImageLoader != null) {
                mImageLoader.displayGridImage(getContext(), mediaBean.getThumbPath(),
                        imageContainer.getIvThumb(), Constant.IMG_THUMBNAIL_SCALRE);

            } else {
                Log.w("NineGridView", "Can not display the image of NineGridView, you'd better set a imageloader!!!!");
            }
        }
    }
}
