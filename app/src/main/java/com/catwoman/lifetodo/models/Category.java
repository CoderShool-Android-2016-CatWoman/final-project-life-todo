package com.catwoman.lifetodo.models;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by annt on 4/9/16.
 */
public class Category extends RealmObject implements Parcelable {
    @PrimaryKey
    private int id;
    private String title;
    private String drawable;
    private String color;

    public Category() {
    }

    public Category(int id, String title, String drawable, String color) {
        this.id = id;
        this.title = title;
        this.drawable = drawable;
        this.color = color;
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

    public String getDrawable() {
        return drawable;
    }

    public void setDrawable(String drawable) {
        this.drawable = drawable;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.drawable);
        dest.writeString(this.color);
    }

    protected Category(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.drawable = in.readString();
        this.color = in.readString();
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
