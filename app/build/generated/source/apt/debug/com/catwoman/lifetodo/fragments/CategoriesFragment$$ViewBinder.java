// Generated code from Butter Knife. Do not modify!
package com.catwoman.lifetodo.fragments;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CategoriesFragment$$ViewBinder<T extends com.catwoman.lifetodo.fragments.CategoriesFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131493019, "field 'rvCategories'");
    target.rvCategories = finder.castView(view, 2131493019, "field 'rvCategories'");
  }

  @Override public void unbind(T target) {
    target.rvCategories = null;
  }
}
