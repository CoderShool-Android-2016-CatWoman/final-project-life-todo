package com.catwoman.lifetodo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.adapters.TodoItemsAdapter;
import com.catwoman.lifetodo.dbs.CategoryDb;
import com.catwoman.lifetodo.dbs.TodoItemDb;
import com.catwoman.lifetodo.fragments.AddTextFragment;
import com.catwoman.lifetodo.models.Category;
import com.catwoman.lifetodo.models.TodoItem;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class TodoItemsActivity extends AppCompatActivity {
    private static final String TAG = "TodoItemsActivity";
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rvItems)
    RecyclerView rvItems;
    @Bind(R.id.tvMessage)
    TextView tvMessage;
    @Bind(R.id.vOverlay)
    View vOverlay;
    @Bind(R.id.famAdd)
    FloatingActionsMenu famAdd;
    @Bind(R.id.fabAddText)
    FloatingActionButton fabAddText;
    @Bind(R.id.fabAddPhoto)
    FloatingActionButton fabAddPhoto;
    @Bind(R.id.fabAddCamera)
    FloatingActionButton fabAddCamera;
    @Bind(R.id.fabAddLocation)
    FloatingActionButton fabAddLocation;
    private RealmResults<TodoItem> todoItems;
    private Category category;
    private CategoryDb categoryDb;
    private TodoItemDb todoItemDb;
    private TodoItemsAdapter todoItemsAdapter;
    private StaggeredGridLayoutManager layoutManager;
    private RealmChangeListener changeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_items);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categoryDb = CategoryDb.getInstance();
        todoItemDb = TodoItemDb.getInstance();

        int categoryId = getIntent().getIntExtra("categoryId", 0);
        category = categoryDb.getCategory(categoryId);
        getSupportActionBar().setTitle(category.getTitle());

        if (category.getName().equals("place")) {
            fabAddLocation.setVisibility(View.VISIBLE);
        }

        populateViews();
        setListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.toString());
                addPlace(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        todoItems.removeChangeListener(changeListener);
    }

    private void populateViews() {
        todoItems = todoItemDb.getItems(category);
        todoItemsAdapter = new TodoItemsAdapter(todoItems);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvItems.setAdapter(todoItemsAdapter);
        rvItems.setLayoutManager(layoutManager);

        String message = getString(R.string.message_no_item, category.getTitle().toLowerCase());
        tvMessage.setText(message);
        toggleMessage();
    }

    private void setListeners() {
        changeListener = new RealmChangeListener() {
            @Override
            public void onChange() {
                todoItemsAdapter.notifyDataSetChanged();
                toggleMessage();
            }
        };
        todoItems.addChangeListener(changeListener);

        famAdd.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                vOverlay.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                vOverlay.setVisibility(View.GONE);
            }
        });

        fabAddCamera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TodoItemsActivity.this, AddCameraActivity.class);
                intent.putExtra("categoryId", category.getId());
                startActivity(intent);
            }
        });

        fabAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TodoItemsActivity.this, AddPhotoActivity.class);
                intent.putExtra("categoryId", category.getId());
                startActivity(intent);
            }
        });
    }

    private void toggleMessage() {
        tvMessage.setVisibility(0 == todoItems.size() ? View.VISIBLE : View.GONE);
    }

    public void collapseFAM(View v) {
        famAdd.collapse();
    }

    public void showAddText(View v) {
        famAdd.collapseImmediately();

        AddTextFragment fragment = AddTextFragment.newInstance(category, null);
        fragment.show(getSupportFragmentManager(), "fragment_add_text");
    }

    public void showAddLocation(View v) {
        famAdd.collapseImmediately();

        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    private void addPlace(Place place) {
        todoItemDb.addItem(String.valueOf(place.getName()), String.valueOf(place.getAddress()),
                place.getLatLng().latitude, place.getLatLng().longitude, category);
    }
}
