package com.lxzh123.albumdemo.view.AlbumGridView;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * description 相册数据源
 * author      Created by lxzh
 * date        2018/8/22
 */
public class GridBean<T extends Parcelable> implements Parcelable
{
    private String thumbUrl;
    private String originUrl;
    private long date;
    private boolean isChecked;
    private T t;

    public GridBean(String thumbUrl, String originUrl,long date, boolean isChecked, T t)
    {
        this.thumbUrl = thumbUrl;
        this.originUrl = originUrl;
        this.date=date;
        this.isChecked=isChecked;
        this.t = t;
    }

    public String getThumbUrl()
    {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl)
    {
        this.thumbUrl = thumbUrl;
    }

    public String getOriginUrl()
    {
        return originUrl;
    }

    public void setOriginUrl(String originUrl)
    {
        this.originUrl = originUrl;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public T getT()
    {
        return t;
    }

    public void setT(T t)
    {
        this.t = t;
    }

    @Override
    public String toString()
    {
        return "GridBean{" +
                "thumbUrl='" + thumbUrl + '\'' +
                ", originUrl='" + originUrl + '\'' +
                ", date='" + date + '\'' +
                ", isChecked='" + isChecked + '\'' +
                ", t=" + t +
                '}';
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.thumbUrl);
        dest.writeString(this.originUrl);
        dest.writeLong(this.date);
        dest.writeByte((byte)(this.isChecked?1:0));
        dest.writeString(t.getClass().getName());//Write the class name of T class
        dest.writeParcelable(this.t, flags);
    }

    protected GridBean(Parcel in)
    {
        this.thumbUrl = in.readString();
        this.originUrl = in.readString();
        this.date=in.readLong();
        this.isChecked=in.readByte()!=0;
        String tClassName = in.readString();//Read the class name of T class
        try
        {
            this.t = in.readParcelable(Class.forName(tClassName).getClassLoader());
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static final Creator<GridBean> CREATOR = new Creator<GridBean>()
    {
        @Override
        public GridBean createFromParcel(Parcel source)
        {
            return new GridBean(source);
        }

        @Override
        public GridBean[] newArray(int size)
        {
            return new GridBean[size];
        }
    };
}
