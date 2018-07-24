package com.foolchen.arch.samples.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar
import com.foolchen.arch.view.recyclerview.ILoadingView

/**
 * @author chenchong
 * 2018/7/24
 * 上午11:53
 */
class LoadingView : ProgressBar, ILoadingView {
  constructor(context: Context?) : super(context)
  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
      defStyleAttr)

  override fun start() {
    // no op
  }

  override fun stop() {
    // no op
  }
}