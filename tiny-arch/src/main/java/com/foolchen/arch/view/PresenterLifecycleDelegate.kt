package com.foolchen.arch.view

import android.os.Bundle
import com.foolchen.arch.factory.PresenterFactory
import com.foolchen.arch.factory.PresenterStorage
import com.foolchen.arch.presenter.Presenter

/**
 * This class adopts a View lifecycle to the Presenter`s lifecycle.
 *
 * @param <P> a type of the presenter.
</P> */
class PresenterLifecycleDelegate<View : ViewWithPresenter<P>, P : Presenter<View>>(
    private var presenterFactory: PresenterFactory<P>?) {
  private var presenter: P? = null
  private var bundle: Bundle? = null

  private var presenterHasView: Boolean = false

  /**
   * [ViewWithPresenter.getPresenterFactory]
   */
  fun getPresenterFactory(): PresenterFactory<P>? {
    return presenterFactory
  }

  /**
   * [ViewWithPresenter.setPresenterFactory]
   */
  fun setPresenterFactory(presenterFactory: PresenterFactory<P>?) {
    if (presenter != null) {
      throw IllegalArgumentException(
          "setPresenterFactory() should be called before onResume()")
    }
    this.presenterFactory = presenterFactory
  }

  /**
   * [ViewWithPresenter.getPresenter]
   */
  fun getPresenter(): P? {
    if (presenterFactory != null) {
      if (presenter == null && bundle != null) {
        presenter = PresenterStorage.INSTANCE.getPresenter<P>(
            bundle!!.getString(PRESENTER_ID_KEY)!!)
      }

      if (presenter == null) {
        presenter = presenterFactory!!.createPresenter()
        PresenterStorage.INSTANCE.add(presenter!!)
        presenter!!.create(if (bundle == null) null else bundle!!.getBundle(PRESENTER_KEY))
      }
      bundle = null
    }
    return presenter
  }

  /**
   * [android.app.Activity.onSaveInstanceState], [android.app.Fragment.onSaveInstanceState], [android.view.View.onSaveInstanceState].
   */
  fun onSaveInstanceState(): Bundle {
    val bundle = Bundle()
    getPresenter()
    if (presenter != null) {
      val presenterBundle = Bundle()
      presenter!!.save(presenterBundle)
      bundle.putBundle(PRESENTER_KEY, presenterBundle)
      bundle.putString(PRESENTER_ID_KEY, PresenterStorage.INSTANCE.getId(presenter!!))
    }
    return bundle
  }

  /**
   * [android.app.Activity.onCreate], [android.app.Fragment.onCreate], [android.view.View.onRestoreInstanceState].
   */
  fun onRestoreInstanceState(presenterState: Bundle) {
    if (presenter != null) {
      throw IllegalArgumentException(
          "onRestoreInstanceState() should be called before onResume()")
    }
    this.bundle = ParcelFn.unmarshall<Bundle>(ParcelFn.marshall(presenterState))
  }

  /**
   * [android.app.Activity.onResume],
   * [android.app.Fragment.onResume],
   * [android.view.View.onAttachedToWindow]
   */
  fun onResume(view: View) {
    getPresenter()
    if (presenter != null && !presenterHasView) {

      presenter!!.takeView(view)
      presenterHasView = true
    }
  }

  /**
   * [android.app.Activity.onDestroy],
   * [android.app.Fragment.onDestroyView],
   * [android.view.View.onDetachedFromWindow]
   */
  fun onDropView() {
    if (presenter != null && presenterHasView) {
      presenter!!.dropView()
      presenterHasView = false
    }
  }

  /**
   * [android.app.Activity.onDestroy],
   * [android.app.Fragment.onDestroy],
   * [android.view.View.onDetachedFromWindow]
   */
  fun onDestroy(isFinal: Boolean) {
    if (presenter != null && isFinal) {
      presenter!!.destroy()
      presenter = null
    }
  }

  companion object {

    private val PRESENTER_KEY = "presenter"
    private val PRESENTER_ID_KEY = "presenter_id"
  }
}
