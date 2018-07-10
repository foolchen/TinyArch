package com.foolchen.arch.factory;

import com.foolchen.arch.presenter.Presenter;

/**
 * 用于创建presenter的工厂
 *
 * @param <P> 要创建的presenter的类型
 */
public interface PresenterFactory<P extends Presenter> {
  /**
   * 创建一个presenter
   */
  P createPresenter();
}
