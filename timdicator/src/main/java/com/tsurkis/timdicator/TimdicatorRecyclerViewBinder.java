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

  private SnapHelper snapHelper;

  private boolean isReversed = false;

  private int currentPage = RecyclerView.NO_POSITION;

  private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
      setTimdicatorToCurrentRecyclerViewPosition();
    }
  };

  private RecyclerView.AdapterDataObserver recyclerViewChangesObserver = new RecyclerView.AdapterDataObserver() {

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
      updateTimdicator();
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
      updateTimdicator();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
      updateTimdicator();
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
      updateTimdicator();
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
      updateTimdicator();
    }
  };

  void attach(@NonNull Timdicator timdicator, @NonNull RecyclerView recyclerView, @NonNull SnapHelper snapHelper) {
    this.timdicator = timdicator;
    this.recyclerView = recyclerView;
    this.snapHelper = snapHelper;
    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
      isReversed = ((LinearLayoutManager) recyclerView.getLayoutManager()).getReverseLayout();
    }
    if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
      isReversed = ((GridLayoutManager) recyclerView.getLayoutManager()).getReverseLayout();
    }
    recyclerView.addOnScrollListener(scrollListener);
    updateTimdicator();
  }

  private void setTimdicatorToCurrentRecyclerViewPosition() {
    if (recyclerView != null && snapHelper != null && recyclerView.getLayoutManager() != null && recyclerView.getAdapter() != null) {
      View currentlySnappedView = snapHelper.findSnapView(recyclerView.getLayoutManager());
      if (currentlySnappedView != null) {
        int snappedPage = recyclerView.getLayoutManager().getPosition(currentlySnappedView);
        if (isReversed) {
          snappedPage = recyclerView.getAdapter().getItemCount() - snappedPage - 1;
        }
        if (snappedPage != currentPage) {
          currentPage = snappedPage;
          timdicator.setIndex(currentPage);
        }
      }
    }
  }

  private void updateTimdicator() {
    if (recyclerView != null && recyclerView.getAdapter() != null) {
      TimdicatorRecyclerViewBinder.this.timdicator.setNumberOfCircles(recyclerView.getAdapter().getItemCount());
      TimdicatorRecyclerViewBinder.this.timdicator.requestLayout();
      setTimdicatorToCurrentRecyclerViewPosition();
    }
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
