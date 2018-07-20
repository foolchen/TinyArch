package com.foolchen.arch.samples.samples.ui

import com.foolchen.arch.view.recyclerview.IMultiTypeConverter

/**
 * 用于类型转换
 *
 * @author chenchong
 * 2018/7/20
 * 上午11:55
 */
class MultiTypeConverter : IMultiTypeConverter {
  override fun getItemViewType(item: Any): Int {

    return when (item) {
      is Texts -> {
        val texts = item.texts
        when {
          texts.size == 1 -> 1001
          texts.size == 2 -> 1002
          else -> 1003
        }
      }
      is Images -> {
        val urls = item.urls
        when {
          urls.size == 1 -> 2001
          urls.size == 2 -> 2002
          else -> 2003
        }
      }

      else -> {
        throw UnsupportedOperationException("暂不支持的类型$item")
      }
    }
  }
}