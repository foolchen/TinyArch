package com.foolchen.arch.samples.samples.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.foolchen.arch.config.sScreenWidth
import com.foolchen.arch.samples.R
import com.foolchen.arch.samples.bean.Photo
import com.foolchen.arch.samples.image.GlideApp
import com.foolchen.arch.view.recyclerview.MultiTypeViewHolder
import com.foolchen.arch.view.recyclerview.MultiTypesAdapter

/**
 * @author chenchong
 * 2018/7/23
 * 下午2:21
 */
class MultiplePresentersAdapter(
    val data: List<Photo>) : MultiTypesAdapter<MultiplePresentersAdapter.ImageHolder>() {

  fun append(newData: List<Photo>) {
    if (newData.isNotEmpty()) {
      val startPosition = this.data.size
      (this.data as MutableList<Photo>).addAll(newData)
      notifyItemRangeInserted(startPosition, newData.size)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
    return ImageHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_multiple_presenters, parent,
            false))

  }

  override fun onBindViewHolder(holder: ImageHolder, position: Int) {
    super.onBindViewHolder(holder, position)
    val item = getItem(position)
    val width = item.width
    val height = item.height
    val imageUrl = item.urls.regular

    val imageView = holder.itemView as ImageView

    // 对ImageView的宽高进行计算
    val finalWidth = sScreenWidth()
    val finalHeight = (finalWidth.toFloat() / width * height).toInt()

    val layoutParams = imageView.layoutParams
    layoutParams.width = finalWidth
    layoutParams.height = finalHeight
    imageView.layoutParams = layoutParams

    GlideApp.with(imageView)
        .load(imageUrl)
        .centerCrop()
        .override(finalWidth, finalHeight)
        .placeholder(R.drawable.ic_launcher_foreground)
        .into(imageView)
  }


  override fun getItem(position: Int): Photo = data[position]

  override fun getItemCount(): Int = data.size


  class ImageHolder(itemView: View) : MultiTypeViewHolder(itemView) {
    init {
      addClick(R.id.image)
      // 初始化时,将ImageView设置为3:2
      val layoutParams = itemView.layoutParams
      layoutParams.width = sScreenWidth()
      layoutParams.height = (sScreenWidth().toFloat() / 3 * 2).toInt()
    }
  }
}