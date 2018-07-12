package com.foolchen.arch.samples.template

import com.foolchen.arch.thirdparty.wechat.BaseWXEntryActivity

open class WXEntryTemplate : BaseWXEntryActivity() {

  override fun onResume() {
    super.onResume()
    finish()
    overridePendingTransition(0, 0)
  }

  override fun onSignInSuccess(user: String) {
    // TODO: 2018/7/12 wayne 此处为登录成功的回调,用于解析和存储数据
  }

  override fun getAuth(authUrl: String) {
    // TODO: 2018/7/12 wayne 此处为第三方登录成功后,向服务器请求用于信息(异步)
    // TODO: 2018/7/12 wayne 在执行完毕后,回调onSignInSuccess存储信息
  }
}
