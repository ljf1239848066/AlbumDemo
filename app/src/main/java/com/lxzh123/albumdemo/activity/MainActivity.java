package com.lxzh123.albumdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lxzh123.albumdemo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSelectPhoto=findViewById(R.id.btn_select_photo);
        btnSelectPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startSelectAlbum();
            }
        });
    }

    private void startSelectAlbum(){
        Intent intent=new Intent(this,AlbumSelectActivity.class);
        startActivity(intent);
    }

}
