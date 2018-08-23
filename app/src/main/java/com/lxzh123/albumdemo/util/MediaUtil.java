package com.lxzh123.albumdemo.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import com.lxzh123.albumdemo.common.Constant;
import com.lxzh123.albumdemo.model.MediaBean;

import java.util.ArrayList;
import java.util.List;

/**
 * description 相册工具类，用于读取系统相册信息
 * author      Created by lxzh
 * date        2018/8/22
 */
public class MediaUtil {
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

    public List<MediaBean> getAlbumGroup(){
        return loadMediaThread.getMediaBeanList();
    }

    class LoadMediaThread extends Thread{
        private String selectPath;
        private List<MediaBean> mediaBeanList;

        public List<MediaBean> getMediaBeanList() {
            return mediaBeanList;
        }

        public LoadMediaThread(String selectPath){
            this.selectPath=selectPath;
            this.mediaBeanList =new ArrayList<>();
        }

        @Override
        public void run() {
            super.run();
            Log.d(TAG,"LoadMediaThread:run:0");
            ContentResolver contentResolver=context.getContentResolver();
            // id,缩略图,原图,文件夹id,文件夹名,文件夹分类的图片总数
            String[] columns = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
            String where= MediaStore.Images.ImageColumns.DATA+" like '"+this.selectPath+"/%'";

            String sortOrder = MediaStore.Images.Media.DATE_MODIFIED;

            mediaBeanList=ImageSelectObservable.getInstance().getFolderAllImages();

            Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    columns, where, null, null);
            Log.d(TAG,"LoadMediaThread:run:1");
            Message msg=Message.obtain();
            msg.what=msgWhat;
            Log.d(TAG,"LoadMediaThread:run:2");
            if (cursor!=null&&cursor.moveToFirst()){
                Log.d(TAG,"LoadAlbumGroupThread:run:3");
                int colId=cursor.getColumnIndex(columns[0]);
                int colPath=cursor.getColumnIndex(columns[1]);
                Log.d(TAG,"LoadMediaThread:run:4");
                do{
                    MediaBean media=new MediaBean();
                    media.setId(cursor.getInt(colId));
                    media.setPath(cursor.getString(colPath));
                    mediaBeanList.add(media);
                }while (cursor.moveToNext());
                msg.arg1= Constant.STATUS_SUCCESS;
            }else{
                msg.arg1=Constant.STATUS_FAIL;
                Log.d(TAG,"LoadMediaThread:run:5");
            }
            handler.sendMessage(msg);
        }
    }
}
