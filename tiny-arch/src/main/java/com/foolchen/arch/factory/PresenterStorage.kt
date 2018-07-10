package com.foolchen.arch.factory

import com.foolchen.arch.presenter.Presenter
import java.util.*

/**
 * This is the singleton where all presenters are stored.
 * 使用枚举实现的单例,用于存放所有创建的presenter
 */
enum class PresenterStorage {

  INSTANCE;

  private val idToPresenter = HashMap<String, Presenter<*>>()
  private val presenterToId = HashMap<Presenter<*>, String>()

  /**
   * Adds a presenter to the storage
   *
   * @param presenter a presenter to add
   */
  fun add(presenter: Presenter<*>) {
    val id = presenter.javaClass.simpleName + "/" + System.nanoTime() + "/" + (Math.random() * Integer.MAX_VALUE).toInt()
    idToPresenter[id] = presenter
    presenterToId[presenter] = id
    presenter.addOnDestroyListener(object : Presenter.OnDestroyListener {
      override fun onDestroy() {
        idToPresenter.remove(presenterToId.remove(presenter))
      }
    })
  }

  /**
   * Returns a presenter by id or null if such presenter does not exist.
   *
   * @param id id of a presenter that has been received by calling [.getId]
   * @param <P> a type of presenter
   * @return a presenter or null
  </P> */
  fun <P> getPresenter(id: String): P {

    return idToPresenter[id] as P
  }

  /**
   * Returns id of a given presenter.
   *
   * @param presenter a presenter to get id for.
   * @return if of the presenter.
   */
  fun getId(presenter: Presenter<*>): String? {
    return presenterToId[presenter]
  }

  /**
   * Removes all presenters from the storage.
   * Use this method for testing purposes only.
   */
  fun clear() {
    idToPresenter.clear()
    presenterToId.clear()
  }
}
