package com.foolchen.arch.app

import android.os.Bundle
import android.support.v4.app.Fragment
import com.foolchen.arch.ISupportLifecycle

/**
 * @author chenchong
 * 2018/6/4
 * 下午4:36
 */
class VisibilityDelegate {
  private var isFirstVisible = true
  private var isSupportResumed = false
  private var isSupportVisible = false
  private var isSupportInVisible = false
  private var mSavedInstanceState: Bundle? = null

  fun onCreate(savedInstanceState: Bundle?) {
    mSavedInstanceState = savedInstanceState
  }

  fun onResume(f: Fragment) {
    if (f is ISupportLifecycle && f.checkRecursiveVisible()) {
      onSupportVisible(f)
    }
    isSupportResumed = true
  }

  fun onPause(f: Fragment) {
    if (f is ISupportLifecycle && f.checkRecursiveVisible()) {
      onSupportInvisible(f)
    }
    isSupportResumed = false
  }

  fun onHiddenChanged(f: Fragment, hidden: Boolean) {
    if (isSupportResumed && f is ISupportLifecycle) {
      if (f.checkRecursiveVisible()) {
        onSupportVisible(f)
        onChildSupportVisible(f)
      } else {
        onSupportInvisible(f)
        onChildSupportInVisible(f)
      }
    }
  }

  fun setUserVisibleHint(f: Fragment, isVisibleToUser: Boolean) {
    if (isSupportResumed && f is ISupportLifecycle) {
      if (f.checkRecursiveVisible()) {
        onSupportVisible(f)
        onChildSupportVisible(f)
      } else {
        onSupportInvisible(f)
        onChildSupportInVisible(f)
      }
    }
  }

  fun isSupportResumed(f: Fragment) = isSupportResumed

  fun isSupportVisible(f: Fragment) = isSupportResumed && isSupportVisible

  private fun onSupportVisible(f: ISupportLifecycle) {
    if (isSupportVisible) return

    if (isFirstVisible) {
      f.onLazyInit(mSavedInstanceState)
      isFirstVisible = false
    }

    f.onSupportVisible()
    isSupportVisible = true
    isSupportInVisible = false
  }

  private fun onChildSupportVisible(f: ISupportLifecycle) {
    // 然后对child fragments进行调用
    (f as? Fragment)?.childFragmentManager?.fragments?.filter { it is ISupportLifecycle }?.forEach {
      val delegate = (it as ISupportLifecycle).getSupportDelegate()
      delegate.onSupportVisible(it)
    }
  }

  private fun onSupportInvisible(f: ISupportLifecycle) {
    if (isSupportInVisible) return

    f.onSupportInvisible()
    isSupportVisible = false
    isSupportInVisible = true
  }

  private fun onChildSupportInVisible(f: ISupportLifecycle) {
    // 然后对child fragments进行调用
    (f as? Fragment)?.childFragmentManager?.fragments?.filter { it is ISupportLifecycle }?.forEach {
      val delegate = (it as ISupportLifecycle).getSupportDelegate()
      delegate.onSupportInvisible(it)
    }
  }

  private fun Fragment?.checkVisible(): Boolean = this != null && !isHidden && userVisibleHint

  // 检查当前Fragment是否可见
  // 注意：仅当当前Fragment及其所有的父Fragment、祖先Fragment都可见时才返回true
  private fun Fragment?.checkRecursiveVisible(): Boolean {
    if (this == null) return false
    return if (checkVisible()) {
      val parent = parentFragment
      // 当parent不存在时，则checkVisible()可证明当前Fragment可见
      // 当parent存在时，则需要递归调用，证明所有的parent都可见
      parent == null || parent.checkRecursiveVisible()
    } else {
      false
    }
  }
}