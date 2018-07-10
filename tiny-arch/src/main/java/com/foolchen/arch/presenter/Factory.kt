package com.foolchen.arch.presenter

interface Factory<T> {
  fun create(): T
}
