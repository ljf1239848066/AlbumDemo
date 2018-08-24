package com.lxzh123.albumdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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

import com.bumptech.glide.Glide;
import com.lxzh123.albumdemo.R;
import com.lxzh123.albumdemo.common.Constant;
import com.lxzh123.albumdemo.dao.AlbumDao;
import com.lxzh123.albumdemo.dao.MediaDao;
import com.lxzh123.albumdemo.model.AlbumGroupBean;
import com.lxzh123.albumdemo.model.MediaBean;
import com.lxzh123.albumdemo.model.MediaDateGroupBean;
import com.lxzh123.albumdemo.util.ViewUtil;
import com.lxzh123.albumdemo.view.CheckableRecyclerView;
import com.lxzh123.albumdemo.view.DateGroupMediaAdapter;
import com.lxzh123.albumdemo.view.GridDecoration;
import com.lxzh123.albumdemo.view.GroupSpanSizeLookup;
import com.lxzh123.albumdemo.view.OnMediaCheckedChangedListener;
import com.lxzh123.albumdemo.view.SpinnerTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumSelectActivity extends AppCompatActivity implements OnMediaCheckedChangedListener {
    private final static String TAG="AlbumSelectActivity";

    private Toolbar toolbar;
    private Button btnBack;
    private SpinnerTextView tvGroupName;
    private AlbumGroupListDialog groupListDialog;
    private CheckableRecyclerView rvMedias;
    private VisiableDialog visiableDialog;
    private AppCompatSpinner spWhoCanSee;
    private Button btnUpload;

    private DateGroupMediaAdapter adapter;
    private Handler handler;
    private AlbumDao albumDao;
    private MediaDao mediaDao;

    private String[] spinnerItemTag={"itemIcon","itemText"};

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
        spWhoCanSee=findViewById(R.id.sp_who_can_see);
        btnUpload=findViewById(R.id.btn_upload);

        albumDao =new AlbumDao(this);
        mediaDao =new MediaDao(this);
        adapter=new DateGroupMediaAdapter(this,rvMedias);
        GridLayoutManager layoutManager=new GridLayoutManager(this,Constant.ALBUM_COLUMN_COUNT);
        GroupSpanSizeLookup lookup = new GroupSpanSizeLookup(adapter, layoutManager);
        layoutManager.setSpanSizeLookup(lookup);
        rvMedias.setLayoutManager(layoutManager);
        rvMedias.setHasFixedSize(true);
        rvMedias.setItemAnimator(new DefaultItemAnimator());
        rvMedias.addItemDecoration(new GridDecoration(Constant.ALBUM_COLUMN_COUNT, 6, true));
        rvMedias.setAdapter(adapter);
        rvMedias.setOnCheckedChangeListener(this);

        initVisiableSpinner();
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
                    case Constant.LOAD_ALBUM_MSGWHAT:
                        if(msg.arg1==Constant.STATUS_SUCCESS){
                            LoadAlbumData();
                        }else{

                        }
                        break;
                }
                super.handleMessage(msg);
            }
        };
        albumDao.asyncLoadAlbumGroup(handler,Constant.LOAD_ALBUM_GROUP_MSGWHAT);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpload();
            }
        });
    }

    private void LoadAlbumGroupData(){
        Log.d(TAG,"LoadAlbumGroupData1");
        List<AlbumGroupBean> albumGroupBeans= albumDao.getAlbumGroup();
        tvGroupName.setClickable(true);
        tvGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlbumGroupDialog();
            }
        });
        onGroupItemSelected(albumGroupBeans.get(0));
        initAlbumGroupDialog(albumGroupBeans);
    }

    private void LoadAlbumData(){
        List<MediaDateGroupBean> mediaDateGroupBeans= mediaDao.getMediaDateGroupBeanList();
        Log.d(TAG,"LoadAlbumData2:size="+mediaDateGroupBeans.size());
        adapter.setMediaDateGroupBeanList(mediaDateGroupBeans);

        adapter.notifyDataSetChanged();
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
        groupListDialog.setCanceledOnTouchOutside(true);
    }

    private void showAlbumGroupDialog(){
        Log.d(TAG,"showAlbumGroupDialog");
        groupListDialog.show();
        tvGroupName.setDropdown(true);
    }

    private void onGroupItemSelected(AlbumGroupBean bean){
        tvGroupName.setDropdown(false);
        tvGroupName.setText(bean.getBucketName());
        tvGroupName.setTag(bean.getPath());
        Log.d(TAG,"onGroupItemSelected:"+bean.getPath());
        if(mediaDao.isWorking()){
            Log.d(TAG,"onGroupItemSelected:isWorking,try stop");
            mediaDao.stopLoading();
        }
        File file=new File(bean.getPath());
        Log.d(TAG,"onGroupItemSelected:reload media");
        mediaDao.asyncLoadMedia(handler,Constant.LOAD_ALBUM_MSGWHAT, file.getParentFile().getAbsolutePath());
    }

    private void initVisiableSpinner() {
        String[] name = {"itemIcon", "itemText"};
        int[] iconIds = {R.drawable.icon_visible_all_enable, R.drawable.icon_visible_family_enable,
                R.drawable.icon_visible_parents_enable, R.drawable.icon_visible_me_enable};
        int[] strIds = {R.string.visiable_all_enable, R.string.visiable_family_enable,
                R.string.visiable_parents_enable, R.string.visiable_me_enable};
        List<Map<String, Integer>> data = new ArrayList<>();
        for (int i = 0; i < iconIds.length; i++) {
            Map<String, Integer> map = new HashMap<>();
            map.put(name[0], iconIds[i]);
            map.put(name[1], strIds[i]);
            data.add(map);
        }
        VisiableAdapter visiableAdapter = new VisiableAdapter(data);
        spWhoCanSee.setAdapter(visiableAdapter);
    }

    @Override
    public void onCheckedChanged(View view) {
        List<MediaBean> checkedItems=adapter.getCheckedItem();
        int cnt=checkedItems.size();
        Log.d(TAG,"onCheckedChanged:cnt="+cnt);
        if(cnt>0){
            btnUpload.setText(String.format("%s(%d)",getString(R.string.upload),cnt));
            btnUpload.setEnabled(true);
            btnUpload.setBackgroundResource(R.drawable.btn_upload_bg_highlight);
        }else{
            btnUpload.setText(getString(R.string.upload));
            btnUpload.setEnabled(false);
            btnUpload.setBackgroundResource(R.drawable.btn_upload_bg_disable);
        }
    }

    public void startUpload(){
        Intent intent=new Intent(this,UploadMediaActivity.class);
        Bundle bundle=new Bundle();
        ArrayList<MediaBean> checkedItems=(ArrayList<MediaBean>)adapter.getCheckedItem();
        bundle.putParcelableArrayList(Constant.START_UPLOAD_DATA_TAG,checkedItems);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(albumDao.isWorking()){
            albumDao.stopLoading();
        }
        if(mediaDao.isWorking()){
            mediaDao.stopLoading();
        }
        if(groupListDialog.isShowing()){
            groupListDialog.dismiss();
        }
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
            Glide.with(getBaseContext()).load(bean.getPath()).centerCrop().into(ivGroupThumb);
//            ivGroupThumb.setImageBitmap(AlbumDao.loadThumbnail(bean.getPath(),Constant.GROUP_THUMBNAIL_SIZE));
            tvGroupNameCnt.setText(String.format("%s(%d)",bean.getBucketName(),bean.getCount()));
            return convertView;
        }
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

    class VisiableAdapter extends BaseAdapter{
        private List<Map<String,Integer>> data;

        public VisiableAdapter() {
            this.data=new ArrayList<>();
        }
        public VisiableAdapter(List<Map<String, Integer>> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater=LayoutInflater.from(getBaseContext());
            convertView=layoutInflater.inflate(R.layout.layout_visiable_spinner_item,null);
            ImageView ivIcon=convertView.findViewById(R.id.iv_icon);
            TextView tvText=convertView.findViewById(R.id.tv_text);
            Map<String,Integer> map=data.get(position);
            ivIcon.setImageResource(map.get(spinnerItemTag[0]));
            tvText.setText(getString(map.get(spinnerItemTag[1])));
            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater=LayoutInflater.from(getBaseContext());
            convertView=layoutInflater.inflate(R.layout.layout_visiable_spinner_item,null);
            if(position==0){
                TextView tvTitle=convertView.findViewById(R.id.tv_title);
                tvTitle.setText(getString(R.string.title_who_can_see));
                tvTitle.setVisibility(View.VISIBLE);
            }
            ImageView ivIcon=convertView.findViewById(R.id.iv_icon);
            TextView tvText=convertView.findViewById(R.id.tv_text);
            Map<String,Integer> map=data.get(position);
            ivIcon.setImageResource(map.get(spinnerItemTag[0]));
            tvText.setText(getString(map.get(spinnerItemTag[1])));
            return convertView;
        }
    }

    class VisiableDialog extends AppCompatDialog{
        public VisiableDialog(Context context) {
            super(context);
        }
    }
}
