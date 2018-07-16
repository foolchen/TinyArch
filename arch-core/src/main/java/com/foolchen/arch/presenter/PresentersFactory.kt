package com.foolchen.arch.presenter

import nucleus5.presenter.Presenter

/**
 * 使用该接口实现一个简单工厂模式,用于多个presenter的创建
 *
 * @author chenchong
 * 2018/7/16
 * 下午2:44
 */
interface PresentersFactory<View> {
  fun createPresenter(o: Any): Presenter<View>
}