package com.foolchen.arch.thirdparty.wechat

import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth

abstract class BaseWXEntryActivity : BaseWXActivity() {
  /**
   * 登录成功,供业务实现
   */
  abstract fun onSignInSuccess(user: String)

  /**
   * 该方法供业务实现
   *
   * 通过authUrl来请求用户信息(**异步**)
   */
  abstract fun getAuth(authUrl: String)

  // 微信发送请求到第三方应用后的回调
  override fun onReq(req: BaseReq) {
  }

  // 第三方应用发送请求到微信后的回调
  override fun onResp(resp: BaseResp) {
    val code = (resp as SendAuth.Resp).code
    val authUrl = StringBuilder()
    authUrl
        .append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=")
        .append(WeChatUtil.APP_ID)
        .append("&secret=")
        .append(WeChatUtil.APP_SECRET)
        .append("&code=")
        .append(code)
        .append("&grant_type=authorization_code")

    getAuth(authUrl.toString())
  }
}