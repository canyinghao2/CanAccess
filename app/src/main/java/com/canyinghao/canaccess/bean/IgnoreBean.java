package com.canyinghao.canaccess.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by yangjian on 15/6/7.
 */
public class IgnoreBean implements Parcelable {


    private String title;
    private String text;
    private Date date=new Date();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.text);
        dest.writeLong(date != null ? date.getTime() : -1);
    }

    public IgnoreBean() {
    }

    private IgnoreBean(Parcel in) {
        this.title = in.readString();
        this.text = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
    }

    public static final Parcelable.Creator<IgnoreBean> CREATOR = new Parcelable.Creator<IgnoreBean>() {
        public IgnoreBean createFromParcel(Parcel source) {
            return new IgnoreBean(source);
        }

        public IgnoreBean[] newArray(int size) {
            return new IgnoreBean[size];
        }
    };
}
