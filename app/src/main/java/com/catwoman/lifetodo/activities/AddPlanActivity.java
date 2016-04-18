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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class AddPlanActivity extends AppCompatActivity
        implements DatePickerFragment.DatePickerFragmentListener {
    private long dueTimeInMillis = 0;
    private Realm realm;
    private RealmResults<Category> categories;
    private boolean isEdit = false;
    private Plan plan;

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

        realm = Realm.getDefaultInstance();
        categories = realm.where(Category.class).findAllSorted("id", Sort.ASCENDING);

        int id = getIntent().getIntExtra("id", 0);
        if (0 != id) {
            plan = realm.where(Plan.class).equalTo("id", id).findFirst();
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
        dueTimeInMillis = plan.getDueTime();
        etDueDate.setText(getDateString(dueTimeInMillis, "MM-dd-yyyy"));
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

        realm.beginTransaction();

        if (!isEdit) {
            int id;
            try {
                id = realm.where(Plan.class).max("id").intValue() + 1;
            } catch (Exception e) {
                id = 1;
            }
            plan = realm.createObject(Plan.class);
            plan.setId(id);
        }

        Category category = categories.get(spCategory.getSelectedItemPosition());
        plan.setCategory(category);
        plan.setTitle(etName.getText().toString());
        plan.setGoal(Integer.valueOf(etGoal.getText().toString()));
        plan.setDueTime(dueTimeInMillis);

        realm.commitTransaction();

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
