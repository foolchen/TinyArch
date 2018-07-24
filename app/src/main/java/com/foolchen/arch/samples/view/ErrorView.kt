package com.foolchen.arch.samples.view

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.foolchen.arch.view.recyclerview.IErrorView

/**
 * @author chenchong
 * 2018/7/24
 * 上午11:55
 */
class ErrorView : AppCompatTextView, IErrorView {
  constructor(context: Context?) : super(context)
  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
      defStyleAttr)
}