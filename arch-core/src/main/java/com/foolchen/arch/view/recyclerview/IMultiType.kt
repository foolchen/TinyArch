package com.foolchen.arch.view.recyclerview

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

  fun registerMultiType(type: IMultiType<VH>)

  fun unregisterMultiType(type: IMultiType<VH>)

  fun getItemViewType(item: IMultiTypeItem): Int

  /**
   * 获取当前IMultiType支持的所有类型
   */
  fun getItemViewTypes(): List<Int>

  /**
   * 判断当前IMultiType中是否包含了对应的类型
   */
  fun containItemViewType(itemViewType: Int): Boolean

  fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

  fun onBindViewHolder(holder: VH, position: Int)
}