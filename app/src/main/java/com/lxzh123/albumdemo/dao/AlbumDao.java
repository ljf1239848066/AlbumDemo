package com.lxzh123.albumdemo.dao;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import com.lxzh123.albumdemo.common.Constant;
import com.lxzh123.albumdemo.model.AlbumGroupBean;
import com.lxzh123.albumdemo.model.MediaType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * description 相册工具类，用于读取系统相册信息
 * author      Created by lxzh
 * date        2018/8/22
 */
public class AlbumDao {
    private final static String TAG = "AlbumDao";
    private Context context;
    private Handler handler;
    private int msgWhat;
    private LoadAlbumGroupThread loadThread;

    public AlbumDao(Context context) {
        this.context = context;
    }

    public boolean asyncLoadAlbumGroup(Handler handler, int msgWhat) {
        if(loadThread!=null&&loadThread.isAlive()){
            return false;
        }else{
            this.handler = handler;
            this.msgWhat = msgWhat;
            loadThread = new LoadAlbumGroupThread();
            loadThread.start();
            return true;
        }
    }

    public boolean isWorking(){
        return loadThread!=null&&loadThread.isAlive();
    }

    public void stopLoading() {
        try {
            if (loadThread != null && loadThread.isAlive()) {
                loadThread.interrupt();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<AlbumGroupBean> getAlbumGroup() {
        return loadThread.getGroupBeanList();
    }

    class LoadAlbumGroupThread extends Thread {
        private List<AlbumGroupBean> groupBeanList;
        private ContentResolver contentResolver;

        public List<AlbumGroupBean> getGroupBeanList() {
            return groupBeanList;
        }

        public LoadAlbumGroupThread() {
            groupBeanList = new ArrayList<>();
        }

        private List<AlbumGroupBean> getImageGroups() {
            // id,缩略图,原图,时间
            String[] columns = {MediaStore.Images.Media._ID, MediaStore.Images.Thumbnails.DATA,
                    MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_TAKEN,
                    MediaStore.Images.Media.BUCKET_ID,MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    "COUNT(1) AS count"};
            String selection = "0==0) GROUP BY (" + MediaStore.Images.Media.BUCKET_ID;

            String sortOrder = "count DESC";

            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            List<AlbumGroupBean> mediaBeanList = getContentProvider(uri, columns, selection, sortOrder, MediaType.IMAGE);

            return mediaBeanList;
        }

        private List<AlbumGroupBean> getVideoGroups() {
            String[] columns = {MediaStore.Video.Media._ID, MediaStore.Video.Thumbnails.DATA,
                    MediaStore.Video.Media.DATA, MediaStore.Video.Media.DATE_TAKEN,
                    MediaStore.Video.Media.BUCKET_ID,MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                    "COUNT(1) AS count"};
            String selection = "0==0) GROUP BY (" + MediaStore.Video.Media.BUCKET_ID;

            String sortOrder = "count DESC";

            Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

            List<AlbumGroupBean> mediaBeanList = getContentProvider(uri, columns, selection, sortOrder, MediaType.VIDEO);

            return mediaBeanList;
        }

        /**
         * 获取媒体内容
         *
         * @param uri
         * @param columns
         * @param where
         * @param orderBy
         * @param mediaType
         * @return
         */
        public List<AlbumGroupBean> getContentProvider(Uri uri, String[] columns, String where, String orderBy, MediaType mediaType) {
            List<AlbumGroupBean> groupBeanList = new ArrayList<>();
            Cursor cursor = contentResolver.query(uri, columns, where, null, orderBy);
            if (null != cursor) {
                int colId = cursor.getColumnIndex(columns[0]);
                int colPath = cursor.getColumnIndex(columns[2]);
                int colTime=cursor.getColumnIndex(columns[3]);
                int colBucketId = cursor.getColumnIndex(columns[4]);
                int colBucketName = cursor.getColumnIndex(columns[5]);
                int colCount = cursor.getColumnIndex("count");
                while (cursor.moveToNext()) {
                    AlbumGroupBean group = new AlbumGroupBean();
                    group.setId(cursor.getInt(colId));
                    group.setPath(cursor.getString(colPath));
                    group.setTime(cursor.getLong(colTime));
                    group.setBucketId(cursor.getInt(colBucketId));
                    String dirName = cursor.getString(colBucketName);
                    group.setBucketName(dirName);
                    group.setThumbType(mediaType);
                    group.setCount(cursor.getInt(colCount));
                    groupBeanList.add(group);
                }
            }
            return groupBeanList;
        }

        /**
         * 合并两个分组
         * @param list1
         * @param list2
         */
        private List<AlbumGroupBean> mergeAlbumGroup(List<AlbumGroupBean> list1, List<AlbumGroupBean> list2) {
            int cnt1 = list1.size();
            int cnt2 = list2.size();
            Log.d(TAG,"list1.size="+cnt1);
            Log.d(TAG,"list2.size="+cnt2);
            for(int i=0;i<cnt1;i++){
                Log.d(TAG,"list1:"+list1.get(i).toDebugString());
            }
            for(int i=0;i<cnt2;i++){
                Log.d(TAG,"list2:"+list2.get(i).toDebugString());
            }
            /**
             * tag=0 未搜索
             * tag=1 合并保留
             * tag=2 合并废弃
             */
            for (int i = 0; i < cnt2; i++) {
                AlbumGroupBean bean2 = list2.get(i);
                int idx=-1;
                for (int j = 0; j < cnt1; j++) {
                    AlbumGroupBean bean1 = list1.get(j);
                    if(bean1.tag>0){
                        continue;
                    }
                    //两个目录相同
                    if (bean1.getBucketId()==bean2.getBucketId()){
                        idx=j;
                        if(bean1.getTime()>bean2.getTime()){
                            bean1.setCount(bean1.getCount()+bean2.getCount());
                            bean2.tag=2;
                            bean1.tag=1;
                        }else{
                            bean2.setCount(bean1.getCount()+bean2.getCount());
                            bean1.tag=2;
                            bean2.tag=1;
                        }
                        break;
                    }
                }
                if(idx<0){
                    bean2.tag=1;
                }
            }
            List<AlbumGroupBean> mergedList=new ArrayList<>();
            for(int i=0;i<cnt1;i++){
                if(list1.get(i).tag!=2){
                    mergedList.add(list1.get(i));
                }
            }
            for(int i=0;i<cnt2;i++){
                if(list2.get(i).tag==1){
                    mergedList.add(list2.get(i));
                }
            }
            Collections.sort(mergedList);
            int cnt=mergedList.size();
            Log.d(TAG,"list.size="+cnt);
            for(int i=0;i<cnt;i++){
                Log.d(TAG,"list:"+mergedList.get(i).toDebugString());
            }
            return mergedList;
        }

        @Override
        public void run() {
            super.run();
            Log.d(TAG, "LoadAlbumGroupThread:run:0");
            contentResolver = context.getContentResolver();

            groupBeanList = mergeAlbumGroup(
                    getImageGroups(), getVideoGroups());

            Message msg = Message.obtain();
            msg.what = msgWhat;

            if (groupBeanList.size() > 0) {
                msg.arg1 = Constant.STATUS_SUCCESS;
            } else {
                msg.arg1 = Constant.STATUS_FAIL;
                Log.d(TAG, "LoadMediaThread:run:5");
            }
            handler.sendMessage(msg);
        }
    }

    /**
     * 计算图片缩放采样率
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 读取缩略图
     *
     * @param path
     * @param size
     * @return
     */
    public static Bitmap loadThumbnail(String path, int size) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        //只读取尺寸信息
        BitmapFactory.decodeFile(path, options);
        //重新计算采样大小
        options.inSampleSize = calculateInSampleSize(options, size, size);
        options.inJustDecodeBounds = false;
        //加载缩略图
        return BitmapFactory.decodeFile(path, options);
    }
}
