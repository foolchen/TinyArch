package com.foolchen.arch.factory

import com.foolchen.arch.presenter.Presenter

/**
 * 用于创建presenter的工厂
 *
 * @param <P> 要创建的presenter的类型
</P> */
interface PresenterFactory<P : Presenter<*>> {
  /**
   * 创建一个presenter
   */
  fun createPresenter(): P
}
