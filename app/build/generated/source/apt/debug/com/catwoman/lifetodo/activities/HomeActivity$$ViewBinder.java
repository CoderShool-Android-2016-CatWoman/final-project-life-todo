// Generated code from Butter Knife. Do not modify!
package com.catwoman.lifetodo.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class HomeActivity$$ViewBinder<T extends com.catwoman.lifetodo.activities.HomeActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492988, "field 'appbar'");
    target.appbar = finder.castView(view, 2131492988, "field 'appbar'");
    view = finder.findRequiredView(source, 2131492989, "field 'toolbar'");
    target.toolbar = finder.castView(view, 2131492989, "field 'toolbar'");
    view = finder.findRequiredView(source, 2131492991, "field 'viewPager'");
    target.viewPager = finder.castView(view, 2131492991, "field 'viewPager'");
    view = finder.findRequiredView(source, 2131492990, "field 'tabLayout'");
    target.tabLayout = finder.castView(view, 2131492990, "field 'tabLayout'");
    view = finder.findRequiredView(source, 2131492992, "field 'fab'");
    target.fab = finder.castView(view, 2131492992, "field 'fab'");
  }

  @Override public void unbind(T target) {
    target.appbar = null;
    target.toolbar = null;
    target.viewPager = null;
    target.tabLayout = null;
    target.fab = null;
  }
}
