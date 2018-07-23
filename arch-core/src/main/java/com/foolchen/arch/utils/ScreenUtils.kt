package com.foolchen.arch.utils

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowManager

/**
 * Return the width of screen, in pixel.
 *
 * @return the width of screen, in pixel
 */
fun Context.getScreenWidth(): Int {
  val wm = getSystemService(Context.WINDOW_SERVICE) as? WindowManager
      ?: return resources.displayMetrics.widthPixels
  val point = Point()
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
    wm.defaultDisplay.getRealSize(point)
  } else {
    wm.defaultDisplay.getSize(point)
  }
  return point.x
}

/**
 * Return the height of screen, in pixel.
 *
 * @return the height of screen, in pixel
 */
fun Context.getScreenHeight(): Int {
  val wm = getSystemService(Context.WINDOW_SERVICE) as? WindowManager
      ?: return resources.displayMetrics.heightPixels
  val point = Point()
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
    wm.defaultDisplay.getRealSize(point)
  } else {
    wm.defaultDisplay.getSize(point)
  }
  return point.y
}
