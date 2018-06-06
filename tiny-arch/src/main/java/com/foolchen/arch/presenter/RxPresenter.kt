package com.foolchen.arch.presenter

import android.util.SparseArray
import com.foolchen.arch.view.OptionalView
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
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


}