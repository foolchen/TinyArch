package com.foolchen.arch.view.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by aspsine on 16/3/12.
 */
public class WrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  // 这是所有HeaderView的容器对应的itemViewType,容器对应的position = 0
  private static final int HEADER = Integer.MIN_VALUE + 1;

  // 占位用View对应的itemViewType,对应的position = 1
  private static final int HOLDER = Integer.MAX_VALUE / 2;

  // 这是所有的FooterView的容器对应的itemViewType,容器对应的position = getItemCount() - 2
  private static final int FOOTER = Integer.MAX_VALUE - 1;

  // 加载更多FooterView对应的itemViewType,容器对应的position = getItemCount() - 1
  private static final int LOAD_MORE_FOOTER = Integer.MAX_VALUE;

  /*
   * 在isHolderEnable = true的情况下
   * 有上述定义的常量可知,可用的内容部分1<position<getItemCount() - 2
   * 在绑定ViewHolder时,position应该减2(减去Header以及Holder的位置)
   *
   * 在isHolderEnable = false的情况下
   * 有上述定义的常量可知,可用的内容部分0<position<getItemCount() - 1
   * 在绑定ViewHolder时,position应该减1(减去Header以及Holder的位置)
   */
  private boolean isHolderEnable = false;
  private boolean isLoadMoreEnable = false;

  private final RecyclerView.Adapter mAdapter;

  @NonNull private final FrameLayout mLoadMoreFooterContainer;
  @NonNull private final LinearLayout mHeaderContainer;
  @NonNull private final FrameLayout mHolderContainer;
  @NonNull private final LinearLayout mFooterContainer;

  private RecyclerView.AdapterDataObserver mObserver = new RecyclerView.AdapterDataObserver() {
    @Override public void onChanged() {
      WrapperAdapter.this.notifyDataSetChanged();
    }

    @Override public void onItemRangeChanged(int positionStart, int itemCount) {
      WrapperAdapter.this.notifyItemRangeChanged(positionStart + 2, itemCount);
    }

    @Override public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
      WrapperAdapter.this.notifyItemRangeChanged(positionStart + 2, itemCount, payload);
    }

    @Override public void onItemRangeInserted(int positionStart, int itemCount) {
      WrapperAdapter.this.notifyItemRangeInserted(positionStart + 2, itemCount);
    }

    @Override public void onItemRangeRemoved(int positionStart, int itemCount) {
      WrapperAdapter.this.notifyItemRangeRemoved(positionStart + 2, itemCount);
    }

    @Override public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
      WrapperAdapter.this.notifyDataSetChanged();
    }
  };

  WrapperAdapter(RecyclerView.Adapter adapter, @NonNull LinearLayout headerContainer,
      @NonNull FrameLayout holderContainer, @NonNull LinearLayout footerContainer,
      @NonNull FrameLayout loadMoreFooterContainer, boolean isHolderEnable,
      boolean isLoadMoreEnable) {
    this.mAdapter = adapter;
    this.mHeaderContainer = headerContainer;
    this.mHolderContainer = holderContainer;
    this.mFooterContainer = footerContainer;
    this.mLoadMoreFooterContainer = loadMoreFooterContainer;
    this.isHolderEnable = isHolderEnable;
    this.isLoadMoreEnable = isLoadMoreEnable;

    mAdapter.registerAdapterDataObserver(mObserver);
  }

  public RecyclerView.Adapter getAdapter() {
    return mAdapter;
  }

  public void addHeaderView(View headerView) {
    if (headerView != null) {
      boolean oldEnable = isHeaderViewContainerEnable();
      mHeaderContainer.addView(headerView);
      if (isHeaderViewContainerEnable() != oldEnable) { // 状态发生了变化
        notifyItemInserted(getHeaderViewContainerPosition());
      } /*else {
        notifyItemChanged(getHeaderViewContainerPosition());
      }*/
    }
  }

  public void removeHeaderView(View headerView) {
    if (headerView != null) {
      boolean oldEnable = isHeaderViewContainerEnable();
      int position = getHeaderViewContainerPosition();
      mHeaderContainer.removeView(headerView);
      if (isHeaderViewContainerEnable() != oldEnable) {
        notifyItemRemoved(position);
      } /*else {
        notifyItemChanged(position);
      }*/
    }
  }

  public void addFooterView(View footerView) {
    if (footerView != null) {
      boolean oldEnable = isFooterViewContainerEnable();
      mFooterContainer.addView(footerView);
      if (isFooterViewContainerEnable() != oldEnable) { // 状态发生了变化
        notifyItemInserted(getFooterViewContainerPosition());
      } /*else {
        notifyItemChanged(getFooterViewContainerPosition());
      }*/
    }
  }

  public void removeFooterView(View footerView) {
    if (footerView != null) {
      boolean oldEnable = isFooterViewContainerEnable();
      int position = getFooterViewContainerPosition();
      mFooterContainer.removeView(footerView);
      if (isFooterViewContainerEnable() != oldEnable) {
        notifyItemRemoved(position);
      } /*else {
        notifyItemChanged(position);
      }*/
    }
  }

  public void setLoadMoreEnable(boolean enable) {
    if (this.isLoadMoreEnable != enable) {
      int position = getLoadMoreFooterViewContainerPosition();
      this.isLoadMoreEnable = enable;
      notifyItemRemoved(position);
    }
  }

  public void setHolderEnable(boolean isHolderEnable) {
    if (this.isHolderEnable != isHolderEnable) {
      this.isHolderEnable = isHolderEnable;
      notifyDataSetChanged();
    }
  }

  @Override public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
    if (layoutManager instanceof GridLayoutManager) {
      final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
      final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
      gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
        @Override public int getSpanSize(int position) {
          WrapperAdapter wrapperAdapter = (WrapperAdapter) recyclerView.getAdapter();
          if (isFullSpanType(wrapperAdapter.getItemViewType(position))) {
            return gridLayoutManager.getSpanCount();
          } else if (spanSizeLookup != null) {
            return spanSizeLookup.getSpanSize(getPosition(position));
          }
          return 1;
        }
      });
    }
  }

  @Override public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
    super.onViewAttachedToWindow(holder);
    int position = holder.getAdapterPosition();
    int type = getItemViewType(position);
    if (isFullSpanType(type)) {
      ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
      if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
        StaggeredGridLayoutManager.LayoutParams lp =
            (StaggeredGridLayoutManager.LayoutParams) layoutParams;
        lp.setFullSpan(true);
      }
    }
  }

  private boolean isFullSpanType(int type) {
    return type == HEADER || type == HOLDER || type == FOOTER || type == LOAD_MORE_FOOTER;
  }

  @Override public int getItemCount() {
    if (isHolderContainerEnable()) {
      return 1;
    }
    int itemCount = mAdapter.getItemCount();
    if (isHeaderViewContainerEnable()) {
      itemCount++;
    }
    if (isHolderContainerEnable()) {
      itemCount++;
    }
    if (isFooterViewContainerEnable()) {
      itemCount++;
    }
    if (isLoadMoreFooterViewEnable()) {
      itemCount++;
    }
    return itemCount;
  }

  @Override public int getItemViewType(int position) {
    if (isHolderContainerEnable()) {
      return HOLDER;
    }
    if (position == getHeaderViewContainerPosition()) {
      return HEADER;
    } else if (position == getHolderViewContainerPosition()) {
      return HOLDER;
    } else if (position == getFooterViewContainerPosition()) {
      return FOOTER;
    } else if (position == getLoadMoreFooterViewContainerPosition()) {
      return LOAD_MORE_FOOTER;
    } else {
      return mAdapter.getItemViewType(getPosition(position));
    }
  }

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == HEADER) {
      return new HeaderContainerViewHolder(mHeaderContainer);
    } else if (viewType == HOLDER) {
      return new HolderContainerViewHolder(mHolderContainer);
    } else if (viewType == FOOTER) {
      return new FooterContainerViewHolder(mFooterContainer);
    } else if (viewType == LOAD_MORE_FOOTER) {
      return new LoadMoreFooterContainerViewHolder(mLoadMoreFooterContainer);
    } else {
      return mAdapter.onCreateViewHolder(parent, viewType);
    }
  }

  @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (position != getHeaderViewContainerPosition()
        && position != getHolderViewContainerPosition()
        && position != getFooterViewContainerPosition()
        && position != getLoadMoreFooterViewContainerPosition()) {
      //noinspection unchecked
      mAdapter.onBindViewHolder(holder, position);
    }
  }

  // 获取绑定数据需要的position
  private int getPosition(int position) {
    int resultPosition = position;
    if (isHeaderViewContainerEnable()) {
      resultPosition--;
    }
    if (isHolderContainerEnable()) {
      resultPosition--;
    }
    return resultPosition;
  }

  private boolean isHeaderViewContainerEnable() {
    return !isHolderContainerEnable() && mHeaderContainer.getChildCount() > 0;
  }

  private boolean isHolderContainerEnable() {
    return isHolderEnable;
  }

  private boolean isFooterViewContainerEnable() {
    return !isHolderContainerEnable() && mFooterContainer.getChildCount() > 0;
  }

  private boolean isLoadMoreFooterViewEnable() {
    if (isHolderContainerEnable()) return false;

    if (isLoadMoreEnable) {
      int childCount = mLoadMoreFooterContainer.getChildCount();
      int visibleChildCount = 0;
      for (int i = 0; i < childCount; i++) {
        View child = mLoadMoreFooterContainer.getChildAt(i);
        if (child != null && child.getVisibility() == View.VISIBLE) {
          visibleChildCount++;
        }
      }
      return visibleChildCount > 0;
    }
    return false;
  }

  /**
   * 获取HeaderViewContainer的位置
   *
   * @return 如果禁用了HeaderViewContainer, 则返回-1
   */
  private int getHeaderViewContainerPosition() {
    int position;
    if (isHeaderViewContainerEnable()) {
      position = 0;
    } else {
      position = -1;
    }
    return position;
  }

  /**
   * 获取HolderViewContainer的位置
   *
   * @return 如果禁用了HolderViewContainer, 则返回-1.<br/>在启用了HolderViewContainer的情况下,如果禁用了HeaderViewContainer,则返回1,否则0.
   */
  private int getHolderViewContainerPosition() {
    int position = -1;
    if (isHolderContainerEnable()) {
      if (isHeaderViewContainerEnable()) {
        position = 1;
      } else {
        position = 0;
      }
    }
    return position;
  }

  /**
   * 获取FooterViewContainer的位置
   */
  private int getFooterViewContainerPosition() {
    int position = -1;
    if (isFooterViewContainerEnable()) {
      position = getItemCount() - 1;
      if (isLoadMoreFooterViewEnable()) {
        position--;
      }
    }
    return position;
  }

  /**
   * 获取加载更多FooterView的位置
   */
  private int getLoadMoreFooterViewContainerPosition() {
    int position = -1;
    if (isLoadMoreFooterViewEnable()) {
      position = getItemCount() - 1;
    }
    return position;
  }

  static class HeaderContainerViewHolder extends RecyclerView.ViewHolder {

    HeaderContainerViewHolder(View itemView) {
      super(itemView);
    }
  }

  static class HolderContainerViewHolder extends RecyclerView.ViewHolder {

    HolderContainerViewHolder(View itemView) {
      super(itemView);
    }
  }

  static class FooterContainerViewHolder extends RecyclerView.ViewHolder {

    FooterContainerViewHolder(View itemView) {
      super(itemView);
    }
  }

  static class LoadMoreFooterContainerViewHolder extends RecyclerView.ViewHolder {

    LoadMoreFooterContainerViewHolder(View itemView) {
      super(itemView);
    }
  }
}
