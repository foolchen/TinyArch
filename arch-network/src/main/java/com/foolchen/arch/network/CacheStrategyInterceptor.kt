package com.foolchen.arch.network

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 缓存拦截器，用于根据Header切换请求的类型（是否强制请求网络或者强制请求缓存）
 *
 * @author chenchong
 * 2018/8/1
 * 下午2:15
 */
class CacheStrategyInterceptor : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {

    val request = chain.request()
    val forceCache = request.header(KEY_CACHE) == "true"
    val newBuilder = request.newBuilder()
    if (forceCache) {
      // 强制读取缓存
      newBuilder.cacheControl(CacheControl.FORCE_CACHE)
    } else {
      newBuilder.cacheControl(CacheControl.FORCE_NETWORK)
    }
    return chain.proceed(newBuilder.build())
  }
}