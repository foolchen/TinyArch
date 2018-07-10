package com.foolchen.arch.factory

import com.foolchen.arch.presenter.Presenter

/**
 * This class represents a [PresenterFactory] that creates a presenter using [Class.newInstance] method.
 *
 * @param <P> the type of the presenter.
</P> */
class ReflectionPresenterFactory<P : Presenter<*>>(
    private val presenterClass: Class<P>) : PresenterFactory<P> {

  /**
   * 使用[RequiresPresenter]注解获取到的类型,然后通过反射创建一个presenter
   */
  override fun createPresenter(): P {
    try {
      return presenterClass.newInstance()
    } catch (e: Exception) {
      throw RuntimeException(e)
    }

  }
}


/**
 * 该方法根据[RequiresPresenter]注解定义的类型返回一个[ReflectionPresenterFactory],如果没有[RequiresPresenter]注解则返回null
 * @param viewClass 视图类
 * @param P presenter类型
 * @return 一个能够生产presenter的[ReflectionPresenterFactory]
</P> */
fun <P : Presenter<*>> fromViewClass(
    viewClass: Class<*>): ReflectionPresenterFactory<P>? {
  val annotation = viewClass.getAnnotation(RequiresPresenter::class.java)

  val presenterClass = if (annotation == null) null else annotation.value as Class<P>
  return if (presenterClass == null) null else ReflectionPresenterFactory(presenterClass)
}