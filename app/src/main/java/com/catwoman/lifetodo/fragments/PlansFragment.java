package com.catwoman.lifetodo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.adapters.PlansAdapter;
import com.catwoman.lifetodo.models.Category;
import com.catwoman.lifetodo.models.Plan;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by annt on 4/9/16.
 */
public class PlansFragment extends Fragment {
    private ArrayList<Plan> plans;
    private PlansAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Bind(R.id.rvPlans) RecyclerView rvPlans;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        plans = new ArrayList<>();
        adapter = new PlansAdapter(plans);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plans, container, false);
        ButterKnife.bind(this, view);

        rvPlans.setAdapter(adapter);
        rvPlans.setLayoutManager(layoutManager);

        populatePlans();
        return view;
    }

    private void populatePlans() {
        plans.addAll(getPlans());
        adapter.notifyDataSetChanged();
    }

    /**
     * Get plans
     * @TODO Load plans from database
     * @return ArrayList<Plan>
     */
    public ArrayList<Plan> getPlans() {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "Books", R.drawable.ic_book, R.color.colorDeepPurple));
        categories.add(new Category(2, "Places", R.drawable.ic_travel, R.color.colorBlue));
        categories.add(new Category(3, "Foods", R.drawable.ic_eat, R.color.colorRed));
        categories.add(new Category(4, "Movies", R.drawable.ic_movie, R.color.colorTeal));
        categories.add(new Category(5, "Friends", R.drawable.ic_people, R.color.colorAmber));
        categories.add(new Category(6, "Moments", R.drawable.ic_calendar, R.color.colorPink));

        ArrayList<Plan> plans = new ArrayList<>();
        plans.add(new Plan(1, "Crazy Reading", categories.get(0), 10, 6, 1461974400000L));
        plans.add(new Plan(2, "Travels in 2016", categories.get(1), 6, 2, 1483142400000L));
        plans.add(new Plan(3, "Unbelievable Challenge", categories.get(3), 1000, 6, 1775779200000L));
        return plans;
    }
}
