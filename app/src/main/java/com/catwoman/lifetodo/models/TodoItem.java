package com.catwoman.lifetodo.models;

import java.util.ArrayList;

public class TodoItem {
    private String itemName;
    private String itemThumbUrl;
    private String itemStatus;
    private String itemDescription;

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

    private static int lastContactId = 0;

    public static ArrayList<TodoItem> createItemsList(int numItems) {
        ArrayList<TodoItem> items = new ArrayList<TodoItem>();

        for (int i = 1; i <= numItems; i++) {
            if (i>10) {
                items.add(new TodoItem("name", "thumb", "Done", "has"));
            }
            else{
                items.add(new TodoItem("name", "thumb", "Progress", "has"));
            }
        }

        return items;
    }

}
