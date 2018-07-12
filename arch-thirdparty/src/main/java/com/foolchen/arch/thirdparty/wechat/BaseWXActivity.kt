package com.foolchen.arch.thirdparty.wechat

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

/**
 * 微信分享回调用的Activity
 * @author chenchong
 * 2018/7/12
 * 上午11:20
 */

abstract class BaseWXActivity : AppCompatActivity(), IWXAPIEventHandler {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WeChatUtil.getWXAPI().handleIntent(intent, this)
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    setIntent(intent)
    WeChatUtil.getWXAPI().handleIntent(getIntent(), this)
  }
}
