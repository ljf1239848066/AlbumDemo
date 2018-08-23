package com.lxzh123.albumdemo.view.AlbumGridView;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.lxzh123.albumdemo.R;
import com.lxzh123.albumdemo.common.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * description 相册选择列表 Grid 容器
 * author      Created by lxzh
 * date        2018/8/22
 */
public class AlbumSelectGridView extends ViewGroup
{
    //Size of imageview while there has only one image
    private int mSingleImageSize = 100;
    //Aspect ratio of only one imageview
    private float mSingleImageRatio = 1.0f;
    //Size of space
    private int mSpaceSize = 3;
    //Width and height of every imageview(more than one image)
    private int mImageWidth, mImageHeight;
    //column count
    private int mColumnCount = 3;
    //raw count,depends on column count
    private int mRawCount;
    //interface of imageloader
    private IGridImageLoader mImageLoader;
    //image datas
    private List<GridBean> mDataList = new ArrayList<>();
    //plus button
    private GridImageView mImgAddData;
    //child view click listener
    private onItemClickListener mListener;
    //weather is in edit mode
    private boolean mIsEditMode;
    //Maximum of image
    private int mMaxNum = 9;

    public AlbumSelectGridView(Context context)
    {
        super(context);
        initParams(context, null);
    }

    public AlbumSelectGridView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initParams(context, attrs);
    }

    private void initParams(Context context, AttributeSet attrs)
    {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mSingleImageSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mSingleImageSize, dm);
        mSpaceSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mSpaceSize, dm);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AlbumSelectGridView);
        if (ta != null)
        {
            int count = ta.getIndexCount();
            for (int i = 0; i < count; i++)
            {
                int index = ta.getIndex(i);
                if (index == R.styleable.AlbumSelectGridView_sapce_size)
                    mSpaceSize = ta.getDimensionPixelSize(index, mSpaceSize);
                else if (index == R.styleable.AlbumSelectGridView_single_image_ratio)
                    mSingleImageRatio = ta.getFloat(index, mSingleImageRatio);
                else if (index == R.styleable.AlbumSelectGridView_single_image_size)
                    mSingleImageSize = ta.getDimensionPixelSize(index, mSingleImageSize);
                else if (index == R.styleable.AlbumSelectGridView_column_count)
                    mColumnCount = ta.getInteger(index, mColumnCount);
                else if (index == R.styleable.AlbumSelectGridView_max_num)
                    mMaxNum = ta.getInteger(index, mMaxNum);
            }
            ta.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int resWidth = 0, resHeight = 0;

        //Measure width
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        //get available width
        int totalWidth = measureWidth - getPaddingLeft() - getPaddingRight();

        //If is non-edit mode,the size of childview depends on data size
        int dataCount = mDataList.size();
        if (mDataList != null && dataCount > 0)
        {
            if (dataCount == 1)
            {
                mImageWidth = mSingleImageSize > totalWidth ? totalWidth : mSingleImageSize;
                mImageHeight = (int) (mImageWidth / mSingleImageRatio);
                //Resize single imageview area size,not allowed to exceed the maximum display range
                if (mImageHeight > mSingleImageSize)
                {
                    float ratio = mSingleImageSize * 1.0f / mImageHeight;
                    mImageWidth = (int) (mImageWidth * ratio);
                    mImageHeight = mSingleImageSize;
                }
                resWidth = mImageWidth + getPaddingLeft() + getPaddingRight();
                resHeight = mImageHeight + getPaddingTop() + getPaddingBottom();
            } else
            {
                mImageWidth = mImageHeight = (totalWidth - (mColumnCount - 1) * mSpaceSize) / mColumnCount;
                if (dataCount < mColumnCount)
                    resWidth = mImageWidth * dataCount + (dataCount - 1) * mSpaceSize + getPaddingLeft() + getPaddingRight();
                else
                    resWidth = mImageWidth * mColumnCount + (mColumnCount - 1) * mSpaceSize + getPaddingLeft() + getPaddingRight();
                resHeight = mImageHeight * mRawCount + (mRawCount - 1) * mSpaceSize + getPaddingTop() + getPaddingBottom();
            }
        }

        setMeasuredDimension(resWidth, resHeight);

        //Measure child view size
        int childrenCount = getChildCount();
        for (int index = 0; index < childrenCount; index++)
        {
            View childView = getChildAt(index);
            int childWidth = mImageWidth;
            int childHeight = mImageHeight;
            int childMode = MeasureSpec.EXACTLY;
            int childWidthSpec = MeasureSpec.makeMeasureSpec(childWidth, childMode);
            int childHeightSpec = MeasureSpec.makeMeasureSpec(childHeight, childMode);
            childView.measure(childWidthSpec, childHeightSpec);
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3)
    {
        int childCount = getChildCount();
        for (int index = 0; index < childCount; index++)
        {
            View childrenView = getChildAt(index);
            int rowNum = index / mColumnCount;
            int columnNum = index % mColumnCount;
            int left = (mImageWidth + mSpaceSize) * columnNum + getPaddingLeft();
            int top = (mImageHeight + mSpaceSize) * rowNum + getPaddingTop();
            int right = left + mImageWidth;
            int bottom = top + mImageHeight;
            childrenView.layout(left, top, right, bottom);
        }
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        setDataList(null);
    }

    /**
     * Set data source
     */
    public void setDataList(List<GridBean> dataList)
    {
        mDataList.clear();
        //Not allowed to exceed the maximum number
        if (dataList != null && dataList.size() > 0)
        {
            if (dataList.size() <= mMaxNum)
                mDataList.addAll(dataList);
            else
                mDataList.addAll(dataList.subList(0, mMaxNum - 1));
        }
        clearAllViews();
        calRawAndColumn();
        initChildViews();
        requestLayout();
    }

    /**
     * Add data source
     */
    public void addDataList(List<GridBean> dataList)
    {
        if (mDataList.size() >= mMaxNum)
            return;
        //Not allowed to exceed the maximum number
        int cha = mMaxNum - mDataList.size();
        if (dataList.size() <= cha)
            mDataList.addAll(dataList);
        else
            mDataList.addAll(dataList.subList(0, cha - 1));

        clearAllViews();
        calRawAndColumn();
        initChildViews();
        requestLayout();
    }

    //calculate the count of raw and column
    private void calRawAndColumn()
    {
        int childSize = mDataList.size();

        //calculate the raw count
        if (childSize == 0)
        {
            mRawCount = 0;
        } else if (childSize <= mColumnCount)
        {
            mRawCount = 1;
        } else
        {
            if (childSize % mColumnCount == 0)
                mRawCount = childSize / mColumnCount;
            else
                mRawCount = childSize / mColumnCount + 1;
        }
    }

    //Initialize child view
    private void initChildViews()
    {
        //add image container
        int dataSize = mDataList.size();
        for (int i = 0; i < dataSize; i++)
        {
            final GridBean gridBean = mDataList.get(i);
            final AlbumSelectGirdContainer imageContainer = new AlbumSelectGirdContainer(getContext());
            imageContainer.setIsSeletedMode(mIsEditMode);
            final int position = i;
            imageContainer.setOnClickSelectListener(new AlbumSelectGirdContainer.onClickSelectListener()
            {
                @Override
                public void onClickSelect()
                {
                    mDataList.remove(position);
                    clearAllViews();
                    calRawAndColumn();
                    initChildViews();
                    requestLayout();
                    if (mListener != null)
                        mListener.onAlbumGirdItemDeleted(position, gridBean, imageContainer);
                }
            });
            imageContainer.getImageView().setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (mListener != null)
                        mListener.onAlbumGirdItemClick(position, gridBean, imageContainer);
                }
            });
            addView(imageContainer, position);

            imageContainer.post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (mImageLoader != null)
                    {
                        if (imageContainer.getImageWidth() != 0 && imageContainer.getImageWidth() != 0)
                            mImageLoader.displayGridImage(getContext(), gridBean.getThumbUrl(), imageContainer.getImageView()
                                    , Constant.IMG_THUMBNAIL_SCALRE);
                        else
                            mImageLoader.displayGridImage(getContext(), gridBean.getThumbUrl(), imageContainer.getImageView());

                    } else
                    {
                        Log.w("NineGridView", "Can not display the image of NineGridView, you'd better set a imageloader!!!!");
                    }
                }
            });
        }

        setIsEditMode(mIsEditMode);
    }

    /**
     * Set weather is in edit mode
     */
    public void setIsEditMode(boolean b)
    {
        mIsEditMode = b;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            View childView = getChildAt(i);
            if (childView instanceof AlbumSelectGirdContainer)
                ((AlbumSelectGirdContainer) childView).setIsSeletedMode(b);
        }

        calRawAndColumn();
        requestLayout();
    }

    /**
     * Set up imageloader
     */
    public void setImageLoader(IGridImageLoader loader)
    {
        this.mImageLoader = loader;
    }

    /**
     * Set column count
     */
    public void setColumnCount(int columnCount)
    {
        this.mColumnCount = columnCount;
        calRawAndColumn();
        requestLayout();
    }

    /**
     * Set the maximum number
     */
    public void setMaxNum(int maxNum)
    {
        this.mMaxNum = maxNum;
    }

    /**
     * Set the space size, dip unit
     */
    public void setSpcaeSize(int dpValue)
    {
        this.mSpaceSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue
                , getContext().getResources().getDisplayMetrics());
    }

    /**
     * Set the size of imageview while there has only one image, dip unit
     */
    public void setSingleImageSize(int dpValue)
    {
        this.mSingleImageSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue
                , getContext().getResources().getDisplayMetrics());
    }

    /**
     * Set the aspect ratio of only one imageview
     */
    public void setSingleImageRatio(float ratio)
    {
        this.mSingleImageRatio = ratio;
    }

    //clear all views
    private void clearAllViews()
    {
        removeAllViews();
        if (mImgAddData != null)
            removeView(mImgAddData);
        mImgAddData = null;
    }

    /**
     * Get data source
     */
    public List<GridBean> getDataList()
    {
        return mDataList;
    }

    /**
     * Set up child view click listener
     */
    public void setOnItemClickListener(onItemClickListener l)
    {
        this.mListener = l;
    }

    public interface onItemClickListener
    {
        /**
         * Callback when image be clicked
         *
         * @param position       position,started with 0
         * @param gridBean       data of image be clicked
         * @param imageContainer image container of image be clicked
         */
        void onAlbumGirdItemClick(int position, GridBean gridBean, AlbumSelectGirdContainer imageContainer);

        /**
         * Callback when one image be deleted
         *
         * @param position       position,started with 0
         * @param gridBean       data of image be clicked
         * @param imageContainer image container of image be clicked
         */
        void onAlbumGirdItemDeleted(int position, GridBean gridBean, AlbumSelectGirdContainer imageContainer);
    }

    /*****************************************************
     * State cache
     ****************************************************************/

    @Override
    protected Parcelable onSaveInstanceState()
    {
        SavedViewState ss = new SavedViewState(super.onSaveInstanceState());
        ss.singleImageSize = mSingleImageSize;
        ss.singleImageRatio = mSingleImageRatio;
        ss.spaceSize = mSpaceSize;
        ss.columnCount = mColumnCount;
        ss.rawCount = mRawCount;
        ss.maxNum = mMaxNum;
        ss.isEditMode = mIsEditMode;
        ss.dataList = mDataList;
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        if (!(state instanceof SavedViewState))
        {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedViewState ss = (SavedViewState) state;
        super.onRestoreInstanceState(ss);
        mSingleImageSize = ss.singleImageSize;
        mSingleImageRatio = ss.singleImageRatio;
        mSpaceSize = ss.spaceSize;
        mColumnCount = ss.columnCount;
        mRawCount = ss.rawCount;
        mMaxNum = ss.maxNum;
        mIsEditMode = ss.isEditMode;
        setDataList(ss.dataList);
    }

    static class SavedViewState extends BaseSavedState
    {
        int singleImageSize;
        float singleImageRatio;
        int spaceSize;
        int columnCount;
        int rawCount;
        int maxNum;
        boolean isEditMode;
        int icAddMoreResId;
        List<GridBean> dataList;

        SavedViewState(Parcelable superState)
        {
            super(superState);
        }

        private SavedViewState(Parcel source)
        {
            super(source);
            singleImageSize = source.readInt();
            singleImageRatio = source.readFloat();
            spaceSize = source.readInt();
            columnCount = source.readInt();
            rawCount = source.readInt();
            maxNum = source.readInt();
            isEditMode = source.readByte() == (byte) 1;
            icAddMoreResId = source.readInt();
            dataList = source.readArrayList(GridBean.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel out, int flags)
        {
            super.writeToParcel(out, flags);
            out.writeInt(singleImageSize);
            out.writeFloat(singleImageRatio);
            out.writeInt(spaceSize);
            out.writeInt(columnCount);
            out.writeInt(rawCount);
            out.writeInt(maxNum);
            out.writeByte(isEditMode ? (byte) 1 : (byte) 0);
            out.writeInt(icAddMoreResId);
            out.writeList(dataList);
        }

        public static final Creator<SavedViewState> CREATOR = new Creator<SavedViewState>()
        {
            @Override
            public SavedViewState createFromParcel(Parcel source)
            {
                return new SavedViewState(source);
            }

            @Override
            public SavedViewState[] newArray(int size)
            {
                return new SavedViewState[size];
            }
        };
    }
}
