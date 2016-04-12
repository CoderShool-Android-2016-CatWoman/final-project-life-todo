// Generated code from Butter Knife. Do not modify!
package com.catwoman.lifetodo.adapters;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CategoriesAdapter$ViewHolder$$ViewBinder<T extends com.catwoman.lifetodo.adapters.CategoriesAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558545, "field 'rlCategory'");
    target.rlCategory = finder.castView(view, 2131558545, "field 'rlCategory'");
    view = finder.findRequiredView(source, 2131558546, "field 'ivThumb'");
    target.ivThumb = finder.castView(view, 2131558546, "field 'ivThumb'");
  }

  @Override public void unbind(T target) {
    target.rlCategory = null;
    target.ivThumb = null;
  }
}
