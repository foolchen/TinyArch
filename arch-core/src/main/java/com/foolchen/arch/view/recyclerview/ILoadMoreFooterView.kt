package com.foolchen.arch.view.recyclerview

/**
 * 上拉加载的FooterView需要实现的接口,用于辅助上拉加载功能的实现
 *
 * @author chenchong
 *
 * 2018/7/25
 * 下午4:36
 */
interface ILoadMoreFooterView {
  fun canLoadMore(): Boolean
}