package com.lxzh123.albumdemo.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * description 相册选择列表单元格容器
 * author      Created by lxzh
 * date        2018/8/22
 */
public class AlbumSelectGirdContainer extends FrameLayout
{
    private int mWidth = 0, mHeight = 0;
    private GridImageView mImageView;
    private ImageView mImgSelected;
    private boolean mIsSelectedMode;
    private onClickSelectListener mListener;
    private int mImageWidth, mImageHeight;

    public AlbumSelectGirdContainer(Context context)
    {
        super(context);
        init(context, null);
    }

    public AlbumSelectGirdContainer(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        setWillNotDraw(false);
        LayoutParams lpImg=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lpImg.gravity= Gravity.CENTER;
        mImageView=new GridImageView(context);
        mImageView.setLayoutParams(lpImg);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        addView(mImageView);

        LayoutParams lpSelected=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lpSelected.gravity= Gravity.TOP|Gravity.RIGHT;
        mImgSelected=new ImageView(context);
        mImgSelected.setLayoutParams(lpSelected);
        if(Build.VERSION.SDK_INT>=23) {
            mImgSelected.setForegroundGravity(Gravity.TOP | Gravity.RIGHT);
        }
        mImgSelected.setScaleType(ImageView.ScaleType.FIT_XY);

        addView(mImgSelected);

        mImgSelected.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mListener != null)
                    mListener.onClickSelect();
            }
        });
        setIsSeletedMode(mIsSelectedMode);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);

        //Measure the size of the select button
        int selSize = mWidth * 1 / 5;
        int selMode = MeasureSpec.EXACTLY;
        int selSpec = MeasureSpec.makeMeasureSpec(selSize, selMode);
        mImgSelected.measure(selSpec, selSpec);
        //Measure the size of imageView
        mImageWidth = 0;
        mImageHeight = 0;
        int imgMode = MeasureSpec.EXACTLY;
        int imgWidthSpec = 0;
        int imgHeightSpec = 0;
        if (mIsSelectedMode)
        {
            mImageWidth = mWidth * 4 / 5;
            mImageHeight = mHeight * 4 / 5;
        } else
        {
            mImageWidth = mWidth;
            mImageHeight = mHeight;
        }
        imgWidthSpec = MeasureSpec.makeMeasureSpec(mImageWidth, imgMode);
        imgHeightSpec = MeasureSpec.makeMeasureSpec(mImageHeight, imgMode);
        mImageView.measure(imgWidthSpec, imgHeightSpec);
    }

    public int getImageWidth()
    {
        return mImageWidth;
    }

    public int getImageHeight()
    {
        return mImageHeight;
    }

    /**
     * Set scantype of imageView
     */
    private void setScanType(ImageView.ScaleType scanType)
    {
        if (mImageView != null)
            mImageView.setScaleType(scanType);
    }

    /**
     * Set if is in the delete mode
     */
    public void setIsSeletedMode(boolean b)
    {
        this.mIsSelectedMode = b;
        if (mIsSelectedMode)
            mImgSelected.setVisibility(VISIBLE);
        else
            mImgSelected.setVisibility(GONE);
        requestLayout();
    }

    /**
     * If is in the select mode
     */
    public boolean isSelectedMode()
    {
        return mIsSelectedMode;
    }

    /**
     * Get imageView object
     */
    public ImageView getImageView()
    {
        return mImageView;
    }

    public interface onClickSelectListener
    {
        void onClickSelect();
    }

    public void setOnClickSelectListener(onClickSelectListener listener)
    {
        this.mListener = listener;
    }
}
