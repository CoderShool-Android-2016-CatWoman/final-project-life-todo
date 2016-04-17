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
import android.widget.TextView;
import android.widget.Toast;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.adapters.PlansAdapter;
import com.catwoman.lifetodo.models.Category;
import com.catwoman.lifetodo.models.Plan;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by annt on 4/9/16.
 */
public class PlansFragment extends Fragment {
    private ArrayList<Plan> plans;
    private PlansAdapter adapter;
    private LinearLayoutManager layoutManager;
    private Realm realm;

    @Bind(R.id.tvMessage)
    TextView tvMessage;
    @Bind(R.id.rvPlans)
    RecyclerView rvPlans;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        plans = new ArrayList<>();
        adapter = new PlansAdapter(plans);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        realm = Realm.getDefaultInstance();
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
        ArrayList<Plan> savedPlans = new ArrayList<>();
        RealmResults<Plan> results = realm.where(Plan.class).findAllSorted("id", Sort.ASCENDING);
        for (int i = 0; i < results.size(); i++) {
            savedPlans.add(results.get(i));
        }
        if (0 == savedPlans.size()) {
            tvMessage.setText(getString(R.string.meesage_no_plans));
            tvMessage.setVisibility(View.VISIBLE);
        } else {
            plans.addAll(savedPlans);
            adapter.notifyDataSetChanged();
        }
    }

    public void addItem(Plan plan) {
        int id;
        try {
            id = realm.where(Plan.class).max("id").intValue() + 1;
        } catch (Exception e) {
            id = 1;
        }
        plan.setId(id);

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(plan);
        realm.commitTransaction();

        tvMessage.setVisibility(View.GONE);
        plans.add(plan);
        adapter.notifyItemInserted(plans.size() - 1);
        rvPlans.scrollToPosition(plans.size() - 1);

        Toast.makeText(getContext(), getString(R.string.message_plan_added), Toast.LENGTH_LONG).show();
    }
}
