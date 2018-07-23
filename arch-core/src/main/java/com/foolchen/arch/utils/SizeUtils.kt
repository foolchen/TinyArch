package com.foolchen.arch.utils

import android.content.res.Resources

/**
 * Value of dp to value of px.
 *
 * @param dpValue The value of dp.
 * @return value of px
 */
fun Number.dp2px(): Int {
  val scale = Resources.getSystem().displayMetrics.density
  return (this.toFloat() * scale + 0.5f).toInt()
}

/**
 * Value of px to value of dp.
 *
 * @param pxValue The value of px.
 * @return value of dp
 */
fun Number.px2dp(): Int {
  val scale = Resources.getSystem().displayMetrics.density
  return (this.toFloat() / scale + 0.5f).toInt()
}

/**
 * Value of sp to value of px.
 *
 * @param spValue The value of sp.
 * @return value of px
 */
fun Number.sp2px(): Int {
  val fontScale = Resources.getSystem().displayMetrics.density
  return (this.toFloat() * fontScale + 0.5f).toInt()
}

/**
 * Value of px to value of sp.
 *
 * @param pxValue The value of px.
 * @return value of sp
 */
fun Number.px2sp(): Int {
  val fontScale = Resources.getSystem().displayMetrics.density
  return (this.toFloat() / fontScale + 0.5f).toInt()
}