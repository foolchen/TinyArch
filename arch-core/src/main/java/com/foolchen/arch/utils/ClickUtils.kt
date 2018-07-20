package com.foolchen.arch.utils

import android.view.View

/**
 * 点击相关的工具
 *
 * @author chenchong
 * 2018/7/20
 * 下午3:18
 */

private val CLICK_INTERVAL = 500 //ms

private var mClickMillis: Long = -1L

fun View?.click(): View? {
  return if (mClickMillis == -1L || System.currentTimeMillis() - mClickMillis > CLICK_INTERVAL) {
    mClickMillis = System.currentTimeMillis()
    this
  } else {
    null
  }
}