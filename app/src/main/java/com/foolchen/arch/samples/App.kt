package com.foolchen.arch.samples

import android.app.Application
import com.foolchen.arch.config.sInit

class App : Application() {
  override fun onCreate() {
    super.onCreate()
    sInit(this)
        .withDevelop(BuildConfig.DEVELOP)
        .withWeChatAppId("123")
        .withWeChatSecret("123")
        .configure()
  }
}