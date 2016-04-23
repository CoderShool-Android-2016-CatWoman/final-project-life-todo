package com.catwoman.lifetodo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.models.TodoItem;
import com.catwoman.lifetodo.services.TodoItemService;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by annt on 4/24/16.
 */
public class TodoItemsAdapter extends RecyclerView.Adapter<TodoItemsAdapter.ViewHolder> {
    private Context context;
    private RealmResults<TodoItem> todoItems;

    public TodoItemsAdapter(RealmResults<TodoItem> todoItems) {
        this.todoItems = todoItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        TodoItem todoItem = todoItems.get(position);

        int color = todoItem.getItemStatus().equals("InProgress") ? R.color.colorBgItemTodo :
                R.color.colorBgItemTodoDone;

        holder.rlItem.setBackgroundColor(context.getResources().getColor(color));
        holder.tvName.setText(todoItem.getItemName());
    }

    @Override
    public int getItemCount() {
        if (null == todoItems) {
            return 0;
        }
        return todoItems.size();
    }

    private void onItemClick(int position) {
        TodoItem todoItem = todoItems.get(position);
        String status = todoItem.getItemStatus().equals("InProgress") ? "Done" : "InProgress";
        TodoItemService.getInstance().updateItem(todoItem.getId(), status);
    }

    private void onItemLongClick(int position) {
        TodoItem todoItem = todoItems.get(position);
        TodoItemService.getInstance().removeItem(todoItem.getId());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.rlItem)
        RelativeLayout rlItem;
        @Bind(R.id.tvName)
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(getLayoutPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClick(getLayoutPosition());
                    return true;
                }
            });
        }
    }
}
