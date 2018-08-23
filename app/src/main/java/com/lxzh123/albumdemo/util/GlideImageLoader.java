package com.lxzh123.albumdemo.util;

import android.content.Context;
import android.widget.ImageView;

import com.lxzh123.albumdemo.R;
import com.lxzh123.albumdemo.view.AlbumGridView.IGridImageLoader;

import com.bumptech.glide.Glide;
/**
 * description $desc$
 * author      Created by lxzh
 * date        2018/8/23
 */
public class GlideImageLoader implements IGridImageLoader{

    public void displayGridImage(Context context, String url, ImageView imageView)
    {
        Glide.with(context).load(url)
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);
    }

    @Override
    public void displayGridImage(Context context, String url, ImageView imageView, float scale)
    {
        Glide.with(context).load(url)
                .placeholder(R.mipmap.ic_launcher)
                .thumbnail(scale)
                .into(imageView);
    }
}
