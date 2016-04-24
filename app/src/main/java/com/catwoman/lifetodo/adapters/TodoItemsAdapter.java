package com.catwoman.lifetodo.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.activities.TodoItemActivity;
import com.catwoman.lifetodo.dbs.TodoItemDb;
import com.catwoman.lifetodo.models.TodoItem;
import com.catwoman.lifetodo.utils.MapsUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by annt on 4/24/16.
 */
public class TodoItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private RealmResults<TodoItem> todoItems;
    private static final int ITEM_VIEW_TYPE_DEFAULT = 1;
    private static final int ITEM_VIEW_TYPE_NO_THUMB = 2;

    public TodoItemsAdapter(RealmResults<TodoItem> todoItems) {
        this.todoItems = todoItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_NO_THUMB) {
            return onCreateViewHolderNothumb(parent, viewType);
        }
        return onCreateViewHolderDefault(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_VIEW_TYPE_NO_THUMB) {
            onBindViewHolderNoThumb((ViewHolderNoThumb) holder, position);
            return;
        }
        onBindViewHolderDefault((ViewHolderDefault) holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        TodoItem todoItem = todoItems.get(position);
        if (!todoItem.getCategory().getName().equals("place") && todoItem.getItemThumbUrl().equals("")) {
            return ITEM_VIEW_TYPE_NO_THUMB;
        }
        return ITEM_VIEW_TYPE_DEFAULT;
    }

    @Override
    public int getItemCount() {
        if (null == todoItems) {
            return 0;
        }
        return todoItems.size();
    }

    private ViewHolderDefault onCreateViewHolderDefault(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false);
        return new ViewHolderDefault(view);
    }

    private ViewHolderNoThumb onCreateViewHolderNothumb(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_todo_no_thumb, parent, false);
        return new ViewHolderNoThumb(view);
    }

    private void onBindViewHolderDefault(final ViewHolderDefault holder, int position) {
        TodoItem todoItem = todoItems.get(position);

        int color = todoItem.getItemStatus().equals("InProgress") ? R.color.colorBgItemTodo :
                R.color.colorBgItemTodoDone;

        holder.rlItem.setBackgroundColor(context.getResources().getColor(color));
        holder.tvName.setText(todoItem.getItemName());

        if (!todoItem.getItemThumbUrl().equals("")) {
            Glide.with(context).load(todoItem.getItemThumbUrl()).into(holder.ivThumb);
        } else if (todoItem.getCategory().getName().equals("place")) {
            String center = !todoItem.getAddress().equals("") ? todoItem.getAddress() : todoItem.getItemName();
            String mapUrl = MapsUtil.getStaticMapUrl(center, context.getString(R.string.google_api_key));
            Glide.with(context).load(mapUrl).placeholder(R.drawable.ic_travel_gray_out).into(holder.ivThumb);
        }
    }

    private void onBindViewHolderNoThumb(final ViewHolderNoThumb holder, int position) {
        TodoItem todoItem = todoItems.get(position);

        int color = todoItem.getItemStatus().equals("InProgress") ? R.color.colorBgItemTodo :
                R.color.colorBgItemTodoDone;

        holder.rlItem.setBackgroundColor(context.getResources().getColor(color));
        holder.tvName.setText(todoItem.getItemName());
    }

    private void onItemClick(int position) {
        TodoItem todoItem = todoItems.get(position);
        Intent intent = new Intent(context, TodoItemActivity.class);
        intent.putExtra("id", todoItem.getId());
        context.startActivity(intent);
    }

    private void onItemLongClick(int position) {
        TodoItem todoItem = todoItems.get(position);
        String status;
        String message;
        if (todoItem.getItemStatus().equals("InProgress")) {
            status = "Done";
            message = context.getString(R.string.message_marked_completed);
        } else {
            status = "InProgress";
            message = context.getString(R.string.message_marked_not_completed);
        }
        TodoItemDb.getInstance().updateItem(todoItem.getId(), status);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    class ViewHolderDefault extends RecyclerView.ViewHolder {
        @Bind(R.id.rlItem)
        RelativeLayout rlItem;
        @Bind(R.id.ivThumb)
        ImageView ivThumb;
        @Bind(R.id.tvName)
        TextView tvName;

        public ViewHolderDefault(View itemView) {
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

    class ViewHolderNoThumb extends RecyclerView.ViewHolder {
        @Bind(R.id.rlItem)
        RelativeLayout rlItem;
        @Bind(R.id.tvName)
        TextView tvName;

        public ViewHolderNoThumb(View itemView) {
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
