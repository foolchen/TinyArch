package com.foolchen.arch.view.recyclerview

/**
 * [IMultiTypeItem]的简单实现
 *
 * 要想实现多条目的数据类型可以简单继承该类,也可以自主实现[IMultiTypeItem]
 *
 * @author chenchong
 *
 * 2018/7/20
 * 上午11:16
 */
open class MultiTypeItemImpl : IMultiTypeItem {
  override fun getItemViewType(converter: IMultiTypeConverter): Int {
    return converter.getItemViewType(this)
  }
}