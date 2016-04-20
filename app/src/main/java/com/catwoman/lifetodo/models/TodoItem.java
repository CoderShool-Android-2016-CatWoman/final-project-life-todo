package com.catwoman.lifetodo.models;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TodoItem extends RealmObject implements Parcelable {
    @PrimaryKey
    private int id;
    private String itemName;
    private String itemThumbUrl;
    private String itemStatus;
    private String itemDescription;
    private String location;
    private String address;
    private double latitude;
    private double longitude;
    private Category category;

    public TodoItem(){

    }

    public TodoItem(int id, String itemName, String itemThumbUrl, String itemStatus, String itemDescription, String location, String address, double latitude, double longitude, Category category) {
        this.id = id;
        this.itemName = itemName;
        this.itemThumbUrl = itemThumbUrl;
        this.itemStatus = itemStatus;
        this.itemDescription = itemDescription;
        this.location = location;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemThumbUrl() {
        return itemThumbUrl;
    }

    public void setItemThumbUrl(String itemThumbUrl) {
        this.itemThumbUrl = itemThumbUrl;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.itemName);
        dest.writeString(this.itemThumbUrl);
        dest.writeString(this.itemStatus);
        dest.writeString(this.itemDescription);
        dest.writeString(this.location);
        dest.writeString(this.address);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeParcelable(this.category, flags);
    }

    protected TodoItem(Parcel in) {
        this.id = in.readInt();
        this.itemName = in.readString();
        this.itemThumbUrl = in.readString();
        this.itemStatus = in.readString();
        this.itemDescription = in.readString();
        this.location = in.readString();
        this.address = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.category = in.readParcelable(Category.class.getClassLoader());
    }

    public static final Creator<TodoItem> CREATOR = new Creator<TodoItem>() {
        @Override
        public TodoItem createFromParcel(Parcel source) {
            return new TodoItem(source);
        }

        @Override
        public TodoItem[] newArray(int size) {
            return new TodoItem[size];
        }
    };
}
