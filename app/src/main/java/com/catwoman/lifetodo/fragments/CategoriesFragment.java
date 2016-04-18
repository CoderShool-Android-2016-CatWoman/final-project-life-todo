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

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by annt on 4/9/16.
 */
public class CategoriesFragment extends Fragment {
    private RealmResults<Category> categories;
    private CategoriesAdapter adapter;
    private GridLayoutManager layoutManager;
    private static int SPAN_COUNT = 2;
    private Realm realm;

    @Bind(R.id.rvCategories)
    RecyclerView rvCategories;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        ButterKnife.bind(this, view);

        realm = Realm.getDefaultInstance();
        categories = realm.where(Category.class).findAllSorted("id", Sort.ASCENDING);
        adapter = new CategoriesAdapter(categories);
        layoutManager = new GridLayoutManager(getContext(), SPAN_COUNT);

        rvCategories.setAdapter(adapter);
        rvCategories.setLayoutManager(layoutManager);

        return view;
    }
}
