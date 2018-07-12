package com.foolchen.arch.samples.template;

import com.foolchen.arch.annotations.WeChatEntryGenerator;

/**
 * 用于生成WXEntryActivity
 * 该接口不能转换为Kotlin,否则注解不生效
 *
 * @author wayne
 * 2018/7/12
 * 下午12:03
 */
@WeChatEntryGenerator(packageName = "com.foolchen.arch.samples", template = WXEntryTemplate.class)
public interface WeChatEntry {
}
