package com.catwoman.lifetodo.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by annt on 4/12/16.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public DatePickerFragment() {
        // Required empty public constructor
    }

    public static DatePickerFragment newInstance(long timeInMillis, int requestCode) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putLong("timeInMillis", timeInMillis);
        args.putInt("requestCode", requestCode);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();

        Bundle args = this.getArguments();
        Long timeInMillis = args.getLong("timeInMillis", 0);
        if (timeInMillis > 0) {
            c.setTimeInMillis(timeInMillis);
        }

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        int requestCode = this.getArguments().getInt("requestCode", 0);
        DatePickerFragmentListener listener = (DatePickerFragmentListener) getActivity();
        listener.onDatePickerDateSet(year, month, day, requestCode);
    }

    public interface DatePickerFragmentListener {
        void onDatePickerDateSet(int year, int month, int day, int requestCode);
    }
}
