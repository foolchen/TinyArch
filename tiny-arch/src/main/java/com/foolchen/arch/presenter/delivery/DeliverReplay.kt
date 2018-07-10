package com.foolchen.arch.presenter.delivery

import com.foolchen.arch.view.OptionalView
import io.reactivex.Notification
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Function
import io.reactivex.subjects.ReplaySubject

class DeliverReplay<View, T>(
    private val view: Observable<OptionalView<View>>) : ObservableTransformer<T, Delivery<View, T>> {

  override fun apply(observable: Observable<T>): Observable<Delivery<View, T>> {
    val subject = ReplaySubject.create<Notification<T>>()
    val disposable = observable.materialize().doOnEach(subject).subscribe()
    return view.switchMap { view ->
      subject.concatMap { notification ->
        validObservable(view, notification)
      }
    }.doOnDispose { disposable.dispose() }
  }
}
