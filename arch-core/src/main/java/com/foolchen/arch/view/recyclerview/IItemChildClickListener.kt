package com.foolchen.arch.view.recyclerview

import android.view.View

/**
 * 针对RecyclerView中的条目,单个View的点击事件
 *
 * @author chenchong
 * 2018/7/20
 * 下午3:18
 */
interface IItemChildClickListener {
  fun onClick(view: View, data: Any, position: Int)
}