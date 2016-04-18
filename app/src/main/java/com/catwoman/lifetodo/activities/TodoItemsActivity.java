package com.catwoman.lifetodo.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
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
import com.catwoman.lifetodo.interfaces.EndlessScrollListener;
import com.catwoman.lifetodo.models.TodoItem;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class TodoItemsActivity extends AppCompatActivity {

    //private TodoItemsAdapter adapter;
    private ArrayList<TodoItem> itemsData;
    private CoordinatorLayout clLayout;
    private TodoItemsAdapter adapterItem;
    private AddTextFragment editNameDialogFragment;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Book");

        realm = Realm.getDefaultInstance();

        //Item list view populate
        String itemType = "book";
        itemListViewPopulate(itemType);

        //Process Floating Action Menu
        floatingActionListener();

    }

    private void itemListViewPopulate(String itemType) {

        RecyclerView rvItems = (RecyclerView) findViewById(R.id.rvItems);

        clLayout = (CoordinatorLayout) findViewById(R.id.itemstodo);

        itemsData = new ArrayList<TodoItem>();

        RealmResults<TodoItem> results = realm.where(TodoItem.class).findAllSorted("id", Sort.ASCENDING);
        for (int i = 0; i < results.size(); i++) {
            itemsData.add(results.get(i));
        }

        adapterItem = new TodoItemsAdapter(itemsData, itemType);

        // Attach the adapter to the recyclerview to populate items
        rvItems.setAdapter(adapterItem);

        adapterItem.notifyDataSetChanged();

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
                TodoItem itemDlete = realm.where(TodoItem.class).equalTo("itemName",itemName).findFirst();

                realm.beginTransaction();
                itemDlete.removeFromRealm();
                realm.commitTransaction();

                itemsData.remove(position);
                adapterItem.notifyItemRemoved(position);
            }
        });



    }

    private void floatingActionListener() {
        final FloatingActionButton addBookAction = (FloatingActionButton) findViewById(R.id.btnAddBook);
        addBookAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TodoItem newBook = new TodoItem("love", "hi", "Progress", "hi");
                //itemsData.add(itemsData.size(), newBook);
                //adapterItem.notifyItemInserted(itemsData.size());
                showEditDialog();
            }
        });

        final FloatingActionButton addPlaceAction = (FloatingActionButton) findViewById(R.id.btnAddPlace);
        addPlaceAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final FloatingActionsMenu expandUpMenuAction = (FloatingActionsMenu) findViewById(R.id.multiple_actions_up);
        expandUpMenuAction.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                //clLayout.setBackgroundColor(Color.parseColor("#805677fc"));
                View mShadowView = (View) findViewById(R.id.shadowView);
                mShadowView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onMenuCollapsed() {
                //clLayout.setBackgroundColor(Color.parseColor("#e5e5e5"));
                View mShadowView = (View) findViewById(R.id.shadowView);
                mShadowView.setVisibility(View.GONE);
            }
        });
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        editNameDialogFragment = AddTextFragment.newInstance("");
        editNameDialogFragment.show(fm, "fragment_edit_name");

        editNameDialogFragment.setAddItemListener(new AddItemListener() {

            @Override
            public void AddItem(String itemName) {
                Log.d("Debug", "AddItem: " + itemName);
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

}
