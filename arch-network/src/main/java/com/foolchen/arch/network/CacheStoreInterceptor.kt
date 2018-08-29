package com.foolchen.arch.network

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 缓存拦截器，用于修改返回值的Header，使缓存能够保存到本地并且下次请求时可用
 *
 * @author chenchong
 * 2018/8/1
 * 下午2:15
 */
class CacheStoreInterceptor : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {

    val request = chain.request()
    val forceCache = request.header("cache") == "true"
    val newBuilder = request.newBuilder()
    if (forceCache) {
      // 强制读取缓存
      newBuilder.cacheControl(CacheControl.FORCE_CACHE)
    } else {
      newBuilder.cacheControl(CacheControl.FORCE_NETWORK)
    }
    newBuilder.removeHeader("cache") // 将多余的Header移除
    val newRequest = newBuilder.build()
    var response = chain.proceed(newRequest)

    if (!forceCache) {
      // 从服务器获取的数据进行3天的缓存
      val maxAge = 60 * 60 * 24 * 3
      response = response.newBuilder()
          .request(newRequest)
          .header("Cache-Control", "public, only-if-cached, max-stale=$maxAge")
          .removeHeader("Pragma") // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
          .build()
    }

    return response
  }
}