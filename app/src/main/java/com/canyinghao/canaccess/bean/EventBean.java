package com.canyinghao.canaccess.bean;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by yangjian on 15/6/6.
 */

public class EventBean implements Parcelable {

    int id;

    private String  eventTypeStr="";
    private int eventType;


    private String packageName="";
    private String label="";
    private Drawable icon;

    private long eventTime;
    private String eventTimeStr="";
    private String className="";

    private String text="";

    private int action;
    private String contentDescription="";

    private String beforeText="";

    private String datail="";




    private Date date=new Date();

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getDatail() {
        return datail;
    }

    public void setDatail(String datail) {
        this.datail = datail;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEventTypeStr() {
        return eventTypeStr;
    }

    public void setEventTypeStr(String eventTypeStr) {
        this.eventTypeStr = eventTypeStr;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventTimeStr() {
        return eventTimeStr;
    }

    public void setEventTimeStr(String eventTimeStr) {
        this.eventTimeStr = eventTimeStr;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getContentDescription() {
        return contentDescription;
    }

    public void setContentDescription(String contentDescription) {
        this.contentDescription = contentDescription;
    }

    public String getBeforeText() {
        return beforeText;
    }

    public void setBeforeText(String beforeText) {
        this.beforeText = beforeText;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


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
        dest.writeInt(this.action);
        dest.writeString(this.contentDescription);
        dest.writeString(this.beforeText);
        dest.writeString(this.datail);
        dest.writeLong(date != null ? date.getTime() : -1);
    }

    public EventBean() {
    }

    private EventBean(Parcel in) {
        this.id = in.readInt();
        this.eventTypeStr = in.readString();
        this.eventType = in.readInt();
        this.packageName = in.readString();
        this.label = in.readString();
        this.eventTime = in.readLong();
        this.eventTimeStr = in.readString();
        this.className = in.readString();
        this.text = in.readString();
        this.action = in.readInt();
        this.contentDescription = in.readString();
        this.beforeText = in.readString();
        this.datail = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
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
