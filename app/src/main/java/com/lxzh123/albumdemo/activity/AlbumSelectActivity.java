package com.lxzh123.albumdemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lxzh123.albumdemo.R;
import com.lxzh123.albumdemo.common.Constant;
import com.lxzh123.albumdemo.model.AlbumGroupBean;
import com.lxzh123.albumdemo.util.AlbumUtil;
import com.lxzh123.albumdemo.util.ImageSelectObservable;
import com.lxzh123.albumdemo.util.ViewUtil;
import com.lxzh123.albumdemo.view.DateGroupMediaAdapter;
import com.lxzh123.albumdemo.view.GroupSpanSizeLookup;
import com.lxzh123.albumdemo.view.SpinnerTextView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class AlbumSelectActivity extends AppCompatActivity implements Observer{
    private final static String TAG="AlbumSelectActivity";

    private Toolbar toolbar;
    private Button btnBack;
    private SpinnerTextView tvGroupName;
    private AlbumGroupListDialog groupListDialog;
    private RecyclerView rvMedias;

    private DateGroupMediaAdapter adapter;
    private Handler handler;
    private AlbumUtil albumUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_album_select);

        Init();
        Handle();
    }

    private void Init(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        btnBack=findViewById(R.id.btn_back);
        tvGroupName=findViewById(R.id.tv_album_group_name);
        rvMedias=findViewById(R.id.rv_album_list);
        ImageSelectObservable.getInstance().addObserver(this);
        albumUtil=new AlbumUtil(this);
        adapter=new DateGroupMediaAdapter(this);
        GridLayoutManager layoutManager=new GridLayoutManager(this,Constant.ALBUM_COLUMN_COUNT);
        GroupSpanSizeLookup lookup = new GroupSpanSizeLookup(adapter, layoutManager);
        layoutManager.setSpanSizeLookup(lookup);
        rvMedias.setLayoutManager(layoutManager);
        rvMedias.setHasFixedSize(false);
        rvMedias.setItemAnimator(new DefaultItemAnimator());
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
        rvMedias.setAdapter(adapter);
    }

    private void LoadAlbumGroupData(){
        Log.d(TAG,"LoadAlbumGroupData1");
        List<AlbumGroupBean> albumGroupBeans= albumUtil.getAlbumGroup();
        tvGroupName.setClickable(true);
        tvGroupName.setText(albumGroupBeans.get(0).getBucketName());
        tvGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlbumGroupDialog();
            }
        });
        Log.d(TAG,"LoadAlbumGroupData2");
        initAlbumGroupDialog(albumGroupBeans);
    }

    private void initAlbumGroupDialog(List<AlbumGroupBean> albumGroupBeans){
        groupListDialog=new AlbumGroupListDialog(this,R.layout.layout_album_group_list,albumGroupBeans);

        Window window = groupListDialog.getWindow();

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w=dm.widthPixels;
        int h=dm.heightPixels;
        Display display = this.getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = w/2; // 设置宽度
        lp.height = h/2; // 设置高度
        lp.x=10;
        lp.y=ViewUtil.getStatusBarHeight(this)+toolbar.getHeight();
        window.setGravity(Gravity.LEFT|Gravity.TOP);
        window.setAttributes(lp);
    }

    private void showAlbumGroupDialog(){
        Log.d(TAG,"showAlbumGroupDialog");
        groupListDialog.show();
        tvGroupName.setDropdown(true);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    class SimpleGroupAdapter extends BaseAdapter{
        private List<AlbumGroupBean> data;

        public SimpleGroupAdapter(List<AlbumGroupBean> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return this.data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.layout_album_group_item,null);
            ImageView ivGroupThumb=convertView.findViewById(R.id.iv_group_thumbnail);
            TextView tvGroupNameCnt=convertView.findViewById(R.id.tv_group_name_cnt);
            AlbumGroupBean bean=this.data.get(position);
            ivGroupThumb.setImageBitmap(AlbumUtil.loadThumbnail(bean.getPath(),Constant.THUMBNAIL_SIZE));
            tvGroupNameCnt.setText(String.format("%s(%d)",bean.getBucketName(),bean.getCount()));
            return convertView;
        }
    }

    private void onGroupItemSelected(AlbumGroupBean bean){
        tvGroupName.setText(bean.getBucketName());
        tvGroupName.setDropdown(false);
    }

    class AlbumGroupListDialog extends AppCompatDialog {

        private ListView lvAlbumGroup;
        private List<AlbumGroupBean> groupBeanList;
        private SimpleGroupAdapter groupAdapter;


        public AlbumGroupListDialog(Context context, int theme, List<AlbumGroupBean> list) {
            super(context, theme);
            groupBeanList=list;
            setContentView(theme);
            lvAlbumGroup=findViewById(R.id.lv_album_group_list);
            groupAdapter=new SimpleGroupAdapter(groupBeanList);
            lvAlbumGroup.setAdapter(groupAdapter);
            lvAlbumGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onDialogListItemClicked(position);
                }
            });
        }
        private void onDialogListItemClicked(int position){
            onGroupItemSelected(groupBeanList.get(position));
            this.hide();
        }
    }
}
