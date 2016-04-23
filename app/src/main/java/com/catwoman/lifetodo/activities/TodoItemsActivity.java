package com.catwoman.lifetodo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.adapters.TodoItemsAdapter;
import com.catwoman.lifetodo.fragments.AddTextFragment;
import com.catwoman.lifetodo.interfaces.EndlessScrollListener;
import com.catwoman.lifetodo.models.Category;
import com.catwoman.lifetodo.models.TodoItem;
import com.catwoman.lifetodo.services.CategoryService;
import com.catwoman.lifetodo.services.TodoItemService;
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
    private RealmResults<TodoItem> itemsData;
    private TodoItemsAdapter adapterItem;
    private AddTextFragment editNameDialogFragment;
    private Category category;
    private CategoryService categoryService;
    private TodoItemService todoItemService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_items);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categoryService = CategoryService.getInstance();
        todoItemService = TodoItemService.getInstance();

        int categoryId = getIntent().getIntExtra("categoryId", 0);
        category = categoryService.getCategory(categoryId);
        getSupportActionBar().setTitle(category.getTitle());

        if (category.getName().equals("place")) {
            fabAddLocation.setVisibility(View.VISIBLE);
        }

        //Item list view populate
        String itemType = "book";
        itemListViewPopulate(itemType);

        //Process Floating Action Menu
        floatingActionListener();

    }

    private void itemListViewPopulate(String itemType) {

        itemsData = todoItemService.getItems(category);

        adapterItem = new TodoItemsAdapter(itemsData, itemType);

        // Attach the adapter to the recyclerview to populate items
        rvItems.setAdapter(adapterItem);

        adapterItem.notifyDataSetChanged();

        itemsData.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange() {
                adapterItem.notifyDataSetChanged();
            }
        });

        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        // Attach the layout manager to the recycler view
        rvItems.setLayoutManager(gridLayoutManager);

        rvItems.setHasFixedSize(true);

        adapterItem.setEndlessScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int position) {
                return true;
            }
        });
    }

    private void floatingActionListener() {
        fabAddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TodoItem newBook = new TodoItem("love", "hi", "Progress", "hi");
                //itemsData.add(itemsData.size(), newBook);
                //adapterItem.notifyItemInserted(itemsData.size());
                showEditDialog();
            }
        });

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
    }

    public void collapseFAM(View v) {
        famAdd.collapse();
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        editNameDialogFragment = AddTextFragment.newInstance("");
        editNameDialogFragment.show(fm, "fragment_edit_name");
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

    private void addPlace(Place place) {
        todoItemService.addOrUpdateItem(
                0,
                String.valueOf(place.getName()),
                "",
                "InProgress",
                String.valueOf(place.getAddress()),
                String.valueOf(place.getName()),
                String.valueOf(place.getAddress()),
                place.getLatLng().latitude,
                place.getLatLng().longitude,
                category
        );
    }
}
