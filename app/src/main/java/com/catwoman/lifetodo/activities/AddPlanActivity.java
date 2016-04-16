package com.catwoman.lifetodo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.fragments.DatePickerFragment;
import com.catwoman.lifetodo.models.Category;
import com.catwoman.lifetodo.models.Plan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddPlanActivity extends AppCompatActivity
        implements DatePickerFragment.DatePickerFragmentListener {
    private long dueTimeInMillis = 0;

    @Bind(R.id.spCategory)
    Spinner spCategory;
    @Bind(R.id.etName)
    EditText etName;
    @Bind(R.id.etGoal)
    EditText etGoal;
    @Bind(R.id.etDueDate)
    EditText etDueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        populateView();
    }

    private void populateView() {
        // spinner
        ArrayAdapter<CharSequence> categoriesAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(categoriesAdapter);
    }

    public void onEtDueDateClick(View v) {
        showDatePicker();
    }

    public void showDatePicker() {
        DatePickerFragment fragment = DatePickerFragment.newInstance(dueTimeInMillis);
        fragment.show(getSupportFragmentManager(), "datePicker");
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

        Plan plan = new Plan();

        // @TODO Load categories from database
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "Books", R.drawable.ic_book, R.color.colorDeepPurple));
        categories.add(new Category(2, "Places", R.drawable.ic_travel, R.color.colorBlue));
        categories.add(new Category(3, "Foods", R.drawable.ic_eat, R.color.colorRed));
        categories.add(new Category(4, "Movies", R.drawable.ic_movie, R.color.colorTeal));
        categories.add(new Category(5, "Friends", R.drawable.ic_people, R.color.colorAmber));
        categories.add(new Category(6, "Moments", R.drawable.ic_calendar, R.color.colorPink));

        Category category = categories.get(spCategory.getSelectedItemPosition());
        plan.setCategory(category);
        plan.setTitle(etName.getText().toString());
        plan.setGoal(Integer.valueOf(etGoal.getText().toString()));
        plan.setDueTime(dueTimeInMillis);

        Intent intent = new Intent();
        intent.putExtra("plan", plan);
        setResult(RESULT_OK, intent);
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

        if (TextUtils.isEmpty(etDueDate.getText())) {
            etDueDate.setError(getString(R.string.error_missing_plan_due_date));
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void onDatePickerDateSet(int year, int month, int day) {
        setDueDate(year, month, day);
    }

    private void setDueDate(int year, int monthOfYear, int dayOfMonth) {
        dueTimeInMillis = ymdToTimeInMillis(year, monthOfYear, dayOfMonth);
        etDueDate.setText(getDateString(dueTimeInMillis, "MM-dd-yyyy"));
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
