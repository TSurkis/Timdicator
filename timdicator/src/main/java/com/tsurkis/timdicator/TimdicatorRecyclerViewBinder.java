package com.tsurkis.timdicator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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

  private boolean isReversed = false;

  private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {

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
      if (isReversed) {
        currentPagePosition = timdicator.getNumberOfCircles() - 1 - currentPagePosition;
      }
      if (currentPage != currentPagePosition) {
        timdicator.setIndex(currentPagePosition);
        currentPage = currentPagePosition;
      }
    }
  };

  private RecyclerView.AdapterDataObserver recyclerViewChangesObserver = new RecyclerView.AdapterDataObserver() {

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
      updateTimdicator(itemCount);
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
      updateTimdicator(itemCount);
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
      updateTimdicator(itemCount);
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
      updateTimdicator(itemCount);
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
      updateTimdicator(itemCount);
    }

    private void updateTimdicator(int newNumberOfCircles) {
      if (TimdicatorRecyclerViewBinder.this.timdicator.getNumberOfCircles() != newNumberOfCircles) {
        TimdicatorRecyclerViewBinder.this.timdicator.setNumberOfCircles(newNumberOfCircles);
        TimdicatorRecyclerViewBinder.this.timdicator.requestLayout();
      }
    }
  };

  public void attach(@NonNull Timdicator timdicator, @NonNull RecyclerView recyclerView, @NonNull SnapHelper snapHelper, boolean isHorizontal) {
    attach(timdicator, recyclerView, snapHelper, isHorizontal, recyclerView.getAdapter() != null ? recyclerView.getAdapter().getItemCount() : 0);
  }

  public void attach(@NonNull Timdicator timdicator, @NonNull RecyclerView recyclerView, @NonNull SnapHelper snapHelper, boolean isHorizontal, int initialIndex) {
    this.timdicator = timdicator;
    this.recyclerView = recyclerView;
    this.snapHelper = snapHelper;
    this.isHorizontal = isHorizontal;
    recyclerView.addOnScrollListener(scrollListener);
    if (recyclerView.getAdapter() != null) {
      timdicator.setNumberOfCircles(recyclerView.getAdapter().getItemCount());
      timdicator.requestLayout();
    }
    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
      isReversed = ((LinearLayoutManager) recyclerView.getLayoutManager()).getReverseLayout();
    }
    if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
      isReversed = ((GridLayoutManager) recyclerView.getLayoutManager()).getReverseLayout();
    }
    if (isReversed) {
      timdicator.setIndex(initialIndex);
    }
    recyclerView.getAdapter().registerAdapterDataObserver(recyclerViewChangesObserver);
  }

  @Override
  public void onDetached() {
    if (timdicator != null) {
      timdicator = null;
    }

    if (recyclerView != null) {
      recyclerView.removeOnScrollListener(scrollListener);

      if (recyclerView.getAdapter() != null) {
        recyclerView.getAdapter().unregisterAdapterDataObserver(recyclerViewChangesObserver);
      }

      recyclerView = null;
    }

    if (snapHelper != null) {
      snapHelper = null;
    }
  }
}
