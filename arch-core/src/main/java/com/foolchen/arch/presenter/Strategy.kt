package com.foolchen.arch.presenter

/**
 * 请求策略
 *
 * @author wayne
 * 2018/7/10
 * 下午5:30
 */
enum class Strategy {
  /**
   * 仅回调第一次产生的结果,并且仅回调一次
   */
  ONCE,
  /**
   * 回调最新产生的结果,并且在每次view被绑定到presenter时都会回调
   */
  LATEST,
  /**
   * 回调产生的所有结果,并且在每次view被绑定到presenter时都会回调
   */
  ALL
}