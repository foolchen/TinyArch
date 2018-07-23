package com.foolchen.arch.samples.samples.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.foolchen.arch.samples.R
import com.foolchen.arch.view.recyclerview.IMultiType

/**
 * @author chenchong
 *
 * 2018/7/20
 * 上午11:49
 */
class MultiImagesAdapter : IMultiType<RecyclerView.ViewHolder> {
  private val mTypes = listOf(2001, 2002, 2003)

  override fun provideViewHolder(context: Context, parent: ViewGroup,
      viewType: Int): RecyclerView.ViewHolder {
    val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_multi_images, parent,
        false) as LinearLayout
    if (viewType == 2001) {
      layout.getChildAt(1).visibility = View.GONE
      layout.getChildAt(2).visibility = View.GONE
    } else if (viewType == 2002) {
      layout.getChildAt(2).visibility = View.GONE
    }
    return Holder(layout)
  }

  override fun bindViewHolder(holder: RecyclerView.ViewHolder, data: Any,
      position: Int, itemViewType: Int) {
    data as Images
  }

  override fun containsItemViewType(viewType: Int): Boolean = mTypes.contains(viewType)
  private class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
}


data class Images(val urls: List<String>, val type: Int)