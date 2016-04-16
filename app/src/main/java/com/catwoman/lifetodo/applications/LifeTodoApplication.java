package com.catwoman.lifetodo.applications;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by annt on 4/16/16.
 */
public class LifeTodoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration configuration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(configuration);
    }
}
