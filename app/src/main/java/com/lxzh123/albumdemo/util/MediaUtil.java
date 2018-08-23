package com.lxzh123.albumdemo.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import com.lxzh123.albumdemo.common.Constant;
import com.lxzh123.albumdemo.model.MediaBean;
import com.lxzh123.albumdemo.model.MediaDateGroupBean;
import com.lxzh123.albumdemo.model.MediaType;

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
public class MediaUtil{
    private final static String TAG="MediaUtil";
    private Context context;
    private Handler handler;
    private int msgWhat;
    private LoadMediaThread loadMediaThread;

    public MediaUtil(Context context){
        this.context=context;
    }

    public void asyncLoadMedia(Handler handler, int msgWhat, String selectPath){
        this.handler=handler;
        this.msgWhat=msgWhat;
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
        private ContentResolver contentResolver;

        public List<MediaDateGroupBean> getMediaDateGroupBeanList() {
            return mediaDateGroupBeanList;
        }

        public LoadMediaThread(String selectPath){
            this.selectPath=selectPath;
            this.mediaDateGroupBeanList =new ArrayList<>();
        }

        private void getImages(List<MediaBean> mediaBeanAll){
            // id,缩略图,原图,时间
            // (最近修改时间 DATE_MODIFIED
            // or
            // 加入ContentResolver时间 DATE_ADDED
            // or
            // 文件时间 DATE_TAKEN)
//            String[] columns = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
//                    MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Media.DATE_MODIFIED};
            String[] columns = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
                    MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Media.DATE_TAKEN};
            String where= MediaStore.Images.ImageColumns.DATA+" like '"+this.selectPath+"/%'";

            String sortOrder = MediaStore.Images.Media.DATE_TAKEN;

            Uri uri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            List<MediaBean> mediaBeanList=getContentProvider(uri,columns,where,sortOrder,MediaType.IMAGE);

            if(mediaBeanList!=null&&mediaBeanList.size()>0){
                mediaBeanAll.addAll(mediaBeanList);
            }
        }

        private void getVedios(List<MediaBean> mediaBeanAll){
            String[] columns = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA,
                    MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Media.DATE_TAKEN};
            String where= MediaStore.Video.VideoColumns.DATA+" like '"+this.selectPath+"/%'";

            String sortOrder = MediaStore.Video.Media.DATE_TAKEN;

            Uri uri=MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

            List<MediaBean> mediaBeanList=getContentProvider(uri,columns,where,sortOrder,MediaType.VEDIO);

            if(mediaBeanList!=null&&mediaBeanList.size()>0){
                mediaBeanAll.addAll(mediaBeanList);
            }
        }

        /**
         * 获取媒体内容
         * @param uri
         * @param columns
         * @param where
         * @param orderBy
         * @param mediaType
         * @return
         */
        public List<MediaBean> getContentProvider(Uri uri,String[] columns,String where, String orderBy,MediaType mediaType) {
            List<MediaBean> mediaBeans = new ArrayList<MediaBean>();
            Cursor cursor = contentResolver.query(uri, columns, where,null, orderBy);
            if (null != cursor) {
                int colId = cursor.getColumnIndex(columns[0]);
                int colPath = cursor.getColumnIndex(columns[1]);
                int colThumb = cursor.getColumnIndex(columns[2]);
                int colTime = cursor.getColumnIndex(columns[3]);
                while (cursor.moveToNext()) {
                    MediaBean media = new MediaBean();
                    media.setId(cursor.getInt(colId));
                    media.setPath(cursor.getString(colPath));
                    media.setThumbPath(cursor.getString(colThumb));
                    media.setTime(cursor.getLong(colTime));
                    media.setMediaType(mediaType);
                    mediaBeans.add(media);
                }
            }
            return mediaBeans;
        }

        @Override
        public void run() {
            super.run();
            Log.d(TAG,"LoadMediaThread:run:0");
            contentResolver=context.getContentResolver();
            List<MediaBean> mediaBeanList=new ArrayList<>();
            getImages(mediaBeanList);
            getVedios(mediaBeanList);
            Message msg=Message.obtain();
            msg.what=msgWhat;
            Log.d(TAG,"LoadMediaThread:run:2");
            if (mediaBeanList.size()>0){
                mediaDateGroupBeanList=getDateSortedMediaList(mediaBeanList);
                msg.arg1= Constant.STATUS_SUCCESS;
            }else{
                msg.arg1=Constant.STATUS_FAIL;
                Log.d(TAG,"LoadMediaThread:run:5");
            }
            handler.sendMessage(msg);
        }
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
            List<MediaBean> subList=(List<MediaBean>) entry.getValue();
            Collections.sort(subList);
            mediaDateGroupBean.setMediaBeans(subList);
            mediaDateGroupBeans.add(mediaDateGroupBean);
        }
        Collections.sort(mediaDateGroupBeans);
        Log.d(TAG,"getDateSortedMediaList:size3="+mediaDateGroupBeans.size());
        return mediaDateGroupBeans;
    }
}
