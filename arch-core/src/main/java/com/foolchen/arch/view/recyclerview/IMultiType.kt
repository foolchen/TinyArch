package com.foolchen.arch.view.recyclerview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * 用于定义多类型RecyclerView相关方法的接口
 *
 * @author chenchong
 * 2018/7/20
 * 上午10:58
 */
interface IMultiType<VH : RecyclerView.ViewHolder> {

  /**
   * 用于为指定的类型提供对应的ViewHolder
   */
  fun provideViewHolder(context: Context, parent: ViewGroup,
      viewType: Int): VH

  /**
   * 向ViewHolder绑定数据
   */
  fun bindViewHolder(holder: VH, data: Any, position: Int, itemViewType: Int)

  /**
   * 判断当前IMultiType是否支持指定的类型
   */
  fun containsItemViewType(viewType: Int): Boolean
}