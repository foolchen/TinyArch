package com.foolchen.arch.presenter

import android.os.Bundle
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
  private val BUNDLES_KEY = "bundles"
  private var mPresentersFactory: PresentersFactory<View>? = null
  private val mPresenters: HashMap<String, Presenter<View>> = HashMap()
  private val mBundles: HashMap<String, Bundle> = HashMap()

  private var mView: View? = null

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

  override fun create(bundle: Bundle?) {
    super.create(bundle)
    val bundles = bundle?.getParcelableArrayList<BundleStorage>(BUNDLES_KEY)
    bundles?.forEach {
      mBundles[it.key] = it.bundle
    }

    mPresenters.forEach { entry ->
      entry.value.create(mBundles[entry.key])
    }
  }

  override fun destroy() {
    super.destroy()
    mPresenters.forEach {
      it.value.destroy()
    }
    mPresenters.clear()
    mBundles.clear()
  }

  override fun save(state: Bundle) {
    super.save(state)
    // 此处保存presenter的状态
    val bundles = ArrayList<BundleStorage>()
    mPresenters.forEach { entry ->
      // 将每个presenter的状态保存到bundle中
      var bundle = mBundles[entry.key]
      if (bundle == null) {
        bundle = Bundle()
        mBundles[entry.key] = bundle
        entry.value.save(bundle)
      }
      bundles.add(BundleStorage(entry.key, bundle))
    }
    // 将所有的bundle保存到其宿主presenter的bundle中

    state.putParcelableArrayList(BUNDLES_KEY, bundles)
  }

  override fun takeView(view: View) {
    super.takeView(view)
    mView = view
    mPresenters.forEach { it.value.takeView(view) }
  }

  override fun dropView() {
    super.dropView()
    mView = null
    mPresenters.forEach { it.value.dropView() }
  }

  /**
   * 为当前presenter设置一个创建presenter的工厂
   */
  fun setPresentersFactory(factory: PresentersFactory<View>) {
    this.mPresentersFactory = factory
  }

  /**
   * 根据特定的标识获取对应的presenter
   */
  fun <P : Presenter<View>> getPresenter(o: Any): P {
    if (mPresentersFactory == null) {
      throw NullPointerException("presenters factory has not been setup")
    }
    var presenter = mPresenters[o.toString()]
    if (presenter == null) {
      // 如果presenter不存在,则使用工厂创建一个
      presenter = mPresentersFactory!!.createPresenter(o)
      mPresenters[o.toString()] = presenter
      presenter.create(mBundles[o.toString()])
    }
    @Suppress("UNCHECKED_CAST")
    return presenter as P
  }
}