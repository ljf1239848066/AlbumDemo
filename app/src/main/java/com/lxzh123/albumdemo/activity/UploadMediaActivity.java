package com.lxzh123.albumdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.lxzh123.albumdemo.R;
import com.lxzh123.albumdemo.common.Constant;
import com.lxzh123.albumdemo.model.MediaBean;

import java.util.ArrayList;

public class UploadMediaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_media);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getUploadData();

    }

    public void getUploadData(){
        Intent intent=getIntent();
        if(intent!=null){
            Bundle bundle=intent.getExtras();
            if(bundle!=null){
                ArrayList<MediaBean> mediaBeans=bundle.getParcelableArrayList(Constant.START_UPLOAD_DATA_TAG);
                int cnt=mediaBeans.size();
                Toast.makeText(this,"Start to upload "+cnt+" media files.",Toast.LENGTH_LONG).show();
            }
        }
    }

}
