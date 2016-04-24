package com.catwoman.lifetodo.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.dbs.TodoItemDb;
import com.catwoman.lifetodo.models.TodoItem;
import com.catwoman.lifetodo.utils.MapsUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TodoItemActivity extends AppCompatActivity {
    @Bind(R.id.ivThumb)
    ImageView ivThumb;
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.tvStatus)
    TextView tvStatus;
    @Bind(R.id.tvStatusAction)
    TextView tvStatusAction;
    private TodoItem todoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        int id = getIntent().getIntExtra("id", 0);
        todoItem = TodoItemDb.getInstance().getItem(id);
        if (null == todoItem) {
            Toast.makeText(this, "Item not found.", Toast.LENGTH_LONG).show();
            finish();
        }

        getSupportActionBar().setTitle(todoItem.getItemName());

        populateViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_todo_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miEditInfo:
                editItemInfo();
                return true;
            case R.id.miEditLocation:
                editItemLocation();
                return true;
            case R.id.miDelete:
                confirmDeleteItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void confirmDeleteItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirm_delete_item_title))
                .setMessage(getString(R.string.confirm_delete_item_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem();
                    }
                })
                .setNegativeButton(android.R.string.no, null);
        builder.show();
    }

    private void deleteItem() {
        TodoItemDb.getInstance().removeItem(todoItem.getId());

        Toast.makeText(this, getString(R.string.message_item_has_been_deleted), Toast.LENGTH_LONG).show();
        finish();
    }

    private void editItemLocation() {

    }

    private void editItemInfo() {

    }

    private void populateViews() {
        if (!todoItem.getItemThumbUrl().equals("")) {
            Glide.with(this).load(todoItem.getItemThumbUrl()).centerCrop().dontTransform().into(ivThumb);
        } else if (todoItem.getCategory().getName().equals("place")) {
            String center = !todoItem.getAddress().equals("") ? todoItem.getAddress() : todoItem.getItemName();
            String mapUrl = MapsUtil.getStaticMapUrl(center, this.getString(R.string.google_api_key));
            Glide.with(this).load(mapUrl).centerCrop().dontTransform().into(ivThumb);
        } else {
            ivThumb.setImageResource(getResources().getIdentifier(
                    "ic_" + todoItem.getCategory().getDrawable() + "_gray_out", "drawable", getPackageName()));
            ivThumb.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        tvName.setText(todoItem.getItemName());

        String status = getString(R.string.status_not_completed);
        String statusAction = getString(R.string.mark_completed);
        if (todoItem.getItemStatus().equals("Done")) {
            status = getString(R.string.status_completed);
            statusAction = getString(R.string.mark_not_completed);
        }
        tvStatus.setText(status);
        tvStatusAction.setText(statusAction);
    }
}
