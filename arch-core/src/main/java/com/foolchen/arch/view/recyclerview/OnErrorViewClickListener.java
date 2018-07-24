package com.foolchen.arch.view.recyclerview;

import android.view.View;

/**
 * @author chenchong
 * 2018/7/24
 * 上午10:48
 */
public class OnErrorViewClickListener implements View.OnClickListener {
  private IErrorViewListener mErrorViewListener;

  public OnErrorViewClickListener(IErrorViewListener errorViewListener) {
    mErrorViewListener = errorViewListener;
  }

  public void setErrorViewListener(IErrorViewListener errorViewListener) {
    mErrorViewListener = errorViewListener;
  }

  @Override public void onClick(View v) {
    if (mErrorViewListener != null) {
      mErrorViewListener.onErrorViewClicked(v);
    }
  }
}
