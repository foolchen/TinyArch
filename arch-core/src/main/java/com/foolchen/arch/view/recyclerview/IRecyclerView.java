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

  private static final int STATUS_SWIPING_TO_REFRESH = 1;

  private static final int STATUS_RELEASE_TO_REFRESH = 2;

  private static final int STATUS_REFRESHING = 3;

  private static final boolean DEBUG = false;

  private int mStatus;

  private boolean mLoadMoreEnabled;

  private OnLoadMoreListener mOnLoadMoreListener;

  private FrameLayout mLoadMoreFooterContainer;

  private LinearLayout mHeaderViewContainer;

  private LinearLayout mFooterViewContainer;

  private View mLoadMoreFooterView;

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
    ensureFooterViewContainer();
    ensureLoadMoreFooterContainer();
    super.setAdapter(new WrapperAdapter(adapter, mHeaderViewContainer, mFooterViewContainer,
        mLoadMoreFooterContainer));
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

  private void ensureFooterViewContainer() {
    if (mFooterViewContainer == null) {
      mFooterViewContainer = new LinearLayout(getContext());
      mFooterViewContainer.setOrientation(LinearLayout.VERTICAL);
      mFooterViewContainer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.WRAP_CONTENT));
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
    if (DEBUG) {
      printStatusLog();
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

      case STATUS_SWIPING_TO_REFRESH:
        statusLog = "status_swiping_to_refresh";
        break;

      case STATUS_RELEASE_TO_REFRESH:
        statusLog = "status_release_to_refresh";
        break;

      case STATUS_REFRESHING:
        statusLog = "status_refreshing";
        break;
      default:
        statusLog = "status_illegal!";
        break;
    }
    return statusLog;
  }
}
