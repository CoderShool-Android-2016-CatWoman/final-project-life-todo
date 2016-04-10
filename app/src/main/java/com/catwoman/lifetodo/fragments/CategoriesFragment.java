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

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by annt on 4/9/16.
 */
public class CategoriesFragment extends Fragment {
    private ArrayList<Category> categories;
    private CategoriesAdapter adapter;
    private GridLayoutManager layoutManager;
    private static int SPAN_COUNT = 2;

    @Bind(R.id.rvCategories) RecyclerView rvCategories;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categories = new ArrayList<>();
        adapter = new CategoriesAdapter(categories);
        layoutManager = new GridLayoutManager(getContext(), SPAN_COUNT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        ButterKnife.bind(this, view);

        rvCategories.setAdapter(adapter);
        rvCategories.setLayoutManager(layoutManager);

        populateCategories();
        return view;
    }

    private void populateCategories() {
        categories.addAll(getCategories());
        adapter.notifyDataSetChanged();
    }

    /**
     * Get category list
     * @TODO 4/10/16 Load categories from database
     * @return ArrayList<Category>
     */
    private ArrayList<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "Books", R.drawable.ic_book, R.color.colorDeepPurple));
        categories.add(new Category(2, "Places", R.drawable.ic_travel, R.color.colorBlue));
        categories.add(new Category(3, "Foods", R.drawable.ic_eat, R.color.colorRed));
        categories.add(new Category(4, "Movies", R.drawable.ic_movie, R.color.colorTeal));
        categories.add(new Category(5, "Friends", R.drawable.ic_people, R.color.colorAmber));
        categories.add(new Category(6, "Moments", R.drawable.ic_calendar, R.color.colorPink));
        return categories;
    }
}
