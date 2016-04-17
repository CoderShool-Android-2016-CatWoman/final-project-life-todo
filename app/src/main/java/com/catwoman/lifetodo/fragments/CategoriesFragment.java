package com.catwoman.lifetodo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.adapters.CategoriesAdapter;
import com.catwoman.lifetodo.models.Category;
import com.catwoman.lifetodo.models.Plan;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by annt on 4/9/16.
 */
public class CategoriesFragment extends Fragment {
    private ArrayList<Category> categories;
    private CategoriesAdapter adapter;
    private GridLayoutManager layoutManager;
    private static int SPAN_COUNT = 2;
    private Realm realm;

    @Bind(R.id.rvCategories) RecyclerView rvCategories;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categories = new ArrayList<>();
        adapter = new CategoriesAdapter(categories);
        layoutManager = new GridLayoutManager(getContext(), SPAN_COUNT);
        realm = Realm.getDefaultInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        ButterKnife.bind(this, view);

        rvCategories.setAdapter(adapter);
        rvCategories.setLayoutManager(layoutManager);

        addDefaultCategories();
        populateCategories();
        return view;
    }

    private void populateCategories() {
        ArrayList<Category> savedCategories = new ArrayList<>();
        RealmResults<Category> results = realm.where(Category.class).findAllSorted("id", Sort.ASCENDING);
        for (int i = 0; i < results.size(); i++) {
            savedCategories.add(results.get(i));
        }

        categories.addAll(savedCategories);
        adapter.notifyDataSetChanged();
    }

    private void addDefaultCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "Books", "book", "DeepPurple"));
        categories.add(new Category(2, "Places", "travel", "Blue"));
        categories.add(new Category(3, "Foods", "eat", "Red"));
        categories.add(new Category(4, "Movies", "movie", "Teal"));
        categories.add(new Category(5, "Friends", "people", "Amber"));
        categories.add(new Category(6, "Moments", "calendar", "Pink"));

        realm.beginTransaction();
        for (int i = 0; i < categories.size(); i++) {
            realm.copyToRealmOrUpdate(categories.get(i));
        }
        realm.commitTransaction();
    }
}
