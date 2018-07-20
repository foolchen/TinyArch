package com.foolchen.arch.view.recyclerview

/**
 * 该接口用于转换数据的类型
 *
 * @author chenchong
 * 2018/7/20
 * 上午11:12
 */
interface IMultiTypeConverter {
  fun <D : IMultiTypeItem> getItemViewType(item: D): Int
}