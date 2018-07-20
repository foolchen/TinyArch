package com.foolchen.arch.view.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * 能够实现多类型的数据适配器
 *
 * 注意:如果要向[MultiTypeAdapter]中注册多个[IMultiType],则其类型都不能冲突,否则会发生绑定数据错误的情况.
 *
 * @author chenchong
 * 2018/7/20
 * 上午10:58
 */
abstract class MultiTypeAdapter<VH : RecyclerView.ViewHolder> : IMultiType<VH>, RecyclerView.Adapter<VH>() {

  private val mMultiTypes = ArrayList<IMultiType<VH>>()
  private var mMultiTypeConverter: IMultiTypeConverter? = null

  /**
   * 针对位置来获取对应的条目数据
   */
  abstract fun getItem(position: Int): IMultiTypeItem

  abstract fun onCreateViewHolderImpl(parent: ViewGroup, viewType: Int): VH

  fun setMultiTypeConverter(converter: IMultiTypeConverter) {
    mMultiTypeConverter = converter
  }

  override fun registerMultiType(type: IMultiType<VH>) {
    mMultiTypes.add(type)
  }

  override fun unregisterMultiType(type: IMultiType<VH>) {
    mMultiTypes.remove(type)
  }

  override fun containItemViewType(itemViewType: Int): Boolean {
    return getItemViewTypes().contains(itemViewType)
  }

  override fun getItemViewType(position: Int): Int {
    if (mMultiTypeConverter == null)
      throw NullPointerException("IMultiTypeConverter should not be null.")

    val item = getItem(position)
    mMultiTypes.forEach {
      val itemViewType = it.getItemViewType(item)
      if (itemViewType != -1) {
        return itemViewType
      }
    }

    return -1
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
    // 此处优先使用子类的实现
    // 如果子类没有实现该类型,则尝试从已注册的类型中获取
    // 如果获取不到对应的类型,则抛出异常
    var vh: VH? = onCreateViewHolderImpl(parent, viewType)
    if (vh == null) {
      val multiType = getIMultiTypeForItemViewType(viewType) ?: throw UnsupportedOperationException(
          "类型{$viewType}尚未实现,请注册新的IMultiType来实现该viewType")
      vh = multiType.onCreateViewHolder(parent, viewType)
    }
    return vh
  }

  override fun onBindViewHolder(holder: VH, position: Int) {
    // 如果onCreateViewHolder未报出异常,则此处能够顺利执行
    getIMultiTypeForItemViewType(getItemViewType(position))!!.onBindViewHolder(holder, position)
  }

  private fun getIMultiTypeForItemViewType(itemViewType: Int): IMultiType<VH>? {
    mMultiTypes.forEach {
      if (it.containItemViewType(itemViewType)) {
        return it
      }
    }
    return null
  }
}