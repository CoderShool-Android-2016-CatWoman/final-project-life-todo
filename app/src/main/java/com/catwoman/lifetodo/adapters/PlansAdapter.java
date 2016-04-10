package com.catwoman.lifetodo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.catwoman.lifetodo.R;
import com.catwoman.lifetodo.models.Category;
import com.catwoman.lifetodo.models.Plan;
import com.makeramen.roundedimageview.RoundedImageView;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by annt on 4/9/16.
 */
public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.ViewHolder> {
    private ArrayList<Plan> plans;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivThumb) RoundedImageView ivThumb;
        @Bind(R.id.tvTitle) TextView tvTitle;
        @Bind(R.id.tvGoal) TextView tvGoal;
        @Bind(R.id.tvRemainingTime) TextView tvRemainingTime;
        @Bind(R.id.pbProgress) ProgressBar pbProgress;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public PlansAdapter(ArrayList<Plan> plans) {
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

        holder.ivThumb.setImageResource(category.getThumbRes());
        holder.ivThumb.setBackgroundResource(category.getColorRes());
        holder.tvTitle.setText(plan.getTitle());
        holder.tvGoal.setText(String.valueOf(plan.getGoal()));
        holder.tvRemainingTime.setText(getStringRemaining(plan.getDueTime()));
        holder.pbProgress.setMax(plan.getGoal());
        holder.pbProgress.setProgress(plan.getProgress());
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
