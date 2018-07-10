package com.foolchen.arch.presenter.delivery

import com.foolchen.arch.view.OptionalView
import io.reactivex.Notification
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

class DeliverLatestCache<View, T>(
    private val view: Observable<OptionalView<View>>) : ObservableTransformer<T, Delivery<View, T>> {

  override fun apply(observable: Observable<T>): ObservableSource<Delivery<View, T>> {
    return Observable
        .combineLatest<OptionalView<View>, Notification<T>, Array<Any>>(
            view,
            observable
                .materialize()
                .filter { notification -> !notification.isOnComplete },
            BiFunction<Any, Notification<T>, Array<Any>> { view, notification ->
              arrayOf(view, notification)
            })
        .concatMap { pack ->
          validObservable(pack[0] as OptionalView<View>, pack[1] as Notification<T>)
        }
  }
}
