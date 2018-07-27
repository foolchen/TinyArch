# 说明

网络工具的集合。

```
+--- com.squareup.retrofit2:retrofit:2.4.0
|    \--- com.squareup.okhttp3:okhttp:3.10.0
|         \--- com.squareup.okio:okio:1.14.0
```

如上依赖关系所表述，`retrofit2`库已依赖`okhttp3`、`okio`。如果需要对内部的`okhttp3`和`okio`进行版本替换的话，需要在使用`retrofit2`时对这两个库进行排除。如下：

```groovy
dependencies {
  // 此处排除retrofit内部依赖的okhttp和okio版本，采用自主选择的版本
  api("com.squareup.retrofit2:retrofit:2.4.0") {
    exclude module: 'okhttp'
    exclude module: 'okio'
  }
  api "com.squareup.retrofit2:converter-gson:2.4.0"
  // 由于已经依赖了最新的rxjava2版本，故此处排除掉adapter-rxjava2中依赖的版本
  api("com.squareup.retrofit2:adapter-rxjava2:2.4.0") {
    exclude group: 'io.reactivex.rxjava2'
    exclude module: 'okhttp'
  }

  if (develop) {
    api("com.facebook.stetho:stetho:1.5.0") {
      exclude module: 'okhttp'
    }
    api("com.facebook.stetho:stetho-okhttp3:1.5.0") {
      exclude module: 'okhttp'
    }
    api "com.squareup.okhttp3:logging-interceptor:3.11.0"
  }
}
```
## 反混淆
具体反混淆规则请查看[proguard-rules.pro](proguard-rules.pro)。

参考来源于：[okhttp3](https://github.com/square/okhttp/blob/master/okhttp/src/main/resources/META-INF/proguard/okhttp3.pro)、[okio](https://github.com/square/okio)、[retrofit2](https://github.com/square/retrofit/blob/master/retrofit/src/main/resources/META-INF/proguard/retrofit2.pro)
