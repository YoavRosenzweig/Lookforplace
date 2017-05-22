package com.project.yoavr.lookforplace;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yoavr on 12/04/2017.
 */

class  MyPlace implements Parcelable {

    String name;
    String icon;
    String image;
    Myphoto[] photos;
    String formatted_address;
    MyGeometry geometry;
    String vicinity;
    int myid;
    String addres;
    String distane;



    protected MyPlace(Parcel in) {
        name = in.readString();
        icon = in.readString();
        formatted_address = in.readString();
        vicinity = in.readString();
    }

    public static final Creator<MyPlace> CREATOR = new Creator<MyPlace>() {
        @Override
        public MyPlace createFromParcel(Parcel in) {
            return new MyPlace(in);
        }

        @Override
        public MyPlace[] newArray(int size) {
            return new MyPlace[size];
        }
    };

    public MyPlace(String icon, String name, String addres, String distane, int myid) {
        this.myid=myid;
        this.name=name;
        this.addres=addres;
        this.distane=distane;
        this.icon=icon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(icon);
        dest.writeString(formatted_address);
        dest.writeString(vicinity);
    }
}
