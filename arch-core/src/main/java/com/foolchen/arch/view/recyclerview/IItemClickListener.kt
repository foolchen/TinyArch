package com.foolchen.arch.view.recyclerview

import android.view.View

/**
 * 针对条目的点击事件
 *
 * @author chenchong
 * 2018/7/20
 * 下午3:26
 */
interface IItemClickListener {
  fun onItemClickListener(view: View, data: Any, position: Int)
}