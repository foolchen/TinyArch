package com.foolchen.arch.samples.samples.presenter

import android.os.Bundle
import android.widget.Toast
import com.foolchen.arch.config.sApplicationContext
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
  private var isRefresh = true
  private var mOffset: Int = 0
  var isForceCache = false

  private val mService = RetrofitUtil.getInstance().get().create(UnsplashService::class.java)

  override fun onCreate(savedState: Bundle?) {
    super.onCreate(savedState)
    produce(ID_PHOTOS, Factory {
      val offset: Int = if (isRefresh) {
        0
      } else {
        mOffset + 10
      }
      //mService.getPhotos(offset, cache = isForceCache)
      (if (isForceCache) mService.getPhotosCache(offset) else mService.getPhotos(offset))
          .map {
            Result(it, offset)
          }
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
    }, BiConsumer { view, result ->
      if (isRefresh) {
        Toast.makeText(sApplicationContext(), if (isForceCache) "缓存加载成功" else "接口加载成功",
            Toast.LENGTH_SHORT).show()
        view.onRefreshSuccess(result.data)
      } else {
        view.onLoadMoreSuccess(result.data)
      }
      mOffset = result.offset
      view.onLoadMoreEnd(result.data.size < 10)
    },
        BiConsumer { view, throwable ->
          if (isRefresh) {
            view.onRefreshError(throwable.localizedMessage)
          } else {
            view.onLoadMoreError(throwable.localizedMessage)
          }
        })
  }

  fun getPhotos() {
    isRefresh = true
    start(ID_PHOTOS)
  }

  fun getMorePhotos() {
    isRefresh = false
    start(ID_PHOTOS)
  }

  data class Result<T>(val data: T, val offset: Int)
}

