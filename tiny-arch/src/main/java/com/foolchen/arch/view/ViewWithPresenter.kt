package com.foolchen.arch.view

import com.foolchen.arch.factory.PresenterFactory
import com.foolchen.arch.presenter.Presenter

/**
 * 该类中包含了基本的与presenter绑定相关的方法,用于被继承<br></br>
 * 一般来说应该被[android.app.Activity],[android.app.Fragment],[android.view.View],[android.view.ViewGroup]等继承
 *
 * @param P 要绑定的presenter的类型
 */
interface ViewWithPresenter<P : Presenter<*>> {

  /**
   * 返回一个presenter工厂
   */
  fun getPresenterFactory(): PresenterFactory<P>

  /**
   * 设定一个presenter工厂.<br/>
   * 该方法应该在onCreate/onFinishInflate之前调用来替代默认的[com.foolchen.arch.factory.ReflectionPresenterFactory].<br/>
   * 使用该方法来进行presenter的依赖注入
   */
  fun setPresenterFactory(presenterFactory: PresenterFactory<P>)

  /**
   *
   * 返回当前视图绑定的presenter.<br/>
   * 在presenter工厂不返回空值的情况下,该方法保证在onResume/onPause和onAttachedToWindow/onDetachedFromWindow生命周期期间返回一个部位空的presenter.
   *
   * @return 当前绑定的presenter或者null.
   */
  val presenter: P?
}
