package com.lxzh123.albumdemo.view;

import android.content.Context;
import android.widget.ImageView;

/**
 * description 相册图片加载器
 * author      Created by lxzh
 * date        2018/8/22
 */
public interface IGridImageLoader
{
    void displayNineGridImage(Context context, String url, ImageView imageView);

    void displayNineGridImage(Context context, String url, ImageView imageView, int width, int height);
}
