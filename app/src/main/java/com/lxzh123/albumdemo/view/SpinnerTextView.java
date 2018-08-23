package com.lxzh123.albumdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.lxzh123.albumdemo.R;

/**
 * description $desc$
 * author      Created by lxzh
 * date        2018/8/23$
 */
public class SpinnerTextView extends AppCompatTextView {
    private boolean mDropdown;

    public boolean isDropdown() {
        return mDropdown;
    }

    public void setDropdown(boolean dropdown) {
        mDropdown = dropdown;
        this.invalidate();
    }

    public SpinnerTextView(Context context) {
        this(context,null,0);
    }

    public SpinnerTextView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public SpinnerTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context, attrs, defStyleAttr);
    }

    private void Init(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        mDropdown=false;
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.SpinnerTextView,defStyleAttr,0);
        final int attrCount=array.getIndexCount();
        for(int i=0;i<attrCount;i++){
            int attr=array.getIndex(i);
            switch (attr){
                case R.styleable.SpinnerTextView_IsDropDown:
                    mDropdown= attrs.getAttributeBooleanValue(attr,false);
                    break;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
        if(mDropdown){
            paint.setColor(Color.GRAY);
        }else{
            paint.setColor(Color.BLACK);
        }
        Rect rect=new Rect();
        getDrawingRect(rect);
        Path path=new Path();
        path.moveTo(rect.right,rect.bottom);
        path.lineTo(rect.right-15,rect.bottom);
        path.lineTo(rect.right,rect.bottom-15);
        path.close();
        canvas.drawPath(path,paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
