package com.catwoman.lifetodo.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.activities.TodoItemsActivity;
import com.catwoman.lifetodo.models.Category;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by annt on 4/9/16.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    private RealmResults<Category> categories;
    private Context context;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.rlCategory)
        RelativeLayout rlCategory;
        @Bind(R.id.ivThumb)
        ImageView ivThumb;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Category category = categories.get(position);
            Intent intent = new Intent(context, TodoItemsActivity.class);
            intent.putExtra("category", category.getTitle());
            context.startActivity(intent);
        }
    }

    public CategoriesAdapter(RealmResults<Category> categories) {
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

        holder.ivThumb.setImageResource(context.getResources().getIdentifier("ic_" + category.getDrawable() + "_color_out",
                "drawable", context.getPackageName()));
    }

    @Override
    public int getItemCount() {
        if (null == categories) {
            return 0;
        }
        return categories.size();
    }
}
