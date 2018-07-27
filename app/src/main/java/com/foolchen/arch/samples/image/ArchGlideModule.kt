package com.foolchen.arch.samples.image

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class ArchGlideModule : AppGlideModule() {

  // 禁用清单解析。这样可以改善 Glide 的初始启动时间，并避免尝试解析元数据时的一些潜在问题
  override fun isManifestParsingEnabled(): Boolean {
    return false
  }
}