# 说明

该库保留了[stetho](https://github.com/facebook/stetho)和[okhttp-logging-interceptor](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor)的API的空实现，以便于在进行打包发布时减少类和方法的数量。

该库保留了日常使用的最小程度的API，如果在开发过程中有需要用到其他的API，可从源码部分复制对应的API添加到该module，以保证编译能够正常通过。
