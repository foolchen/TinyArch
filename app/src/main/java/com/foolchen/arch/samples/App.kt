package com.foolchen.arch.samples

import android.app.Application
import com.foolchen.arch.config.sInit
import com.foolchen.arch.network.RetrofitUtil
import com.foolchen.arch.samples.network.UnsplashAuthorizationInterceptor

class App : Application() {
  override fun onCreate() {
    super.onCreate()
    sInit(this)
        .withDevelop(BuildConfig.DEVELOP)
        .withWeChatAppId("123")
        .withWeChatSecret("123")
        .withBaseUrl("https://www.baidu.com/")
        .configure()

    // 初始化retrofit
    RetrofitUtil.get().init(networkInterceptors = listOf(UnsplashAuthorizationInterceptor()))
  }
}