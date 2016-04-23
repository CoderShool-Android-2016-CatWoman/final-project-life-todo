package com.catwoman.lifetodo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.fragments.DatePickerFragment;
import com.catwoman.lifetodo.models.Category;
import com.catwoman.lifetodo.models.Plan;
import com.catwoman.lifetodo.services.CategoryService;
import com.catwoman.lifetodo.services.PlanService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class AddPlanActivity extends AppCompatActivity
        implements DatePickerFragment.DatePickerFragmentListener {
    private static final int REQUEST_START_DATE = 1;
    private static final int REQUEST_END_DATE = 2;
    private static final String DATE_FORMAT = "MM-dd-yyyy";
    @Bind(R.id.spCategory)
    Spinner spCategory;
    @Bind(R.id.etName)
    EditText etName;
    @Bind(R.id.etGoal)
    EditText etGoal;
    @Bind(R.id.etStartDate)
    EditText etStartDate;
    @Bind(R.id.etEndDate)
    EditText etEndDate;
    private long startTimeInMillis = 0;
    private long endTimeInMillis = 0;
    private RealmResults<Category> categories;
    private boolean isEdit = false;
    private Plan plan;
    private CategoryService categoryService;
    private PlanService planService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        categoryService = CategoryService.getInstance();
        planService = PlanService.getInstance();

        categories = categoryService.getCategories();

        int id = getIntent().getIntExtra("id", 0);
        if (0 != id) {
            plan = planService.getPlan(id);
            if (null == plan) {
                Toast.makeText(this, R.string.error_plan_not_found, Toast.LENGTH_LONG).show();
                finish();
            }

            isEdit = true;
            getSupportActionBar().setTitle(getString(R.string.edit_plan));
        }

        populateViews();
    }

    private void populateViews() {
        // spinner
        String[] categoryArray = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            categoryArray[i] = categories.get(i).getTitle();
        }

        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categoryArray);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(categoriesAdapter);

        if (isEdit) {
            populateValues();
        } else {
            startTimeInMillis = System.currentTimeMillis();
            etStartDate.setText(getDateString(startTimeInMillis, DATE_FORMAT));
        }
    }

    private void populateValues() {
        for (int i = 0; i < categories.size(); i++) {
            if (plan.getCategory().getId() == categories.get(i).getId()) {
                spCategory.setSelection(i);
            }
        }

        etName.setText(plan.getTitle());
        etGoal.setText(String.valueOf(plan.getGoal()));
        startTimeInMillis = plan.getStartTime();
        etStartDate.setText(getDateString(startTimeInMillis, DATE_FORMAT));
        endTimeInMillis = plan.getEndTime();
        etEndDate.setText(getDateString(endTimeInMillis, DATE_FORMAT));
    }

    public void onEtStartDateClick(View v) {
        DatePickerFragment fragment = DatePickerFragment.newInstance(startTimeInMillis, REQUEST_START_DATE);
        fragment.show(getSupportFragmentManager(), "startDatePicker");
    }

    public void onEtEndDateClick(View v) {
        DatePickerFragment fragment = DatePickerFragment.newInstance(endTimeInMillis, REQUEST_END_DATE);
        fragment.show(getSupportFragmentManager(), "endDatePicker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_plan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miSave:
                savePlan();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void savePlan() {
        if (!isValidPlanData()) {
            return;
        }

        int id = isEdit ? plan.getId() : 0;
        String title = etName.getText().toString();
        Category category = categories.get(spCategory.getSelectedItemPosition());
        int goal = Integer.valueOf(etGoal.getText().toString());

        planService.addOrUpdatePlan(id, title, category, goal, startTimeInMillis, endTimeInMillis);

        String message = getString(isEdit ? R.string.message_plan_updated : R.string.message_plan_added);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        finish();
    }

    private boolean isValidPlanData() {
        boolean isValid = true;

        if (TextUtils.isEmpty(etName.getText())) {
            etName.setError(getString(R.string.error_missing_plan_name));
            isValid = false;
        }

        if (TextUtils.isEmpty(etGoal.getText()) || Integer.valueOf(etGoal.getText().toString()) <= 0) {
            etGoal.setError(getString(R.string.error_missing_plan_goal));
            isValid = false;
        }

        if (TextUtils.isEmpty(etStartDate.getText())) {
            etStartDate.setError(getString(R.string.error_missing_plan_start_date));
            isValid = false;
        }

        if (TextUtils.isEmpty(etEndDate.getText())) {
            etEndDate.setError(getString(R.string.error_missing_plan_end_date));
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void onDatePickerDateSet(int year, int month, int day, int requestCode) {
        switch (requestCode) {
            case REQUEST_START_DATE:
                setStartDate(year, month, day);
                break;
            case REQUEST_END_DATE:
                setEndDate(year, month, day);
                break;
        }
    }

    private void setEndDate(int year, int month, int day) {
        endTimeInMillis = ymdToTimeInMillis(year, month, day);
        etEndDate.setText(getDateString(endTimeInMillis, DATE_FORMAT));
    }

    private void setStartDate(int year, int month, int day) {
        startTimeInMillis = ymdToTimeInMillis(year, month, day);
        etStartDate.setText(getDateString(startTimeInMillis, DATE_FORMAT));
    }

    private long ymdToTimeInMillis(int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return c.getTimeInMillis();
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
