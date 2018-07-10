// 用于将生成的类命名为RxPresenter,供Java调用
@file:JvmName("RxPresenter")

package com.foolchen.arch.presenter

import android.text.TextUtils.split
import android.util.SparseArray
import com.foolchen.arch.presenter.delivery.DeliverFirst
import com.foolchen.arch.presenter.delivery.DeliverLatestCache
import com.foolchen.arch.presenter.delivery.DeliverReplay
import com.foolchen.arch.presenter.delivery.Delivery
import com.foolchen.arch.view.OptionalView
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiConsumer
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject


private const val REQUEST_KEY = "RxPresenter#requested"

/**
 * @author chenchong
 * 2018/6/6
 * 上午11:21
 */
open class RxPresenter<View> {
  private val mViews = BehaviorSubject.create<OptionalView<View>>()
  // private val mSubscriptions = CompositeDisposable()

  private val mRestartables = SparseArray<Factory<Disposable>>()
  private val mRestartableDisposables = SparseArray<Disposable>()
  private val mRequested = ArrayList<Int>()


  /**
   * 返回一个包含[OptionalView]的[Observable]，[OptionalView]中的[View]可能是null。
   * 查看[BehaviorSubject]来了解更多。
   *
   * @return 包含[OptionalView]的[Observable]，[OptionalView]中的[View]可能是null
   * @see [BehaviorSubject]
   */
  fun view(): Observable<OptionalView<View>> = mViews

  /**
   * restartable可以是任意可用的（可被订阅的）RxJava [Observable]。
   * 如果在Presenter在保存状态时，restartable还没有执行完毕，则在Presenter恢复状态时，
   * 应该自动重新执行这个restartable。
   *
   * 注册一个Factory。当Presenter状态被还原时，自动重新执行restartable。
   *
   * @param restartableId 要执行的restartable的id
   * @param factory 用于创建restartable的工厂
   *
   */
  fun restartable(restartableId: Int, factory: Factory<Disposable>) {
    mRestartables.put(restartableId, factory)
    if (mRequested.contains(restartableId)) {
      start(restartableId)
    }
  }

  /**
   * 根据id开始执行一个restartable
   * @param restartableId 要执行的restartable的id
   */
  fun start(restartableId: Int) {
    // 在执行新的操作之前，将之前的操作停止。防止出现重复操作和回调。
    stop(restartableId)
    mRequested.add(restartableId)
    // 此处将新的回调添加到SparseArray中，会覆盖之前已经过时的回调
    mRestartableDisposables.put(restartableId, mRestartables.get(restartableId).create())
  }

  /**
   * 取消一个restartable的执行
   *
   * @param restartableId 正在执行的restartable的id
   */
  fun stop(restartableId: Int) {
    mRequested.remove(restartableId)
    val disposable = mRestartableDisposables.get(restartableId)
    disposable?.dispose()
  }

  /**
   * 判断一个restartable是否已经被取消
   *
   * @param restartableId 要取消的restartable的id
   * @return 如果restartable不存在或者已经被取消则返回true，否则返回false
   */
  fun isDisposed(restartableId: Int): Boolean {
    return mRestartableDisposables.get(restartableId)?.isDisposed == true
  }

  /**
   * 该方法是[restartable],[deliverFirst]和[split]的组合使用,用于实现仅回调第一次发送的结果
   * @param restartableId 要执行的restartable的id
   * @param observableFactory 该Factory在restartable要执行的时候创建并返回一个Observable
   * @param onNext 当处理成功时发送给该回调
   * @param onError 当处理失败时发送给该回调
   */
  fun <T> restartableFirst(restartableId: Int, observableFactory: Factory<Observable<T>>,
      onNext: BiConsumer<View, T>, onError: BiConsumer<View, Throwable>?) {
    restartable(restartableId, Factory {
      observableFactory.create()
          .compose(this@RxPresenter.deliverFirst())
          .subscribe(split(onNext, onError))
    })
  }

  /**
   * [restartableFirst]的重载
   */
  fun <T> restartableFirst(restartableId: Int, observableFactory: Factory<Observable<T>>,
      onNext: BiConsumer<View, T>) {
    restartableFirst(restartableId, observableFactory, onNext, null)
  }

