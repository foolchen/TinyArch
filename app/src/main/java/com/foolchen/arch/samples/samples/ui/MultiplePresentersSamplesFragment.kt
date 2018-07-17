package com.foolchen.arch.samples.samples.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.foolchen.arch.app.ArchFragment
import com.foolchen.arch.samples.R
import com.foolchen.arch.samples.bean.Photo
import com.foolchen.arch.samples.samples.contracts.MultiplePresentersContract
import com.foolchen.arch.samples.samples.presenter.MultiplePresentersPresenter
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_multiple_presenters_samples_fragment.*
import nucleus5.factory.PresenterFactory

/**
 * 演示多个Presenter的Fragment
 * @author chenchong
 * 2018/7/16
 * 下午5:03
 */
// 在手动设置了PresenterFactory的情况下,不需要该注解
// @RequiresPresenter(MultiplePresentersPresenter::class)
class MultiplePresentersSamplesFragment : ArchFragment<MultiplePresentersPresenter>(), MultiplePresentersContract {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // 为了防止使用反射来创建presenter,此处手动设置一个工厂
    presenterFactory = PresenterFactory { MultiplePresentersPresenter() }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_multiple_presenters_samples_fragment, container,
        false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    btn_load_photos.setOnClickListener {
      presenter.getPhotos()
    }
  }

  override fun onPhotosLoaded(photos: List<Photo>) {
    val json = GsonBuilder()
        .setPrettyPrinting()
        .create()
        .toJson(photos)
    text_photos.text = json
  }

  override fun onFailure(message: String) {
    text_photos.text = message
  }
}