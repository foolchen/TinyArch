package com.foolchen.arch.view.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.foolchen.arch.R;

/**
 * Created by aspsine on 16/3/3.
 */
public class IRecyclerView extends RecyclerView {
  private static final String TAG = IRecyclerView.class.getSimpleName();

  private static final int STATUS_DEFAULT = 0;

  private static final int STATUS_LOADING = 1;

  private static final int STATUS_ERROR = 2;

  private static final boolean DEBUG = false;

  private int mStatus;

  private boolean mLoadMoreEnabled;

  private OnLoadMoreListener mOnLoadMoreListener;

  private FrameLayout mLoadMoreFooterContainer;

  private LinearLayout mHeaderViewContainer;

  private FrameLayout mHolderViewContainer;

  private LinearLayout mFooterViewContainer;

  private View mLoadingView;

  private View mErrorView;

  private View mLoadMoreFooterView;

  private LayoutInflater mInflater;

  public IRecyclerView(Context context) {
    this(context, null);
  }

  public IRecyclerView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public IRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    final TypedArray a =
        context.obtainStyledAttributes(attrs, R.styleable.IRecyclerView, defStyle, 0);
    @LayoutRes int loadMoreFooterLayoutRes = -1;
    boolean loadMoreEnabled;

    try {
      loadMoreEnabled = a.getBoolean(R.styleable.IRecyclerView_loadMoreEnabled, false);
      loadMoreFooterLayoutRes = a.getResourceId(R.styleable.IRecyclerView_loadMoreFooterLayout, -1);
    } finally {
      a.recycle();
    }

    setLoadMoreEnabled(loadMoreEnabled);

