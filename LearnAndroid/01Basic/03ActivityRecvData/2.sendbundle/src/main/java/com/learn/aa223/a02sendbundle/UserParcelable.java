package com.learn.aa223.a02sendbundle;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aa223 on 2016/10/1.
 */

public class UserParcelable implements Parcelable {

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static Creator<UserParcelable> getCREATOR() {
        return CREATOR;
    }

    protected UserParcelable(Parcel in) {

        setName(in.readString());
        setAge(in.readInt());
    }

    public UserParcelable(String name,int age){

        setName(name);
        setAge(age);
    }

    // 向Parcel中共享的記憶體區域寫入資料
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(getName());
        dest.writeInt(getAge());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserParcelable> CREATOR = new Creator<UserParcelable>() {

        // 使用存在Pacel指向的共享記憶體創造對象
        @Override
        public UserParcelable createFromParcel(Parcel in) {
            return new UserParcelable(in);
        }

        @Override
        public UserParcelable[] newArray(int size) {
            return new UserParcelable[size];
        }
    };
}
