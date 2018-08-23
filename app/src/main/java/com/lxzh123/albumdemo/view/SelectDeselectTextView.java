package com.lxzh123.albumdemo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * description $desc$
 * author      Created by lxzh
 * date        2018/8/23
 */
public class SelectDeselectTextView extends AppCompatTextView {
    private boolean mSelectAll;

    public boolean isSelectAll() {
        return mSelectAll;
    }

    public void setSelectAll(boolean selectAll) {
        mSelectAll = selectAll;
    }

    public SelectDeselectTextView(Context context) {
        super(context);
        init();
    }

    public SelectDeselectTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelectDeselectTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mSelectAll=true;
    }
}
