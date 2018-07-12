package com.foolchen.arch.config

import android.content.Context


/**
 * 初始化配置工具
 */
fun sInit(context: Context): Configurator {
  Configurator.withApplicationContext(context.applicationContext)
  return Configurator
}

fun <T> sConfiguration(key: Any) = Configurator.getConfiguration<T>(key)

fun sApplicationContext() = Configurator.getConfiguration<Context>(APPLICATION_CONTEXT)

fun sDevelop() = Configurator.getConfiguration<Boolean>(DEVELOP)



