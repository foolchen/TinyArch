package com.foolchen.arch.view.layout

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import com.foolchen.arch.utils.dp2px
import org.jetbrains.anko.matchParent

/**
 * @author chenchong
 * 2018/7/24
 * 下午2:27
 */
class DefaultHeaderView : TextView, ISwipeToRefreshHeader {

  constructor(context: Context?) : super(context)
  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
      defStyleAttr)

  init {
    this.layoutParams = ViewGroup.LayoutParams(matchParent, 60.dp2px())
    gravity = Gravity.CENTER
    text = "默认HeaderView"
  }
}