package com.foolchen.arch.samples.samples.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.foolchen.arch.samples.R
import com.foolchen.arch.view.recyclerview.IMultiType

/**
 * @author chenchong
 *
 * 2018/7/20
 * 上午11:49
 */
class MultiTextAdapter : IMultiType<RecyclerView.ViewHolder> {
  private val mTypes = listOf(1001, 1002, 1003)

  override fun provideViewHolder(context: Context, parent: ViewGroup,
      viewType: Int): RecyclerView.ViewHolder {
    val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_multi_texts, parent,
        false) as LinearLayout
    if (viewType == 1001) {
      layout.getChildAt(1).visibility = View.GONE
      layout.getChildAt(2).visibility = View.GONE
    } else if (viewType == 1002) {
      layout.getChildAt(2).visibility = View.GONE
    }
    return Holder(layout)
  }

  override fun bindViewHolder(holder: RecyclerView.ViewHolder, data: Any,
      position: Int, itemViewType: Int) {
    data as Texts
    val groups = holder.itemView as LinearLayout
    when (itemViewType) {
      1001 -> (groups.getChildAt(0) as TextView).text = data.texts[0]
      1002 -> {
        (groups.getChildAt(0) as TextView).text = data.texts[0]
        (groups.getChildAt(1) as TextView).text = data.texts[1]
      }
      else -> {
        (groups.getChildAt(0) as TextView).text = data.texts[0]
        (groups.getChildAt(1) as TextView).text = data.texts[1]
        (groups.getChildAt(2) as TextView).text = data.texts[2]
      }
    }
  }

  override fun containsItemViewType(viewType: Int): Boolean = mTypes.contains(viewType)
  private class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
}


data class Texts(val texts: List<String>, val type: Int)