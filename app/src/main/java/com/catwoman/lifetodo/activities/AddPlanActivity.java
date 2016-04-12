package com.catwoman.lifetodo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.fragments.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddPlanActivity extends AppCompatActivity
        implements DatePickerFragment.DatePickerFragmentListener {
    private long dueTimeInMillis = 0;

    @Bind(R.id.spCategory) Spinner spCategory;
    @Bind(R.id.etName) EditText etName;
    @Bind(R.id.etGoal) EditText etGoal;
    @Bind(R.id.etDueDate) EditText etDueDate;

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

    private String getDateString(long timeStamp, String format){
        if (timeStamp > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = (new Date(timeStamp));
            return sdf.format(date);
        }

        return "";
    }
}
