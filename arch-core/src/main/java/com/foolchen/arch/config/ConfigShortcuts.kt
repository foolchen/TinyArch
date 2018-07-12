package com.foolchen.arch.config

import android.content.Context


/**
 * 初始化配置工具
 */
fun sInit(context: Context): Configurator {
  Configurator.withApplicationContext(context.applicationContext)
  return Configurator
}

fun sApplicationContext() = Configurator.getConfiguration<Context>(APPLICATION_CONTEXT)



