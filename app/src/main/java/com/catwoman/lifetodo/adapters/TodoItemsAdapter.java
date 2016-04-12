package com.catwoman.lifetodo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.interfaces.DeleteItemListener;
import com.catwoman.lifetodo.interfaces.EndlessScrollListener;
import com.catwoman.lifetodo.models.TodoItem;

import java.util.ArrayList;

/**
 * Created by annt on 4/9/16.
 */
public class TodoItemsAdapter extends RecyclerView.Adapter<TodoItemsAdapter.ViewHolder> {

    // Store a member variable for the articles
    private ArrayList<TodoItem> mItemsData;
    private String mitemStyle;

    private EndlessScrollListener endlessScrollListener;
    private DeleteItemListener deleteItemListener;

    public void setEndlessScrollListener(EndlessScrollListener endlessScrollListener) {
        this.endlessScrollListener = endlessScrollListener;
    }

    public void setDeleteItemListener(DeleteItemListener deleteItemListener){
        this.deleteItemListener = deleteItemListener;
    }

    // Pass in the contact array into the constructor
    public TodoItemsAdapter(ArrayList<TodoItem> itemsData, String itemStyle) {
        mItemsData = itemsData;
        mitemStyle = itemStyle;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        public TextView tvName;
        public ImageView ivThumbUrl;
        public CardView cvItem;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvItemName);
            ivThumbUrl = (ImageView) itemView.findViewById(R.id.ivItemThumb);
            cvItem = (CardView) itemView.findViewById(R.id.cardView);

        }
    }


    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public TodoItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View itemView = inflater.inflate(R.layout.todo_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(final TodoItemsAdapter.ViewHolder viewHolder, final int position) {
        // Get the data model based on position
        final TodoItem item = mItemsData.get(position);
        viewHolder.tvName.setText(item.getItemName());
        viewHolder.ivThumbUrl.setImageResource(R.drawable.iconbook);


        if(item.getItemStatus() == "Done"){
            viewHolder.cvItem.setCardBackgroundColor(Color.parseColor("#A8F28F"));

        }

        viewHolder.ivThumbUrl.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                 viewHolder.cvItem.setCardBackgroundColor(Color.parseColor("#A8F28F"));
                item.setItemStatus("Done");
            }
        });

        viewHolder.ivThumbUrl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteItemListener.deleteItem(position);
                return false;
            }
        });
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        //return mArticles.size();
        if (mItemsData == null){
            return 0;
        }
        return mItemsData.size();
    }


}
