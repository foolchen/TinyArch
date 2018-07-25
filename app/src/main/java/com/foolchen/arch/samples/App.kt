package com.foolchen.arch.samples

import android.app.Application
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.foolchen.arch.config.sDevelop
import com.foolchen.arch.config.sInit
import com.foolchen.arch.network.RetrofitUtil
import com.foolchen.arch.samples.network.UnsplashAuthorizationInterceptor
import com.foolchen.arch.utils.getScreenHeight
import com.foolchen.arch.utils.getScreenWidth
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import okhttp3.logging.HttpLoggingInterceptor

class App : Application() {

  init {
    SmartRefreshLayout.setDefaultRefreshInitializer { context, layout ->
      layout.setEnableLoadMore(false)
      layout.setEnableOverScrollDrag(false)
    }
  }

  override fun onCreate() {
    super.onCreate()
    sInit(this)
        .withDevelop(BuildConfig.DEVELOP)
        .withWeChatAppId("123")
        .withWeChatSecret("123")
        .withBaseUrl(
            "https://api.unsplash.com/")
        .withTimeoutSeconds(10)
        .withScreenWidth(getScreenWidth())
        .withScreenHeight(getScreenHeight())
        .configure()

    var stethoInterceptor: StethoInterceptor? = null
    // 初始化stetho
    if (sDevelop()) {
      Stetho.initializeWithDefaults(this)
      stethoInterceptor = StethoInterceptor()
    }

    // 初始化retrofit
    RetrofitUtil.getInstance().init(
        networkInterceptors = listOf(UnsplashAuthorizationInterceptor(), stethoInterceptor,
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)))
  }
}