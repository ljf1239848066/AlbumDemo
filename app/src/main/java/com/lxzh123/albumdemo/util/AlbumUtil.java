package com.lxzh123.albumdemo.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import com.lxzh123.albumdemo.common.Constant;
import com.lxzh123.albumdemo.model.AlbumGroupBean;

import java.util.ArrayList;
import java.util.List;

/**
 * description 相册工具类，用于读取系统相册信息
 * author      Created by lxzh
 * date        2018/8/22
 */
public class AlbumUtil {
    private final static String TAG="AlbumUtil";
    private Context context;
    private Handler handler;
    private int msgWhat;
    private LoadAlbumGroupThread loadAlbumGroupThread;

    public AlbumUtil(Context context){
        this.context=context;
    }

    public void asyncLoadAlbumGroup(Handler handler,int msgWhat){
        this.handler=handler;
        this.msgWhat=msgWhat;
        loadAlbumGroupThread=new LoadAlbumGroupThread();
        loadAlbumGroupThread.start();
    }

    public void stopLoadAlbumGroup(){
        try{
            if(loadAlbumGroupThread!=null&&loadAlbumGroupThread.isAlive()){
                loadAlbumGroupThread.interrupt();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public List<AlbumGroupBean> getAlbumGroup(){
        return loadAlbumGroupThread.getGroupBeanList();
    }

    class LoadAlbumGroupThread extends Thread{
        private List<AlbumGroupBean> groupBeanList;

        public List<AlbumGroupBean> getGroupBeanList() {
            return groupBeanList;
        }

        public LoadAlbumGroupThread(){
            groupBeanList=new ArrayList<>();
        }

        @Override
        public void run() {
            super.run();
            Log.d(TAG,"LoadAlbumGroupThread:run:0");
            ContentResolver contentResolver=context.getContentResolver();
            // id,缩略图,原图,文件夹id,文件夹名,文件夹分类的图片总数
            String[] columns = {MediaStore.Images.Media._ID, MediaStore.Images.Thumbnails.DATA,
                    MediaStore.Images.Media.DATA, MediaStore.Images.Media.BUCKET_ID,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME, "COUNT(1) AS count"};
            String selection = "0==0) GROUP BY (" + MediaStore.Images.Media.BUCKET_ID;
            String sortOrder = "count DESC";
            Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, selection, null, sortOrder);
            Log.d(TAG,"LoadAlbumGroupThread:run:1");
            Message msg=Message.obtain();
            msg.what=msgWhat;
            Log.d(TAG,"LoadAlbumGroupThread:run:2");
            if (cursor!=null&&cursor.moveToFirst()){
                Log.d(TAG,"LoadAlbumGroupThread:run:3");
                int colId=cursor.getColumnIndex(columns[0]);
                int colPath=cursor.getColumnIndex(columns[1]);
//            int mediaPath=cursor.getColumnIndex(columns[2]);
                int colDirId=cursor.getColumnIndex(columns[3]);
                int colDirName=cursor.getColumnIndex(columns[4]);
                int colCount=cursor.getColumnIndex("count");
                Log.d(TAG,"LoadAlbumGroupThread:run:4");
                do{
                    AlbumGroupBean group=new AlbumGroupBean();
                    group.setId(cursor.getInt(colId));
//                group.setMedia(cursor.getString(mediaPath));
//                group.setThumbnails(cursor.get);
                    group.setPath(cursor.getString(colPath));
                    group.setBucketId(cursor.getInt(colDirId));
                    String dirName=cursor.getString(colDirName);
                    group.setBucketName(dirName);
                    group.setCount(cursor.getInt(colCount));
                    Log.d(TAG,"LoadAlbumGroupThread:run:4:dirName="+dirName);
//                    if(new File(dirName).exists()){
                        groupBeanList.add(group);
//                    }
                }while (cursor.moveToNext());
                msg.arg1= Constant.STATUS_SUCCESS;
            }else{
                msg.arg1=Constant.STATUS_FAIL;
                Log.d(TAG,"LoadAlbumGroupThread:run:5");
            }
            handler.sendMessage(msg);
        }
    }

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

    public static Bitmap loadThumbnail(String path,int size){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;

        //只读取尺寸信息
        BitmapFactory.decodeFile(path,options);
        //重新计算采样大小
        options.inSampleSize=calculateInSampleSize(options,size,size);
        options.inJustDecodeBounds=false;
        //加载缩略图
        return BitmapFactory.decodeFile(path,options);
    }
}
