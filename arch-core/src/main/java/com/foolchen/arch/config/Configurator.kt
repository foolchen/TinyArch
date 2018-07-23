package com.foolchen.arch.config

import android.content.Context
import android.util.Log

/**
 * 全局配置用工具
 *
 * @author chenchong
 * 2018/7/11
 * 下午5:29
 */
object Configurator {
  // 用于存放配置项
  private val ARCH_CONFIGS = HashMap<Any, Any>()
  // 全局使用的Handler.待定
  // private val ARCH_HANDLER = Handler()

  init {
    Log.d("DEBUG", "Configurator init.")
    ARCH_CONFIGS[CONFIG_READY] = false
  }

  /**
   * 完成配置<br/>
   * 调用该方法时,会将Configurator的配置标记为完成<br/>
   * 如果在该方法被调用前调用了其他方法,会报出初始化未完成错误
   */
  fun configure() {
    ARCH_CONFIGS[CONFIG_READY] = true
  }

  /**
   * 根据特定的key获取对应的配置项
   */
  fun <T> getConfiguration(key: Any): T {
    checkConfiguration()
    checkConfiguration(key)
    return ARCH_CONFIGS[key]!! as T
  }

  fun withConfiguration(key: Any, value: Any): Configurator {
    ARCH_CONFIGS[key] = value
    return this
  }

  internal fun withApplicationContext(context: Context): Configurator {
    withConfiguration(APPLICATION_CONTEXT, context)
    return this
  }

  fun withDevelop(isDevelop: Boolean): Configurator {
    withConfiguration(DEVELOP, isDevelop)
    return this
  }

  /**
   * 配置微信分享的APP Id
   */
  fun withWeChatAppId(appId: String): Configurator {
    withConfiguration(WECHAT_APP_ID, appId)
    return this
  }

  /**
   * 配置微信分享的APP Secret
   */
  fun withWeChatSecret(appSecret: String): Configurator {
    withConfiguration(WECHAT_APP_SECRET, appSecret)
    return this
  }

  fun withBaseUrl(baseUrl: String): Configurator {
    withConfiguration(BASE_URL, baseUrl)
    return this
  }

  fun withTimeoutSeconds(seconds: Int): Configurator {
    withConfiguration(TIME_OUT_SECONDS, seconds)
    return this
  }

  fun withScreenWidth(width: Int): Configurator {
    withConfiguration(SCREEN_WIDTH, width)
    return this
  }

  fun withScreenHeight(height: Int): Configurator {
    withConfiguration(SCREEN_HEIGHT, height)
    return this
  }

  /*
   /**
   * 配置一个全局的Activity,用于被各第三方组件使用
   *
   * **注意:配置在此处的Activity,应该为整个应用的主Activity(一直在站内存在的Activity).**
   * **由于配置在此处的Activity会被一直被配置文件持有引用,所以如果此处配置的为其他Activity的话,会造成内存泄露.**
   * **该Activity不应该被拿来做创建布局等与上下文有关的工作.**
   */
  fun withActivity(activity: Activity): Configurator {
    withConfiguration(ACTIVITY, activity)
    return this
  }
  */

  private fun checkConfiguration() {
    checkConfiguration(CONFIG_READY)
    if (ARCH_CONFIGS[CONFIG_READY] != true) {
      throw ConfigNotInitException("Config has not been completed.")
    }
  }

  @Throws(ConfigNotInitException::class)
  private fun checkConfiguration(key: Any) {
    if (!ARCH_CONFIGS.containsKey(key)) {
      throw ConfigNotInitException("$key has not been initialized.")
    }
  }
}

private const val CONFIG_READY = "config_ready"
const val APPLICATION_CONTEXT = "application_context"
const val WECHAT_APP_ID = "wechat_app_id"
const val WECHAT_APP_SECRET = "wechat_app_secret"
const val DEVELOP = "develop"
const val BASE_URL = "base_url"
const val TIME_OUT_SECONDS = "time_out_seconds"
const val SCREEN_WIDTH = "screen_width"
const val SCREEN_HEIGHT = "screen_height"