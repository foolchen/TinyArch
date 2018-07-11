package com.foolchen.arch.presenter

import io.reactivex.Observable
import nucleus5.presenter.Factory

/**
 * 用于组建请求
 * @author wayne
 * 2018/7/10
 * 下午5:36
 */
class RequestBuilder(strategy: Strategy?) {
  private var strategy: Strategy = Strategy.ONCE
  var id: Int? = null

  init {
    if (strategy != null) {
      this.strategy = strategy
    }
  }

}

fun <View> BasePresenter<View>.create(strategy: Strategy? = Strategy.ONCE) = RequestBuilder(
    strategy)

fun RequestBuilder.id(id: Int): RequestBuilder {
  this.id = id
  return this
}

fun <T> RequestBuilder.factory(factory: Factory<Observable<T>>) {

}