package com.canyinghao.canaccess.bean;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yangjian on 15/6/6.
 */

public class EventBean implements Parcelable {

    int id;

    public String  eventTypeStr="";
    public int eventType;


    public String packageName="";
    public String label="";
    public Drawable icon;

    public long eventTime;
    public String eventTimeStr="";
    public String className="";

    public String text="";

    public int flag;
    public String contentDescription="";

    public String beforeText="";

    public String datail="";






   


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.eventTypeStr);
        dest.writeInt(this.eventType);
        dest.writeString(this.packageName);
        dest.writeString(this.label);
        dest.writeLong(this.eventTime);
        dest.writeString(this.eventTimeStr);
        dest.writeString(this.className);
        dest.writeString(this.text);
        dest.writeInt(this.flag);
        dest.writeString(this.contentDescription);
        dest.writeString(this.beforeText);
        dest.writeString(this.datail);

    }

    public EventBean() {
    }

    public EventBean(Parcel in) {
        this.id = in.readInt();
        this.eventTypeStr = in.readString();
        this.eventType = in.readInt();
        this.packageName = in.readString();
        this.label = in.readString();
        this.eventTime = in.readLong();
        this.eventTimeStr = in.readString();
        this.className = in.readString();
        this.text = in.readString();
        this.flag = in.readInt();
        this.contentDescription = in.readString();
        this.beforeText = in.readString();
        this.datail = in.readString();


    }

    public static final Parcelable.Creator<EventBean> CREATOR = new Parcelable.Creator<EventBean>() {
        public EventBean createFromParcel(Parcel source) {
            return new EventBean(source);
        }

        public EventBean[] newArray(int size) {
            return new EventBean[size];
        }
    };
}
