// Generated code from Butter Knife. Do not modify!
package com.catwoman.lifetodo.fragments;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PlansFragment$$ViewBinder<T extends com.catwoman.lifetodo.fragments.PlansFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493024, "field 'tvMessage'");
    target.tvMessage = finder.castView(view, 2131493024, "field 'tvMessage'");
    view = finder.findRequiredView(source, 2131493025, "field 'rvPlans'");
    target.rvPlans = finder.castView(view, 2131493025, "field 'rvPlans'");
  }

  @Override public void unbind(T target) {
    target.tvMessage = null;
    target.rvPlans = null;
  }
}
