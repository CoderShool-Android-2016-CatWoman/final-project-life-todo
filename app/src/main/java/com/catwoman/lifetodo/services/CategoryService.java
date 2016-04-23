package com.catwoman.lifetodo.services;

import com.catwoman.lifetodo.models.Category;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by annt on 4/23/16.
 */
public class CategoryService {
    private static final CategoryService INSTANCE = new CategoryService();
    private Realm realm = Realm.getDefaultInstance();

    private CategoryService() {
    }

    public static CategoryService getInstance() {
        return INSTANCE;
    }

    public Category getCategory(int id) {
        return realm.where(Category.class).equalTo("id", id).findFirst();
    }

    public RealmResults<Category> getCategories() {
        return realm.where(Category.class).findAllSorted("id", Sort.ASCENDING);
    }
}
