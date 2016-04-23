package com.catwoman.lifetodo.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.models.Plan;
import com.catwoman.lifetodo.services.PlanService;
import com.txusballesteros.widgets.FitChart;
import com.txusballesteros.widgets.FitChartValue;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;

public class PlanActivity extends AppCompatActivity {
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.fcProgress)
    FitChart fcProgress;
    @Bind(R.id.ivThumb)
    ImageView ivThumb;
    @Bind(R.id.tvRemainingItems)
    TextView tvRemainingItems;
    @Bind(R.id.tvGoal)
    TextView tvGoal;
    @Bind(R.id.tvStartToEndDate)
    TextView tvStartToEndDate;
    @Bind(R.id.tvRemainingTime)
    TextView tvRemainingTime;
    private Plan plan;
    private PlanService planService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        planService = PlanService.getInstance();

        int id = getIntent().getIntExtra("id", 0);
        plan = planService.getPlan(id);
        if (null == plan) {
            Toast.makeText(this, R.string.error_plan_not_found, Toast.LENGTH_LONG).show();
            finish();
        }

        setListeners();
        populateViews();
    }

    private void setListeners() {
        plan.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange() {
                populateViews();
            }
        });

        fcProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanActivity.this, TodoItemsActivity.class);
                intent.putExtra("categoryId", plan.getCategory().getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_plan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miEdit:
                editPlan();
                return true;
            case R.id.miDelete:
                confirmDeletePlan();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void confirmDeletePlan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirm_delete_plan_title))
                .setMessage(getString(R.string.confirm_delete_plan_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePlan();
                    }
                })
                .setNegativeButton(android.R.string.no, null);
        builder.show();
    }

    private void deletePlan() {
        planService.removePlan(plan.getId());

        Toast.makeText(this, getString(R.string.message_plan_has_been_deleted), Toast.LENGTH_LONG).show();
        finish();
    }

    private void editPlan() {
        Intent intent = new Intent(this, AddPlanActivity.class);
        intent.putExtra("id", plan.getId());
        startActivity(intent);
    }

    private void populateViews() {
        getSupportActionBar().setTitle(plan.getTitle());
        tvName.setText(plan.getTitle());

        ivThumb.setImageResource(getResources().getIdentifier("ic_" + plan.getCategory().getDrawable() + "_color_full",
                "drawable", getPackageName()));

        int remainingItems = plan.getGoal() - plan.getProgress();
        if (0 >= remainingItems) {
            tvRemainingItems.setText(getString(R.string.message_completed));
        } else {
            tvRemainingItems.setText(getString(R.string.number_items_to_go, remainingItems));
        }

        tvGoal.setText(getString(R.string.your_goal_is_number_items, plan.getGoal()));

        fcProgress.setMinValue(0f);
        fcProgress.setMaxValue(plan.getGoal());
        Collection<FitChartValue> values = new ArrayList<>();
        values.add(new FitChartValue(plan.getProgress(), getResources().getColor(
                getResources().getIdentifier("color" + plan.getCategory().getColor(), "color", getPackageName()))));
        fcProgress.setValues(values);

        String startDate = getDateString(plan.getStartTime(), "MMM d yyyy");
        String endDate = getDateString(plan.getEndTime(), "MMM d yyyy");
        String startToEndDate = startDate + " - " + endDate;
        tvStartToEndDate.setText(startToEndDate);

        if (plan.getProgress() < plan.getGoal()) {
            tvRemainingTime.setText(getStringRemaining(plan.getEndTime()));
        } else {
            tvRemainingTime.setText("");
        }
    }

    private String getDateString(long timeStamp, String format) {
        if (timeStamp > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = (new Date(timeStamp));
            return sdf.format(date);
        }

        return "";
    }

    private String getStringRemaining(long time) {
        String remaining = "";
        PrettyTime pt = new PrettyTime();
        if (System.currentTimeMillis() <= time) {
            String duration = pt.formatDuration(new Date(time));
            remaining = getString(R.string._1_s_remaining, duration);
        } else {
            remaining = pt.format(new Date(time));
        }
        return remaining;
    }
}
