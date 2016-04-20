package com.catwoman.lifetodo.applications;

import android.app.Application;

import com.catwoman.lifetodo.models.Category;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by annt on 4/16/16.
 */
public class LifeTodoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration configuration = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(configuration);

        addDefaultData();
    }

    private void addDefaultData() {
        Realm realm = Realm.getDefaultInstance();

        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "book", "Books", "book", "DeepPurple"));
        categories.add(new Category(2, "place", "Places", "travel", "Blue"));
        categories.add(new Category(3, "food", "Foods", "eat", "Red"));
        categories.add(new Category(4, "movie", "Movies", "movie", "Teal"));
        categories.add(new Category(5, "friend", "Friends", "people", "Amber"));
        categories.add(new Category(6, "moment", "Moments", "calendar", "Pink"));

        realm.beginTransaction();
        for (int i = 0; i < categories.size(); i++) {
            realm.copyToRealmOrUpdate(categories.get(i));
        }
        realm.commitTransaction();
    }
}
