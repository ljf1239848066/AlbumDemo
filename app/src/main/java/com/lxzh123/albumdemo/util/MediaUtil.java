package com.lxzh123.albumdemo.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.lxzh123.albumdemo.common.Constant;
import com.lxzh123.albumdemo.model.MediaBean;
import com.lxzh123.albumdemo.model.MediaDateGroupBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description 相册工具类，用于读取系统相册信息
 * author      Created by lxzh
 * date        2018/8/22
 */
public class MediaUtil implements LoaderManager.LoaderCallbacks<Cursor>{
    private final static String TAG="MediaUtil";
    private Context context;
    private Handler handler;
    private int msgWhat;
    private LoadMediaThread loadMediaThread;
    private String selectPath;

    public MediaUtil(Context context){
        this.context=context;
    }

    public void asyncLoadMedia(Handler handler, int msgWhat, String selectPath){
        this.handler=handler;
        this.msgWhat=msgWhat;
        this.selectPath=selectPath;
        loadMediaThread =new LoadMediaThread(selectPath);
        loadMediaThread.start();
    }

    public void stopLoadMedia(){
        try{
            if(loadMediaThread !=null&& loadMediaThread.isAlive()){
                loadMediaThread.interrupt();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public List<MediaDateGroupBean> getMediaDateGroupBeanList(){
        return loadMediaThread.getMediaDateGroupBeanList();
    }

    class LoadMediaThread extends Thread{
        private String selectPath;
        private List<MediaDateGroupBean> mediaDateGroupBeanList;

        public List<MediaDateGroupBean> getMediaDateGroupBeanList() {
            return mediaDateGroupBeanList;
        }

        public LoadMediaThread(String selectPath){
            this.selectPath=selectPath;
            this.mediaDateGroupBeanList =new ArrayList<>();
        }

        @Override
        public void run() {
            super.run();
            Log.d(TAG,"LoadMediaThread:run:0");
            ContentResolver contentResolver=context.getContentResolver();
            // id,缩略图,原图,文件夹id,文件夹名,文件夹分类的图片总数
            String[] columns = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
                    MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Media.DATE_MODIFIED};
            String where= MediaStore.Images.ImageColumns.DATA+" like '"+this.selectPath+"/%'";

            String sortOrder = MediaStore.Images.Media.DATE_MODIFIED;

            List<MediaBean> mediaBeanList=ImageSelectObservable.getInstance().getFolderAllImages();

            Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columns, where, null, null);
            Log.d(TAG,"LoadMediaThread:run:1");
            Message msg=Message.obtain();
            msg.what=msgWhat;
            Log.d(TAG,"LoadMediaThread:run:2");
            if (cursor!=null&&cursor.moveToFirst()){
                Log.d(TAG,"LoadMediaThread:run:3");
                int colId=cursor.getColumnIndex(columns[0]);
                int colPath=cursor.getColumnIndex(columns[1]);
                int colThumb=cursor.getColumnIndex(columns[2]);
                int colTime=cursor.getColumnIndex(columns[3]);
                Log.d(TAG,"LoadMediaThread:run:4");
                int index=0;
                do{
//                    Log.d(TAG,"LoadMediaThread:run:5-"+(index++));
                    MediaBean media=new MediaBean();
                    media.setId(cursor.getInt(colId));
                    media.setPath(cursor.getString(colPath));
                    media.setThumbPath(cursor.getString(colThumb));
                    media.setTime(cursor.getLong(colTime));
                    mediaBeanList.add(media);
                }while (cursor.moveToNext());
                mediaDateGroupBeanList=getDateSortedMediaList(mediaBeanList);
                msg.arg1= Constant.STATUS_SUCCESS;
            }else{
                msg.arg1=Constant.STATUS_FAIL;
                Log.d(TAG,"LoadMediaThread:run:5");
            }
            handler.sendMessage(msg);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        // id,缩略图,原图,文件夹id,文件夹名,文件夹分类的图片总数
        String[] columns = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
                MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Media.DATE_MODIFIED};
        String where= MediaStore.Images.ImageColumns.DATA+" like '"+this.selectPath+"/%'";

        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED;

        CursorLoader cursorLoader=new CursorLoader(
                context,MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns,where,null,null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        Log.d(TAG,"onLoadFinished:run:1");
        Message msg=Message.obtain();
        msg.what=msgWhat;
        Log.d(TAG,"onLoadFinished:run:2");
        if(cursor.moveToFirst()){
            List<MediaBean> data=new ArrayList<>();
            int colId=cursor.getColumnIndex(MediaStore.Images.Media._ID);
            int colPath=cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int colThumb=cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
            int colTime=cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED);
            do{
                MediaBean media=new MediaBean();
                media.setId(cursor.getInt(colId));
                media.setPath(cursor.getString(colPath));
                media.setThumbPath(cursor.getString(colThumb));
                media.setTime(cursor.getLong(colTime));
                data.add(media);
            }while (cursor.moveToNext());
            msg.arg1= Constant.STATUS_SUCCESS;
//            msg.obj=data;
            msg.obj=getDateSortedMediaList(data);
        } else {
            msg.arg1=Constant.STATUS_FAIL;
            Log.d(TAG,"onLoadFinished:run:3");
        }
        handler.sendMessage(msg);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public static List<MediaDateGroupBean> getDateSortedMediaList(List<MediaBean> mediaBeans){
        Map<String,List<MediaBean>> listMap=new HashMap<>();
        int cnt=mediaBeans.size();
        Log.d(TAG,"getDateSortedMediaList:size1="+cnt);
        for(int i=0;i<cnt;i++){
            MediaBean mediaBean=mediaBeans.get(i);
            List<MediaBean> subList;
            String key=mediaBean.getTimeStr();
            if(listMap.containsKey(key)){
                subList=listMap.get(key);
            }else{
                subList=new ArrayList<>();
                listMap.put(key,subList);
            }
            subList.add(mediaBean);
        }
        Log.d(TAG,"getDateSortedMediaList:size2="+listMap.size());
        List<MediaDateGroupBean> mediaDateGroupBeans=new ArrayList<>();
        for (Map.Entry entry:listMap.entrySet()){
            MediaDateGroupBean mediaDateGroupBean=new MediaDateGroupBean(entry.getKey().toString());
            mediaDateGroupBean.setMediaBeans((List<MediaBean>) entry.getValue());
            mediaDateGroupBeans.add(mediaDateGroupBean);
        }
        Collections.sort(mediaDateGroupBeans);
        Log.d(TAG,"getDateSortedMediaList:size3="+mediaDateGroupBeans.size());
        return mediaDateGroupBeans;
    }
}
