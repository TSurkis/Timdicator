package com.tsurkis.timdicator;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;

class TimdicatorRecyclerViewBinder implements LifeCycleObserver {

  private Timdicator timdicator;

  private RecyclerView recyclerView;

  private boolean isHorizontal;

  private SnapHelper snapHelper;

  private int pageSize;

  private int currentScrollPosition = 0;

  private int currentPage = RecyclerView.NO_POSITION;

  private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener(){

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
      if (pageSize == 0) {
        View somePage = snapHelper.findSnapView(recyclerView.getLayoutManager());
        if (somePage != null) {
          pageSize = isHorizontal ? somePage.getWidth() : somePage.getHeight();
        }
      }
      currentScrollPosition -= isHorizontal ? dx : dy;
      int currentPagePosition = Math.abs(currentScrollPosition) / pageSize;
      if (currentPage != currentPagePosition) {
        timdicator.setIndex(currentPagePosition);
        currentPage = currentPagePosition;
      }
    }
  };

  public void attach(@NonNull Timdicator timdicator, @NonNull RecyclerView recyclerView, @NonNull SnapHelper snapHelper, boolean isHorizontal) {
    this.timdicator = timdicator;
    this.recyclerView = recyclerView;
    this.snapHelper = snapHelper;
    this.isHorizontal = isHorizontal;
    recyclerView.addOnScrollListener(scrollListener);
  }

  @Override
  public void onDetached() {
    if (timdicator != null) {
      timdicator = null;
    }

    if (recyclerView != null) {
      recyclerView.removeOnScrollListener(scrollListener);
      recyclerView = null;
    }

    if (snapHelper != null) {
      snapHelper = null;
    }
  }
}
