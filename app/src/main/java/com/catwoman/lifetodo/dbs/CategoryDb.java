package com.catwoman.lifetodo.dbs;

import com.catwoman.lifetodo.models.Category;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by annt on 4/23/16.
 */
public class CategoryDb {
    private static final CategoryDb INSTANCE = new CategoryDb();
    private Realm realm = Realm.getDefaultInstance();

    private CategoryDb() {
    }

    public static CategoryDb getInstance() {
        return INSTANCE;
    }

    public Category getCategory(int id) {
        return realm.where(Category.class).equalTo("id", id).findFirst();
    }

    public RealmResults<Category> getCategories() {
        return realm.where(Category.class).findAllSorted("id", Sort.ASCENDING);
    }
}
