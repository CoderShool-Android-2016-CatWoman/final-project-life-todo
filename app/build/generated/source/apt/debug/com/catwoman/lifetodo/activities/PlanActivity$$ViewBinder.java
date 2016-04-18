// Generated code from Butter Knife. Do not modify!
package com.catwoman.lifetodo.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PlanActivity$$ViewBinder<T extends com.catwoman.lifetodo.activities.PlanActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492993, "field 'tvName'");
    target.tvName = finder.castView(view, 2131492993, "field 'tvName'");
    view = finder.findRequiredView(source, 2131492995, "field 'fcProgress'");
    target.fcProgress = finder.castView(view, 2131492995, "field 'fcProgress'");
    view = finder.findRequiredView(source, 2131492996, "field 'ivThumb'");
    target.ivThumb = finder.castView(view, 2131492996, "field 'ivThumb'");
    view = finder.findRequiredView(source, 2131492997, "field 'tvRemainingItems'");
    target.tvRemainingItems = finder.castView(view, 2131492997, "field 'tvRemainingItems'");
    view = finder.findRequiredView(source, 2131492998, "field 'tvGoal'");
    target.tvGoal = finder.castView(view, 2131492998, "field 'tvGoal'");
    view = finder.findRequiredView(source, 2131493001, "field 'tvDueDate'");
    target.tvDueDate = finder.castView(view, 2131493001, "field 'tvDueDate'");
  }

  @Override public void unbind(T target) {
    target.tvName = null;
    target.fcProgress = null;
    target.ivThumb = null;
    target.tvRemainingItems = null;
    target.tvGoal = null;
    target.tvDueDate = null;
  }
}
