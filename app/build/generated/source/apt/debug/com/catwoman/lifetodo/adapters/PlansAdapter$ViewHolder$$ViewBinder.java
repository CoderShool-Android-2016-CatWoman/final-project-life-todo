// Generated code from Butter Knife. Do not modify!
package com.catwoman.lifetodo.adapters;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PlansAdapter$ViewHolder$$ViewBinder<T extends com.catwoman.lifetodo.adapters.PlansAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492996, "field 'ivThumb'");
    target.ivThumb = finder.castView(view, 2131492996, "field 'ivThumb'");
    view = finder.findRequiredView(source, 2131493027, "field 'tvTitle'");
    target.tvTitle = finder.castView(view, 2131493027, "field 'tvTitle'");
    view = finder.findRequiredView(source, 2131492998, "field 'tvGoal'");
    target.tvGoal = finder.castView(view, 2131492998, "field 'tvGoal'");
    view = finder.findRequiredView(source, 2131493029, "field 'tvRemainingTime'");
    target.tvRemainingTime = finder.castView(view, 2131493029, "field 'tvRemainingTime'");
    view = finder.findRequiredView(source, 2131493030, "field 'pbProgress'");
    target.pbProgress = finder.castView(view, 2131493030, "field 'pbProgress'");
  }

  @Override public void unbind(T target) {
    target.ivThumb = null;
    target.tvTitle = null;
    target.tvGoal = null;
    target.tvRemainingTime = null;
    target.pbProgress = null;
  }
}
