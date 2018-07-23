package com.foolchen.arch.samples.samples.contracts

import com.foolchen.arch.samples.bean.Photo

interface MultiplePresentersContract {

  fun onRefreshSuccess(photos: List<Photo>)

  fun onRefreshError(message: String)

  fun onLoadMoreSuccess(photos: List<Photo>)

  fun onLoadMoreError(message: String)

  fun onLoadMoreEnd(isEnd: Boolean, message: String = "没有更多了")
}