package com.foolchen.arch.view.recyclerview

/**
 * 定义多条目item需要的接口
 * @author chenchong
 * 2018/7/20
 * 上午11:09
 */
interface IMultiTypeItem {
  fun getItemViewType(converter: IMultiTypeConverter): Int
}