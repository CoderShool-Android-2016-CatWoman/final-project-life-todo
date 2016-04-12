package com.catwoman.lifetodo.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.activities.TodoItemsActivity;
import com.catwoman.lifetodo.models.Category;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by annt on 4/9/16.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    private ArrayList<Category> categories;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.rlCategory) RelativeLayout rlCategory;
        @Bind(R.id.ivThumb) ImageView ivThumb;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public CategoriesAdapter(ArrayList<Category> categories) {
        this.categories = categories;
    }

    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoriesAdapter.ViewHolder holder, int position) {
        final Category category = categories.get(position);

        holder.rlCategory.setBackgroundResource(category.getColorRes());
        holder.ivThumb.setImageResource(category.getThumbRes());

        holder.ivThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TodoItemsActivity.class);
                intent.putExtra("category", category.getTitle());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == categories) {
            return 0;
        }
        return categories.size();
    }
}
