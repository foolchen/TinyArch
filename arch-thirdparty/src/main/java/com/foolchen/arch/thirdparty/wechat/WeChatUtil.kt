package com.foolchen.arch.thirdparty.wechat

import com.foolchen.arch.config.WECHAT_APP_ID
import com.foolchen.arch.config.WECHAT_APP_SECRET
import com.foolchen.arch.config.sApplicationContext
import com.foolchen.arch.config.sConfiguration
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * 微信工具箱
 *
 * @author chenchong
 * 2018/7/12
 * 上午10:25
 */

object WeChatUtil {
  private val WXAPI: IWXAPI
  val APP_ID = sConfiguration<String>(WECHAT_APP_ID)
  val APP_SECRET = sConfiguration<String>(WECHAT_APP_SECRET)

  init {
    WXAPI = WXAPIFactory.createWXAPI(sApplicationContext(),
        APP_ID, true)
    WXAPI.registerApp(APP_ID)
  }

  fun getWXAPI() = WXAPI
}