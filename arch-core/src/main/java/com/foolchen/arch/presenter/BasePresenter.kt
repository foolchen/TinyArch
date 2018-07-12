package com.foolchen.arch.presenter

import com.foolchen.arch.config.sApplicationContext
import com.foolchen.arch.utils.warning
import io.reactivex.Observable
import io.reactivex.functions.BiConsumer
import nucleus5.presenter.Factory
import nucleus5.presenter.Presenter
import nucleus5.presenter.RxPresenter

/**
 * [RxPresenter]的子类,用于封装了请求的创建方式
 * @author wayne
 * 2018/7/10
 * 下午5:28
 */
class BasePresenter<View> : RxPresenter<View>() {
  private val mPresenters = ArrayList<Presenter<*>>()

  fun <T> produce(restartableId: Int,
      factory: Factory<Observable<T>>,
      success: BiConsumer<View, T>, failure: BiConsumer<View, Throwable>? = null,
      strategy: Strategy = Strategy.ONCE) {
    when (strategy) {
      Strategy.LATEST -> {
        // 回调最新产生的结果
        restartableLatestCache(restartableId, factory, success, failure)
      }
      Strategy.ALL -> {
        // 回调所有产生的结果
        restartableReplay(restartableId, factory, success, failure)
      }
      else -> {
        // 仅回调第一次产生的结果
        restartableFirst(restartableId, factory, success, failure)
      }
    }
  }

  fun registerPresenter(p: Presenter<View>) {
    if (mPresenters.contains(p)) {
      sApplicationContext().warning("$p has been registered already.Happen in {$javaClass}")
    }
    mPresenters.add(p)
  }

  fun unregisterPresenter(p: Presenter<View>) {
    mPresenters.remove(p)
  }
}