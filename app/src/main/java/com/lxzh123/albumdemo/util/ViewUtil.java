package com.lxzh123.albumdemo.util;

import android.content.Context;

/**
 * description 视图工具类
 * author      Created by lxzh
 * date        2018/8/23
 */
public class ViewUtil {
    public static int getStatusBarHeight(Context context){
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
