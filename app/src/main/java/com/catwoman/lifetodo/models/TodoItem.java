package com.catwoman.lifetodo.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TodoItem extends RealmObject {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @PrimaryKey
    private int id;
    private String itemName;
    private String itemThumbUrl;
    private String itemStatus;
    private String itemDescription;

    public TodoItem(){

    }

    public TodoItem(String itemName, String itemThumbUrl, String itemStatus, String itemDescription) {
        this.itemName = itemName;
        this.itemThumbUrl = itemThumbUrl;
        this.itemStatus = itemStatus;
        this.itemDescription = itemDescription;
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

}
