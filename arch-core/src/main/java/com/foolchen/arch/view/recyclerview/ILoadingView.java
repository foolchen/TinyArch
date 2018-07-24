package com.foolchen.arch.view.recyclerview;

/**
 * 与{@link IRecyclerView}结合使用,用来实现LoadingView
 *
 * @author chenchong
 * 2018/7/23
 * 下午5:23
 */
public interface ILoadingView {

  /**
   * 如果LoadingView中需要执行动画,则覆写该方法
   */
  void start();

  /**
   * 如果LoadingView中需要停止执行动画,覆写该方法
   */
  void stop();
}