  /**
   * 该方法是[restartable],[deliverLatestCache]和[split]的组合使用<br/>
   * 在每次view被绑定时,都会发送最新的结果
   *
   * @param restartableId 要执行的restartable的id
   * @param observableFactory 该Factory在restartable要执行的时候创建并返回一个Observable
   * @param onNext 当处理成功时发送给该回调
   * @param onError 当处理失败时发送给该回调
   */
  fun <T> restartableLatestCache(restartableId: Int, observableFactory: Factory<Observable<T>>,
      onNext: BiConsumer<View, T>, onError: BiConsumer<View, Throwable>?) {
    restartable(restartableId, Factory {
      observableFactory.create()
          .compose(this@RxPresenter.deliverLatestCache())
          .subscribe(split(onNext, onError))
    })
  }

  /**
   * [restartableLatestCache]的重载,没有失败回调
   */
  fun <T> restartableLatestCache(restartableId: Int, observableFactory: Factory<Observable<T>>,
      onNext: BiConsumer<View, T>) {
    restartableLatestCache(restartableId, observableFactory, onNext, null)
  }

  /**
   * 该方法是[restartable],[deliverLatestCache]和[split]的组合使用<br/>
   * 会保留所有产生的结果,并在每次view被绑定到presenter时发送所有的结果
   *
   * @param restartableId 要执行的restartable的id
   * @param observableFactory 该Factory在restartable要执行的时候创建并返回一个Observable
   * @param onNext 当处理成功时发送给该回调
   * @param onError 当处理失败时发送给该回调
   * */
  fun <T> restartableReplay(restartableId: Int, observableFactory: Factory<Observable<T>>,
      onNext: BiConsumer<View, T>, onError: BiConsumer<View, Throwable>?) {
    restartable(restartableId, Factory {
      observableFactory.create()
          .compose(this@RxPresenter.deliverReplay())
          .subscribe(split(onNext, onError))

    })
  }

  /**
   * [restartableReplay]的重载,没有失败回调
   */
  fun <T> restartableReplay(restartableId: Int, observableFactory: Factory<Observable<T>>,
      onNext: BiConsumer<View, T>) {
    restartableReplay(restartableId, observableFactory, onNext, null)
  }

  /**
   * 返回一个[io.reactivex.ObservableTransformer]对象,它将view和源[io.reactivex.Observable]发出的数据绑定在一起
   *
   * [deliverFirst]方法仅处理源[io.reactivex.Observable]发出的第一个数据
   *
   * @param T observable发出的数据的类型
   */
  fun <T> deliverFirst(): DeliverFirst<View, T> = DeliverFirst(mViews)

  /**
   * 返回一个[io.reactivex.ObservableTransformer]对象,它将view和源[io.reactivex.Observable]发出的数据绑定在一起
   *
   * [deliverLatestCache]方法会保留最后一个onNext的结果,并且在每当有view被绑定Presenter时,都会发送结果.
   * 在view已经被绑定到Presenter的情况下,如果有一个新的onNext结果产生,则会立即发送.
   *
   * @param T observable发出的数据的类型
   */
  fun <T> deliverLatestCache(): DeliverLatestCache<View, T> = DeliverLatestCache(mViews)

  /**
   * 返回一个[io.reactivex.ObservableTransformer]对象,它将view和源[io.reactivex.Observable]发出的数据绑定在一起
   *
   * [deliverReplay]会保留所有的结果,并且在view被绑定到Presenter进行发送.
   * 在view已经被绑定的情况下产生了新的结果,则会立即发送.
   *
   * @param T observable发出的数据的类型
   */
  fun <T> deliverReplay(): DeliverReplay<View, T> = DeliverReplay(mViews)

  /**
   * 该方法用于将两个回调[onNext]和[onError]合并为一个[io.reactivex.functions.Consumer]返回，
   * 这个Consumer用于处理[onNext]和[onError]回调
   *
   * @param onNext  如果[Delivery]中的状态为onNext,则会执行该回调
   * @param onError 如果[Delivery]onError,则会执行该回调
   * @param <T>     onNext回调的类型
   * @return 用来将一个[Delivery]分割成两个[BiConsumer]回调的[Consumer]对象.
  </T> */
  fun <T> split(onNext: BiConsumer<View, T>,
      onError: BiConsumer<View, Throwable>? = null): Consumer<Delivery<View, T>> {
    return Consumer { delivery -> delivery.split(onNext, onError) }
  }
}