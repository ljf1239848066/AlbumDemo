package com.lxzh123.albumdemo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * description $desc$
 * author      Created by lxzh
 * date        2018/8/24
 */
public class CheckableRecyclerView extends RecyclerView {
    private final String TAG="CheckableRecyclerView";
    private OnMediaCheckedChangedListener mOnMediaCheckedChangedListener;
    private boolean isCheckChangedBlocked=false;

    public CheckableRecyclerView(@NonNull Context context) {
        super(context);
    }

    public CheckableRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnCheckedChangeListener(@Nullable OnMediaCheckedChangedListener listener){
        mOnMediaCheckedChangedListener =listener;
    }

    public void BlockCheckChangedEvent(){
        isCheckChangedBlocked=true;
    }

    public void CancelBlockCheckChangedEvent(){
        isCheckChangedBlocked=false;
    }

    public void OnMediaCheckedChangeListener(View view){
        Log.d(TAG,"CheckableRecyclerView:OnMediaCheckedChangeListener1");
        Log.d(TAG,"CheckableRecyclerView:OnMediaCheckedChangeListener1:"+(mOnMediaCheckedChangedListener));
        Log.d(TAG,"CheckableRecyclerView:OnMediaCheckedChangeListener1:"+(isCheckChangedBlocked));
        if(mOnMediaCheckedChangedListener !=null&&!isCheckChangedBlocked){
            Log.d(TAG,"CheckableRecyclerView:OnMediaCheckedChangeListener2");
            mOnMediaCheckedChangedListener.onCheckedChanged(view);
        }
    }
}
