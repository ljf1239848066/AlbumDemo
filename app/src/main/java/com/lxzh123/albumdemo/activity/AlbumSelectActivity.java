package com.lxzh123.albumdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.lxzh123.albumdemo.R;

public class AlbumSelectActivity extends AppCompatActivity {
    private final static String TAG="AlbumSelectActivity";

    private Button btnBack;
    private Spinner spAlbumName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_select);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnBack=findViewById(R.id.btn_back);
        spAlbumName=findViewById(R.id.sp_album_name);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}
