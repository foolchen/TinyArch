package com.foolchen.arch.presenter

import android.os.Bundle
import java.util.concurrent.CopyOnWriteArrayList

/**
 * 所有presenter的基类.其子类可以覆写[onCreate],[onDestroy],[onSave],[onTakeView],[onDropView]
 * 等方法来实现功能.
 *
 */
open class Presenter<View> {
  var view: View? = null
    private set
  private val onDestroyListeners = CopyOnWriteArrayList<OnDestroyListener>()
  /**
   * 该方法在Presenter创建后被调用<br/>
   * 该方法用于被继承覆写.
   *
   * @param savedState 当presenter在页面重新创建并且重新绑定了presenter后,则该Bundle包含了它之前在[onSave]中保存的状态和数据
   */
  protected open fun onCreate(savedState: Bundle?) {}

  /**
   * 在用户关闭了view(销毁)时,该方法被调用<br/>
   * 该方法用于被继承覆写.
   */
  protected open fun onDestroy() {}

  /**
   * 通过[state]保存当前presenter的状态,在页面重建后将保存的状态传递给新的presenter<br/>
   * 该方法用于被继承覆写
   * @param state 用于保存presenter的状态和数据
   */
  protected open fun onSave(state: Bundle) {}

  /**
   * 该方法在view被绑定到presenter时被调用<br/>
   * 一般来说,该调用发生在[android.app.Activity.onResume],[android.app.Fragment.onResume]和[android.view.View.onAttachedToWindow]
   * 被调用时.
   * <br/>
   * 该方法用于被继承覆写.
   * @param view 要被绑定的视图
   */
  protected open fun onTakeView(view: View) {}

  /**
   * 该方法在view被绑定到presenter时被调用<br/>
   * 一般来说,该调用发生在[android.app.Activity.onPause],[android.app.Fragment.onPause]和[android.view.View.onDetachedFromWindow]
   * 被调用时.
   * <br/>
   * 该方法用于被继承覆写.
   */
  protected open fun onDropView() {}

  /**
   * 在presenter销毁时调用的回调
   */
  interface OnDestroyListener {
    /**
     * 在[Presenter.onDestroy]调用前,该方法被调用
     */
    fun onDestroy()
  }

  /**
   * 添加一个监听[onDestroy]方法执行的监听器
   *
   * @param listener 要添加的监听器
   */
  fun addOnDestroyListener(listener: OnDestroyListener) {
    onDestroyListeners.add(listener)
  }

  /**
   * 移除一个监听器
   * @param listener 要移除的监听器
   */
  fun removeOnDestroyListener(listener: OnDestroyListener) {
    onDestroyListeners.remove(listener)
  }

  /**
   * 初始化presenter
   */
  fun create(bundle: Bundle) {
    onCreate(bundle)
  }

  /**
   * 销毁presenter,并且调用所有已注册的[Presenter.OnDestroyListener]
   */
  fun destroy() {
    for (listener in onDestroyListeners)
      listener.onDestroy()
    onDestroy()
  }

  /**
   * 保存presenter当前的状态和数据
   */
  fun save(state: Bundle) {
    onSave(state)
  }

  /**
   * 将view与presenter绑定
   *
   * @param view 要绑定的view
   */
  fun takeView(view: View) {
    this.view = view
    onTakeView(view)
  }

  /**
   * 将view从presenter解绑
   */
  fun dropView() {
    onDropView()
    this.view = null
  }
}
