package com.catwoman.lifetodo.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.dbs.TodoItemDb;
import com.catwoman.lifetodo.models.TodoItem;
import com.catwoman.lifetodo.utils.MapsUtil;
import com.github.florent37.glidepalette.GlidePalette;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;

public class TodoItemActivity extends AppCompatActivity {
    @Bind(R.id.ivThumb)
    ImageView ivThumb;
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.tvStatus)
    TextView tvStatus;
    @Bind(R.id.tvStatusAction)
    TextView tvStatusAction;
    @Bind(R.id.rlAddress)
    RelativeLayout rlAddress;
    @Bind(R.id.tvAddress)
    TextView tvAddress;
    @Bind(R.id.tvDescription)
    TextView tvDescription;
    private TodoItem todoItem;
    private RealmChangeListener changeListener;

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
        setListeners();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        todoItem.removeChangeListener(changeListener);
    }

    public void onStatusActionClick(View v) {
        String status = todoItem.getItemStatus().equals("Done") ? "InProgress" : "Done";
        TodoItemDb.getInstance().updateItem(todoItem.getId(), status);
    }

    public void onAddressActionClick(View v) {
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f?z=10", todoItem.getLatitude(),
                todoItem.getLongitude());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
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
        String thumbUrl = "";
        if (!todoItem.getItemThumbUrl().equals("")) {
            thumbUrl = todoItem.getItemThumbUrl();
        } else if (!todoItem.getAddress().equals("")) {
            thumbUrl = MapsUtil.getStaticMapUrl(todoItem.getAddress(), 10, 400, 225, this.getString(R.string.google_api_key));
        }

        if (!thumbUrl.equals("")) {
            Glide.with(this).load(thumbUrl)
                    .listener(GlidePalette.with(thumbUrl)
                            .use(GlidePalette.Profile.VIBRANT_DARK)
                            .intoBackground(tvName)
                            .intoTextColor(tvName))
                    .centerCrop()
                    .dontTransform()
                    .into(ivThumb);
        } else {
            ivThumb.setImageResource(getResources().getIdentifier(
                    "ic_" + todoItem.getCategory().getDrawable() + "_gray_out", "drawable", getPackageName()));
            ivThumb.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        tvName.setText(todoItem.getItemName());

        String status = getString(R.string.status_not_completed);
        boolean statusActive = false;
        if (todoItem.getItemStatus().equals("Done")) {
            status = getString(R.string.status_completed);
            statusActive = true;
        }
        tvStatus.setText(status);
        tvStatus.setActivated(statusActive);

        if (!todoItem.getAddress().equals("")) {
            tvAddress.setText(todoItem.getAddress());
            rlAddress.setVisibility(View.VISIBLE);
        }

        if (!todoItem.getItemDescription().equals("")) {
            tvDescription.setText(todoItem.getItemDescription());
            tvDescription.setVisibility(View.VISIBLE);
        }
    }

    private void setListeners() {
        changeListener = new RealmChangeListener() {
            @Override
            public void onChange() {
                populateViews();
            }
        };
        todoItem.addChangeListener(changeListener);
    }
}
