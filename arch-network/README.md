# 说明

```
+--- com.squareup.retrofit2:retrofit:2.4.0
|    \--- com.squareup.okhttp3:okhttp:3.10.0
|         \--- com.squareup.okio:okio:1.14.0
```

如上依赖关系所表述，`retrofit2`库已依赖`okhttp3`、`okio`。如果需要对内部的`okhttp3`和`okio`进行版本替换的话，需要在使用`retrofit2`时对这两个库进行排除。如下：

```groovy
dependencies {
  api("com.squareup.retrofit2:retrofit:2.4.0") {
    exclude module: "okhttp3"
  }
}
```
