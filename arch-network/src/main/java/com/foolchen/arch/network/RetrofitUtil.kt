package com.foolchen.arch.network

import com.foolchen.arch.config.BASE_URL
import com.foolchen.arch.config.TIME_OUT_SECONDS
import com.foolchen.arch.config.sApplicationContext
import com.foolchen.arch.config.sConfiguration
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * retrofit的单例
 *
 * @author chenchong
 * 2018/7/17
 * 下午3:59
 */

class RetrofitUtil private constructor() {
  private var sRetrofit: Retrofit? = null

  companion object {
    fun get(): RetrofitUtil {
      return Holder.sInstance
    }
  }

  private object Holder {
    val sInstance = RetrofitUtil()
  }

  /**
   * 获取一个retrofit实例,用于创建网络请求
   *
   * **注意:如果之前未初始化,则此处会对retrofit进行初始化**
   */
  fun get(): Retrofit {
    if (sRetrofit == null) {
      init()
    }

    if (sRetrofit == null) {
      throw RuntimeException("Retrofit is not initialized correctly.")
    }

    return sRetrofit!!
  }

  /**
   * 对retrofit进行初始化
   */
  @Synchronized
  fun init(networkInterceptors: List<Interceptor>? = null,
      localInterceptors: List<Interceptor>? = null) {
    val builder = OkHttpClient.Builder()
    // 设置超时时间
    builder
        .connectTimeout(sConfiguration(TIME_OUT_SECONDS), TimeUnit.SECONDS)
        .readTimeout(sConfiguration(TIME_OUT_SECONDS), TimeUnit.SECONDS)
        .writeTimeout(sConfiguration(TIME_OUT_SECONDS), TimeUnit.SECONDS)

    // 定义缓存为10MB
    val cache = Cache(sApplicationContext().cacheDir, 10 * 1024 * 1024)
    builder.cache(cache)

    // 添加拦截器
    networkInterceptors?.forEach {
      builder.addNetworkInterceptor(it)
    }

    localInterceptors?.forEach {
      builder.addInterceptor(it)
    }

    // 生成client
    val client = builder.build()

    sRetrofit = Retrofit.Builder()
        .baseUrl(sConfiguration(BASE_URL) as String)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

  }
}
