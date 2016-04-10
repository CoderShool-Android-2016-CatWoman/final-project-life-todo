package com.catwoman.lifetodo.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by annt on 4/9/16.
 */
public class Category implements Parcelable {
    private int id;
    private String title;
    private int thumbRes;
    private int colorRes;

    public Category() {
    }

    public Category(int id, String title, int thumbRes, int colorRes) {
        this.id = id;
        this.title = title;
        this.thumbRes = thumbRes;
        this.colorRes = colorRes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getThumbRes() {
        return thumbRes;
    }

    public void setThumbRes(int thumbRes) {
        this.thumbRes = thumbRes;
    }

    public int getColorRes() {
        return colorRes;
    }

    public void setColorRes(int colorRes) {
        this.colorRes = colorRes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeInt(this.thumbRes);
        dest.writeInt(this.colorRes);
    }

    protected Category(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.thumbRes = in.readInt();
        this.colorRes = in.readInt();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
