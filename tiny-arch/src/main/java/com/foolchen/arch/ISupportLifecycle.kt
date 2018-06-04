package com.foolchen.arch

import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * 重新定义的生命周期接口，用于替代Fragment中的onResume()、onPause()、setUserVisibleHint()、onHiddenChanged()
 * @author chenchong
 * 2018/6/4
 * 下午2:58
 */
interface ISupportLifecycle {

  fun onLazyInit(savedInstanceState: Bundle?)

  fun isSupportResumed(): Boolean

  /**
   * 用于取代[Fragment.setUserVisibleHint]以及[Fragment.onHiddenChanged]
   */
  fun onSupportVisible()

  /**
   * 用于取代[Fragment.setUserVisibleHint]以及[Fragment.onHiddenChanged]
   */
  fun onSupportInvisible()

  fun isSupportVisible(): Boolean
}