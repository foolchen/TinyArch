package com.foolchen.arch.factory

import com.foolchen.arch.presenter.Presenter
import java.lang.annotation.Inherited
import kotlin.reflect.KClass

/**
 * 该注解用于标注一个视图应该绑定的presenter的类型
 *
 * @param value 要绑定的presenter的类型
 */
@Inherited
@Retention(AnnotationRetention.RUNTIME)
annotation class RequiresPresenter(val value: KClass<out Presenter<*>>)
