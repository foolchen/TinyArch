package com.foolchen.arch.samples.base

import android.os.Bundle
import android.util.Log
import com.foolchen.arch.app.NoPresenterFragment

/**
 * @author chenchong
 * 2018/6/4
 * 下午5:13
 */
abstract class NoPresenterLogFragment : NoPresenterFragment() {
  override fun onLazyInit(savedInstanceState: Bundle?) {
    super.onLazyInit(savedInstanceState)
    Log.i("Arch", "${javaClass.simpleName}#onLazyInit($savedInstanceState)")
  }

  override fun onSupportVisible() {
    super.onSupportVisible()
    Log.i("Arch", "${javaClass.simpleName}#onSupportVisible")
  }

  override fun onSupportInvisible() {
    super.onSupportInvisible()
    Log.i("Arch", "${javaClass.simpleName}#onSupportInvisible")
  }
}