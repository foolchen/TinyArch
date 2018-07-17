package com.foolchen.arch.samples.samples.presenter

import android.os.Bundle
import com.foolchen.arch.network.RetrofitUtil
import com.foolchen.arch.presenter.BasePresenter
import com.foolchen.arch.samples.network.UnsplashService
import com.foolchen.arch.samples.samples.contracts.MultiplePresentersContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiConsumer
import io.reactivex.schedulers.Schedulers
import nucleus5.presenter.Factory

/**
 * 演示多个Presenter同时使用的情况
 * @author chenchong
 * 2018/7/16
 * 下午5:04
 */
class MultiplePresentersPresenter : BasePresenter<MultiplePresentersContract>() {
  private val ID_PHOTOS = 1

  private val mService = RetrofitUtil.getInstance().get().create(UnsplashService::class.java)

  override fun onCreate(savedState: Bundle?) {
    super.onCreate(savedState)
    produce(ID_PHOTOS, Factory {
      mService.getPhotos(0)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
    }, BiConsumer { view, result -> view.onPhotosLoaded(result) },
        BiConsumer { view, throwable -> view.onFailure(throwable.localizedMessage) })
  }

  fun getPhotos() {
    start(ID_PHOTOS)
  }
}
