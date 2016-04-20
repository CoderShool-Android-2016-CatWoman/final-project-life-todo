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
import com.catwoman.lifetodo.interfaces.AddItemListener;
import com.catwoman.lifetodo.interfaces.DeleteItemListener;
import com.catwoman.lifetodo.interfaces.EditItemListener;
import com.catwoman.lifetodo.interfaces.EndlessScrollListener;
import com.catwoman.lifetodo.models.Category;
import com.catwoman.lifetodo.models.TodoItem;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class TodoItemsActivity extends AppCompatActivity {
    private static final String TAG = "TodoItemsActivity";
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private RealmResults<TodoItem> itemsData;
    private TodoItemsAdapter adapterItem;
    private AddTextFragment editNameDialogFragment;
    private Realm realm;
    private Category category;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.rvItems) RecyclerView rvItems;
    @Bind(R.id.vOverlay) View vOverlay;
    @Bind(R.id.famAdd) FloatingActionsMenu famAdd;
    @Bind(R.id.fabAddText) FloatingActionButton fabAddText;
    @Bind(R.id.fabAddPhoto) FloatingActionButton fabAddPhoto;
    @Bind(R.id.fabAddCamera) FloatingActionButton fabAddCamera;
    @Bind(R.id.fabAddLocation) FloatingActionButton fabAddLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_items);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        realm = Realm.getDefaultInstance();

        int categoryId = getIntent().getIntExtra("categoryId", 0);
        category = realm.where(Category.class).equalTo("id", categoryId).findFirst();
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

        itemsData = realm
                .where(TodoItem.class)
                .equalTo("category.id", category.getId())
                .findAllSorted("id", Sort.ASCENDING);

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

        adapterItem.setDeleteItemListener(new DeleteItemListener() {
            @Override
            public void deleteItem(int position, String itemName) {
                TodoItem itemDelete = realm.where(TodoItem.class).equalTo("itemName", itemName).findFirst();

                realm.beginTransaction();
                itemDelete.removeFromRealm();
                realm.commitTransaction();
            }
        });

        adapterItem.setEditItemListener(new EditItemListener() {
            @Override
            public void EditItem(int position,String itemName, String itemStatus) {
                //TodoItem itemEdit = new TodoItem();
                TodoItem itemCurr = realm.where(TodoItem.class).equalTo("itemName", itemName).findFirst();

                realm.beginTransaction();
                //itemCurr.setId(itemCurr.getId());
                itemCurr.setItemStatus(itemStatus);
                realm.copyToRealmOrUpdate(itemCurr);
                realm.commitTransaction();
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

        editNameDialogFragment.setAddItemListener(new AddItemListener() {

            @Override
            public void AddItem(String itemName) {
                //Log.d("Debug", "AddItem: " + itemName);
                TodoItem item = new TodoItem();
                int id;

                try {
                    id = realm.where(TodoItem.class).max("id").intValue() + 1;
                } catch (Exception e) {
                    id = 1;
                }
                item.setId(id);
                item.setItemName(itemName);
                item.setItemStatus("InProgress");
                item.setItemThumbUrl("Default");

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(item);
                realm.commitTransaction();

                itemsData.add(item);
                adapterItem.notifyItemInserted(itemsData.size() - 1);
            }
        });

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
        int id;
        try {
            id = realm.where(TodoItem.class).max("id").intValue() + 1;
        } catch (Exception e) {
            id = 1;
        }

        realm.beginTransaction();
        TodoItem todoItem = realm.createObject(TodoItem.class);
        todoItem.setId(id);
        todoItem.setCategory(category);
        todoItem.setItemName(String.valueOf(place.getName()));
        todoItem.setItemDescription(String.valueOf(place.getAddress()));
        todoItem.setLocation(String.valueOf(place.getName()));
        todoItem.setAddress(String.valueOf(place.getAddress()));
        todoItem.setLatitude(place.getLatLng().latitude);
        todoItem.setLongitude(place.getLatLng().longitude);
        todoItem.setItemStatus("InProgress");
        realm.commitTransaction();
    }
}
