package com.foolchen.arch.network

import android.util.Log
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 缓存拦截器
 *
 * @author chenchong
 * 2018/8/1
 * 下午2:15
 */
class CacheInterceptor : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {

    var request = chain.request()
    val forceCache = request.header("cache") == "true"
    Log.d("CacheInterceptor", "forceCache = $forceCache")
    val newBuilder = request.newBuilder()
    if (forceCache) {
      // 强制读取缓存
      newBuilder.cacheControl(CacheControl.FORCE_CACHE)
    } else {
      newBuilder.cacheControl(CacheControl.FORCE_NETWORK)
    }
    newBuilder.removeHeader("cache") // 将多余的Header移除

    var response = chain.proceed(newBuilder.build())

    if (!forceCache) {
      // 从服务器获取的数据进行3天的缓存
      val maxAge = 60 * 60 * 24 * 3
      response = response.newBuilder()
          .header("Cache-Control", "public, max-age=" + maxAge)
          .removeHeader("Pragma") // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
          .build()
    }

    return response
  }
}