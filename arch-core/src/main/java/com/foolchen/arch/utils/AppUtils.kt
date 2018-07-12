package com.foolchen.arch.utils

import com.foolchen.arch.config.sApplicationContext

/**
 * @author wayne
 * 2018/7/12
 * 下午2:20
 */
/**
 * Return the application's package name.
 *
 * @return the application's package name
 */
fun getAppPackageName(): String {
  return sApplicationContext().packageName
}