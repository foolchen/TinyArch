# 说明

## 注意事项
在使用`nucleus`库来实现MVP时，需要注意`PresenterFactory`的处理。
在使用`RequiresPresenter`注解，并且没有手动设置`PresenterFactory`的情况下，会使用`ReflectionPresenterFactory`来创建需要的presenter。具体可以参考`NucleusSupportFragment`和`ReflectionPresenterFactory.<P>fromViewClass()`的实现：

```java
public abstract class NucleusSupportFragment<P extends Presenter> extends Fragment implements ViewWithPresenter<P> {
    // ... 省略

    private PresenterLifecycleDelegate<P> presenterDelegate =
        // 注意此处：此处传入了getClass()，获取了类对象，以备反射使用
        new PresenterLifecycleDelegate<>(ReflectionPresenterFactory.<P>fromViewClass(getClass()));
    // ... 省略
}
```
```java
public class ReflectionPresenterFactory<P extends Presenter> implements PresenterFactory<P> {

    private Class<P> presenterClass;

    @Nullable
    public static <P extends Presenter> ReflectionPresenterFactory<P> fromViewClass(Class<?> viewClass) {
        RequiresPresenter annotation = viewClass.getAnnotation(RequiresPresenter.class);
        Class<P> presenterClass = annotation == null ? null : (Class<P>)annotation.value();
        // 注意此处：在传入的Class不为空的情况下，无论如何都会创建一个ReflectionPresenterFactory。但是该factory不一定会被使用
        // 具体见PresenterLifecycleDelegate的实现
        return presenterClass == null ? null : new ReflectionPresenterFactory<>(presenterClass);
    }

    public ReflectionPresenterFactory(Class<P> presenterClass) {
        this.presenterClass = presenterClass;
    }

    @Override
    public P createPresenter() {
        try {
            // 在ReflectionPresenterFactory没有被替换掉的情况下，会采用反射的方式来创建一个presenter实例
            return presenterClass.newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

```

从上面的实现中发现，在创建`PresenterLifecycleDelegate`的实例时，无论如何都会创建一个`ReflectionPresenterFactory`。此时如果`ReflectionPresenterFactory`没有被手动替换掉，则会调用`ReflectionPresenterFactory.createPresenter()`方法来通过反射创建一个presenter。
而如果通过`PresenterLifecycleDelegate.setFactory()`方法来替换掉`ReflectionPresenterFactory`，则会使用通过`PresenterLifecycleDelegate.setFactory()`传入的工厂来创建presenter，而避免了使用反射来创建实例。
并且也不需要使用`RequiresPresenter`注解。

而在没有手动设置presenter工厂时，为了保证factory的存在，`RequiresPresenter`是必须的。
