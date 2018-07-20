package com.foolchen.arch.view.recyclerview

import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.View
import com.foolchen.arch.utils.click

/**
 * 供业务实现调用的ViewHolder,可以方便的添加点击事件等
 *
 * @author chenchong
 * 2018/7/20
 * 下午3:59
 */
abstract class MultiTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  private lateinit var mAdapter: MultiTypesAdapter<*>

  fun setAdapter(adapter: MultiTypesAdapter<*>) {
    mAdapter = adapter
  }


  fun addClick(@IdRes viewId: Int) {
    itemView.findViewById<View>(viewId)?.setOnClickListener {
      it.click()?.let {
        mAdapter.getOnItemChildClickListener()?.onClick(it, mAdapter.getItem(adapterPosition),
            adapterPosition)
      }
    }
  }

  fun enableItemClick(enable: Boolean) {
    itemView.setOnClickListener(if (enable) View.OnClickListener {
      it.click()?.let {
        mAdapter.getOnItemClickListener()?.onItemClickListener(it,
            mAdapter.getItem(adapterPosition),
            adapterPosition)
      }
    } else null)
  }
}