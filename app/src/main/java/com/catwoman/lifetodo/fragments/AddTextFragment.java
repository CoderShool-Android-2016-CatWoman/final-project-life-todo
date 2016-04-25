package com.catwoman.lifetodo.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.dbs.TodoItemDb;
import com.catwoman.lifetodo.models.Category;
import com.catwoman.lifetodo.models.TodoItem;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by annt on 4/25/16.
 */
public class AddTextFragment extends DialogFragment {
    @Bind(R.id.etName)
    EditText etName;
    @Bind(R.id.etDescription)
    EditText etDescription;

    public AddTextFragment() {}

    public static AddTextFragment newInstance(Category category, TodoItem todoItem) {
        Bundle args = new Bundle();
        args.putParcelable("category", category);
        args.putParcelable("todoItem", todoItem);

        AddTextFragment fragment = new AddTextFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_text, null);
        ButterKnife.bind(this, view);

        TodoItem todoItem = getArguments().getParcelable("todoItem");
        builder.setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", null);

        AlertDialog dialog = builder.create();

        if (null != todoItem) {
            etName.setText(todoItem.getItemName());
            etDescription.setText(todoItem.getItemDescription());
        }

        etName.requestFocus();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });

        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void saveItem() {
        if (TextUtils.isEmpty(etName.getText())) {
            etName.setError("Please input item name.");
            return;
        }

        TodoItem todoItem = getArguments().getParcelable("todoItem");
        Category category = getArguments().getParcelable("category");
        String name = etName.getText().toString();
        String description = etDescription.getText().toString();

        if (null != todoItem) {
            TodoItemDb.getInstance().updateItem(todoItem.getId(), name, description);
        } else {
            TodoItemDb.getInstance().addItem(name, description, category);
        }

        dismiss();
    }
}
