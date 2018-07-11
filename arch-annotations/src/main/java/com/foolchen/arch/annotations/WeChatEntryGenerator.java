package com.foolchen.arch.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解用于标注一个微信分享的回调模板,通过处理器生成对应的回调Activity<br/>
 * 而不需要手动定义在源码中
 */
@Target(ElementType.TYPE) @Retention(RetentionPolicy.SOURCE)
public @interface WeChatEntryGenerator {
  /**
   * 用于定义要生成文件的包
   */
  String packageName();

  /**
   * 用于指定要使用的模板类
   */
  Class<?> template();
}
