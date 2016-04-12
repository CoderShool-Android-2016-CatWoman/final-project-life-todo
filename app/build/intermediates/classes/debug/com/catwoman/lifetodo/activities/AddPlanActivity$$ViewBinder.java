// Generated code from Butter Knife. Do not modify!
package com.catwoman.lifetodo.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AddPlanActivity$$ViewBinder<T extends com.catwoman.lifetodo.activities.AddPlanActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558513, "field 'spCategory'");
    target.spCategory = finder.castView(view, 2131558513, "field 'spCategory'");
    view = finder.findRequiredView(source, 2131558516, "field 'etName'");
    target.etName = finder.castView(view, 2131558516, "field 'etName'");
    view = finder.findRequiredView(source, 2131558518, "field 'etGoal'");
    target.etGoal = finder.castView(view, 2131558518, "field 'etGoal'");
    view = finder.findRequiredView(source, 2131558520, "field 'etDueDate'");
    target.etDueDate = finder.castView(view, 2131558520, "field 'etDueDate'");
  }

  @Override public void unbind(T target) {
    target.spCategory = null;
    target.etName = null;
    target.etGoal = null;
    target.etDueDate = null;
  }
}
