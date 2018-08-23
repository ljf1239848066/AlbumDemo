package com.lxzh123.albumdemo.view.AlbumGridView;

import android.content.Context;
import android.widget.ImageView;

/**
 * description 相册图片加载器
 * author      Created by lxzh
 * date        2018/8/22
 */
public interface IGridImageLoader
{
    void displayGridImage(Context context, String url, ImageView imageView);

    void displayGridImage(Context context, String url, ImageView imageView, float scale);
}
