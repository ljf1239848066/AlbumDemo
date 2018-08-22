package com.lxzh123.albumdemo.view.AlbumGridView;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * description 自定义透明度随着选择状态变化的ImageView显示控件
 * author      Created by lxzh
 * date        2018/8/22
 */
public class GridImageView extends AppCompatImageView
{
    public GridImageView(Context context)
    {
        super(context);
        setClickable(true);
    }

    public GridImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setClickable(true);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                setAlpha(0.7f);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                setAlpha(1.0f);
                break;
        }
        return super.dispatchTouchEvent(event);
    }
}
