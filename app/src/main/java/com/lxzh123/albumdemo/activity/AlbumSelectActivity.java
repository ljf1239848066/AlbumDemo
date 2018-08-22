package com.lxzh123.albumdemo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.lxzh123.albumdemo.R;
import com.lxzh123.albumdemo.common.Constant;
import com.lxzh123.albumdemo.model.AlbumGroupBean;
import com.lxzh123.albumdemo.util.AlbumUtil;
import com.lxzh123.albumdemo.view.AlbumGridView.GridBean;
import com.lxzh123.albumdemo.view.AlbumSelectAdapter;

import java.util.ArrayList;
import java.util.List;

public class AlbumSelectActivity extends AppCompatActivity {
    private final static String TAG="AlbumSelectActivity";

    private Button btnBack;
    private Spinner spAlbumName;
    private ArrayAdapter<String> groupAdapter;
    private ListView listAlbum;

    private AlbumSelectAdapter adapter;
    private Handler handler;
    private AlbumUtil albumUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_select);

        Init();
        Handle();
    }

    private void Init(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnBack=findViewById(R.id.btn_back);
        spAlbumName=findViewById(R.id.sp_album_name);
        listAlbum=findViewById(R.id.lv_album_list);
        albumUtil=new AlbumUtil(this);
        adapter=new AlbumSelectAdapter(this, new ArrayList<GridBean>());
    }

    private void Handle(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case Constant.LOAD_ALBUM_GROUP_MSGWHAT:
                        if(msg.arg1==Constant.STATUS_SUCCESS){
                            LoadAlbumGroupData();
                        }else{

                        }
                        break;
                }
                super.handleMessage(msg);
            }
        };
        albumUtil.asyncLoadAlbumGroup(handler,Constant.LOAD_ALBUM_GROUP_MSGWHAT);
        Log.d(TAG,"Handle");
        listAlbum.setAdapter(adapter);
        listAlbum.setDividerHeight(1);
    }

    private void LoadAlbumGroupData(){
        Log.d(TAG,"LoadAlbumGroupData1");
        List<AlbumGroupBean> albumGroupBeans= albumUtil.getAlbumGroup();
        int len=albumGroupBeans.size();
        Log.d(TAG,"LoadAlbumGroupData:len="+len);
        List<String> groupCnts=new ArrayList<>();
        for(int i=0;i<len;i++){
            AlbumGroupBean bean=albumGroupBeans.get(i);
            groupCnts.add(String.format("%s(%d)",bean.getBucketName(),bean.getCount()));
        }
        Log.d(TAG,"LoadAlbumGroupData2");
        groupAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,groupCnts);
        spAlbumName.setAdapter(groupAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
