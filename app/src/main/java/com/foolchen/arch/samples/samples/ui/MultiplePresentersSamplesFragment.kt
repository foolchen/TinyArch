package com.foolchen.arch.samples.samples.ui

import android.os.Bundle
import com.foolchen.arch.app.ArchFragment
import com.foolchen.arch.samples.samples.presenter.MultiplePresentersPresenter
import nucleus5.factory.PresenterFactory

/**
 * 演示多个Presenter的Fragment
 * @author chenchong
 * 2018/7/16
 * 下午5:03
 */
// 在手动设置了PresenterFactory的情况下,不需要该注解
// @RequiresPresenter(MultiplePresentersPresenter::class)
class MultiplePresentersSamplesFragment : ArchFragment<MultiplePresentersPresenter>() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // 为了防止使用反射来创建presenter,此处手动设置一个工厂
    presenterFactory = PresenterFactory { MultiplePresentersPresenter() }
  }

  override fun onResume() {
    super.onResume()

    println(presenter)
  }
}