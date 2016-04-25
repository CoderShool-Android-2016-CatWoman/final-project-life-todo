package com.catwoman.lifetodo.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        TodoItem todoItem = todoItems.get(position);

        int color = todoItem.getItemStatus().equals("InProgress") ? R.color.colorBgItemTodo :
                R.color.colorBgItemTodoDone;

        holder.rlItem.setBackgroundColor(context.getResources().getColor(color));
        holder.tvName.setText(todoItem.getItemName());

        String thumbUrl = "";
        if (!todoItem.getItemThumbUrl().equals("")) {
            thumbUrl = todoItem.getItemThumbUrl();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(thumbUrl, options);

        } else if (!todoItem.getAddress().equals("")) {
            thumbUrl = MapsUtil.getStaticMapUrl(todoItem.getAddress(), 10, 200, 200, context.getString(R.string.google_api_key));
        }
        if (!thumbUrl.equals("")) {
            Glide.with(context).load(thumbUrl).into(holder.ivThumb);
        } else {
            holder.ivThumb.setImageResource(0);
        }
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

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.rlItem)
        RelativeLayout rlItem;
        @Bind(R.id.ivThumb)
        ImageView ivThumb;
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
