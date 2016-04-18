package com.catwoman.lifetodo.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.activities.PlanActivity;
import com.catwoman.lifetodo.models.Category;
import com.catwoman.lifetodo.models.Plan;
import com.makeramen.roundedimageview.RoundedImageView;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by annt on 4/9/16.
 */
public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.ViewHolder> {
    private RealmResults<Plan> plans;
    private Context context;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.ivThumb)
        RoundedImageView ivThumb;
        @Bind(R.id.tvTitle)
        TextView tvTitle;
        @Bind(R.id.tvGoal)
        TextView tvGoal;
        @Bind(R.id.tvRemainingTime)
        TextView tvRemainingTime;
        @Bind(R.id.pbProgress)
        ProgressBar pbProgress;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Plan plan = plans.get(position);
            Intent intent = new Intent(context, PlanActivity.class);
            intent.putExtra("id", plan.getId());
            context.startActivity(intent);
        }
    }

    public PlansAdapter(RealmResults<Plan> plans) {
        this.plans = plans;
    }

    @Override
    public PlansAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_plan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlansAdapter.ViewHolder holder, int position) {
        Plan plan = plans.get(position);
        Category category = plan.getCategory();

        holder.ivThumb.setImageResource(context.getResources().getIdentifier("ic_" + category.getDrawable() + "_color_out",
                "drawable", context.getPackageName()));
        holder.tvTitle.setText(plan.getTitle());
        holder.tvGoal.setText(String.valueOf(plan.getGoal()));
        // @TODO Calculate progress
        // dummy progress
        int progress = 1;
        if (progress == plan.getGoal()) {
            holder.tvRemainingTime.setText(context.getString(R.string.message_completed));
        } else {
            holder.tvRemainingTime.setText(getStringRemaining(plan.getDueTime()));
        }
        holder.pbProgress.setMax(plan.getGoal());
        holder.pbProgress.getProgressDrawable().setColorFilter(context.getResources().getColor(
                context.getResources().getIdentifier("color" + plan.getCategory().getColor(), "color",
                        context.getPackageName())), android.graphics.PorterDuff.Mode.SRC_IN);
        holder.pbProgress.setProgress(progress);
    }

    @Override
    public int getItemCount() {
        if (null == plans) {
            return 0;
        }
        return plans.size();
    }

    private String getStringRemaining(long time) {
        String remaining = "";
        PrettyTime pt = new PrettyTime();
        if (System.currentTimeMillis() <= time) {
            String duration = pt.formatDuration(new Date(time));
            remaining = context.getString(R.string._1_s_remaining, duration);
        } else {
            remaining = pt.format(new Date(time));
        }
        return remaining;
    }
}
