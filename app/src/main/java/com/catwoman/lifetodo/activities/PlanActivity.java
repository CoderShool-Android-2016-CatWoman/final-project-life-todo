package com.catwoman.lifetodo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.models.Plan;
import com.txusballesteros.widgets.FitChart;
import com.txusballesteros.widgets.FitChartValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlanActivity extends AppCompatActivity {
    private Plan plan;

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
    @Bind(R.id.tvDueDate)
    TextView tvDueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        plan = getIntent().getParcelableExtra("plan");
        getSupportActionBar().setTitle(plan.getTitle());

        populateViews();
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
                deletePlan();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deletePlan() {

    }

    private void editPlan() {

    }

    /**
     * @TODO Calculate progress
     */
    private void populateViews() {
        // dummy progress
        plan.setProgress(1);

        tvName.setText(plan.getTitle());

        ivThumb.setImageResource(getResources().getIdentifier("ic_" + plan.getCategory().getDrawable() + "_color_full",
                "drawable", getPackageName()));

        int remainingItems = plan.getGoal() - plan.getProgress();
        if (0 == remainingItems) {
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

        tvDueDate.setText(getDateString(plan.getDueTime(), "MMM d yyyy"));
    }

    private String getDateString(long timeStamp, String format) {
        if (timeStamp > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = (new Date(timeStamp));
            return sdf.format(date);
        }

        return "";
    }
}
