package com.foolchen.arch.view.recyclerview

import android.content.Context
import android.support.annotation.CallSuper
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * @author chenchong
 * 2018/7/20
 * 下午2:19
 */

abstract class MultiTypesAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

  private val mMultiTypes = ArrayList<IMultiType<VH>>()
  private var mMultiTypeConverter: IMultiTypeConverter? = null

  private var mItemChildClickListener: IItemChildClickListener? = null
  private var mItemClickListener: IItemClickListener? = null

  abstract fun getItem(position: Int): Any

  /**
   * 向当前数据适配器注册类型处理器
   */
  fun registerMultiType(type: IMultiType<VH>) {
    mMultiTypes.add(type)
  }

  /**
   * 从当前数据适配器反注册类型处理器
   */
  fun unregisterMultiType(type: IMultiType<VH>) {
    mMultiTypes.remove(type)
  }

  fun setOnItemChildClickListener(listener: IItemChildClickListener) {
    mItemChildClickListener = listener
  }

  fun getOnItemChildClickListener(): IItemChildClickListener? = mItemChildClickListener

  fun getOnItemClickListener(): IItemClickListener? = mItemClickListener

  fun setOnItemClickListener(listener: IItemClickListener) {
    mItemClickListener = listener
  }


  /**
   * 设置类型转换器,用于将各个列表中不同的数据类型转换为统一定义的viewType
   */
  fun setConverter(converter: IMultiTypeConverter) {
    mMultiTypeConverter = converter
  }

  override fun getItemViewType(position: Int): Int {
    if (mMultiTypeConverter == null) {
      throw NullPointerException("IMultiTypeConverter不能为空")
    }
    return mMultiTypeConverter!!.getItemViewType(getItem(position))
  }

  /**
   * 该方法会优先使用当前类的实现来创建ViewHolder.
   *
   * 如果当前类没有实现,则会尝试使用已注册的[IMultiType]来创建ViewHolder.
   *
   * 如果没有发现对应的实现,则会抛出异常.
   */
  @CallSuper
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
    //var vh = getIMultiTypeForItemViewType(viewType)?.provideViewHolder(parent.context, viewType)
    var vh = onCreateViewHolder(parent.context, parent, viewType)
    if (vh == null) {
      val type = getIMultiTypeForItemViewType(viewType) ?: throw UnsupportedOperationException(
          "未注册处理{$viewType}类型的IMultiType")
      vh = type.provideViewHolder(parent.context, parent, viewType)
    }
    return vh
  }

  fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH? = null

  override fun onBindViewHolder(holder: VH, position: Int) {
    val itemViewType = getItemViewType(position)
    val item = getItem(position)
    getIMultiTypeForItemViewType(itemViewType)?.bindViewHolder(holder,
        item, position, itemViewType)
    (holder as? MultiTypeViewHolder)?.setAdapter(this@MultiTypesAdapter)
  }

  // 根据itemViewType来获取提供了对应类型ViewHolder的IMultiType
  private fun getIMultiTypeForItemViewType(viewType: Int): IMultiType<VH>? {
    mMultiTypes.forEach {
      if (it.containsItemViewType(viewType)) return it
    }
    return null
  }
}