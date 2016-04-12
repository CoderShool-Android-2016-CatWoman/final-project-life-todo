package com.catwoman.lifetodo.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.adapters.TodoItemsAdapter;
import com.catwoman.lifetodo.interfaces.DeleteItemListener;
import com.catwoman.lifetodo.interfaces.EndlessScrollListener;
import com.catwoman.lifetodo.models.TodoItem;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

public class TodoItemsActivity extends AppCompatActivity {

    //private TodoItemsAdapter adapter;
    private ArrayList<TodoItem> itemsData;
    private CoordinatorLayout clLayout;
    private TodoItemsAdapter adapterItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //Item list view populate
        String itemType = "book";
        itemListViewPopulate(itemType);

        //Process Floating Action Menu
        floatingActionListener();

    }

    private void itemListViewPopulate(String itemType) {

        RecyclerView rvItems = (RecyclerView) findViewById(R.id.rvItems);

        clLayout = (CoordinatorLayout) findViewById(R.id.itemstodo);

        itemsData = TodoItem.createItemsList(20);

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
            public void deleteItem(int position) {
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
                TodoItem newBook = new TodoItem("love", "hi", "Progress", "hi");
                itemsData.add(itemsData.size(), newBook);
                adapterItem.notifyItemInserted(itemsData.size());
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
                clLayout.setBackgroundColor(Color.parseColor("#805677fc"));

            }

            @Override
            public void onMenuCollapsed() {
                clLayout.setBackgroundColor(Color.parseColor("#e5e5e5"));
            }
        });
    }

}
