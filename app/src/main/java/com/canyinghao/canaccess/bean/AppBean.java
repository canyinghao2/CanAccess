/*
 * Copyright 2012 Mark Injerd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.canyinghao.canaccess.bean;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class AppBean implements Parcelable {
    int id;
	public String packageName, label;
	public int type;
    public Drawable icon;

    public AppBean(Drawable icon, int type, String label, String packageName) {
        this.icon = icon;
        this.type=type;
        this.label = label;
        this.packageName = packageName;
    }


    public AppBean() {
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.packageName);
        dest.writeString(this.label);
        dest.writeInt(this.type);

    }

    private AppBean(Parcel in) {
        this.id = in.readInt();
        this.packageName = in.readString();
        this.label = in.readString();
        this.type = in.readInt();

    }

    public static final Parcelable.Creator<AppBean> CREATOR = new Parcelable.Creator<AppBean>() {
        public AppBean createFromParcel(Parcel source) {
            return new AppBean(source);
        }

        public AppBean[] newArray(int size) {
            return new AppBean[size];
        }
    };
}
