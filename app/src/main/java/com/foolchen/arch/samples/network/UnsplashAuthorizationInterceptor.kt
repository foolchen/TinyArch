package com.foolchen.arch.samples.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * 对unsplash进行授权的拦截器
 *
 * @author chenchong
 * 2018/7/17
 * 下午4:23
 */
class UnsplashAuthorizationInterceptor : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val builder = request.newBuilder()
    builder.addHeader("Authorization: Client-ID",
        "1bcf57fcb6416c044b79065bb463aed67f0ee82f4005f9d02f26295cd4cf2b90")
    return chain.proceed(builder.build())
  }


}