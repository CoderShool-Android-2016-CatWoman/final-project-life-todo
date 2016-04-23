package com.catwoman.lifetodo.models;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by annt on 4/9/16.
 */
public class Plan extends RealmObject implements Parcelable {
    @PrimaryKey
    private int id;
    private String title;
    private Category category;
    private int goal;
    private int progress;
    private long startTime;
    private long endTime;

    public Plan() {
    }

    public Plan(int id, String title, Category category, int goal, int progress, long startTime, long endTime) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.goal = goal;
        this.progress = progress;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeParcelable(this.category, flags);
        dest.writeInt(this.goal);
        dest.writeInt(this.progress);
        dest.writeLong(this.startTime);
        dest.writeLong(this.endTime);
    }

    protected Plan(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.category = in.readParcelable(Category.class.getClassLoader());
        this.goal = in.readInt();
        this.progress = in.readInt();
        this.startTime = in.readLong();
        this.endTime = in.readLong();
    }

    public static final Creator<Plan> CREATOR = new Creator<Plan>() {
        @Override
        public Plan createFromParcel(Parcel source) {
            return new Plan(source);
        }

        @Override
        public Plan[] newArray(int size) {
            return new Plan[size];
        }
    };
}
