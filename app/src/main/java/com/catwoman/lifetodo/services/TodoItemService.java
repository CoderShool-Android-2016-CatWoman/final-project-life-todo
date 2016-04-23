package com.catwoman.lifetodo.services;

import com.catwoman.lifetodo.models.Category;
import com.catwoman.lifetodo.models.Plan;
import com.catwoman.lifetodo.models.TodoItem;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by annt on 4/23/16.
 */
public class TodoItemService {
    // Don't forget to update plan progress after update item
    private static final TodoItemService INSTANCE = new TodoItemService();
    private Realm realm = Realm.getDefaultInstance();

    private TodoItemService() {
    }

    public static TodoItemService getInstance() {
        return INSTANCE;
    }

    public void removeItem(int id) {
        TodoItem todoItem = this.getItem(id);
        realm.beginTransaction();
        todoItem.removeFromRealm();
        realm.commitTransaction();
    }

    public void updateItem(int id, String itemStatus) {
        TodoItem todoItem = this.getItem(id);
        realm.beginTransaction();
        todoItem.setItemStatus(itemStatus);
        realm.commitTransaction();

        PlanService.getInstance().updatePlansProgress(todoItem);
    }

    public void addOrUpdateItem(int id, String itemName, String itemThumbUrl, String itemStatus,
                                String itemDescription, String location, String address,
                                double latitude, double longitude, Category category) {
        TodoItem todoItem;
        realm.beginTransaction();
        if (0 == id) {
            try {
                id = realm.where(TodoItem.class).max("id").intValue() + 1;
            } catch (Exception e) {
                id = 1;
            }
            todoItem = realm.createObject(TodoItem.class);
            todoItem.setId(id);
            todoItem.setCreatedTime(System.currentTimeMillis());
        } else {
            todoItem = this.getItem(id);
        }
        todoItem.setItemName(itemName);
        todoItem.setItemThumbUrl(itemThumbUrl);
        todoItem.setItemStatus(itemStatus);
        todoItem.setItemDescription(itemDescription);
        todoItem.setLocation(location);
        todoItem.setAddress(address);
        todoItem.setLatitude(latitude);
        todoItem.setLongitude(longitude);
        todoItem.setCategory(category);
        todoItem.setModifiedTime(System.currentTimeMillis());
        realm.commitTransaction();

        PlanService.getInstance().updatePlansProgress(todoItem);
    }

    public TodoItem getItem(int id) {
        return realm.where(TodoItem.class).equalTo("id", id).findFirst();
    }

    public RealmResults<TodoItem> getItems(Category category) {
        return realm.where(TodoItem.class)
                .equalTo("category.id", category.getId())
                .findAllSorted("id", Sort.ASCENDING);
    }

    public RealmResults<TodoItem> getDoneItems(Plan plan) {
        return realm.where(TodoItem.class)
                .equalTo("category.id", plan.getCategory().getId())
                .equalTo("itemStatus", "Done")
                .greaterThanOrEqualTo("modifiedTime", plan.getStartTime())
                .findAll();
    }

    public int getDoneItemsCount(Plan plan) {
        RealmResults<TodoItem> doneItems = this.getDoneItems(plan);
        return doneItems.size();
    }
}
