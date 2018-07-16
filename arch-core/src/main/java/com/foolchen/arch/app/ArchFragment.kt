package com.foolchen.arch.app

import android.os.Bundle
import nucleus5.presenter.Presenter
import nucleus5.view.NucleusSupportFragment

/**
 * 所有业务Fragment的基类,可以应用MVP模式
 *
 * @author chenchong
 * 2018/6/4
 * 下午4:25
 */
abstract class ArchFragment<P : Presenter<*>> : NucleusSupportFragment<P>(), ISupportLifecycle {
  private val mDelegate = VisibilityDelegate()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mDelegate.onCreate(savedInstanceState)
  }

  override fun onResume() {
    super.onResume()
    mDelegate.onResume(this)
  }

  override fun onPause() {
    super.onPause()
    mDelegate.onPause(this)
  }

  override fun onDestroyView() {
    super.onDestroyView()
  }

  override fun onDestroy() {
    super.onDestroy()
  }

  override fun onHiddenChanged(hidden: Boolean) {
    super.onHiddenChanged(hidden)
    mDelegate.onHiddenChanged(this, hidden)
  }

  override fun setUserVisibleHint(isVisibleToUser: Boolean) {
    super.setUserVisibleHint(isVisibleToUser)
    mDelegate.setUserVisibleHint(this, isVisibleToUser)
  }

  override fun getSupportDelegate(): VisibilityDelegate {
    return mDelegate
  }

  override fun onSupportVisible() {
  }

  override fun onSupportInvisible() {
  }

  override fun onLazyInit(savedInstanceState: Bundle?) {
  }

  override fun isSupportResumed(): Boolean = mDelegate.isSupportResumed(this)

  override fun isSupportVisible(): Boolean = mDelegate.isSupportVisible(this)
}