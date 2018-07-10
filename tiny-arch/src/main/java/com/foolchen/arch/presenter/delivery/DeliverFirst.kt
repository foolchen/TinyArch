package com.foolchen.arch.presenter.delivery

import com.foolchen.arch.view.OptionalView
import io.reactivex.Notification
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.functions.Function

import com.foolchen.arch.presenter.delivery.validObservable

class DeliverFirst<View, T>(
    private val view: Observable<OptionalView<View>>) : ObservableTransformer<T, Delivery<View, T>> {

  override fun apply(observable: Observable<T>): ObservableSource<Delivery<View, T>> {
    return observable.materialize()
        .take(1)
        .switchMap { notification ->
          view.concatMap { view ->
            validObservable(view, notification)
          }
        }
        .take(1)
  }
}
