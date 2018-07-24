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

  private final RecyclerView.Adapter mAdapter;

  private final FrameLayout mLoadMoreFooterContainer;

  private final LinearLayout mHeaderContainer;

  private final FrameLayout mHolderContainer;

  private final LinearLayout mFooterContainer;

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

  public WrapperAdapter(RecyclerView.Adapter adapter, LinearLayout headerContainer,
      FrameLayout holderContainer, LinearLayout footerContainer,
      FrameLayout loadMoreFooterContainer) {
    this.mAdapter = adapter;
    this.mHeaderContainer = headerContainer;
    this.mHolderContainer = holderContainer;
    this.mFooterContainer = footerContainer;
    this.mLoadMoreFooterContainer = loadMoreFooterContainer;

    mAdapter.registerAdapterDataObserver(mObserver);
  }

  public RecyclerView.Adapter getAdapter() {
    return mAdapter;
  }

  public void setHolderEnable(boolean isHolderEnable) {
    if (this.isHolderEnable != isHolderEnable) {
      /*int range = getItemCount();
      this.isHolderEnable = isHolderEnable;
      //notifyDataSetChanged();
      // 使用notifyItemRangeChanged以便于启用View切换的动画
      //notifyItemRangeChanged(0, range);
      if (this.isHolderEnable) {
        notifyItemRangeRemoved(1, range - 1);
      } else {
        notifyItemRangeChanged(0, range);
      }*/
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
            return spanSizeLookup.getSpanSize(position - 2);
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
    if (isHolderEnable) {
      // 在Holder可见时,不应该显示内容
      // 此时应该仅显示Holder
      return 1;
    } else {
      // 在Holder不可见时,应该显示内容
      return mAdapter.getItemCount() + 3;
    }
  }

  @Override public int getItemViewType(int position) {
    if (isHolderEnable) {
      return HOLDER;
    } else {
      if (position == 0) {
        return HEADER;
      } else if (getLowerPosition() <= position && position <= getHigherPosition()) {
        return mAdapter.getItemViewType(getPosition(position));
      } else if (position == getFooterPosition()) {
        return FOOTER;
      } else if (position == getLoadMoreFooterPosition()) {
        return LOAD_MORE_FOOTER;
      }
    }

    throw new IllegalArgumentException("Wrong type! Position = " + position);
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
    if (getLowerPosition() <= position && position <= getHigherPosition()) {
      //noinspection unchecked
      mAdapter.onBindViewHolder(holder, getPosition(position));
    }
  }

  // 获取有效的数据下边界(包含返回值)
  private int getLowerPosition() {
    return 1;
  }

  // 获取有效的数据上边界(包含返回值)
  private int getHigherPosition() {
    return mAdapter.getItemCount();
  }

  private int getPosition(int position) {
    return position - 1;
  }

  private int getFooterPosition() {
    return mAdapter.getItemCount() + 1;
  }

  private int getLoadMoreFooterPosition() {
    return mAdapter.getItemCount() + 2;
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