    if (loadMoreFooterLayoutRes != -1) {
      setLoadMoreFooterView(loadMoreFooterLayoutRes);
    }
    setStatus(STATUS_DEFAULT);
  }

  public void setLoadMoreEnabled(boolean enabled) {
    if (this.mLoadMoreEnabled != enabled) {
      this.mLoadMoreEnabled = enabled;
      if (mLoadMoreEnabled) {
        removeOnScrollListener(mOnLoadMoreScrollListener);
        addOnScrollListener(mOnLoadMoreScrollListener);
      } else {
        removeOnScrollListener(mOnLoadMoreScrollListener);
      }
    }
  }

  public void setOnLoadMoreListener(OnLoadMoreListener listener) {
    this.mOnLoadMoreListener = listener;
  }

  public void setLoading() {
    setStatus(STATUS_LOADING);
  }

  public void setError() {
    setStatus(STATUS_ERROR);
  }

  public void setNormal() {
    setStatus(STATUS_DEFAULT);
  }

  public void setLoadingView(@LayoutRes int loadingViewLayoutRes) {
    ensureInflater();
    ensureHolderViewContainer();
    setLoadingView(mInflater.inflate(loadingViewLayoutRes, mHolderViewContainer, false));
  }

  public void setLoadingView(View loadingView) {
    if (!(loadingView instanceof ILoadingView)) {
      throw new IllegalArgumentException("LoadingView必须实现ILoadingView接口");
    }
    ensureHolderViewContainer();

    if (mLoadingView != loadingView) {
      mHolderViewContainer.removeView(mLoadingView);
      mLoadingView = loadingView;
      mHolderViewContainer.addView(mLoadingView);
    }
  }

  public void setErrorView(@LayoutRes int errorViewLayoutRes) {
    ensureInflater();
    ensureHolderViewContainer();
    setLoadingView(mInflater.inflate(errorViewLayoutRes, mHolderViewContainer, false));
  }

  public void setErrorView(View errorView) {
    if (!(errorView instanceof IErrorView)) {
      throw new IllegalArgumentException("ErrorView必须实现IErrorView接口");
    }
    ensureHolderViewContainer();
    if (mErrorView != errorView) {
      mHolderViewContainer.removeView(mErrorView);
      mErrorView = errorView;
      mHolderViewContainer.addView(mErrorView);
    }
  }

  public void setLoadMoreFooterView(View loadMoreFooterView) {
    if (mLoadMoreFooterView != null) {
      removeLoadMoreFooterView();
    }
    if (mLoadMoreFooterView != loadMoreFooterView) {
      this.mLoadMoreFooterView = loadMoreFooterView;
      ensureLoadMoreFooterContainer();
      mLoadMoreFooterContainer.addView(loadMoreFooterView);
    }
  }

  public void setLoadMoreFooterView(@LayoutRes int loadMoreFooterLayoutRes) {
    ensureLoadMoreFooterContainer();
    final View loadMoreFooter = LayoutInflater.from(getContext())
        .inflate(loadMoreFooterLayoutRes, mLoadMoreFooterContainer, false);
    if (loadMoreFooter != null) {
      setLoadMoreFooterView(loadMoreFooter);
    }
  }

  public View getLoadMoreFooterView() {
    return mLoadMoreFooterView;
  }

  public LinearLayout getHeaderContainer() {
    ensureHeaderViewContainer();
    return mHeaderViewContainer;
  }

  public FrameLayout getHolderContainer() {
    ensureHolderViewContainer();
    return mHolderViewContainer;
  }

  public LinearLayout getFooterContainer() {
    ensureFooterViewContainer();
    return mFooterViewContainer;
  }

  public void addHeaderView(View headerView) {
    ensureHeaderViewContainer();
    mHeaderViewContainer.addView(headerView);
    Adapter adapter = getAdapter();
    if (adapter != null) {
      adapter.notifyItemChanged(1);
    }
  }

  public void addFooterView(View footerView) {
    ensureFooterViewContainer();
    mFooterViewContainer.addView(footerView);
    Adapter adapter = getAdapter();
    if (adapter != null) {
      adapter.notifyItemChanged(adapter.getItemCount() - 2);
    }
  }

  public void setIAdapter(Adapter adapter) {
    ensureHeaderViewContainer();
    ensureHolderViewContainer();
    ensureFooterViewContainer();
    ensureLoadMoreFooterContainer();
    super.setAdapter(new WrapperAdapter(adapter, mHeaderViewContainer, mHolderViewContainer,
        mFooterViewContainer, mLoadMoreFooterContainer));
  }

  public Adapter getIAdapter() {
    return ((WrapperAdapter) super.getAdapter()).getAdapter();
  }

  private void ensureLoadMoreFooterContainer() {
    if (mLoadMoreFooterContainer == null) {
      mLoadMoreFooterContainer = new FrameLayout(getContext());
      mLoadMoreFooterContainer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.WRAP_CONTENT));
    }
  }

  private void ensureHeaderViewContainer() {
    if (mHeaderViewContainer == null) {
      mHeaderViewContainer = new LinearLayout(getContext());
      mHeaderViewContainer.setOrientation(LinearLayout.VERTICAL);
      mHeaderViewContainer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.WRAP_CONTENT));
    }
  }

  private void ensureHolderViewContainer() {
    if (mHolderViewContainer == null) {
      mHolderViewContainer = new FrameLayout(getContext());
      FrameLayout.LayoutParams lp =
          new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT);
      mHolderViewContainer.setLayoutParams(lp);
    }
  }

  private void ensureFooterViewContainer() {
    if (mFooterViewContainer == null) {
      mFooterViewContainer = new LinearLayout(getContext());
      mFooterViewContainer.setOrientation(LinearLayout.VERTICAL);
      mFooterViewContainer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.WRAP_CONTENT));
    }
  }

  private void ensureInflater() {
    if (mInflater == null) {
      mInflater = LayoutInflater.from(this.getContext());
    }
  }

  private void removeLoadMoreFooterView() {
    if (mLoadMoreFooterContainer != null) {
      mLoadMoreFooterContainer.removeView(mLoadMoreFooterView);
    }
  }

  private OnLoadMoreScrollListener mOnLoadMoreScrollListener = new OnLoadMoreScrollListener() {
    @Override public void onLoadMore(RecyclerView recyclerView) {
      if (mOnLoadMoreListener != null && mStatus == STATUS_DEFAULT) {
        mOnLoadMoreListener.onLoadMore();
      }
    }
  };

  private void setStatus(int status) {
    this.mStatus = status;
    ensureStatus(this.mStatus);
    if (DEBUG) {
      printStatusLog();
    }
  }

  private void ensureStatus(int status) {
    Adapter adapter = getAdapter();
    if (adapter instanceof WrapperAdapter) {
      switch (status) {
        case STATUS_DEFAULT:
          mLoadingView.setVisibility(View.INVISIBLE);
          mErrorView.setVisibility(INVISIBLE);
          ((WrapperAdapter) adapter).setHolderEnable(false);
          break;
        case STATUS_LOADING:
          mLoadingView.setVisibility(VISIBLE);
          mErrorView.setVisibility(INVISIBLE);
          ((WrapperAdapter) adapter).setHolderEnable(true);
          break;
        case STATUS_ERROR:
          mLoadingView.setVisibility(INVISIBLE);
          mErrorView.setVisibility(VISIBLE);
          ((WrapperAdapter) adapter).setHolderEnable(true);
          break;
        default:
          break;
      }
    }
  }

  private void printStatusLog() {
    Log.i(TAG, getStatusLog(mStatus));
  }

  private String getStatusLog(int status) {
    final String statusLog;
    switch (status) {
      case STATUS_DEFAULT:
        statusLog = "status_default";
        break;
      case STATUS_LOADING:
        statusLog = "status_loading";
        break;
      case STATUS_ERROR:
        statusLog = "status_error";
        break;
      default:
        statusLog = "status_illegal!";
        break;
    }
    return statusLog;
  }
}
