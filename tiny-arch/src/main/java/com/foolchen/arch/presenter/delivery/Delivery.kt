package com.foolchen.arch.presenter.delivery

import com.foolchen.arch.view.OptionalView
import io.reactivex.Notification
import io.reactivex.Observable
import io.reactivex.functions.BiConsumer

/**
 * 该类用来将视图和数据进行绑定
 *
 * @param view 要回调的视图
 * @param notification 处理结果的标识,用于标识处理是否成功
 *
</T></View> */
class Delivery<View, T>(private val view: View?, private val notification: Notification<T>?) {

  /**
   * 用于根据[notification]标识,将结果发送到不同的回调
   */
  @Throws(Exception::class)
  fun split(onNext: BiConsumer<View, T>, onError: BiConsumer<View, Throwable>?) {
    if (notification!!.isOnNext)
      onNext.accept(view, notification.value)
    else if (onError != null && notification.isOnError)
      onError.accept(view, notification.error)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || javaClass != other.javaClass) return false

    val delivery = other as Delivery<*, *>?

    return if (if (view != null) view != delivery!!.view else delivery!!.view != null) false else !if (notification != null) notification != delivery.notification else delivery.notification != null
  }


  override fun hashCode(): Int {
    var result = view?.hashCode() ?: 0
    result = 31 * result + (notification?.hashCode() ?: 0)
    return result
  }

  override fun toString(): String {
    return "Delivery{" +
        "view=" + view +
        ", notification=" + notification +
        '}'.toString()
  }
}

fun isValid(view: OptionalView<*>, notification: Notification<*>): Boolean {
  return view.view != null && (notification.isOnNext || notification.isOnError)
}

fun <View, T> validObservable(view: OptionalView<View>,
    notification: Notification<T>): Observable<Delivery<View, T>> {
  return if (isValid(view, notification))
    Observable.just(Delivery(view.view, notification))
  else
    Observable.empty()
}