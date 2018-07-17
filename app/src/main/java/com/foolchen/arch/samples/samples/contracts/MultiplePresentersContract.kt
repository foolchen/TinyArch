package com.foolchen.arch.samples.samples.contracts

import com.foolchen.arch.samples.bean.Photo

interface MultiplePresentersContract {

  fun onPhotosLoaded(photos: List<Photo>)

  fun onFailure(message: String)
}