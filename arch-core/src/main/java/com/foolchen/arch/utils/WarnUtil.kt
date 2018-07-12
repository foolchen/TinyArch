package com.foolchen.arch.utils

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.support.annotation.ColorInt
import android.util.Log
import android.widget.Toast
import com.foolchen.arch.config.sDevelop

fun Context.warning(msg: String) {
  if (sDevelop()) {
    Log.w(tag(), msg)
    toast(msg, Color.YELLOW)
  }
}

fun Context.error(msg: String) {
  Log.e(TAG, msg)
  toast(tag(), Color.RED)
}

private fun Context.toast(msg: String, @ColorInt color: Int) {
  val toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
  val view = toast.view
  view.setBackgroundColor(color)
  toast.show()
}

private fun Context.tag(): String = "$packageName:WARNING"