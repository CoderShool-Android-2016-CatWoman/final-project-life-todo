package com.catwoman.lifetodo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.adapters.PlansAdapter;
import com.catwoman.lifetodo.models.Plan;
import com.catwoman.lifetodo.services.PlanService;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by annt on 4/9/16.
 */
public class PlansFragment extends Fragment {
    @Bind(R.id.tvMessage)
    TextView tvMessage;
    @Bind(R.id.rvPlans)
    RecyclerView rvPlans;
    private RealmResults<Plan> plans;
    private PlansAdapter adapter;
    private LinearLayoutManager layoutManager;
    private PlanService planService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plans, container, false);
        ButterKnife.bind(this, view);

        planService = PlanService.getInstance();
        plans = planService.getPlans();
        adapter = new PlansAdapter(plans);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rvPlans.setAdapter(adapter);
        rvPlans.setLayoutManager(layoutManager);

        toggleMessage();

        setListeners();
        return view;
    }

    private void setListeners() {
        RealmChangeListener changeListener = new RealmChangeListener() {
            @Override
            public void onChange() {
                toggleMessage();
                adapter.notifyDataSetChanged();
            }
        };

        plans.addChangeListener(changeListener);
    }

    private void toggleMessage() {
        tvMessage.setVisibility(0 == plans.size() ? View.VISIBLE : View.GONE);
    }
}
